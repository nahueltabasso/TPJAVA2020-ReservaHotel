package controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import data.PersonaRepository;
import data.RolRepository;
import entities.Persona;
import entities.Rol;
import utils.Hash;
import utils.Utils;
import utils.email.EmailService;

public class PersonaController {

	private Logger logger = LogManager.getLogger(getClass());
	private PersonaRepository personaRepository = new PersonaRepository();
	private RolRepository rolRepository = new RolRepository();
	private DomicilioController domicilioCtrl = new DomicilioController();
	private EmailService emailService = new EmailService();
	
	public Persona registrarPersona(Persona persona) throws Exception {
		logger.log(Level.INFO, "Ingresa a registrarPersona()");
		preValidation(persona, true);
		validPersonaBeforeSave(persona);
		Persona personaDB = personaRepository.save(persona);
		emailService.sendEmail(personaDB, "Has completado la registracion!\nBienvenido al sistema!", "Registracion exitosa!");
		return personaDB;
	}
	
	public List<Persona> getAll() throws Exception {
		logger.log(Level.INFO, "Ingresa a getAll()");
		return personaRepository.findAll();
	}
	
	public Persona getById(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a getById()");
		Persona personaDb = personaRepository.findById(id);
		if (personaDb == null) {
			throw new Exception("No existe la persona en la base de datos!");
		}
		return personaDb;
	}
	
	public Persona getByDocumento(Persona persona) throws Exception {
		logger.log(Level.INFO, "Ingresa a getByDocumento()");
		return personaRepository.findByTipoAndNroDocumento(persona.getTipoDocumento(), persona.getNroDocumento());
	}
	
	public void delete(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a delete()");
		Persona personaDB = personaRepository.findById(id);
		if (personaDB == null) {
			throw new Exception("ERROR - No existe el usuario en la base de datos!");
		}
		Rol rolPersona = personaDB.getRol();
		if (rolPersona.getNombreRol().equalsIgnoreCase(Rol.ADMINISTRADOR)) {
			throw new Exception("ERROR - No se puede borrar un usuario con rol ADMINITRADOR");
		}
		personaRepository.delete(id);
	}
	
	public Persona actualizarPersona(Long id, Persona persona) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizarPersona()");
		preValidation(persona, false);
		return actualizar(id, persona);
	}
	
	public Persona getPersonaByDocumento(String tipoDocumento, Long numeroDocumento) throws Exception {
		Persona persona = personaRepository.findByTipoAndNroDocumento(tipoDocumento, numeroDocumento);
		if (persona.getId() == null) {
			return null;
		}
		return persona;
	}
	
	private void preValidation(Persona persona, boolean isCreate) throws Exception {
		logger.log(Level.INFO, "Ingresa a preValidation()");
		boolean valid;
		if (isCreate) {
			valid = personaRepository.existPersonaByEmailOrTipoAndNroDocumento(persona);
			if (valid) {
				throw new Exception ("El usuario ya existe en la base de datos");
			}
		} else {
			Persona personaDB = personaRepository.findById(persona.getId());
			if (!personaDB.getEmail().equalsIgnoreCase(persona.getEmail())) {
				valid = personaRepository.existPersonaByEmail(persona.getEmail());
				if (valid) {
					throw new Exception("El email ya existe en la base de datos");
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private String generarHashPassword(String password) throws UnsupportedEncodingException {
		logger.log(Level.INFO, "Ingresa a generarHashPassword()");
		String passwordHash = Hash.getPasswordHashSHA512(password);
		return password;
	}
	
	private void validPersonaBeforeSave(Persona persona) throws Exception {
		logger.log(Level.INFO, "Ingresa a validPersonaBeforeSave()");

		// Validamos si el nombre o el apellido contienen digitos
		if (Utils.cadContainsDigit(persona.getNombre()) || Utils.cadContainsDigit(persona.getApellido())) {
			throw new Exception("Nombre y el apellido no pueden contener digitos!");
		}
		
		// Validamos el numero de documento
		if (Utils.cadContainsLetters(persona.getNroDocumento().toString())) {
			throw new Exception("Numero de documento no valido!");
		}
		
		// Validamos el numero de telefono
		if (Utils.cadContainsLetters(persona.getTelefono().toString())) {
			throw new Exception("Numero de telefono no valido!");
		}
		
		// Validamos el email
		if (!Utils.validaEmail(persona.getEmail())) {
			throw new Exception("Formato de email no valido!");
		}
		
		// Validamos la seguridad del password antes del hash
		if (!Utils.validaSeguridadPassword(persona.getPassword())) {
			throw new Exception("Password no segura!");
		}
		String passwordHash = null;
		passwordHash = Hash.getPasswordHashSHA512(persona.getPassword());
		persona.setPassword(passwordHash);
		
		// Validamos el cuit
		String cuit = persona.getCuit();
		String dni = persona.getNroDocumento().toString();
		String genero = persona.getGenero();
		if (!Utils.validarCuit(cuit, dni, genero)) {
			throw new Exception("CUIT/CUIL no valido!");
		}
		
		// Asignamos el rol correspondiente a la persona
		List<Rol> roles = setRol(persona.getRol());
		if (Utils.isNullOrEmpty(roles)) {
			persona.setRol(rolRepository.findByNombre(Rol.CLIENTE));
			persona.setSueldoMensual(null);
			persona.setDescripcion(null);
			persona.setLegajo(null);
		} else {
			persona.setRol(rolRepository.findById(persona.getRol().getId()));
		}
		
		persona.setFechaCreacion(new Date());
		persona.setFechaEliminacion(null);
		
		// Antes de persistir la persona debemos crear el domicilio
		persona.setDomicilio(domicilioCtrl.registrarDomicilio(persona.getDomicilio()));
	}
	
	private Persona actualizar(Long id, Persona persona) throws Exception {
		logger.log(Level.INFO, "Ingresa a actualizar()");

		// Obtenemos la persona de la BD por su id
		Persona personaDb = personaRepository.findById(id);
		
		if (personaDb == null) {
			throw new Exception("No existe la persona en la base de datos!");
		}
		
		if (!Utils.validaEmail(persona.getEmail())) {
			throw new Exception("Formato de email no valido!");
		}
		personaDb.setEmail(persona.getEmail());
		
		if (Utils.cadContainsLetters(persona.getTelefono().toString())) {
			throw new Exception("Numero de telefono no valido!");
		}
		persona.setTelefono(persona.getTelefono());
		
		// Si la persona tiene rol empleado o rol administrador
		if (personaDb.getRol().getNombreRol().equalsIgnoreCase(Rol.EMPLEADO) || personaDb.getRol().getNombreRol().equalsIgnoreCase(Rol.ADMINISTRADOR)) {
			personaDb.setSueldoMensual(persona.getSueldoMensual());
			personaDb.setDescripcion(persona.getDescripcion());
		}
		return personaRepository.update(personaDb);
	}
	
	private List<Rol> setRol(Rol rol) {
		List<Rol> roles = new ArrayList<Rol>();
		if (rol != null) {
			roles.add(rol);
			return roles;
		} 
		return roles;
	}
}

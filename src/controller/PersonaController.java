package controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

public class PersonaController {

	private Logger logger = LogManager.getLogger(getClass());
	private PersonaRepository personaRepository = new PersonaRepository();
	private RolRepository rolRepository = new RolRepository();
	
	public Persona registrarPersona(Persona persona) throws Exception {
		logger.log(Level.INFO, "Ingresa a registrarPersona()");
		preValidation(persona, true);
		String passwordHash = persona.getPassword();
		persona.setPassword(passwordHash);
		List<Rol> roles = new ArrayList<Rol>();
		roles.add(persona.getRol());
		if (Utils.isNullOrEmpty(roles)) {
			persona.setRol(rolRepository.findByNombre(Rol.CLIENTE));
		} else {
			persona.setRol(rolRepository.findById(persona.getRol().getId()));
		}
		Persona personaDB = personaRepository.save(persona);
		return persona;
	}
	
	public List<Persona> getAll() throws Exception {
		logger.log(Level.INFO, "Ingresa a getAll()");
		return personaRepository.findAll();
	}
	
	public Persona getById(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a getById()");
		return personaRepository.findById(id);
	}
	
	public Persona getByDocumento(Persona persona) throws Exception {
		logger.log(Level.INFO, "Ingresa a getByDocumento()");
		return personaRepository.findByTipoAndNroDocumento(persona.getTipoDocumento(), persona.getNroDocumento());
	}
	
	public void delete(Long id) throws Exception {
		logger.log(Level.INFO, "Ingresa a delete()");
		personaRepository.delete(id);
	}
	
	private void preValidation(Persona persona, boolean isCreate) throws Exception {
		logger.log(Level.INFO, "Ingresa a preValidation()");
		Persona personaDB = personaRepository.existPersonaByEmailOrTipoAndNroDocumento(persona);
		if (personaDB != null) {
			throw new Exception ("El usuario ya existe en la base de datos");
		}
	}
	
	private String generarHashPassword(String password) throws UnsupportedEncodingException {
		logger.log(Level.INFO, "Ingresa a generarHashPassword()");
		String passwordHash = Hash.getPasswordHashSHA512(password);
		return password;
	}
}

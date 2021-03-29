package controller;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import data.PasswordResetTokenRepository;
import data.PersonaRepository;
import entities.PasswordResetToken;
import entities.Persona;
import exceptions.LoginException;
import security.JwtManager;
import utils.Hash;
import utils.Utils;
import utils.email.EmailService;

public class LoginController {

	private Logger logger = LogManager.getLogger(getClass());
	private PersonaRepository personaRepository = new PersonaRepository();
	private PasswordResetTokenRepository prtRepository = new PasswordResetTokenRepository();
	private EmailService emailService = new EmailService();
	private JwtManager jwtManager = new JwtManager();
	
	public Persona login(Persona persona) throws Exception {
		logger.log(Level.INFO, "Ingresa a login()");
		Persona personaDB = personaRepository.findByEmail(persona.getEmail());
		if (personaDB == null) {
			throw new LoginException("Usuario o Contraseña Incorrectas", Level.ERROR);
		}
		if (!this.validarPassword(personaDB.getPassword(), persona.getPassword())) {
			throw new LoginException("Usuario o Contraseña Incorrectas", Level.ERROR);
		}
		// Generamos el JsonWebToken(JWT) 
		String jwt = jwtManager.createToken(personaDB.getEmail(), personaDB.getRol().getNombreRol());
		personaDB.setToken(jwt);
		return personaDB;
	}
	
	public void requestResetPassword(String email) throws Exception {
		// Primero validamos que el email pertenezca a un usuario activo del sistema
		if (!Utils.validaEmail(email)) {
            throw new Exception();
		}
		
		Persona persona = personaRepository.findByEmail(email);
		if (persona.getId() == null) {
			throw new Exception("No existe el usuario en la base de datos del sistema!");
		}
		
		// Generamos el token de seguridad
        final String token = UUID.randomUUID().toString();
        PasswordResetToken prt = new PasswordResetToken(null, persona.getId(), token);
        prtRepository.save(prt);
        // Cambiar luego el host al host de prd
        String body = "http://localhost:4200/modificar-contraseña?token=" + token;
        emailService.sendEmail(persona, body, "Solicitud Cambio de Contraseña");
	}
	
	public void resetPassword(String token, String newPassword) throws Exception {
		// Validamos que el token sea valido
		PasswordResetToken prt = prtRepository.findByToken(token);
		if (prt.getId() == null) {
			throw new Exception("Token invalido!");
		}
		
		Persona persona = personaRepository.findById(prt.getIdUsuario());
		String passwordHash = null;
		passwordHash = Hash.getPasswordHashSHA512(newPassword);
		persona.setPassword(passwordHash);
		personaRepository.actualizarContraseña(persona);
	}
	
	private boolean validarPassword(String passwordDB, String password) throws UnsupportedEncodingException {
		logger.log(Level.INFO, "Ingresa a validarPassword()");
		password = Hash.getPasswordHashSHA512(password);
		if (passwordDB.equalsIgnoreCase(password)) {
			return true;
		}
		return false;
	}
}

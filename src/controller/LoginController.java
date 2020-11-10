package controller;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import data.PersonaRepository;
import entities.Persona;
import exceptions.LoginException;
import utils.Hash;

public class LoginController {

	private Logger logger = LogManager.getLogger(getClass());
	private PersonaRepository personaRepository = new PersonaRepository();
	
	public Persona login(Persona persona) throws Exception {
		logger.log(Level.INFO, "Ingresa a login()");
		Persona personaDB = personaRepository.findByEmail(persona.getEmail());
		if (personaDB == null) {
			throw new LoginException("Usuario o Contraseña Incorrectas", Level.ERROR);
		}
		if (!this.validarPassword(personaDB.getPassword(), persona.getPassword())) {
			throw new LoginException("Usuario o Contraseña Incorrectas", Level.ERROR);
		}
		return personaDB;
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

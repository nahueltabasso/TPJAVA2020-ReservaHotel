package utils;

import java.nio.file.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import data.PersonaRepository;
import entities.Persona;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import security.JwtManager;

public class AppSession {

	private static final Logger logger = LogManager.getLogger(AppSession.class);
	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer";
	private static PersonaRepository personaRepository = new PersonaRepository();
	
	public static Persona getUsuarioLogueado(HttpServletRequest request) throws Exception {
		logger.info("Ingresa a getUsuarioLogueado()");
		String jwt = getBearerToken(request);
		if (jwt != null && !jwt.isEmpty()) {
			JwtManager jwtManager = new JwtManager();
			Jws<Claims> jws = jwtManager.parseToken(jwt);
			String emailUser = jws.getBody().getSubject();
			Persona personaLogin = personaRepository.findByEmail(emailUser);
			return personaLogin;
		}
		return null;
	}
	
	private static String getBearerToken(HttpServletRequest request) throws AccessDeniedException {
		logger.info("Ingresa a getBearerToken()");
		String authHeader = request.getHeader(AUTH_HEADER_KEY);
		if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		} else {
			throw new AccessDeniedException("Acceso Denegado");
		}
	}
}

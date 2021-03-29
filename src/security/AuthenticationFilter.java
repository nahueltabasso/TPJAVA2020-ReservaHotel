/*
package security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebFilter
public class AuthenticationFilter implements Filter {

	private static final Logger logger = LogManager.getLogger(AuthenticationFilter.class);
	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer";
	private static final int STATUS_CODE_UNAUTHORIZED = 401;
	
	@Override
	public void destroy() {
		logger.info("AuthenticationFilter destruido");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		boolean loggedIn = false;
		try {
			String jwt = getBearerToken(httpRequest);
			
			if (jwt != null && !jwt.isEmpty()) {
				httpRequest.login(jwt, "");
				loggedIn = true;
				logger.info("Logueado con JWT");
			} else {
				logger.info("No provee JWT, Debe autenticarse primero");
			}
			
			filterChain.doFilter(request, response);
			if (loggedIn) {
				httpRequest.logout();
				logger.info("Logout");
			}
		} catch(final Exception e) {
			logger.log(org.apache.logging.log4j.Level.WARN, "Fallo el token de seguridad");
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setContentLength(0);
			httpResponse.setStatus(STATUS_CODE_UNAUTHORIZED);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("AuthenticationFilter inicializado!");
	}
	
	private String getBearerToken(HttpServletRequest request) {
		String authHeader = request.getHeader(AUTH_HEADER_KEY);
		if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		}
		return null;
	}

}*/

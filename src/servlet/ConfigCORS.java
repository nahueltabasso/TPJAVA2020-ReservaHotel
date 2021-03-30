package servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfigCORS implements Filter {

	public ConfigCORS() {}
	
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		System.out.println("CORS Config Filter HTTP Request: " + request.getMethod());

		// Autorizamos los dominios para que consuman los recursos de la Apis
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "*");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Max-Age", "1728000");
		
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		// Para el metodo OPTIONS, La respuesta es HTTP 200 (Aceptado) segun protocolo de CORS
		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
			response.setStatus(200);
			return;
		}
		
		chain.doFilter(request, servletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

}

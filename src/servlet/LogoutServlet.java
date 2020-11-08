package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import entities.Persona;

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
 
	
    public LogoutServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			request.getSession().invalidate();
			logger.log(Level.INFO, "Usuario: " + personaLogueada.getNombre() + " " + personaLogueada.getApellido() + " logout exitoso!");
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print("Logout con exito!");
		    response.getWriter().flush();
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage());
		}
	}

}

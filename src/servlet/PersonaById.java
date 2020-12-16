package servlet;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import controller.PersonaController;
import entities.Persona;
import response.MessageErrorResponse;

@WebServlet("/PersonaById")
public class PersonaById extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
	private PersonaController personaCtrl = new PersonaController();
	
    public PersonaById() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			
			Long id = Long.parseLong(request.getParameter("idPersona"));
			Persona persona = personaCtrl.getById(id);
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(persona));
			response.setStatus(200);
		    response.getWriter().flush();
		} catch (AccessDeniedException e) {
			logger.log(Level.ERROR, e.getMessage());
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(401);
			response.getWriter().print(gson.toJson(mensaje));
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(500);
			response.getWriter().print(gson.toJson(mensaje));
		}
	}

}

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

import controller.EstadoReservaController;
import entities.EstadoReserva;
import entities.Persona;

@WebServlet("/EstadoReserva")
public class EstadoReservaByDescripcion extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
    private EstadoReservaController estadoReservaCtrl = new EstadoReservaController();
    
    public EstadoReservaByDescripcion() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		
		try {
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			if (personaLogueada == null) {
				// Excepcion 
				throw new AccessDeniedException("Acceso denegado");
			}
			String descripcion = request.getParameter("descripcion");

			EstadoReserva estado = estadoReservaCtrl.getByDescripcion(descripcion);
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(estado));
		    response.setStatus(200);
		    response.getWriter().flush();
		} catch (AccessDeniedException e) {
			response.getWriter().print(e.getMessage());
			response.setStatus(401);
			e.printStackTrace();
		} catch (Exception e) {
			Logger logger = LogManager.getLogger(getClass());
			logger.log(Level.ERROR, e.getMessage());		
			response.getWriter().print(e.getMessage());
			response.setStatus(500);
		}

	}

}

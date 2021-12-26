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
import response.MessageErrorResponse;
import utils.AppSession;
import utils.HttpStatusCode;

@WebServlet("/EstadoReservaByDescripcion")
public class EstadoReservaByDescripcion extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
    private EstadoReservaController estadoReservaCtrl = new EstadoReservaController();
    
    public EstadoReservaByDescripcion() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		
		try {
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			if (personaLogueada == null) {
				// Excepcion 
				throw new AccessDeniedException("Acceso denegado");
			}
			String descripcion = request.getParameter("descripcion");
			
			EstadoReserva estado = estadoReservaCtrl.getByDescripcion(descripcion);
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(estado));
		    response.setStatus(HttpStatusCode.HTTP_STATUS_OK);
		    response.getWriter().flush();
		} catch (AccessDeniedException e) {
			logger.log(Level.ERROR, e.getMessage());
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(HttpStatusCode.HTTP_STATUS_UNAUTHORIZED);
			response.getWriter().print(gson.toJson(mensaje));
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(HttpStatusCode.HTTP_STATUS_INTERNAR_SERVER_ERROR);
			response.getWriter().print(gson.toJson(mensaje));
		}

	}

}

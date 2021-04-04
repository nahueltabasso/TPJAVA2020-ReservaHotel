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

import controller.ReservaController;
import entities.Persona;
import response.MessageErrorResponse;
import utils.AppSession;
import utils.HttpStatusCode;

@WebServlet("/CancelarReserva")
public class CancelarReservaServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
    private ReservaController reservaCtrl = new ReservaController();   

    public CancelarReservaServlet() {}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			// Recuperamos el usuario logueado
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			// Validamos que tipo de persona se esta creando de acuerdo al rol
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			
			Long id = Long.parseLong(request.getParameter("idReserva"));
			
			// Si llego aca implica que se cumplen con los permisos, persistimos el objeto
			reservaCtrl.cancelarReserva(id);
			
			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print("Reserva Cancelada");
			response.setStatus(HttpStatusCode.HTTP_STATUS_CREATED);
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

package servlet;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

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
import entities.Reserva;
import entities.Rol;
import response.MessageErrorResponse;
import utils.JsonToJavaObject;

@WebServlet("/Reserva")
public class ReservaServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
    private ReservaController reservaCtrl = new ReservaController();   

    public ReservaServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			
			List<Reserva> reservaList = new ArrayList<Reserva>();
			if (personaLogueada.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				reservaList = reservaCtrl.getAllByPersona(personaLogueada);
			} else {
				reservaList = reservaCtrl.getAll();
			}
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(reservaList));
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			String payloadRequest = JsonToJavaObject.getBody(request);
			
			Reserva reserva = new Reserva();
			reserva = new Gson().fromJson(payloadRequest, Reserva.class);
			
			// Recuperamos el usuario logueado
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			
			// Validamos que tipo de persona se esta creando de acuerdo al rol
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			
			// Si llego aca implica que se cumplen con los permisos, persistimos el objeto
			Reserva reservaDb = reservaCtrl.registrarReserva(reserva);

			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(reservaDb));
			response.setStatus(201);
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

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			Long id = Long.parseLong(request.getParameter("idReserva"));
			reservaCtrl.delete(id);
			logger.log(Level.INFO, "Reserva eliminada con exito");
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

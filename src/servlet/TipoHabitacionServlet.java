package servlet;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
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
import controller.TipoHabitacionController;
import entities.Persona;
import entities.Rol;
import entities.TipoHabitacion;
import response.MessageErrorResponse;
import utils.AppSession;
import utils.HttpStatusCode;
import utils.JsonToJavaObject;

@WebServlet("/TipoHabitacion")
public class TipoHabitacionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
	private TipoHabitacionController tipoHabitacionController = new TipoHabitacionController();
    
    public TipoHabitacionServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			if (personaLogueada == null ) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			
			List<TipoHabitacion> tipoHabitacionList = tipoHabitacionController.getAll();
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(tipoHabitacionList));
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			String payloadRequest = JsonToJavaObject.getBody(request);
			
			TipoHabitacion tipoHabitacion = new TipoHabitacion();
			tipoHabitacion = new Gson().fromJson(payloadRequest, TipoHabitacion.class);
			
			// Recuperamos el usuario logueado
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			// Validamos que tipo de persona está creando el tipo de habitación
			
			if (personaLogueada == null || personaLogueada.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				throw new AccessDeniedException("Acceso Denegado - Solo un perfil Administrador o Empleado puede crear Tipos de Habitaciones");
			}
			
			// Si llego aca implica que se cumplen con los permisos, persistimos el objeto
			TipoHabitacion tipoHabitacionDb = tipoHabitacionController.registrarTipoHabitacion(tipoHabitacion);
			logger.log(Level.INFO, "Tipo De Habitación: " + tipoHabitacionDb.getDescripcion() + " registrada con exito!");

			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(tipoHabitacionDb));
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
			response.setStatus(500);
			response.getWriter().print(gson.toJson(mensaje));
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			if (personaLogueada == null || personaLogueada.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			Long id = Long.parseLong(request.getParameter("idTipoHabitacion"));
			tipoHabitacionController.delete(id);
			
			logger.log(Level.INFO, "Habitación eliminada con exito");
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpStatusCode.HTTP_STATUS_NO_CONTENT);
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

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			String payloadRequest = JsonToJavaObject.getBody(request);
			
			TipoHabitacion tipoHabitacion = new TipoHabitacion();
			tipoHabitacion = new Gson().fromJson(payloadRequest, TipoHabitacion.class);
			
			// Recuperamos el usuario logueado
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			// Validamos que el usuario este logueado
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso Denegado");
			}

			// Si el usuario logueado es un cliente, no puede modificar las tipoHabitaciones
			if (personaLogueada.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				throw new AccessDeniedException("Acceso Denegado - Un perfil con rol Cliente no puede modificar los datos los Tipos de Habitaciones!");
 			}
			
			// Si llego aca implica que se cumplen con los permisos, persistimos el objeto
			Long id = Long.parseLong(request.getParameter("idTipoHabitacion"));
			TipoHabitacion tipoHabitacionDB = tipoHabitacionController.actualizarTipoHabitacion(id, tipoHabitacion);
			logger.log(Level.INFO, "Habitación: " + tipoHabitacionDB.getDescripcion() + " actualizado con exito!");

			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(tipoHabitacionDB));
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

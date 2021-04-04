package servlet;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
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

import controller.HabitacionController;
import entities.Habitacion;
import entities.Persona;
import response.MessageErrorResponse;
import utils.AppSession;
import utils.HttpStatusCode;

import java.text.SimpleDateFormat;  

@WebServlet("/HabitacionesDisponibles")
public class HabitacionesDisponiblesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
	private HabitacionController habitacionCtrl = new HabitacionController();
       
    public HabitacionesDisponiblesServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			
			// Obtenemos los parametros del request
			Long idTipoHabitacion = Long.parseLong(request.getParameter("idTipoHabitacion"));
		    Date fechaDesde = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(request.getParameter("fechaDesde"));  
			
			List<Habitacion> habitacionesDisponibles = habitacionCtrl.getHabitacionesDisponiblesParaReservar(idTipoHabitacion, fechaDesde);
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(habitacionesDisponibles));
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

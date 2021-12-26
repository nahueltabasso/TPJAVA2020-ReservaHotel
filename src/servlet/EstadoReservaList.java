package servlet;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import controller.EstadoReservaController;
import entities.EstadoReserva;
import entities.Persona;
import response.MessageErrorResponse;
import utils.AppSession;
import utils.HttpStatusCode;

@WebServlet("/EstadoReservaList")
public class EstadoReservaList extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private EstadoReservaController estadoReservaCtrl = new EstadoReservaController();
       
    public EstadoReservaList() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		
		try {
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			if (personaLogueada == null) {
				// Excepcion 
				throw new AccessDeniedException("Acceso denegado");
			}
			List<EstadoReserva> estados = estadoReservaCtrl.getAll();
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(HttpStatusCode.HTTP_STATUS_OK);
		    response.getWriter().print(gson.toJson(estados));
		    response.getWriter().flush();
		} catch (AccessDeniedException e) {
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(HttpStatusCode.HTTP_STATUS_UNAUTHORIZED);
			response.getWriter().print(gson.toJson(mensaje));
		} catch (Exception e) {
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(HttpStatusCode.HTTP_STATUS_INTERNAR_SERVER_ERROR);
			response.getWriter().print(gson.toJson(mensaje));
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

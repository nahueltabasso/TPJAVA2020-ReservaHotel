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
import controller.FacturaController;
import entities.Persona;
import entities.Rol;
import entities.Factura;
import response.MessageErrorResponse;
import utils.AppSession;
import utils.HttpStatusCode;
import utils.JsonToJavaObject;

@WebServlet("/Factura")
public class FacturaServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
	private FacturaController facturaController = new FacturaController();
    
    public FacturaServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			if (personaLogueada == null ) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			
			List<Factura> facturaList = facturaController.getAll();
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(facturaList));
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
			request.setCharacterEncoding("UTF-8");
			String payloadRequest = JsonToJavaObject.getBody(request);
			
			Factura factura = new Factura();
			factura = new Gson().fromJson(payloadRequest, Factura.class);
			
			// Recuperamos el usuario logueado
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			// Validamos que tipo de persona está creando la factura
			if (personaLogueada == null || personaLogueada.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				throw new AccessDeniedException("Acceso Denegado - Solo un perfil Administrador o Empleado puede crear Facturas");
			}
			
			// Si llego aca implica que se cumplen con los permisos, persistimos el objeto
			Factura facturaDB = facturaController.registrarFactura(factura);
			logger.log(Level.INFO, "Factura: " + facturaDB.getNumeroFactura() + " registrada con exito!");

			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(facturaDB));
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

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			if (personaLogueada == null || personaLogueada.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			Long id = Long.parseLong(request.getParameter("idFactura"));
			facturaController.delete(id);
			logger.log(Level.INFO, "Factura eliminada con exito");
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
	
	/* Las facturas no se pueden actualizar */
}

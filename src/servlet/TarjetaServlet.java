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
import controller.TarjetaController;
import entities.Persona;
import entities.Tarjeta;
import response.MessageErrorResponse;
import utils.JsonToJavaObject;

@WebServlet("/Tarjeta")
public class TarjetaServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
	private TarjetaController tarjetaController = new TarjetaController();
    
    public TarjetaServlet() {}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			
			if (personaLogueada == null ) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			
			List<Tarjeta> tarjetaList = tarjetaController.getAll();
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(tarjetaList));
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
			request.setCharacterEncoding("UTF-8");
			String payloadRequest = JsonToJavaObject.getBody(request);
			
			Tarjeta tarjeta = new Tarjeta();
			tarjeta = new Gson().fromJson(payloadRequest, Tarjeta.class);
			
			// Recuperamos el usuario logueado
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			
			// Validamos que la persona esté logueada antes de crear la tarjeta
			
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso Denegado - Debe estar logueado");
			}
			
			// Si llego aca implica que se cumplen con los permisos, persistimos el objeto
			Tarjeta tarjetaDB = new Tarjeta();
			tarjetaDB = tarjetaController.registrarTarjeta(tarjeta);
			logger.log(Level.INFO, "Tarjeta " + tarjetaDB.getNumeroTarjeta() + " registrada con exito!");
			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(tarjetaDB));
			response.setStatus(201);
		    response.getWriter().flush();
		    
		} catch (AccessDeniedException e) {
			logger.log(Level.ERROR, e.getMessage());
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(401);
			response.getWriter().print(gson.toJson(mensaje));
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
			System.out.println(e);
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(500);
			response.getWriter().print(gson.toJson(mensaje));
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			Long id = Long.parseLong(request.getParameter("idTarjeta"));
			tarjetaController.delete(id);
			logger.log(Level.INFO, "Tarjeta eliminada con éxito");
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
	
	// No actualizamos la tarjeta. Omitimos el doPut

}

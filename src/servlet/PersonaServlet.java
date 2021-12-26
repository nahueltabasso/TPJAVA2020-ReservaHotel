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

import controller.PersonaController;
import entities.Persona;
import entities.Rol;
import response.MessageErrorResponse;
import utils.AppSession;
import utils.HttpStatusCode;
import utils.JsonToJavaObject;

@WebServlet("/Persona")
public class PersonaServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
	private PersonaController personaCtrl = new PersonaController();
    
    public PersonaServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			if (personaLogueada == null || personaLogueada.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				throw new AccessDeniedException("Acceso denegado!");
			}
			
			List<Persona> personaList = personaCtrl.getAll();
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(personaList));
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
			
			Persona persona = new Persona();
			persona = new Gson().fromJson(payloadRequest, Persona.class);

			// Si el rol es igual a null implica que se registra un usuario comun por lo tanto no pasa la validacion de seguridad
			if (persona.getRol() != null) {
				
				// Recuperamos el usuario logueado
				Persona personaLogueada = AppSession.getUsuarioLogueado(request);
				
				// Validamos que tipo de persona se esta creando de acuerdo al rol
				if (personaLogueada == null) {
					if (persona.getRol() != null) {
						throw new AccessDeniedException("Acceso Denegado - Solo un perfil Administrador puede crear Empleados o Administradores");
					}
				} else {
					if (personaLogueada == null || !personaLogueada.getRol().getNombreRol().equalsIgnoreCase(Rol.ADMINISTRADOR)) {
						throw new AccessDeniedException("Acceso Denegado - Solo un perfil Administrador puede crear Empleados o Administradores");
					}
				}
			}
			
			// Si llego aca implica que se cumplen con los permisos, persistimos el objeto
			Persona personaDB = personaCtrl.registrarPersona(persona);
			logger.log(Level.INFO, "Usuario: " + personaDB.getNombre() + " " + personaDB.getApellido() + " registrada con exito!");

			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(personaDB));
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
			
			Long id = Long.parseLong(request.getParameter("idPersona"));
			personaCtrl.delete(id);
			
			logger.log(Level.INFO, "Persona eliminada con exito");
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
			
			Persona persona = new Persona();
			persona = new Gson().fromJson(payloadRequest, Persona.class);
			
			// Recuperamos el usuario logueado
			Persona personaLogueada = AppSession.getUsuarioLogueado(request);
			
			// Validamos que el usuario este logueado
			if (personaLogueada == null) {
				throw new AccessDeniedException("Acceso Denegado");
			}

			// Si el usuario logueado es un cliente validamos que este modificando sus propios datos personales
			if (persona.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				if (persona.getId() == null || persona.getId() != personaLogueada.getId()) {
					throw new AccessDeniedException("Acceso Denegado - Un perfil con rol Cliente no puede modificar los datos de otro usuario!");
 				}
			}
			
			// Si llego aca implica que se cumplen con los permisos, persistimos el objeto
			Long id = Long.parseLong(request.getParameter("idPersona"));
			Persona personaDB = personaCtrl.actualizarPersona(id, persona);
			logger.log(Level.INFO, "Usuario: " + personaDB.getNombre() + " " + personaDB.getApellido() + " actualizado con exito!");

			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(personaDB));
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

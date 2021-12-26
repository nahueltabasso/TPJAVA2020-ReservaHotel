package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import controller.LoginController;
import entities.Persona;
import response.MessageErrorResponse;
import utils.HttpStatusCode;
import utils.JsonToJavaObject;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
	private LoginController loginCtrl = new LoginController();
	
    public LoginServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			String payloadRequest = JsonToJavaObject.getBody(request);
			
			Persona persona = new Persona();
			persona = new Gson().fromJson(payloadRequest, Persona.class);
			
			Persona personaLogueada = loginCtrl.login(persona);
			logger.log(Level.INFO, "Usuario: " + persona.getNombre() + " " + persona.getApellido() + " logueado con exito!");
			request.getSession().setAttribute("usuario", personaLogueada);
			request.getSession().setAttribute("messageInfo", "Bienvenido al Sistema");
		
		    response.getWriter().print(new Gson().toJson(personaLogueada));
		    response.setStatus(HttpStatusCode.HTTP_STATUS_OK);
		    response.getWriter().flush();
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(HttpStatusCode.HTTP_STATUS_UNAUTHORIZED);
			response.getWriter().print(gson.toJson(mensaje));
		}
	}

}

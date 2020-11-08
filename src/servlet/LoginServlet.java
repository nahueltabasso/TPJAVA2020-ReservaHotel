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
import org.json.JSONObject;

import com.google.gson.Gson;

import controller.LoginController;
import entities.Persona;
import entities.Rol;
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
		try {
			String payloadRequest = JsonToJavaObject.getBody(request);
			
			Persona persona = new Persona();
			persona = new Gson().fromJson(payloadRequest, Persona.class);
			
			Persona personaLogueada = loginCtrl.login(persona);
			logger.log(Level.INFO, "Usuario: " + persona.getNombre() + " " + persona.getApellido() + " logueado con exito!");
			request.getSession().setAttribute("usuario", personaLogueada);
			request.getSession().setAttribute("messageInfo", "Bienvenido al Sistema");
			
			JSONObject personaLogueadaJson = new JSONObject();
			personaLogueadaJson.put("id", personaLogueada.getId());
			personaLogueadaJson.put("nombre", personaLogueada.getNombre());
			personaLogueadaJson.put("apellido", personaLogueada.getApellido());
			personaLogueadaJson.put("email", personaLogueada.getEmail());
			personaLogueadaJson.put("tipoDocumento", personaLogueada.getTipoDocumento());
			personaLogueadaJson.put("numeroDocumento", personaLogueada.getNroDocumento());
			Rol r = personaLogueada.getRol(); 
			JSONObject rolJson = new JSONObject();
			rolJson.put("id", r.getId());
			rolJson.put("nombreRol", r.getNombreRol());
			personaLogueadaJson.put("rol", rolJson);
			System.out.println("Json : " + personaLogueadaJson);
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(personaLogueadaJson.toString());
		    response.getWriter().flush();
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
			e.printStackTrace();
		}
	}

}

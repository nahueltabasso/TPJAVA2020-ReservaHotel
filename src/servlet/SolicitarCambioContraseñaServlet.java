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
import response.MessageErrorResponse;
import utils.HttpStatusCode;

@WebServlet("/SolicitarCambioContraseña")
public class SolicitarCambioContraseñaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(getClass());
	private LoginController loginCtrl = new LoginController();
	
    public SolicitarCambioContraseñaServlet() {}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		try {
			String email = request.getParameter("email");
			loginCtrl.requestResetPassword(email);
			
			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(new Gson().toJson("{message:" + "Se te ha enviado un correo a tu casilla con los pasos a seguir! }"));
			response.setStatus(HttpStatusCode.HTTP_STATUS_OK);
		    response.getWriter().flush();
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(HttpStatusCode.HTTP_STATUS_INTERNAR_SERVER_ERROR);
			response.getWriter().print(gson.toJson(mensaje));
		}
	}

}

package servlet;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

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
			loginCtrl.requestResertPassword(email);
			
			// Response
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print("Se ha enviado un mail a su correo. Por favor revise su casilla!");
			response.setStatus(201);
		    response.getWriter().flush();
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
			MessageErrorResponse mensaje = new MessageErrorResponse(e.getMessage());
			response.setStatus(500);
			response.getWriter().print(gson.toJson(mensaje));
		}
	}

}

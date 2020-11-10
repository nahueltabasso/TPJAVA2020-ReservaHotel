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
import org.json.JSONArray;
import org.json.JSONObject;

import controller.EstadoReservaController;
import entities.EstadoReserva;
import entities.Persona;

@WebServlet("/EstadoReservaList")
public class EstadoReservaList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EstadoReservaController estadoReservaCtrl = new EstadoReservaController();
       
    public EstadoReservaList() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Persona personaLogueada = (Persona) request.getSession().getAttribute("usuario");
			if (personaLogueada == null) {
				// Excepcion 
				throw new AccessDeniedException("Acceso denegado");
			}
			
			List<EstadoReserva> estados = estadoReservaCtrl.getAll();
			JSONArray dtoListJson = new JSONArray();
			for (EstadoReserva e : estados) {
				JSONObject estadoJson = new JSONObject();
				estadoJson.put("id", e.getId());
				estadoJson.put("descripcion", e.getDescripcion());
				dtoListJson.put(estadoJson);
			}
			System.out.println("Json : " + dtoListJson);
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(dtoListJson.toString());
		    response.getWriter().flush();

		} catch (AccessDeniedException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Logger logger = LogManager.getLogger(getClass());
			logger.log(Level.ERROR, e.getMessage());			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

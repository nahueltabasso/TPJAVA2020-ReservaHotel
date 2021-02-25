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

import controller.LocalidadController;
import entities.Localidad;
import entities.Persona;

@WebServlet("/LocalidadByProvincia")
public class LocalidadByProvinciaServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private LocalidadController localidadCtrl = new LocalidadController();
	
	public LocalidadByProvinciaServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		
		try {
			Long idProvincia = Long.parseLong(request.getParameter("idProvincia"));
			List<Localidad> localidades = localidadCtrl.getLocalidadesByProvincia(idProvincia);
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(localidades));
		    response.getWriter().flush();
		} catch (Exception e) {
			Logger logger = LogManager.getLogger(getClass());
			logger.log(Level.ERROR, e.getMessage());			
		}
	}

}

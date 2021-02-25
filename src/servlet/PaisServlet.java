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

import controller.PaisController;
import entities.Pais;
import entities.Persona;

@WebServlet("/Pais")
public class PaisServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private PaisController paisCtrl = new PaisController();   
	
    public PaisServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		
		try {
			List<Pais> paises = paisCtrl.getAll();
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(gson.toJson(paises));
		    response.getWriter().flush();
		} catch (Exception e) {
			Logger logger = LogManager.getLogger(getClass());
			logger.log(Level.ERROR, e.getMessage());			
		}	
	}

}

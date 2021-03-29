package servlet;

import java.io.IOException;
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

import controller.ProvinciaController;
import entities.Provincia;
import utils.HttpStatusCode;

@WebServlet("/ProvinciaByPais")
public class ProvinciaByPaisServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ProvinciaController provinciaCtrl = new ProvinciaController();
       
    public ProvinciaByPaisServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		
		try {
			Long idPais = Long.parseLong(request.getParameter("idPais"));
			List<Provincia> provincias = provinciaCtrl.getAllProvinciasByPais(idPais);
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(HttpStatusCode.HTTP_STATUS_OK);
		    response.getWriter().print(gson.toJson(provincias));
		    response.getWriter().flush();
		} catch (Exception e) {
			Logger logger = LogManager.getLogger(getClass());
			logger.log(Level.ERROR, e.getMessage());			
		}
	}

}

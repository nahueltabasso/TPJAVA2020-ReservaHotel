package servlet;

import java.io.IOException;
import java.util.ArrayList;
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

import controller.RolController;
import entities.Rol;

@WebServlet("/RolList")
public class RolList extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private RolController rolCtrl = new RolController();
	
    public RolList() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Rol> roles = new ArrayList<Rol>();
			roles = rolCtrl.getAll();
			
			JSONArray dtoListJson = new JSONArray();
			for (Rol r : roles) {
				JSONObject rolJson = new JSONObject();
				rolJson.put("id", r.getId());
				rolJson.put("nombreRol", r.getNombreRol());
				dtoListJson.put(rolJson);
			}
			System.out.println("Json : " + dtoListJson);
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().print(dtoListJson.toString());
		    response.getWriter().flush();
		} catch (Exception e) {
			Logger logger = LogManager.getLogger(getClass());
			logger.log(Level.ERROR, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

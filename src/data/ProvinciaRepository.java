package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import entities.Pais;
import entities.Provincia;
import utils.Utils;

public class ProvinciaRepository {

	private PaisRepository paisRepository = new PaisRepository();
	
	/**
	 * Metodo que construye el objeto Provincia
	 * @param resultSet
	 * @return
	 */
	private Provincia buildProvincia(ResultSet resultSet) {
		Provincia provincia = new Provincia();
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.DATE_PATTERN);
		Date fechaCreacion = null;
		Date fechaEliminacion = null;
		try {
			provincia.setId(resultSet.getLong("id"));
			provincia.setNombre(resultSet.getNString("nombre"));
			fechaCreacion = resultSet.getDate("fechaCreacion");
			fechaEliminacion = resultSet.getDate("fechaEliminacion");
			if (fechaCreacion != null) {
				provincia.setFechaCreacion(sdf.parse(fechaCreacion.toString()));
			} 
			if (fechaEliminacion != null) {
				provincia.setFechaEliminacion(sdf.parse(fechaEliminacion.toString()));
			}
			Pais pais = paisRepository.findById(resultSet.getLong("idPais"));
			provincia.setPais(pais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return provincia;
	}
	
	/**
	 * Metodo que retorna una lista de provincias segun el pais
	 * @param idPais
	 * @return
	 */
	public List<Provincia> findProvinciasByIdPais(Long idPais) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Provincia> provinciaList = new ArrayList<Provincia>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from provincias where idPais = ? order by nombre asc");
			statement.setLong(1, idPais);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Provincia provincia = buildProvincia(resultSet);
				provinciaList.add(provincia);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return provinciaList;
	}
	
	/**
	 * Metodo que retorna una provincia segun su id
	 * @param id
	 * @return
	 */
	public Provincia findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Provincia provincia = new Provincia();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from provincias where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				provincia = buildProvincia(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return provincia;
	}
}

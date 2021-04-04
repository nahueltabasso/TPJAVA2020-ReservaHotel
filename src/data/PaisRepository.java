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
import utils.Utils;

public class PaisRepository {
	
	/**
	 * Metodo que construye el objeto Pais
	 * @param resultSet
	 * @return
	 */
	private Pais buildPais(ResultSet resultSet) {
		Pais pais = new Pais();
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.DATE_PATTERN);
		Date fechaCreacion = null;
		Date fechaEliminacion = null;
		try {
			pais.setId(resultSet.getLong("id"));
			pais.setNombre(resultSet.getNString("nombre"));
			fechaCreacion = resultSet.getDate("fechaCreacion");
			fechaEliminacion = resultSet.getDate("fechaEliminacion");
			if (fechaCreacion != null) {
				pais.setFechaCreacion(sdf.parse(fechaCreacion.toString()));
			} 
			if (fechaEliminacion != null) {
				pais.setFechaEliminacion(sdf.parse(fechaEliminacion.toString()));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return pais;
	}

	/**
	 * Metodo que retorna una lista con todos los paises
	 * @return
	 */
	public List<Pais> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Pais> paisList = new ArrayList<Pais>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from paises order by nombre asc");

			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Pais pais = buildPais(resultSet);
				paisList.add(pais);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		
		return paisList;
	}
	
	/**
	 * Metodo que retorna un pais por su id
	 * @param id
	 * @return
	 */
	public Pais findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Pais pais = new Pais();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from paises where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				pais = buildPais(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return pais;
	}
}

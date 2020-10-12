package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Provincia;

public class ProvinciaRepository {

	private PaisRepository paisRepository;
	
	/**
	 * Metodo que construye el objeto Provincia
	 * @param resultSet
	 * @return
	 */
	private Provincia buildProvincia(ResultSet resultSet) {
		Provincia provincia = new Provincia();
		try {
			provincia.setId(resultSet.getLong("id"));
			provincia.setNombre(resultSet.getNString("nombre"));
			provincia.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			provincia.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
			Long idPais = resultSet.getLong("idPais");
			provincia.setPais(paisRepository.findById(idPais));
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
			statement = connection.prepareStatement("select * from provincias where idPais = ?");
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

package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entities.Localidad;
import entities.Provincia;


public class LocalidadRepository {

	private ProvinciaRepository provinciaRepository = new ProvinciaRepository();
	
	/** 
	 * Metodo que construye el objeto Localidad
	 * @param resultSet
	 * @return
	 */
	private Localidad buildLocalidad(ResultSet resultSet) {
		Localidad localidad = new Localidad();
		try {
			localidad.setId(resultSet.getLong("id"));
			localidad.setNombre(resultSet.getString("nombre"));
			localidad.setCodigoPostal(resultSet.getString("codigoPostal"));
//			localidad.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			localidad.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
			Provincia provincia = provinciaRepository.findById(resultSet.getLong("idProvincia"));
			localidad.setProvincia(provincia);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localidad;
	}
	
	/**
	 * Metodo que retorna una lista de localidades segun la provincia
	 * @param idProvincia
	 * @return
	 */
	public List<Localidad> findAllByIdProvincia(Long idProvincia) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Localidad> localidadList = new ArrayList<Localidad>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from localidades where idProvincia = ? order by nombre asc");
			statement.setLong(1, idProvincia);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Localidad localidad = buildLocalidad(resultSet);
				localidadList.add(localidad);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return localidadList;
	}
	
	/**
	 * Metodo que retorna una Localidad por su id
	 * @param id
	 * @return
	 */
	public Localidad findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Localidad localidad = new Localidad();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from localidades where id = ?");
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				localidad = buildLocalidad(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return localidad;
	}
}

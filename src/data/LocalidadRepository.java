package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Localidad;

public class LocalidadRepository {

	private ProvinciaRepository provinciaRepository;
	
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
			localidad.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			localidad.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
			localidad.setProvincia(provinciaRepository.findById(resultSet.getLong("idProvincia")));
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
			statement = connection.prepareStatement("select * from localidades where idProvincia = ?");
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
}

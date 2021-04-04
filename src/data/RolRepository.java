package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import entities.Rol;
import utils.Utils;

public class RolRepository {

	private Rol buildRol(ResultSet rs) throws SQLException {
		Rol rol = new Rol();
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.DATE_PATTERN);
		Date fechaCreacion = null;
		Date fechaEliminacion = null;
		try {
			rol.setId(rs.getLong("id"));
			rol.setNombreRol(rs.getString("nombreRol"));
			fechaCreacion = rs.getDate("fechaCreacion");
			fechaEliminacion = rs.getDate("fechaEliminacion");
			if (fechaCreacion != null) {
				rol.setFechaCreacion(sdf.parse(fechaCreacion.toString()));
			} 
			if (fechaEliminacion != null) {
				rol.setFechaEliminacion(sdf.parse(fechaEliminacion.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rol;
	}
	
	/**
	 * Metodo que retorna todos los roles
	 * @return
	 */
	public List<Rol> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Rol> roles = new ArrayList<Rol>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from roles");
			resultSet = statement.executeQuery();

			if (resultSet != null) {
				while(resultSet.next()) {
					Rol rol = buildRol(resultSet);
					roles.add(rol);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return roles;
	}
	
	/**
	 * Metodo que retorna un rol por su id
	 * @param id
	 * @return
	 */
	public Rol findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Rol rol = new Rol();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from roles where id = ?");
			statement.setLong(1, id);

			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				rol = buildRol(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		
		return rol;
	}
	
 	public Rol findByNombre(String nombreRol) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Rol rol = new Rol();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from roles where nombreRol = ?");
			statement.setString(1, nombreRol);

			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				rol = buildRol(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		
		return rol;
	}
}

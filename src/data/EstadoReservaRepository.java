package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.EstadoReserva;

public class EstadoReservaRepository {

	private EstadoReserva buildEstadoReserva(ResultSet resultSet) {
		EstadoReserva estado = new EstadoReserva();
		try {
			estado.setId(resultSet.getLong("id"));
			estado.setDescripcion(resultSet.getString("descripcion"));
//			estado.setFechaCreacion(resultSet.getDate("fechaCreacion"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return estado;
	}
	
	public EstadoReserva findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		EstadoReserva estado = new EstadoReserva();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from estadoreservas where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				estado = buildEstadoReserva(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return estado;
	}
	
	public List<EstadoReserva> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<EstadoReserva> list = new ArrayList<EstadoReserva>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from estadoreservas");

			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				EstadoReserva estado = buildEstadoReserva(resultSet);
				list.add(estado);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return list;
	}
	
	public EstadoReserva findByDescripcion(String descripcion) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		EstadoReserva estado = new EstadoReserva();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from estadoreservas where descripcion = ?");
			statement.setString(1, descripcion);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				estado = buildEstadoReserva(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return estado;
	}
}

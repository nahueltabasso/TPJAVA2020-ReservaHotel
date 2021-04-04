package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import entities.EstadoReserva;
import utils.Utils;

public class EstadoReservaRepository {

	private EstadoReserva buildEstadoReserva(ResultSet resultSet) {
		EstadoReserva estadoReservaReserva = new EstadoReserva();
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.DATE_PATTERN);
		Date fechaCreacion = null;
		Date fechaEliminacion = null;
		try {
			estadoReservaReserva.setId(resultSet.getLong("id"));
			estadoReservaReserva.setDescripcion(resultSet.getString("descripcion"));
			fechaCreacion = resultSet.getDate("fechaCreacion");
			fechaEliminacion = resultSet.getDate("fechaEliminacion");
			if (fechaCreacion != null) {
				estadoReservaReserva.setFechaCreacion(sdf.parse(fechaCreacion.toString()));
			} 
			if (fechaEliminacion != null) {
				estadoReservaReserva.setFechaEliminacion(sdf.parse(fechaEliminacion.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return estadoReservaReserva;
	}
	
	public EstadoReserva findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		EstadoReserva estadoReserva = new EstadoReserva();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from estadoreservas where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				estadoReserva = buildEstadoReserva(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return estadoReserva;
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
				EstadoReserva estadoReserva = buildEstadoReserva(resultSet);
				list.add(estadoReserva);
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
		EstadoReserva estadoReserva = new EstadoReserva();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from estadoreservas where descripcion = ?");
			statement.setString(1, descripcion);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				estadoReserva = buildEstadoReserva(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return estadoReserva;
	}
}

package data;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import entities.TipoHabitacion;
import exceptions.DataException;

public class TipoHabitacionRepository {
	
	private Logger logger = LogManager.getLogger(getClass());
	
	
	/**
	 * Metodo que construye el objeto TipoHabitacion
	 * @param resultSet
	 * @return
	 */
	private TipoHabitacion buildTipoHabitacion(ResultSet resultSet) {
		TipoHabitacion tipoHabitacion = new TipoHabitacion();
		try {
			tipoHabitacion.setId(resultSet.getLong("id"));
			tipoHabitacion.setDescripcion(resultSet.getNString("descripcion"));
			tipoHabitacion.setCapacidad(resultSet.getInt("capacidad"));
			tipoHabitacion.setFoto(resultSet.getBlob("foto"));
			tipoHabitacion.setDenominacion(resultSet.getNString("denominacion"));
			tipoHabitacion.setPrecioPorDia(resultSet.getFloat("precioPorDia"));
			tipoHabitacion.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			tipoHabitacion.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return tipoHabitacion;
	}
		
		
	/**
	 * Metodo que retorna un tipoHabitacion segun su id
	 * @param id
	 * @return
	 */
	public TipoHabitacion findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		TipoHabitacion tipoHabitacion = new TipoHabitacion();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from tipoHabitaciones where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				tipoHabitacion = buildTipoHabitacion(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return tipoHabitacion;
	}

		
	
	/**
	 * Metodo que inserta un registro en la base de datos
	 * @param tipoHabitacion
	 * @return
	 * @throws Exception
	 */
	public TipoHabitacion save(TipoHabitacion tipoHabitacion) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into tipoHabitaciones(descripcion, capacidad, foto, denominacion, precioPorDia, fechaCreacion, fechaEliminacion) "
												  + "values (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setString(1, tipoHabitacion.getDescripcion());
			statement.setInt(2, tipoHabitacion.getCapacidad());
			statement.setBlob(3, tipoHabitacion.getFoto());
			statement.setString(4, tipoHabitacion.getDenominacion());
			statement.setFloat(5, tipoHabitacion.getPrecioPorDia());
			statement.setDate(6, new Date(tipoHabitacion.getFechaCreacion().getTime()));
			// Fecha de eliminacion de tipoHabitacion se guarda como null
			statement.setNull(7, java.sql.Types.DATE);
					
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar el tipoHabitacion
			if (resultSet != null && resultSet.next()) {
				tipoHabitacion.setId(resultSet.getLong(1));
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			e.printStackTrace();
			throw new DataException(null, "Ocurrio un error en la base de datos, contactar con el Administrador del Sistema", Level.ERROR);
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
			
		}
		return tipoHabitacion;
	}
	
	/**
	 * Metodo que elimina un registro de la base de datos por su id
	 * @param id
	 * @throws Exception
	 */
	public void delete(Long id) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("delete from tipoHabitaciones where id = ?");
			statement.setLong(1, id);
			int row = statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw new DataException(null, "Ocurrio un error en la base de datos, contactar con el Administrador del Sistema", Level.ERROR);
		} finally {
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
	}



}

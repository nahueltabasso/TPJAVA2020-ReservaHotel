package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.Utils;
import java.text.SimpleDateFormat;
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
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.DATE_PATTERN);
		Date fechaCreacion = null;
		Date fechaEliminacion = null;
		try {
			tipoHabitacion.setId(resultSet.getLong("id"));
			tipoHabitacion.setDescripcion(resultSet.getNString("descripcion"));
			tipoHabitacion.setCapacidad(resultSet.getInt("capacidad"));
			tipoHabitacion.setFoto(resultSet.getBlob("foto"));
			tipoHabitacion.setDenominacion(resultSet.getNString("denominacion"));
			tipoHabitacion.setPrecioPorDia(resultSet.getDouble("precioPorDia"));
			fechaCreacion = resultSet.getDate("fechaCreacion");
			fechaEliminacion = resultSet.getDate("fechaEliminacion");
			if (fechaCreacion != null) {
				tipoHabitacion.setFechaCreacion(sdf.parse(fechaCreacion.toString()));
			} 
			if (fechaEliminacion != null) {
				tipoHabitacion.setFechaEliminacion(sdf.parse(fechaEliminacion.toString()));
			}
			
			System.out.println();
			System.out.println(resultSet.getDate("fechaCreacion"));
			System.out.println(resultSet.getDate("fechaCreacion").toString());
			System.out.println(tipoHabitacion.getFechaCreacion());
			System.out.println(tipoHabitacion.getFechaEliminacion());
			System.out.println();
			
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
	 * Metodo que retorna una lista de tipoHabitacion
	 * @param 
	 * @return
	 */
	public List<TipoHabitacion> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<TipoHabitacion> tipoHabitacionesList = new ArrayList<TipoHabitacion>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from tipoHabitaciones");
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				TipoHabitacion tipoHabitacion = buildTipoHabitacion(resultSet);
				tipoHabitacionesList.add(tipoHabitacion);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return tipoHabitacionesList;
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
			statement.setDouble(5, tipoHabitacion.getPrecioPorDia());
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
	 * Metodo que actualiza un registro de la base de datos
	 * @param tipoHabitacion
	 * @return
	 * @throws Exception
	 */
	
	public TipoHabitacion update(TipoHabitacion tipoHabitacion) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("update tipoHabitaciones set descripcion = ?, capacidad = ?, foto = ?, denominacion = ?, precioPorDia = ? WHERE id = ?");
			statement.setString(1, tipoHabitacion.getDescripcion());
			statement.setInt(2, tipoHabitacion.getCapacidad());				
			statement.setBlob(3, tipoHabitacion.getFoto());
			statement.setString(4, tipoHabitacion.getDenominacion());
			statement.setDouble(5, tipoHabitacion.getPrecioPorDia());
			statement.setLong(6, tipoHabitacion.getId());
			
			int row = statement.executeUpdate();
			tipoHabitacion = this.findById(tipoHabitacion.getId());
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
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

	// Devuelve true si ya existe la denominación o descripción de un tipo de habitación
	public boolean existTipoHabitacion (TipoHabitacion tipoHabitacion) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean row;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from tipoHabitaciones where denominacion = ? or descripcion = ?");
			statement.setString(1, tipoHabitacion.getDenominacion());
			statement.setString(2, tipoHabitacion.getDescripcion());
			resultSet = statement.executeQuery();
			row = resultSet.isBeforeFirst();
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}

		return row;
	}

}

package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import entities.Habitacion;
import exceptions.DataException;
import utils.Utils;

public class HabitacionRepository {
	
	private Logger logger = LogManager.getLogger(getClass());
	private TipoHabitacionRepository tipoHabitacionRepository = new TipoHabitacionRepository();
	
	/**
	 * Metodo que construye el objeto Habitacion
	 * @param resultSet
	 * @return
	 */
	private Habitacion buildHabitacion(ResultSet resultSet) {
		Habitacion habitacion = new Habitacion();
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.DATE_PATTERN);
		Date fechaCreacion = null;
		Date fechaEliminacion = null;
		try {
			habitacion.setId(resultSet.getLong("id"));
			habitacion.setNumeroHabitacion(resultSet.getInt("numeroHabitacion"));
			fechaCreacion = resultSet.getDate("fechaCreacion");
			fechaEliminacion = resultSet.getDate("fechaEliminacion");
			if (fechaCreacion != null) {
				habitacion.setFechaCreacion(sdf.parse(fechaCreacion.toString()));
			} 
			if (fechaEliminacion != null) {
				habitacion.setFechaEliminacion(sdf.parse(fechaEliminacion.toString()));
			}
			Long idTipoHabitacion = resultSet.getLong("idTipoHabitacion");
			habitacion.setTipoHabitacion(tipoHabitacionRepository.findById(idTipoHabitacion));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return habitacion;
	}
		
		
	/**
	 * Metodo que retorna un habitacion segun su id
	 * @param id
	 * @return
	 */
	public Habitacion findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Habitacion habitacion = new Habitacion();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from habitaciones where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				habitacion = buildHabitacion(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return habitacion;
	}

	/**
	 * Metodo que retorna una lista de todas las habitaciones
	 * @return
	 * @trhows Exception
	 */
	public List<Habitacion> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Habitacion> habitacionesList = new ArrayList<Habitacion>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from habitaciones");
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Habitacion habitacion = buildHabitacion(resultSet);
				habitacionesList.add(habitacion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return habitacionesList;
	}	
	
	/**
	 * Metodo que retorna una lista de habitaciones segun el tipo de habitación
	 * @param idFactura
	 * @return
	 */
	public List<Habitacion> findHabitacionesByIdTipoHabitacion(Long idTipoHabitacion) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Habitacion> habitacionesList = new ArrayList<Habitacion>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from habitaciones where idTipoHabitacion = ?");
			statement.setLong(1, idTipoHabitacion);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Habitacion habitacion = buildHabitacion(resultSet);
				habitacionesList.add(habitacion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return habitacionesList;
	}	
	
	/**
	 * Metodo que inserta un registro en la base de datos
	 * @param habitacion
	 * @return
	 * @throws Exception
	 */
	public Habitacion save(Habitacion habitacion) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into habitaciones(numeroHabitacion, fechaCreacion, fechaEliminacion, idTipoHabitacion) "
												  + "values (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setInt(1, habitacion.getNumeroHabitacion());
			statement.setDate(2, new Date(habitacion.getFechaCreacion().getTime()));
			// Fecha de eliminacion de habitacion se guarda como null
			statement.setNull(3, java.sql.Types.DATE);
			statement.setLong(4, habitacion.getTipoHabitacion().getId());
			
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar el habitacion
			if (resultSet != null && resultSet.next()) {
				habitacion.setId(resultSet.getLong(1));
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
		return habitacion;
	}
	
	
	/**
	 * Metodo que actualiza un registro de la base de datos
	 * @param habitacion
	 * @return
	 * @throws Exception
	 */
	
	public Habitacion update(Habitacion habitacion) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("update habitaciones set numeroHabitacion = ? WHERE id = ?");
			statement.setInt(1, habitacion.getNumeroHabitacion());
			statement.setLong(2, habitacion.getId());
			
			int row = statement.executeUpdate();
			habitacion = this.findById(habitacion.getId());
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return habitacion;
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
			statement = connection.prepareStatement("delete from habitaciones where id = ?");
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

	public boolean existHabitacionByNumeroHabitacion(Habitacion habitacion) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean row;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from habitaciones where numeroHabitacion = ? ");
			statement.setInt(1, habitacion.getNumeroHabitacion());
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
	
	public List<Habitacion> findHabitacionesDisponiblesParaReserva(Date fechaDesde, Long idTipoHabitacion) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Habitacion> habitacionesList = new ArrayList<Habitacion>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select hab.* " + 
													"from habitaciones hab " + 
													"inner join tipohabitaciones th on th.id = hab.idTipoHabitacion " + 
													"where hab.id not in (select res.idHabitacion from reservas res where ? not between res.fechaEntrada and res.fechaSalida and res.fechaCancelacion is null " + 
													"					and res.idHabitacion is not null) and th.id = ?");

			statement.setDate(1, fechaDesde);
			statement.setLong(2, idTipoHabitacion);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Habitacion habitacion = buildHabitacion(resultSet);
				habitacionesList.add(habitacion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return habitacionesList;
	}

}

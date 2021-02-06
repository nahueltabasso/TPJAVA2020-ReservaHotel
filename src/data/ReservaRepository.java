package data;

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

import entities.EstadoReserva;
import entities.Reserva;
import exceptions.DataException;

public class ReservaRepository {
	
	private Logger logger = LogManager.getLogger(getClass());
	private PersonaRepository personaRepository = new PersonaRepository();
	private EstadoReservaRepository estadoReservaRepository = new EstadoReservaRepository();
	private HabitacionRepository habitacionRepository = new HabitacionRepository();
	private SalonRepository salonRepository = new SalonRepository();
	
	/**
	 * Metodo que construye el objeto Reserva
	 * @param resultSet
	 * @return
	 */
	private Reserva buildReserva(ResultSet resultSet) {
		Reserva reserva = new Reserva();
		try {
			reserva.setId(resultSet.getLong("id"));
			reserva.setFechaReserva(resultSet.getDate("fechaReserva"));
			reserva.setFechaCancelacion(resultSet.getDate("fechaCancelacion"));
			reserva.setCantDias(resultSet.getInt("cantDias"));
			reserva.setFechaEntrada(resultSet.getDate("fechaEntrada"));
			reserva.setFechaSalida(resultSet.getDate("fechaSalida"));
			reserva.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			reserva.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
			Long idPersona = resultSet.getLong("idPersona");
			reserva.setPersona(personaRepository.findById(idPersona));
			Long idEstadoReserva = resultSet.getLong("idEstadoReserva");
			reserva.setEstadoReserva(estadoReservaRepository.findById(idEstadoReserva));
			Long idHabitacion = resultSet.getLong("idHabitacion");
			reserva.setHabitacion(habitacionRepository.findById(idHabitacion));
			Long idSalon = resultSet.getLong("idSalon");
			reserva.setSalon(salonRepository.findById(idSalon));
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		return reserva;
	}
		
		
	/**
	 * Metodo que retorna una reserva segun su id
	 * @param id
	 * @return
	 */
	public Reserva findById(Long id) throws Exception{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Reserva reserva = new Reserva();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from reservas where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				reserva = buildReserva(resultSet);
			}
		} catch(SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return reserva;
	}
	
	/**
	 * Metodo que retorna todas las reservas de la base de datos
	 * @return
	 * @throws Exception
	 */
	public List<Reserva> findAll() throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Reserva> list = new ArrayList<Reserva>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from reservas");
			resultSet = statement.executeQuery();
			
			if (resultSet != null) {
				while (resultSet.next()) {
					Reserva reserva = buildReserva(resultSet);
					list.add(reserva);
				}
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return list;
	}

	/**
	 * Metodo que retorna una lista de reservas segun el idEstadoReserva
	 * @param idEstadoReserva
	 * @return
	 */
	public List<Reserva> findReservasByIdEstadoReserva(Long idEstadoReserva) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Reserva> reservasList = new ArrayList<Reserva>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from reservas where idEstadoReserva = ?");
			statement.setLong(1, idEstadoReserva);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Reserva reserva = buildReserva(resultSet);
				reservasList.add(reserva);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return reservasList;
	}	
	
	/**
	 * Metodo que retorna una lista de reservas segun el idPersona
	 * @param idPersona
	 * @return
	 */
	public List<Reserva> findReservasByIdPersona(Long idPersona) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Reserva> reservasList = new ArrayList<Reserva>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from reservas where idPersona = ?");
			statement.setLong(1, idPersona);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Reserva reserva = buildReserva(resultSet);
				reservasList.add(reserva);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return reservasList;
	}	
	
	/**
	 * Metodo que retorna una lista de reservas segun el idHabitacion
	 * @param idHabitacion
	 * @return
	 */
	public List<Reserva> findReservasByIdHabitacion(Long idHabitacion) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Reserva> reservasList = new ArrayList<Reserva>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from reservas where idHabitacion = ?");
			statement.setLong(1, idHabitacion);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Reserva reserva = buildReserva(resultSet);
				reservasList.add(reserva);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return reservasList;
	}	
	
	/**
	 * Metodo que retorna una lista de reservas segun el idSalon
	 * @param idSalon
	 * @return
	 */
	public List<Reserva> findReservasByIdSalon(Long idSalon) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Reserva> reservasList = new ArrayList<Reserva>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from reservas where idSalon = ?");
			statement.setLong(1, idSalon);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Reserva reserva = buildReserva(resultSet);
				reservasList.add(reserva);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return reservasList;
	}	

	
	/**
	 * Metodo que inserta un registro en la base de datos
	 * @param reserva
	 * @return
	 * @throws Exception
	 */
	public Reserva save(Reserva reserva) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into reservas(fechaReserva, fechaCancelacion, cantDias, fechaEntrada, fechaSalida, fechaCreacion, fechaEliminacion, idPersona, idEstadoReserva, idHabitacion, idSalon) "
												  + "values (?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setDate(1, new Date(reserva.getFechaReserva().getTime()));
			// fechaCancelacion de reserva se guarda como null
			statement.setNull(2, java.sql.Types.DATE);
			statement.setInt(3, reserva.getCantDias());
			statement.setDate(4, new Date(reserva.getFechaEntrada().getTime()));
			statement.setDate(5, new Date(reserva.getFechaSalida().getTime()));
			statement.setDate(6, new Date(reserva.getFechaCreacion().getTime()));
			// fechaEliminacion de reserva se guarda como null
			statement.setNull(7, java.sql.Types.DATE);
			statement.setLong(8, reserva.getPersona().getId());
			statement.setLong(9, reserva.getEstadoReserva().getId());
			
			if (reserva.getHabitacion() != null) {
				statement.setLong(10, reserva.getHabitacion().getId());
				statement.setNull(11, java.sql.Types.BIGINT);				
			} else {
				statement.setNull(10, java.sql.Types.BIGINT);
				statement.setLong(11, reserva.getSalon().getId());				
			}
			
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar la reserva
			if (resultSet != null && resultSet.next()) {
				reserva.setId(resultSet.getLong(1));
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
		return reserva;
	}
	

	/**
	 * Metodo que actualiza un registro de la base de datos
	 * @param reserva
	 * @return
	 * @throws Exception
	 */
	
	public Reserva update(Reserva reserva) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("update reservas set fechaReserva = ?, set fechaCancelacion = ?, set cantDias = ?, set fechaEntrada = ?, set fechaSalida = ? WHERE id = ?");
			statement.setDate(1, new Date(reserva.getFechaReserva().getTime()));
			statement.setDate(2, new Date(reserva.getFechaCancelacion().getTime()));
			statement.setInt(3, reserva.getCantDias());				
			statement.setDate(4, new Date(reserva.getFechaEntrada().getTime()));
			statement.setDate(5, new Date(reserva.getFechaSalida().getTime()));
			statement.setLong(6, reserva.getId());
			
			int row = statement.executeUpdate();
			reserva = this.findById(reserva.getId());
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return reserva;
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
			statement = connection.prepareStatement("delete from reservas where id = ?");
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

	public int cancelar(Long id) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int row = 0;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("update reservas set fechaCancelacion = current_timestamp(), " 
													+ "idEstadoReserva = (select id from estadoreservas where descripcion = ?) WHERE id = ?");
			statement.setString(1, EstadoReserva.CANCELADA);
			statement.setLong(2, id);
			row = statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return row;
	}

}

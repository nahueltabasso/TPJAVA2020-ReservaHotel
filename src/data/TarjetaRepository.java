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

import entities.Salon;
import entities.Tarjeta;
import exceptions.DataException;

public class TarjetaRepository {
	
	private Logger logger = LogManager.getLogger(getClass());
	private PersonaRepository personaRepository = new PersonaRepository();
	
	/**
	 * Metodo que construye el objeto Tarjeta
	 * @param resultSet
	 * @return
	 */
	private Tarjeta buildTarjeta(ResultSet resultSet) {
		Tarjeta tarjeta = new Tarjeta();
		try {
			tarjeta.setId(resultSet.getLong("id"));
			tarjeta.setNumeroTarjeta(resultSet.getLong("numeroTarjeta"));
			tarjeta.setFechaVencimiento(resultSet.getDate("fechaVencimiento"));
			tarjeta.setNombreTarjeta(resultSet.getNString("nombreTarjeta"));
			tarjeta.setMonto(resultSet.getFloat("monto"));
			tarjeta.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			tarjeta.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
			Long idPersona = resultSet.getLong("idPersona");
			tarjeta.setPersona(personaRepository.findById(idPersona));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return tarjeta;
	}
		
		
	/**
	 * Metodo que retorna un tarjeta segun su id
	 * @param id
	 * @return
	 */
	public Tarjeta findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Tarjeta tarjeta = new Tarjeta();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from tarjetas where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				tarjeta = buildTarjeta(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return tarjeta;
	}

	/**
	 * Metodo que retorna una lista de tarjetas
	 * @param 
	 * @return
	 */
	public List<Tarjeta> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Tarjeta> tarjetaList = new ArrayList<Tarjeta>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from tarjetas");
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Tarjeta tarjeta = buildTarjeta(resultSet);
				tarjetaList.add(tarjeta);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return tarjetaList;
	}	
	
	/**
	 * Metodo que retorna una lista de tarjetas segun la persona
	 * @param idPersona
	 * @return
	 */
	public List<Tarjeta> findTarjetasByIdPersona(Long idPersona) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Tarjeta> tarjetaList = new ArrayList<Tarjeta>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from tarjetas where tarjetas.idPersona = ?");
			statement.setLong(1, idPersona);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Tarjeta tarjeta = buildTarjeta(resultSet);
				tarjetaList.add(tarjeta);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return tarjetaList;
	}	
	
	/**
	 * Metodo que inserta un registro en la base de datos
	 * @param tarjeta
	 * @return
	 * @throws Exception
	 */
	public Tarjeta save(Tarjeta tarjeta) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into tarjetas(numeroTarjeta, fechaVencimiento, nombreTarjeta, monto, fechaCreacion, fechaEliminacion, idPersona) "
												  + "values (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setLong(1, tarjeta.getNumeroTarjeta());
			statement.setDate(2, new Date(tarjeta.getFechaVencimiento().getTime()));
			statement.setString(3, tarjeta.getNombreTarjeta());
			statement.setFloat(4, tarjeta.getMonto());
			statement.setDate(5, new Date(tarjeta.getFechaCreacion().getTime()));
			// Fecha de eliminacion de persona se guarda como null
			statement.setNull(6, java.sql.Types.DATE);
			statement.setLong(7, tarjeta.getPersona().getId());
			
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar el tarjeta
			if (resultSet != null && resultSet.next()) {
				tarjeta.setId(resultSet.getLong(1));
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
		return tarjeta;
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
			statement = connection.prepareStatement("delete from tarjetas where id = ?");
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

	
	/**
	 * Metodo que actualiza un registro de la base de datos
	 * @param tarjeta
	 * @return
	 * @throws Exception
	 */
	
	public Tarjeta update(Tarjeta tarjeta) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("update tarjetas set numeroTarjeta = ?, fechaVencimiento = ?, set nombreTarjeta = ?, set monto = ? WHERE id = ?");
			statement.setLong(1, tarjeta.getNumeroTarjeta());
			statement.setDate(2, new Date(tarjeta.getFechaVencimiento().getTime()));
			statement.setString(3, tarjeta.getNombreTarjeta());
			statement.setFloat(4, tarjeta.getMonto());				
			statement.setLong(5, tarjeta.getId());
			
			int row = statement.executeUpdate();
			tarjeta = this.findById(tarjeta.getId());
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return tarjeta;
	}
	
	
	// Devuelve true si ya existe la tarjeta en la BD
	public boolean existTarjeta (Tarjeta tarjeta) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean row;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from tarjetas where numeroTarjeta = ? ");
			statement.setLong(1, tarjeta.getNumeroTarjeta());
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

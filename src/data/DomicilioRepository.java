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

import entities.Domicilio;
import exceptions.DataException;

public class DomicilioRepository {
	
	private Logger logger = LogManager.getLogger(getClass());
	private LocalidadRepository localidadRepository;
	
	/**
	 * Metodo que construye el objeto Domicilio
	 * @param resultSet
	 * @return
	 */
	private Domicilio buildDomicilio(ResultSet resultSet) {
		Domicilio domicilio = new Domicilio();
		try {
			domicilio.setId(resultSet.getLong("id"));
			domicilio.setCalle(resultSet.getNString("calle"));
			domicilio.setNumero(resultSet.getNString("numero"));
			domicilio.setPiso(resultSet.getNString("piso"));
			domicilio.setDepartamento(resultSet.getNString("departamento"));
			domicilio.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			domicilio.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
			Long idLocalidad = resultSet.getLong("idLocalidad");
			domicilio.setLocalidad(localidadRepository.findById(idLocalidad));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return domicilio;
	}
	
	/**
	 * Metodo que retorna una lista de domicilios segun la localidad
	 * @param idLocalidad
	 * @return
	 */
	public List<Domicilio> findDomiciliosByIdLocalidad(Long idLocalidad) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Domicilio> domicilioList = new ArrayList<Domicilio>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from domicilios where idLocalidad = ?");
			statement.setLong(1, idLocalidad);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Domicilio domicilio = buildDomicilio(resultSet);
				domicilioList.add(domicilio);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return domicilioList;
	}
	
	/**
	 * Metodo que retorna una lista de domicilios segun la persona
	 * @param idLocalidad
	 * @return
	 */
	public List<Domicilio> findDomiciliosByIdPersona(Long idPersona) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Domicilio> domicilioList = new ArrayList<Domicilio>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select d.id, d.calle, d.numero, d.piso, d.departamento, d.fechaCreacion, d.fechaEliminacion, d.idLocalidad from domicilios d inner join personas on personas.idDomicilio = d.id where personas.id = ?");
			statement.setLong(1, idPersona);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Domicilio domicilio = buildDomicilio(resultSet);
				domicilioList.add(domicilio);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return domicilioList;
	}
	
	/**
	 * Metodo que retorna un domicilio segun su id
	 * @param id
	 * @return
	 */
	public Domicilio findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Domicilio domicilio = new Domicilio();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from domicilios where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				domicilio = buildDomicilio(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return domicilio;
	}

	
	/**
	 * Metodo que inserta un registro en la base de datos
	 * @param domicilio
	 * @return
	 * @throws Exception
	 */
	public Domicilio save(Domicilio domicilio) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into domicilios(calle, numero, piso, departamento, fechaCreacion, fechaEliminacion, idLocalidad) "
												  + "values (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setString(1, domicilio.getCalle());
			statement.setString(2, domicilio.getNumero());
			statement.setString(3, domicilio.getPiso());
			statement.setString(4, domicilio.getDepartamento());
			statement.setDate(5, new Date(domicilio.getFechaCreacion().getTime()));
			// Fecha de eliminacion de persona se guarda como null
			statement.setNull(6, java.sql.Types.DATE);
			statement.setLong(7, domicilio.getLocalidad().getId());
			
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar el domicilio
			if (resultSet != null && resultSet.next()) {
				domicilio.setId(resultSet.getLong(1));
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
		return domicilio;
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
			statement = connection.prepareStatement("delete from domicilios where id = ?");
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

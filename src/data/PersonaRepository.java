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
import entities.Persona;
import entities.Rol;
import exceptions.DataException;

public class PersonaRepository {

	private Logger logger = LogManager.getLogger(getClass());
	private RolRepository rolRepository = new RolRepository();
	private DomicilioRepository domicilioRepository = new DomicilioRepository();

	private Persona buildPersona(ResultSet rs) throws Exception {
		Persona persona = new Persona();
		Rol rol = new Rol();
		Domicilio domicilio = new Domicilio();
		try {
			persona.setId(rs.getLong("id"));
			persona.setNombre(rs.getString("nombre"));
			persona.setApellido(rs.getString("apellido"));
			persona.setTipoDocumento(rs.getString("tipoDocumento"));
			persona.setNroDocumento(rs.getLong("nroDocumento"));
			persona.setEmail(rs.getString("email"));
			persona.setPassword(rs.getString("password"));
			persona.setCuit(rs.getString("cuit"));
			persona.setTelefono(rs.getLong("telefono"));
			persona.setGenero(rs.getString("genero"));
			persona.setFechaCreacion(rs.getDate("fechaCreacion"));
			persona.setSueldoMensual(rs.getDouble("sueldoMensual"));
			persona.setDescripcion(rs.getString("descripcion"));
			persona.setLegajo(rs.getLong("legajo"));
			rol = rolRepository.findById(rs.getLong("idRol"));
			persona.setRol(rol);
			domicilio = domicilioRepository.findById(rs.getLong("idDomicilio"));
			persona.setDomicilio(domicilio);
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		return persona;
	}
	
	/**
	 * Metodo que retorna todas las personas de la base de datos
	 * @return
	 * @throws Exception
	 */
	public List<Persona> findAll() throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Persona> list = new ArrayList<Persona>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from personas");
			resultSet = statement.executeQuery();
			
			if (resultSet != null) {
				while (resultSet.next()) {
					Persona persona = buildPersona(resultSet);
					list.add(persona);
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
	 * Metodo que retorna una persona segun su id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Persona findById(Long id) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Persona persona = new Persona();
		
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from personas where id = ?");
			statement.setLong(1, id);

			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				persona = buildPersona(resultSet);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		
		return persona;
	}
	
	/**
	 * Metodo que retorna una persona por su email
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public Persona findByEmail(String email) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Persona persona = new Persona();
		
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from personas where email = ?");
			statement.setString(1, email);

			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				persona = buildPersona(resultSet);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		
		return persona;
	}
	
	/**
	 * Metodo que retorna una persona por su tipo y numero de documento
	 * @param tipoDocumento
	 * @param nroDocumento
	 * @return
	 * @throws Exception
	 */
	public Persona findByTipoAndNroDocumento(String tipoDocumento, Long nroDocumento) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Persona persona = new Persona();
		
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from personas where tipoDocumento = ? and nroDocumento = ?");
			statement.setString(1, tipoDocumento);
			statement.setLong(2, nroDocumento);

			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				persona = buildPersona(resultSet);
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		
		return persona;
	}

	/**
	 * Metodo que inserta un registro en la base de datos
	 * @param persona
	 * @return
	 * @throws Exception
	 */
	public Persona save(Persona persona) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into personas(nombre, apellido, tipoDocumento, nroDocumento, email, password, cuit, telefono, genero, fechaCreacion, fechaEliminacion, sueldoMensual, descripcion, legajo, idRol, idDomicilio) "
												  + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setString(1, persona.getNombre());
			statement.setString(2, persona.getApellido());
			statement.setString(3, persona.getTipoDocumento());
			statement.setLong(4, persona.getNroDocumento());
			statement.setString(5, persona.getEmail());
			statement.setString(6, persona.getPassword());
			statement.setString(7, persona.getCuit());
			statement.setLong(8, persona.getTelefono());
			statement.setString(9, persona.getGenero());
			statement.setDate(10, new Date(persona.getFechaCreacion().getTime()));
			// Fecha de eliminacion de persona se guarda como null
			statement.setNull(11, java.sql.Types.DATE);
			if (persona.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				statement.setNull(12, java.sql.Types.DOUBLE);
				statement.setNull(13, java.sql.Types.VARCHAR);
				statement.setNull(14, java.sql.Types.BIGINT);
			} else {
				statement.setDouble(12, persona.getSueldoMensual());
				statement.setString(13, persona.getDescripcion());
				statement.setLong(14, persona.getLegajo());
			}
			statement.setLong(15, persona.getRol().getId());
			statement.setLong(16, persona.getDomicilio().getId());

			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar la persona
			if (resultSet != null && resultSet.next()) {
				persona.setId(resultSet.getLong(1));
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			e.printStackTrace();
			throw new DataException(null, "Ocurrio un error en la base de datos, contactar con el Administrador del Sistema", Level.ERROR);
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return persona;
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
			statement = connection.prepareStatement("delete from personas where id = ?");
			statement.setLong(1, id);
			int row = statement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw new DataException(null, "Ocurrio un error en la base de datos, contactar con el Administrador del Sistema", Level.ERROR);
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
		}
	}
	
	/**
	 * Metodo que devuelve true si un persona ya existe en la base
	 * @param persona
	 * @return
	 * @throws Exception
	 */
	public boolean existPersonaByEmailOrTipoAndNroDocumento(Persona persona) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean row;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from personas where email = ? or (tipoDocumento = ? and nroDocumento = ?)");
			statement.setString(1, persona.getEmail());
			statement.setString(2, persona.getTipoDocumento());
			statement.setLong(3, persona.getNroDocumento());

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
	
	/**
	 * Metodo que devuelve true el email ya esta asociado a una persona en la base de datos
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public boolean existPersonaByEmail(String email) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean row;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from personas where email = ?");
			statement.setString(1, email);
			
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
	
	/**
	 * Metodo que actualiza un registro de la base de datos
	 * @param persona
	 * @return
	 * @throws Exception
	 */
	public Persona update(Persona persona) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			if (persona.getRol().getNombreRol().equalsIgnoreCase(Rol.CLIENTE)) {
				statement = connection.prepareStatement("update personas set email = ?, telefono = ? WHERE id = ?");
				statement.setString(1, persona.getEmail());
				statement.setLong(2, persona.getTelefono());				
				statement.setLong(3, persona.getId());
			} else {
				statement = connection.prepareStatement("update personas set email = ?, telefono = ?, descripcion = ?, sueldoMensual = ? WHERE id = ?");
				statement.setString(1, persona.getEmail());
				statement.setLong(2, persona.getTelefono());				
				statement.setString(3, persona.getDescripcion());
				statement.setDouble(4, persona.getSueldoMensual());				
				statement.setLong(5, persona.getId());
			}

			int row = statement.executeUpdate();
			persona = this.findById(persona.getId());
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return persona;
	}
}

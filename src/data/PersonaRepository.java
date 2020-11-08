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

import entities.Persona;
import entities.Rol;
import exceptions.DataException;

public class PersonaRepository {

	private Logger logger = LogManager.getLogger(getClass());
	private RolRepository rolRepository = new RolRepository();

	private Persona buildPersona(ResultSet rs) throws Exception {
		Persona persona = new Persona();
		try {
			persona.setId(rs.getLong("id"));
			persona.setNombre(rs.getString("nombre"));
			persona.setApellido(rs.getString("apellido"));
			persona.setTipoDocumento(rs.getString("tipoDocumento"));
			persona.setNroDocumento(rs.getLong("nroDocumento"));
			persona.setEmail(rs.getString("email"));
			persona.setCuit(rs.getString("cuit"));
			persona.setTelefono(rs.getLong("telefono"));
			persona.setGenero(rs.getString("genero"));
			persona.setFechaCreacion(rs.getDate("fechaCreacion"));
			persona.setSueldoMensual(rs.getDouble("sueldoMensual"));
			persona.setDescripcion(rs.getString("descripcion"));
			persona.setLegajo(rs.getLong("legajo"));
			Rol rol = rolRepository.findById(rs.getLong("idRol"));
			persona.setRol(rol);
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		return persona;
	}
	
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

	public Persona save(Persona persona) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into personas(nombre, apellido, tipoDocumento, nroDocumento, email, password, cuit, telefono, genero, fechaCreacion, fechaEliminacion, sueldoMensual, descripcion, legajo, idRol) "
												  + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
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
			statement.setDate(10, (Date) persona.getFechaCreacion());
			// Fecha de eliminacion de persona se guarda como null
			statement.setDate(11, null);
			statement.setDouble(12, persona.getSueldoMensual());
			statement.setString(12, persona.getDescripcion());
			statement.setLong(13, persona.getLegajo());
			statement.setLong(14, persona.getRol().getId());

			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar la persona
			if (resultSet != null && resultSet.next()) {
				persona.setId(resultSet.getLong(1));
			}
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw new DataException(null, "Ocurrio un error en la base de datos, contactar con el Administrador del Sistema", Level.ERROR);
		} finally {
			DataBaseConnection.closeConnection(connection);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeResultSet(resultSet);
		}
		return persona;
	}
	
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
	
	public Persona existPersonaByEmailOrTipoAndNroDocumento(Persona persona) {
		
		return null;
	}
}

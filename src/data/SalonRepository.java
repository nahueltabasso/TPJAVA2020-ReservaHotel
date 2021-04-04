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
import entities.Salon;
import entities.TipoHabitacion;
import exceptions.DataException;
import utils.Utils;

public class SalonRepository {

private Logger logger = LogManager.getLogger(getClass());
	
	/**
	 * Metodo que construye el objeto Salon
	 * @param resultSet
	 * @return
	 */
	private Salon buildSalon(ResultSet resultSet) {
		Salon salon = new Salon();
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.DATE_PATTERN);
		Date fechaCreacion = null;
		Date fechaEliminacion = null;
		try {
			salon.setId(resultSet.getLong("id"));
			salon.setCapacidad(resultSet.getInt("capacidad"));
			salon.setNombreSalon(resultSet.getNString("nombreSalon"));
			salon.setDescripcion(resultSet.getNString("descripcion"));
			salon.setFoto(resultSet.getBlob("foto"));
			salon.setPrecioPorDia(resultSet.getDouble("precioPorDia"));
			fechaCreacion = resultSet.getDate("fechaCreacion");
			fechaEliminacion = resultSet.getDate("fechaEliminacion");
			if (fechaCreacion != null) {
				salon.setFechaCreacion(sdf.parse(fechaCreacion.toString()));
			} 
			if (fechaEliminacion != null) {
				salon.setFechaEliminacion(sdf.parse(fechaEliminacion.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salon;
	}
		
		
	/**
	 * Metodo que retorna un salon segun su id
	 * @param id
	 * @return
	 */
	public Salon findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Salon salon = new Salon();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from salones where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				salon = buildSalon(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return salon;
	}
	
	
	/**
	 * Metodo que retorna una lista de salones
	 * @param 
	 * @return
	 */
	public List<Salon> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Salon> salonesList = new ArrayList<Salon>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from salones");
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Salon salon = buildSalon(resultSet);
				salonesList.add(salon);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return salonesList;
	}	
	
	/**
	 * Metodo que inserta un registro en la base de datos
	 * @param salon
	 * @return
	 * @throws Exception
	 */
	public Salon save(Salon salon) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into salones(capacidad, nombreSalon, descripcion, foto, precioPorDia, fechaCreacion, fechaEliminacion) "
												  + "values (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setInt(1, salon.getCapacidad());
			statement.setString(2, salon.getNombreSalon());
			statement.setString(3, salon.getDescripcion());
			statement.setBlob(4, salon.getFoto());
			statement.setDouble(5, salon.getPrecioPorDia());
			statement.setDate(6, new Date(salon.getFechaCreacion().getTime()));
			// Fecha de eliminacion de salon se guarda como null
			statement.setNull(7, java.sql.Types.DATE);
					
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar el salon
			if (resultSet != null && resultSet.next()) {
				salon.setId(resultSet.getLong(1));
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
		return salon;
	}
	
	
	/**
	 * Metodo que actualiza un registro de la base de datos
	 * @param salon
	 * @return
	 * @throws Exception
	 */
	
	public Salon update(Salon salon) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("update salones set capacidad = ?, nombreSalon = ?, descripcion = ?, foto = ?, precioPorDia = ? WHERE id = ?");
			statement.setInt(1, salon.getCapacidad());
			statement.setString(2, salon.getNombreSalon());				
			statement.setString(3, salon.getDescripcion());
			statement.setBlob(4, salon.getFoto());
			statement.setDouble(5,  salon.getPrecioPorDia());
			statement.setLong(6, salon.getId());
			
			int row = statement.executeUpdate();
			salon = this.findById(salon.getId());
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return salon;
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
			statement = connection.prepareStatement("delete from salones where id = ?");
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

	// Devuelve true si ya existe el nombre del sal�n en la BD
		public boolean existSalon (Salon salon) throws Exception {
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			boolean row;
			try {
				connection = DataBaseConnection.getConnection();
				statement = connection.prepareStatement("select * from salones where nombreSalon = ? ");
				statement.setString(1, salon.getNombreSalon());
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

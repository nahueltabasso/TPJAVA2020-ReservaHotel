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
import entities.LineaFactura;
import exceptions.DataException;

public class LineaFacturaRepository {
	
	private Logger logger = LogManager.getLogger(getClass());
	private FacturaRepository facturaRepository;
	
	/**
	 * Metodo que construye el objeto LineaFactura
	 * @param resultSet
	 * @return
	 */
	private LineaFactura buildLineaFactura(ResultSet resultSet) {
		LineaFactura lineaFactura = new LineaFactura();
		try {
			lineaFactura.setId(resultSet.getLong("id"));
			lineaFactura.setCantidad(resultSet.getInt("cantidad"));
			lineaFactura.setMonto(resultSet.getFloat("monto"));
			lineaFactura.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			lineaFactura.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
			Long idFactura = resultSet.getLong("idFactura");
			lineaFactura.setFactura(facturaRepository.findById(idFactura));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return lineaFactura;
	}
		
		
	/**
	 * Metodo que retorna un lineaFactura segun su id
	 * @param id
	 * @return
	 */
	public LineaFactura findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		LineaFactura lineaFactura = new LineaFactura();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from lineafacturas where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				lineaFactura = buildLineaFactura(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return lineaFactura;
	}

	/**
	 * Metodo que retorna una lista de lineafacturas segun la factura
	 * @param idFactura
	 * @return
	 */
	public List<LineaFactura> findLineaFacturasByIdFactura(Long idFactura) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<LineaFactura> lineaFacturaList = new ArrayList<LineaFactura>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from lineafacturas where idFactura = ?");
			statement.setLong(1, idFactura);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				LineaFactura lineaFactura = buildLineaFactura(resultSet);
				lineaFacturaList.add(lineaFactura);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return lineaFacturaList;
	}	
	
	/**
	 * Metodo que inserta un registro en la base de datos
	 * @param lineaFactura
	 * @return
	 * @throws Exception
	 */
	public LineaFactura save(LineaFactura lineaFactura) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into lineafacturas(cantidad, monto, fechaCreacion, fechaEliminacion, idFactura) "
												  + "values (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setInt(1, lineaFactura.getCantidad());
			statement.setFloat(2, lineaFactura.getMonto());
			statement.setDate(3, new Date(lineaFactura.getFechaCreacion().getTime()));
			// Fecha de eliminacion de lineaFactura se guarda como null
			statement.setNull(4, java.sql.Types.DATE);
			statement.setLong(5, lineaFactura.getFactura().getId());
			
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar el lineaFactura
			if (resultSet != null && resultSet.next()) {
				lineaFactura.setId(resultSet.getLong(1));
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
		return lineaFactura;
	}
	
	
	/**
	 * Metodo que actualiza un registro de la base de datos
	 * @param lineaFactura
	 * @return
	 * @throws Exception
	 */
	
	public LineaFactura update(LineaFactura lineaFactura) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("update lineaFacturas set cantidad = ?, set monto = ? WHERE id = ?");
			statement.setInt(1, lineaFactura.getCantidad());
			statement.setFloat(2, lineaFactura.getMonto());				
			statement.setLong(3, lineaFactura.getId());
			
			int row = statement.executeUpdate();
			lineaFactura = this.findById(lineaFactura.getId());
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return lineaFactura;
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
			statement = connection.prepareStatement("delete from lineafacturas where id = ?");
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

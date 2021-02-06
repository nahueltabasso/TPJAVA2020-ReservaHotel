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
import entities.Factura;
import entities.Tarjeta;
import exceptions.DataException;

public class FacturaRepository {
	
	private Logger logger = LogManager.getLogger(getClass());
	private TarjetaRepository tarjetaRepository = new TarjetaRepository();
	
	/**
	 * Metodo que construye el objeto Factura
	 * @param resultSet
	 * @return
	 */
	private Factura buildFactura(ResultSet resultSet) {
		Factura factura = new Factura();
		Tarjeta tarjeta = new Tarjeta();
		try {
			factura.setId(resultSet.getLong("id"));
			factura.setNumeroFactura(resultSet.getLong("numeroFactura"));
			factura.setMonto(resultSet.getFloat("monto"));
			factura.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			factura.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
			tarjeta = tarjetaRepository.findById(resultSet.getLong("idTarjeta"));
			factura.setTarjeta(tarjeta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return factura;
	}
		
		
	/**
	 * Metodo que retorna un factura segun su id
	 * @param id
	 * @return
	 */
	public Factura findById(Long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Factura factura = new Factura();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from facturas where id = ?");
			statement.setLong(1, id);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				factura = buildFactura(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return factura;
	}

	/**
	 * Metodo que retorna una lista de facturas segun el id de la tarjeta
	 * @param idTarjeta
	 * @return
	 */
	public List<Factura> findFacturasByIdTarjeta(Long idTarjeta) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Factura> facturaList = new ArrayList<Factura>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from facturas where facturas.idTarjeta = ?");
			statement.setLong(1, idTarjeta);
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Factura factura = buildFactura(resultSet);
				facturaList.add(factura);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return facturaList;
	}	
	
	/**
	 * Metodo que retorna una lista de facturas
	 * @param 
	 * @return
	 */
	public List<Factura> findAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Factura> facturaList = new ArrayList<Factura>();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from facturas");
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Factura factura = buildFactura(resultSet);
				facturaList.add(factura);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return facturaList;
	}	
	
	/**
	 * Metodo que inserta un registro en la base de datos
	 * @param factura
	 * @return
	 * @throws Exception
	 */
	public Factura save(Factura factura) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into facturas(numeroFactura, monto, fechaCreacion, fechaEliminacion, idTarjeta) "
												  + "values (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setLong(1, factura.getNumeroFactura());
			statement.setFloat(2, factura.getMonto());
			statement.setDate(3, new Date(factura.getFechaCreacion().getTime()));
			// Fecha de eliminacion de factura se guarda como null
			statement.setNull(4, java.sql.Types.DATE);
			statement.setLong(5, factura.getTarjeta().getId());
			
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id para retornar el factura
			if (resultSet != null && resultSet.next()) {
				factura.setId(resultSet.getLong(1));
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
		return factura;
	}
	
	
	/**
	 * Metodo que actualiza un registro de la base de datos
	 * @param factura
	 * @return
	 * @throws Exception
	 */
	
	public Factura update(Factura factura) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("update facturas set numeroFactura = ?, set monto = ? WHERE id = ?");
			statement.setLong(1, factura.getNumeroFactura());
			statement.setFloat(2, factura.getMonto());				
			statement.setLong(3, factura.getId());
			
			int row = statement.executeUpdate();
			factura = this.findById(factura.getId());
		} catch (SQLException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw e;
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return factura;
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
			statement = connection.prepareStatement("delete from facturas where id = ?");
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

	// Devuelve true si ya existe el numero de la factura en la BD
	public boolean existFactura (Factura factura) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		boolean row;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from facturas where numeroFactura = ? ");
			statement.setLong(1, factura.getNumeroFactura());
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

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
import exceptions.DataException;

public class FacturaRepository {
	
	private Logger logger = LogManager.getLogger(getClass());
	private TarjetaRepository tarjetaRepository;
	
	/**
	 * Metodo que construye el objeto Factura
	 * @param resultSet
	 * @return
	 */
	private Factura buildFactura(ResultSet resultSet) {
		Factura factura = new Factura();
		try {
			factura.setId(resultSet.getLong("id"));
			factura.setNumeroFactura(resultSet.getLong("numeroFactura"));
			factura.setMonto(resultSet.getFloat("monto"));
			factura.setFechaCreacion(resultSet.getDate("fechaCreacion"));
			factura.setFechaEliminacion(resultSet.getDate("fechaEliminacion"));
			Long idTarjeta = resultSet.getLong("idTarjeta");
			factura.setTarjeta(tarjetaRepository.findById(idTarjeta));
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
	 * Metodo que retorna una lista de facturas segun la tarjeta
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
			statement.setFloat(4, factura.getMonto());
			statement.setDate(5, new Date(factura.getFechaCreacion().getTime()));
			// Fecha de eliminacion de factura se guarda como null
			statement.setNull(6, java.sql.Types.DATE);
			statement.setLong(7, factura.getTarjeta().getId());
			
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


}

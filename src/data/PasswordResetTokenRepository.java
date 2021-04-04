package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import entities.PasswordResetToken;
import exceptions.DataException;

public class PasswordResetTokenRepository {

	private Logger logger = LogManager.getLogger(getClass());
	
	private PasswordResetToken buildPasswordResetToken(ResultSet resultSet) {
		PasswordResetToken prt = new PasswordResetToken();
		try {
			prt.setId(resultSet.getLong("id"));
			prt.setIdUsuario(resultSet.getLong("id_usuario"));
			prt.setToken(resultSet.getString("token"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return prt;
	}
	
	public PasswordResetToken save(PasswordResetToken prt) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("insert into passwordresettoken(id_usuario, token) "
												  + "values (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			// Pasamos los parametros para la query nativa
			statement.setLong(1, prt.getIdUsuario());
			statement.setString(2, prt.getToken());			
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			
			// Validamos que nos devuelva el id 
			if (resultSet != null && resultSet.next()) {
				prt.setId(resultSet.getLong(1));
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
		return prt;
	}
	
	public PasswordResetToken findByToken(String token) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		PasswordResetToken prt = new PasswordResetToken();
		try {
			connection = DataBaseConnection.getConnection();
			statement = connection.prepareStatement("select * from passwordresettoken where token = ?");
			statement.setString(1, token);
			
			resultSet = statement.executeQuery();
			if (resultSet != null && resultSet.next()) {
				prt = buildPasswordResetToken(resultSet);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DataBaseConnection.closeResultSet(resultSet);
			DataBaseConnection.closePreparedStatement(statement);
			DataBaseConnection.closeConnection(connection);
		}
		return prt;
	}

}

package net.sitina.bp.modules.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * the purpose of this simple class is to fetch values from key-value tables and
 * in case the value is missing then insert it and return the id
 * 
 */
public class DatabaseValueProvider implements ValueProvider<String, Long> {

	protected Logger log = Logger.getLogger(this.getClass());
	
	private final Connection connection;
	
	private final String selectQuery;
	
	private final String insertQuery;

	public DatabaseValueProvider(Connection connection, String tableName) {
		this(connection, tableName, "id", "value");
	}
	
	public DatabaseValueProvider(Connection connection, String tableName, String idField, String valueField) {
		this.connection = connection;
		selectQuery = "SELECT " + idField + " FROM " + tableName + " WHERE " + valueField + " = ?";
		insertQuery = "INSERT INTO " + tableName + " (" + valueField + ") values (?)";
        String createQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT, `value` varchar(200) DEFAULT NULL, PRIMARY KEY (`id`), UNIQUE KEY `valueIndex` (`value`)) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(createQuery);
            stmt.execute();
            log.debug("Sucessfully created/updated table " + tableName + ".");
        } catch (SQLException e) {
            log.error("Unable to create/check state of necessary table. Ending module.", e);
            throw new IllegalStateException("Unable to create/check state of necessary table. Ending module.");
        }
	}

	
	@Override
	public Long getValue(String key) {
		Long value = null;
		
		try {
			value = findValue(key);
		
			if (value != null) {
				return value;
			}
			
			return insertValue(key);
		} catch (SQLException e) {
			log.error("Error fetching value from database.", e);
		}

		return null;
	}
	
	public Long findValue(String key) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(selectQuery);
		stmt.setString(1, key);
		
		ResultSet resultSet = stmt.executeQuery();
		
		while (resultSet.next()) {
			return resultSet.getLong(1);
		}
		
		return null;
	}
	
	public Long insertValue(String key) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(insertQuery);
		stmt.setString(1, key);
		
		try {
            if (stmt.execute() && stmt.getMoreResults()) {
                ResultSet rs = stmt.getResultSet();
                return rs.getLong(0);
            }
		} catch (Exception e) {
            log.error("Error inserting value '" + key + "' with query '" + insertQuery + "'.", e);
		}
		return findValue(key);
	}

}

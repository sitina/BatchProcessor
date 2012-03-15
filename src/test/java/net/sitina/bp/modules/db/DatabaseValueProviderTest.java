package net.sitina.bp.modules.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

public class DatabaseValueProviderTest {

	@Test
	public void testCreateValue() throws Exception {
		String connectionString = "jdbc:mysql://localhost/companies";
		String user = "root";
		String password = "root";
		String databaseDriver = "com.mysql.jdbc.Driver";
		String tableName = "size";
		
		Connection connection = DriverManager.getConnection(connectionString, user, password);
		Class.forName(databaseDriver);
		
		DatabaseValueProvider provider = new DatabaseValueProvider(connection, tableName);
		
		Long value = provider.getValue("test" + System.currentTimeMillis() + "test");
		
		assertNotNull(value);
	}
	
}

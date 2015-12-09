package net.sitina.bp.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DerbyDriver {

    public void connect() {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String dbName = "jdbcDemoDB";
        String connectionURL = "jdbc:derby:" + dbName + ";create=true";

        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(connectionURL);
            Statement s = conn.createStatement();
            s.execute("CREATE " +
                    "TABLE tab " +
                    "(pk int not null, " +
                    "pk2 varchar, PRIMARY KEY (pk));");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}

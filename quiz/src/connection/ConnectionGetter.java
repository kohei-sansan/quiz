package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionGetter {
	private static final String URL = "jdbc:postgresql://localhost:5432/myDB";
    private static final String USER = "tanakatairaakira";
    private static final String PASSWORD = "tanak0w724";
	public static Connection getConnection() {
		Connection con = null;
		try{
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(URL, USER, PASSWORD);
		}catch(SQLException|ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
}

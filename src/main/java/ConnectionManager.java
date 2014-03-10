import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Taldykin V.S.
 * @version 1.00 10.03.14 19:21
 */
public class ConnectionManager {
    private Properties dbProperties;
    private Connection connection;
    private static ConnectionManager connectionManager = null;

    private ConnectionManager() throws IOException {
        dbProperties = new Properties();
        dbProperties.load(new FileInputStream("database.properties"));
    }

    public static ConnectionManager getInstance() throws IOException {
        if(connectionManager == null) {
            connectionManager = new ConnectionManager();
        }
        return connectionManager;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException, JDBCConnectionException {
        String url = dbProperties.getProperty("url");
        String driverName = dbProperties.getProperty("driver");
        String user = dbProperties.getProperty("user");
        String password = dbProperties.getProperty("password");

        Class.forName(driverName);
        connection = DriverManager.getConnection(url, user, password);

        if(connection == null) {
            throw new JDBCConnectionException("Driver type is not correct in URL "
                    + url + ".");
        }

        return connection;
    }

    public void close() throws JDBCConnectionException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new JDBCConnectionException("Can't close connection", e);
            }
        }
    }

}

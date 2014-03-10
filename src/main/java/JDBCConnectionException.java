/**
 * @author Taldykin V.S.
 * @version 1.00 10.03.14 19:38
 */
public class JDBCConnectionException extends Exception {
    public JDBCConnectionException(String message) {
        super(message);
    }

    public JDBCConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}

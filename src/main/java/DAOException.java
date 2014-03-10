/**
 * @author Taldykin V.S.
 * @version 1.00 10.03.14 19:48
 */
public class DAOException extends Exception {
    public DAOException(String s, JDBCConnectionException e) {
        super(s, e);
    }
}

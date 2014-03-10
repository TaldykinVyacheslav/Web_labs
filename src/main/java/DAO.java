import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Taldykin V.S.
 * @version 1.00 10.03.14 19:45
 */
public class DAO {
    protected ConnectionManager connectionManager;
    private final String listTestsQuery = "select test_name from tests";
    private final String listQuestionsOfTestQuery = "select question_text from questions where test_name=?";
    private final String getResultByTestAndStudentQuery = "select r.result from"
            + " students st, tests t, register r"
            + " where r.student_id = st.student_id and r.test_id = t.test_id"
            + " and t.test_name = ? and st.student_name = ?"
            + " and st.student_surname = ?";
    private final String assignTestToStudentQuery = "insert into students_tests (student_id, test_id) values (?, ?)";
    private final String saveResultOfTestQuery = "insert into register (student_id, test_id, result) values (?, ?, ?)";

    public DAO() throws IOException {
        connectionManager = ConnectionManager.getInstance();
    }
    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public List<String> listTests()
            throws JDBCConnectionException, SQLException, ClassNotFoundException {
        List<String> tests;
        Statement statement = null;
        ResultSet resultSet = null;
        tests = new ArrayList<String>();
        try {
            statement = connectionManager.getConnection().createStatement();
            resultSet = statement.executeQuery(listTestsQuery);
            while(resultSet.next()) {
                tests.add(resultSet.getString("test_name"));
            }
        } finally {
            if(resultSet != null)
                resultSet.close();
            if(statement != null)
                statement.close();
            connectionManager.close();
        }

        return tests;
    }

    public List<String> listQuestionsOfTest(String testName)
            throws JDBCConnectionException, SQLException, ClassNotFoundException {
        List<String> questions;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        questions = new ArrayList<String>();
        try {
            statement = connectionManager.getConnection().prepareStatement(listQuestionsOfTestQuery);
            statement.setString(0, testName);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                questions.add(resultSet.getString("question_text"));
            }
        } finally {
            if(resultSet != null)
                resultSet.close();
            if(statement != null)
                statement.close();
            connectionManager.close();
        }

        return questions;
    }

    public int getResultByTestAndStudent(String testName, String studentName, String studentSurname)
            throws JDBCConnectionException, SQLException, ClassNotFoundException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int testResult = -1;
        try {
            statement = connectionManager.getConnection().prepareStatement(getResultByTestAndStudentQuery);
            statement.setString(0, testName);
            statement.setString(1, studentName);
            statement.setString(2, studentSurname);
            resultSet = statement.executeQuery();
            resultSet.next();
            testResult = resultSet.getInt(0);
        } finally {
            if(resultSet != null)
                resultSet.close();
            if(statement != null)
                statement.close();
            connectionManager.close();
        }

        return testResult;
    }

    public void assignTestToStudentAndWriteTestResults() {

    }
}

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
    private final String listQuestionsOfTestQuery = "select q.question_text from questions q, tests t"
            + " where t.test_id = q.test_id and t.test_name=?";
    private final String getResultByTestAndStudentQuery = "select r.result from"
            + " students st, tests t, register r"
            + " where r.student_id = st.student_id and r.test_id = t.test_id"
            + " and t.test_name = ? and st.student_name = ?"
            + " and st.student_surname = ?";
    private final String assignTestToStudentQuery = "insert into students_tests (student_id, test_id)"
            + " select st.student_id, t.test_id"
            + " from students st, tests t"
            + " where t.test_name = ? and st.student_name = ? and st.student_surname = ?";
    private final String saveResultOfTestQuery = "insert into register (student_id, test_id, result)"
            + " select st.student_id, t.test_id, r.result"
            + " from students st, tests t, register r"
            + " where r.student_id = st.student_id and r.test_id = t.test_id"
            + " and t.test_name = ? and st.student_name = ? and st.student_surname = ?";

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
            statement.setString(1, testName);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                questions.add(resultSet.getString("question_text"));
            }
            System.out.println(questions.size());
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
            statement.setString(1, testName);
            statement.setString(2, studentName);
            statement.setString(3, studentSurname);
            resultSet = statement.executeQuery();
            resultSet.next();
            testResult = resultSet.getInt(1);
        } finally {
            if(resultSet != null)
                resultSet.close();
            if(statement != null)
                statement.close();
            connectionManager.close();
        }

        return testResult;
    }

    public void assignTestToStudentAndWriteTestResults(
            String testName, String studentName, String studentSurname, int testResult)
            throws JDBCConnectionException, SQLException, ClassNotFoundException {
        PreparedStatement assignStatement = null;
        PreparedStatement saveStatement = null;
        ResultSet resultSet = null;
        try {
            assignStatement = connectionManager.getConnection().prepareStatement(assignTestToStudentQuery);
            saveStatement = connectionManager.getConnection().prepareStatement(saveResultOfTestQuery);
            assignStatement.setString(1, testName);
            assignStatement.setString(2, studentName);
            assignStatement.setString(3, studentSurname);
            saveStatement.setString(1, testName);
            saveStatement.setString(2, studentName);
            saveStatement.setString(3, studentSurname);

            saveStatement.executeUpdate();
            assignStatement.executeUpdate();
        } finally {
            if(resultSet != null)
                resultSet.close();
            if(assignStatement != null)
                assignStatement.close();
            if(saveStatement != null)
                saveStatement.close();
            connectionManager.close();
        }
    }
}

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Taldykin V.S.
 * @version 1.00 04.03.14 18:53
 */
public class Main {
    private static Scanner input = new Scanner(System.in);
    private static boolean isExit = false;
    private static DAO dao;
    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Creating data access object");
        createDAO();
        logger.info("Starting user menu");
        userMenu();
    }

    private static void createDAO() {
        try {
            dao = new DAO();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while creating DAO!", e);
        }
    }

    private static void userMenu() {
        while(!isExit) {
            System.out.println("Select one of actions: \n"
                    + "1. List all tests \n"
                    + "2. List of questions of test \n"
                    + "3. List result of test of student \n"
                    + "4. Assign test to student, save test results \n"
                    + "0. Exit");
            int choice;
            choice = input.nextInt();
            input.nextLine();
            processClientChoice(choice);
        }
    }

    private static void processClientChoice(int choice) {
        String testName;
        String studentName;
        String studentSurname;

        switch (choice) {
            case 1:
                listTests();
                break;
            case 2:
                System.out.println("Enter test name:");
                testName = input.nextLine();
                listQuestions(testName);
                break;
            case 3:
                System.out.println("Enter test name");
                testName = input.nextLine();
                System.out.println("Enter student name");
                studentName = input.nextLine();
                System.out.println("Enter student surname");
                studentSurname = input.nextLine();
                showTestResult(testName, studentName, studentSurname);
                break;
            case 4:
                System.out.println("Enter test name");
                testName = input.nextLine();
                System.out.println("Enter student name");
                studentName = input.nextLine();
                System.out.println("Enter student surname");
                studentSurname = input.nextLine();
                assignTestAndSaveTestResult(testName, studentName, studentSurname);
                break;
            case 0:
                isExit = true;
                break;
            default:
                logger.error("WRONG NUMBER! TRY AGAIN!");
        }
    }

    private static void listTests() {
        logger.info("Listing tests");
        List<String> tests = null;
        try {
            tests = dao.listTests();
        } catch (JDBCConnectionException e) {
            logger.error("Exception in JDBC connection!", e);
        } catch (SQLException e) {
            logger.error("Exception while executing SQL statement!", e);
        } catch (ClassNotFoundException e) {
            logger.error("Cannot find class!", e);
        }
        if(tests != null) {
            System.out.println("Available tests are:\n");
            System.out.println(Arrays.toString(tests.toArray()));
        }
    }

    private static void listQuestions(String testName) {
        logger.info("Listing questions of test");
        List<String> questions = null;
        try {
            questions = dao.listQuestionsOfTest(testName);
        } catch (JDBCConnectionException e) {
            logger.error("Exception in JDBC connection!", e);
        } catch (SQLException e) {
            logger.error("Exception while executing SQL statement!", e);
        } catch (ClassNotFoundException e) {
            logger.error("Cannot find class!", e);
        }
        if(questions != null) {
            System.out.println("Questions of test " + testName + " are:");
            System.out.println(Arrays.toString(questions.toArray()));
        }
    }

    private static void showTestResult(
            String testName, String studentName, String studentSurname) {
        logger.info("Retrieving test result of specific student");
        int testResult = -1;
        try {
            testResult = dao.getResultByTestAndStudent(testName, studentName, studentSurname);
        } catch (JDBCConnectionException e) {
            logger.error("Exception in JDBC connection!", e);
        } catch (SQLException e) {
            logger.error("Exception while executing SQL statement!", e);
        } catch (ClassNotFoundException e) {
            logger.error("Cannot find class!", e);
        }
        if(testResult >= 0) {
            System.out.println("Result of student "
                    + studentName + " " + studentSurname
                    + " in test " + testName
                    + " is " + testResult);
        }
    }

    private static void assignTestAndSaveTestResult(
            String testName, String studentName, String studentSurname) {
        logger.info("Assigning test to student and saving its results to register");

        int testResult;
        testResult = (int)(Math.random() * 100);
        try {
            dao.assignTestToStudentAndWriteTestResults(
                    testName, studentName, studentSurname, testResult);
        } catch (JDBCConnectionException e) {
            logger.error("Exception in JDBC connection!", e);
        } catch (SQLException e) {
            logger.error("Exception while executing SQL statement!", e);
        } catch (ClassNotFoundException e) {
            logger.error("Cannot find class!", e);
        }

        System.out.println("Test " + testName + " is assigned to student "
                + studentName + " and its result(" + testResult + ") is saved in register.");
    }
}

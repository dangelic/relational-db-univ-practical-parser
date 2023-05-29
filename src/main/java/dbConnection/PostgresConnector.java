package dbConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.ResultSetMetaData;

/**
 * The PostgresConnector class provides methods to connect and interact with a PostgreSQL database.
 * See debug logfile in ./logs directory for more information on rejected queries. Use this log only for debugging, as most of the fails are intended.
 */
public class PostgresConnector {
    private static Connection connection;
    private String username;
    private String password;

    /**
     * Creates a new instance of the PostgresConnector class with the specified username and password.
     *
     * @param username The username for the database connection.
     * @param password The password for the database connection.
     */
    public PostgresConnector(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Connects to the PostgreSQL database using the provided username and password.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        connection = DriverManager.getConnection(url, username, password);
    }

    /**
     * Disconnects from the PostgreSQL database.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private static void writeErrorToFile(String sql, String errorMessage, String pathToLogFile) {
        try {
            FileWriter writer = new FileWriter(pathToLogFile, true);
            writer.write("SQL Statement: " + sql + "\n");
            writer.write("Error Message: " + errorMessage + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a batch of SQL queries and returns the results as a list of HashMaps.
     * Important: It processes a batch of statement but sequentially in database as we want to check each query individually.
     *
     * @param sqlStatements  The list of SQL statements to execute.
     * @param pathToLogFile  The path to the log file for error logging.
     * @return A list of HashMaps representing the query results.
     */
    public static List<HashMap<String, Object>> executeSQLQueryBatch(List<String> sqlStatements, String pathToLogFile) {
        List<HashMap<String, Object>> results = new ArrayList<>();
        try {
            for (String sqlStatement : sqlStatements) {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sqlStatement);
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    while (resultSet.next()) {
                        HashMap<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            Object value = resultSet.getObject(i);
                            row.put(columnName, value);
                        }
                        results.add(row);
                    }

                    statement.close();
                    System.out.println("\u001B[32mQuery executed successfully: " + sqlStatement + "\u001B[0m");
                } catch (SQLException e) {
                        if (e.getMessage().contains("No results were returned by the query.")) {
                            System.out.println("\u001B[32mQuery executed successfully: " + sqlStatement + "\u001B[0m");
                        } else {
                            // System.out.println("\u001B[31m" + e.getMessage() + " FAILED QUERY: " + sqlStatement + "\u001B[0m");
                            writeErrorToFile(sqlStatement, e.getMessage(), pathToLogFile);
                        }
                    }
                }
        } catch (Exception e) {
            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
        }
        return results;
    }
}

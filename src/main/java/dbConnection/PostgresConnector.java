package dbConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PostgresConnector {
    private Connection connection;
    private String username;
    private String password;

    public PostgresConnector(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        connection = DriverManager.getConnection(url, username, password);
    }

    public void executeSQL(String sql, String pathToLogFile) {
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.commit();
            statement.close();
            System.out.println("Query executed successfully: " + sql);
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println(e.getMessage());
                writeErrorToFile(sql, e.getMessage(), pathToLogFile);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void writeErrorToFile(String sql, String errorMessage, String pathToLogFile) {
        try {
            FileWriter writer = new FileWriter(pathToLogFile, true);
            writer.write("SQL Statement: " + sql + "\n");
            writer.write("Error Message: " + errorMessage + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static void executeSQLQueryBatch(List<String> sqlStatements, String pathToLogFile) {
        // Local db instance
        String username = "postgres";
        String password = "";

        PostgresConnector dbConnection = new PostgresConnector(username, password);

        try {
            dbConnection.connect();
            for (String sqlStatement : sqlStatements) {
                dbConnection.executeSQL(sqlStatement, pathToLogFile);
            }
            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
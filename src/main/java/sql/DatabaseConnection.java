package sql;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private Connection connection;
    private String username;
    private String password;

    public DatabaseConnection(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        connection = DriverManager.getConnection(url, username, password);
    }

    public void executeSQL(String sql) {
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
                writeErrorToFile(sql, e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void writeErrorToFile(String sql, String errorMessage) {
        try {
            FileWriter writer = new FileWriter("./src/logs/sql_error_log.txt", true);
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

    public static void main(String[] args) {
        String username = "postgres";
        String password = "";
        String sqlStatement = "INSERT INTO your_table (column1, column2) VALUES ('value1', 'value2')";

        DatabaseConnection dbConnection = new DatabaseConnection(username, password);

        try {
            dbConnection.connect();
            dbConnection.executeSQL(sqlStatement);
            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package dbConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.ResultSetMetaData;




import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class PostgresConnector {
    private static Connection connection;
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

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static void executeSQL(String sql, String pathToLogFile) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            System.out.println("\u001B[32mQuery executed successfully: " + sql + "\u001B[0m");
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            writeErrorToFile(sql, e.getMessage(), pathToLogFile);
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

    public ResultSet executeSQLQuerySingle(String sql, List<String> values, String pathToLogFile) {
        ResultSet resultSet = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            // Set parameter values
            for (int i = 0; i < values.size(); i++) {
                statement.setString(i + 1, values.get(i));
            }
            resultSet = statement.executeQuery();
            System.out.println("\u001B[32mQuery executed successfully: " + sql + "\u001B[0m");
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            writeErrorToFile(sql, e.getMessage(), pathToLogFile);
        }
        return resultSet;
    }

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
                    if (!e.getMessage().contains("insert or update on table \"books\" violates foreign key constraint \"books_asin_fkey") &&
                            !e.getMessage().contains("insert or update on table \"dvds\" violates foreign key constraint \"dvds_asin_fkey") &&
                            !e.getMessage().contains("insert or update on table \"cds\" violates foreign key constraint \"cds_asin_fkey"))
                    {
                        if (e.getMessage().contains("No results were returned by the query")) {System.out.println("\u001B[32mQuery executed successfully: " + sqlStatement + "\u001B[0m");}
                        else {System.out.println("\u001B[31m" + e.getMessage() + " FAILED QUERY: " + sqlStatement +"\u001B[0m");}
                        writeErrorToFile(sqlStatement, e.getMessage(), pathToLogFile);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            writeErrorToFile(sqlStatements.toString(), e.getMessage(), pathToLogFile);
        }
        return results;
    }

}

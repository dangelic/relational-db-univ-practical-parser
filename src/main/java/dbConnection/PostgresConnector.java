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
                    if (!e.getMessage().contains("insert or update on table \"books\" violates foreign key constraint \"fk_book_products_asin") &&
                            !e.getMessage().contains("insert or update on table \"dvds\" violates foreign key constraint \"fk_dvds_products_asin") &&
                            !e.getMessage().contains("insert or update on table \"cds\" violates foreign key constraint \"fk_cds_products_asin") &&
                            !e.getMessage().contains("null value in column \"name\" of relation \"tracks\" violates not-null constraint") &&
                            !e.getMessage().contains("null value in column \"asin\" of relation \"books\" violates not-null constraint") &&
                            !e.getMessage().contains("null value in column \"asin\" of relation \"cds\" violates not-null constraint") &&
                            !e.getMessage().contains("null value in column \"asin\" of relation \"dvds\" violates not-null constraint") &&
                            !e.getMessage().contains("null value in column \"products_asin\" of relation \"priceinfos\" violates not-null constraint") &&
                            !e.getMessage().contains("null value in column \"cds_asin\" of relation \"tracks\" violates not-null constraint") &&
                            !e.getMessage().contains("null value in column \"products_asin\" of relation \"products_similars\" violates not-null constraint") &&
                            !e.getMessage().contains("duplicate key value violates unique constraint \"users_pkey\"") &&
                            !e.getMessage().contains("duplicate key value violates unique constraint \"unique_trackname_cd\"") &&
                            !e.getMessage().contains("duplicate key value violates unique constraint \"symmetrical_uniqueness_similars\"") &&
                            !e.getMessage().contains("insert or update on table \"products_similars\" violates foreign key constraint \"fk_products_similars_products\"") &&
                            !e.getMessage().contains("insert or update on table \"tracks\" violates foreign key constraint \"fk_tracks_cds\"") &&
                            !e.getMessage().contains("insert or update on table \"products_similars\" violates foreign key constraint \"fk_products_similars_similar_product\"") &&
                            !e.getMessage().contains("insert or update on table \"guestreviews\" violates foreign key constraint \"fk_guestreviews_products\"") &&
                            !e.getMessage().contains("insert or update on table \"userreviews\" violates foreign key constraint \"fk_userreviews_products\"") &&
                            !e.getMessage().contains("duplicate key value violates unique constraint \"unique_username\"") &&
                            !e.getMessage().contains("insert or update on table \"priceinfos\" violates foreign key constraint \"fk_priceinfos_products\""))

                    {
                        if (e.getMessage().contains("No results were returned by the query.")) {System.out.println("\u001B[32mQuery executed successfully: " + sqlStatement + "\u001B[0m");}
                        else {
                            System.out.println("\u001B[31m" + e.getMessage() + " FAILED QUERY: " + sqlStatement +"\u001B[0m");
                            writeErrorToFile(sqlStatement, e.getMessage(), pathToLogFile);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
        }
        return results;
    }
}
package queryBuilderJunctions;

import dbConnection.PostgresConnector;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * This class implements the algorithm to match a junction between two tables.
 */
public class QueryBuilderJunctions {

    /**
     * This method implements the algorithm to match a junction between two tables.
     * @param inputList List of HashMaps, where each HashMap represents a row in the table.
     * @param aValueKey The key in the hashmap representing the column value
     * @param aIdKey The ID to put in the junction table as FK1
     * @param bValueKey The key where the value in b to look up is stored
     * @param bIdKey The ID to put in the junction table as FK2
     * @param aTableName Name of the table a
     * @param bTableName Name of the table b
     * @param junctionTableName Name of the junction table
     * @param junctionTableFK1 Name of FK a for the junction table
     * @param junctionTableFK2 Name of FK b for the junction table
     * @throws SQLException
     */
    public static void executeQuery(
            List<HashMap<String, List<String>>> inputList,
            String aValueKey,
            String aIdKey,
            String bValueKey,
            String bIdKey,
            String aTableName,
            String bTableName,
            String junctionTableName,
            String junctionTableFK1,
            String junctionTableFK2
    ) throws SQLException {
        // Create a new PostgresConnector instance and connect to the database
        PostgresConnector connector = new PostgresConnector("postgres", "");
        List<String> creationStatements = new ArrayList<>();

        try {
            connector.connect();
            // Perform the SQL query to get all entries from table a
            List<String> aQuery = new ArrayList<>();
            aQuery.add("SELECT * FROM " + aTableName);
            List<HashMap<String, Object>> aResults = connector.executeSQLQueryBatch(aQuery, null);
            System.out.println("\u001B[34mRetrieved " + aResults.size() + " entries from table " + aTableName + "\u001B[0m");

            // Perform the SQL query to get all entries from table b
            List<String> bQuery = new ArrayList<>();
            bQuery.add("SELECT * FROM " + bTableName);
            List<HashMap<String, Object>> bResults = connector.executeSQLQueryBatch(bQuery, null);
            System.out.println("\u001B[34mRetrieved " + bResults.size() + " entries from " + bTableName + "\u001B[0m");

            // Process each entry from table a
            for (HashMap<String, Object> aEntry : aResults) {
                String a_id = (String) aEntry.get(aIdKey);
                System.out.println("\u001B[34mProcessing entry with " + aIdKey + " = " + a_id + "\u001B[0m");

                // Get the list of values (v) for the current a_id from the input
                List<String> values = getValuesForAId(a_id, inputList, aValueKey, aIdKey);
                System.out.println("\u001B[34mValues for " + aIdKey + " = " + a_id + ": " + values + "\u001B[0m");

                // Check for matches in table b
                for (HashMap<String, Object> bEntry : bResults) {
                    String b_id = (String) bEntry.get(bIdKey);
                    String j = (String) bEntry.get(bValueKey);

                    // Check if the value (j) from table b matches any value from the list (v)
                    if (values.contains(j)) {
                        System.out.println("\u001B[34mMatch found: " + aIdKey + " = " + a_id + ": " + bIdKey + " = " + b_id + "\u001B[0m");
                        // Write the matches to table c as SQL insert statements
                        String sql = "INSERT INTO " + junctionTableName + " (" + junctionTableFK1 + ", " + junctionTableFK2 + ") VALUES ('" + a_id + "', '" + b_id + "');";
                        creationStatements.add(sql);
                    }
                }
            }
        } finally {
            connector.executeSQLQueryBatch(creationStatements, "./logs/demolog.log");
            connector.disconnect();
        }
    }

    private static List<String> getValuesForAId(
            String a_id,
            List<HashMap<String, List<String>>> inputList,
            String aValueKey,
            String aIdKey
    ) {
        // Retrieve the list of values for the given a_id from the input (List of HashMaps)
        for (HashMap<String, List<String>> entry : inputList) {
            if (entry.containsKey(aIdKey) && entry.get(aIdKey).contains(a_id)) {
                return entry.get(aValueKey);
            }
        }
        return null; // Return null if no matching entry is found (handle this case appropriately in your code)
    }
}
package helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The sqlParser class provides a method to parse an SQL file and extract individual SQL statements.
 * Helper to get queries out of an SQL file.
 */
public class sqlParser {
    /**
     * Parses an SQL file and returns a list of individual SQL statements.
     *
     * @param filePath The path of the SQL file to parse.
     * @return A list of individual SQL statements.
     */
    public static List<String> parseSQLFile(String filePath) {
        List<String> sqlStatements = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder statementBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                    continue; // Skip empty lines and SQL comments
                }

                statementBuilder.append(line);

                if (line.endsWith(";")) {
                    String statement = statementBuilder.toString().trim();
                    sqlStatements.add(statement);
                    statementBuilder.setLength(0); // Clear the string builder
                } else {
                    statementBuilder.append(" ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlStatements;
    }
}

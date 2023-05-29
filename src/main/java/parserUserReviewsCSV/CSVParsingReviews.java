package parserUserReviewsCSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Parses a CSV file containing registered user reviews and returns a list of customer data.
 *
 * @param CSV_FILE_PATH The file path of the CSV file to parse.
 * @return A list of hash maps representing the parsed customer data.
 */
public class CSVParsingReviews {

    /**
     * Parses a CSV file containing registered user reviews and returns a list of customer data.
     *
     * @param CSV_FILE_PATH The file path of the CSV file to parse.
     * @return A list of hash maps representing the parsed customer data.
     */
    public static List<HashMap<String, List<String>>> parseCSVCRegisteredUserReviews(String CSV_FILE_PATH) {
        List<HashMap<String, List<String>>> customerData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String headerLine = reader.readLine();
            String[] headers = headerLine.replaceAll("\"", "").split(",");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                HashMap<String, List<String>> dataEntry = new HashMap<>();

                for (int i = 0; i < headers.length; i++) {
                    String value = escapeString(values[i].replaceAll("\"", ""));
                    List<String> valueList = dataEntry.getOrDefault(headers[i], new ArrayList<>());
                    valueList.add(value);
                    dataEntry.put(headers[i], valueList);
                }

                List<String> userList = dataEntry.get("user");
                if (userList == null || !userList.contains("guest")) {
                    customerData.add(dataEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customerData;
    }

    /**
     * Parses a CSV file containing guest reviews and returns a list of customer data.
     *
     * @param CSV_FILE_PATH The file path of the CSV file to parse.
     * @return A list of hash maps representing the parsed customer data.
     */
    public static List<HashMap<String, List<String>>> parseCSVGuestReviews(String CSV_FILE_PATH) {
        List<HashMap<String, List<String>>> customerData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String headerLine = reader.readLine();
            String[] headers = headerLine.replaceAll("\"", "").split(",");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                HashMap<String, List<String>> dataEntry = new HashMap<>();

                for (int i = 0; i < headers.length; i++) {
                    String value = escapeString(values[i].replaceAll("\"", ""));
                    List<String> valueList = dataEntry.getOrDefault(headers[i], new ArrayList<>());
                    valueList.add(value);
                    dataEntry.put(headers[i], valueList);
                }

                List<String> userList = dataEntry.get("user");
                if (userList == null || userList.contains("guest")) {
                    customerData.add(dataEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customerData;
    }

    /**
     * Escapes special characters in the input string.
     *
     * @param input The input string to escape.
     * @return The escaped string.
     */
    private static String escapeString(String input) {
        input = input.replace("'", "\\'");
        input = input.replace("\"", "\\\"");
        input = input.replace("&", "\\&");
        input = input.replace("[", "\\[");
        input = input.replace("]", "\\]");
        input = input.replace(";", "\\;");
        input = input.replace("\\&quot\\;", "\\\"");
        input = input.replace("\\&amp\\;", "\\&");
        input = input.replace("\\&lt;\\", "\\<");
        input = input.replace("\\&gt;\\", "\\>");
        input = input.replace("\\&lt\\", "\\<");
        input = input.replace("\\&gt\\", "\\>");
        input = input.replace("\\<;BR\\>;", "\\<br\\>");
        input = input.replace("\\<;P\\>;", "\\<p\\>");
        input = input.replace("\\&#228\\;", "ä");
        input = input.replace("\\&#196\\;", "Ä");
        input = input.replace("\\&#252\\;", "ü");
        input = input.replace("\\&#220\\;", "Ü");
        input = input.replace("\\&#246\\;", "ö");
        input = input.replace("\\&#214\\;", "Ö");
        input = input.replace("\\&#223\\;", "ß");
        input = input.replace("\\&#x27\\;", "\\'");
        input = input.replace("\\&#180\\;", "´");
        input = input.replace("\\&#176\\;", "°");
        input = input.replace("\\&#8364\\;", "€");

        return input;
    }
}

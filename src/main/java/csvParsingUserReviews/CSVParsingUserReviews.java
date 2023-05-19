package csvParsingUserReviews;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVParsingUserReviews {
    private static final String CSV_FILE_PATH = "./data/raw/csv/reviews.csv";

    public List<HashMap<String, List<String>>> parseCSVCustomer() {
        List<HashMap<String, List<String>>> customerData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String headerLine = reader.readLine();
            String[] headers = headerLine.split(",");

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

                    customerData.add(dataEntry);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return customerData;
    }



    private String escapeString(String input) {
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

    public static void main(String[] args) {
        CSVParsingUserReviews csvParser = new CSVParsingUserReviews();
        List<HashMap<String, List<String>>> customerData = csvParser.parseCSVCustomer();

        // Print the parsed data for customers
        System.out.println(" Data:");
        for (HashMap<String, List<String>> entry : customerData) {
            System.out.println(entry);
        }
    }
}
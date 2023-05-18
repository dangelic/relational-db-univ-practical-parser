package map;

import queryBuilder.QueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Date;


public class MapperDataTables {

    public static void mapCommonTable(List<HashMap<String, List<String>>> data, HashMap<String, String> dataTypeMapping, String entityName) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);

        for (HashMap<String, List<String>> hashMap : data) {
            Object[] values = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];
                String dataType = dataTypeMapping.get(column);
                List<String> columnData = hashMap.get(column);
                values[i] = getColumnValue(columnData, dataType);
            }

            String sql = QueryBuilder.buildInsertStatement(entityName, columns, values);
            System.out.println(sql); // Print the SQL statement
        }
    }

    private static Object getColumnValue(List<String> columnData, String dataType) {
        if (columnData != null && !columnData.isEmpty()) {
            String value = columnData.get(0);
            if (value != null && !value.isEmpty()) {
                if ("date".equalsIgnoreCase(dataType)) {
                    return convertToDate(value);
                } else if ("integer".equalsIgnoreCase(dataType)) {
                    return convertToInteger(value);
                } else {
                    return value;
                }
            }
        }
        return null;
    }

    private static Date convertToDate(String value) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Modify the date format as per your input
            return dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Integer convertToInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}

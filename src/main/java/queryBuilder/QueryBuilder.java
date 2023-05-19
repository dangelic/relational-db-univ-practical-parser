package queryBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class QueryBuilder {

    public static List<String> getInsertQueriesForCommonEntity(List<HashMap<String, List<String>>> data, HashMap<String, String> dataTypeMapping, String entityName) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);
        List<String> queryList = new ArrayList<>();

        for (HashMap<String, List<String>> hashMap : data) {
            Object[] values = new Object[columns.length];
            String[] mappedColumns = new String[columns.length]; // New array to store mapped column names
            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];
                String dataType = dataTypeMapping.get(column);
                String columnName = getColumnName(dataType);
                List<String> columnData = hashMap.get(column);
                values[i] = getColumnValue(columnData, dataType);
                mappedColumns[i] = columnName; // Store mapped column names
            }

            String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values); // Use mapped column names
            queryList.add(sql);
            System.out.println(sql); // Print the SQL statement
        }

        return queryList;
    }

    public static List<String> getInsertQueriesForNestedEntity(List<HashMap<String, List<String>>> data, HashMap<String, String> dataTypeMapping, String entityName, String fieldName, Integer idStart, String idName) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);
        List<String> queryList = new ArrayList<>();

        int id = idStart;
        for (HashMap<String, List<String>> hashMap : data) {
            List<String> fieldItems = hashMap.get(fieldName);

            if (fieldItems != null && !fieldItems.isEmpty()) {
                for (String fieldItem : fieldItems) {
                    Object[] values = new Object[columns.length + 1];
                    String[] mappedColumns = new String[columns.length + 1];
                    values[0] = id;
                    mappedColumns[0] = idName;

                    for (int i = 0; i < columns.length; i++) {
                        String column = columns[i];
                        String dataType = dataTypeMapping.get(column);
                        String columnName = getColumnName(dataType);
                        List<String> columnData = hashMap.get(column);

                        if (column.equals(fieldName)) {
                            values[i + 1] = fieldItem;
                        } else {
                            values[i + 1] = getColumnValue(columnData, dataType);
                        }

                        mappedColumns[i + 1] = columnName;
                    }

                    String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values);
                    queryList.add(sql);
                    System.out.println(sql);

                    id++;
                }
            }
        }

        return queryList;
    }


    public static List<String> getInsertQueriesForNestedEntitySuppressDuplicates(List<HashMap<String, List<String>>> data, HashMap<String, String> dataTypeMapping, String entityName, String fieldName, Integer idStart, String idName) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);
        List<String> queryList = new ArrayList<>();
        Set<String> insertedTitles = new HashSet<>();

        int id = idStart;
        for (HashMap<String, List<String>> hashMap : data) {
            List<String> fieldItems = hashMap.get(fieldName);

            if (fieldItems != null && !fieldItems.isEmpty()) {
                for (String fieldItem : fieldItems) {
                    if (insertedTitles.contains(fieldItem)) {
                        continue; // Skip duplicate item
                    }

                    Object[] values = new Object[columns.length + 1];
                    String[] mappedColumns = new String[columns.length + 1];
                    values[0] = id;
                    mappedColumns[0] = idName;

                    for (int i = 0; i < columns.length; i++) {
                        String column = columns[i];
                        String dataType = dataTypeMapping.get(column);
                        String columnName = getColumnName(dataType);
                        List<String> columnData = hashMap.get(column);

                        if (column.equals(fieldName)) {
                            values[i + 1] = fieldItem;
                        } else {
                            values[i + 1] = getColumnValue(columnData, dataType);
                        }

                        mappedColumns[i + 1] = columnName;
                    }

                    String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values);
                    queryList.add(sql);
                    System.out.println(sql);

                    insertedTitles.add(fieldItem); // Add inserted field item to the set
                    id++;
                }
            }
        }

        return queryList;
    }



    private static String getColumnName(String dataType) {
        int index = dataType.indexOf('@');
        if (index >= 0) {
            return dataType.substring(0, index);
        }
        return dataType;
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

    private static java.util.Date convertToDate(String value) {
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

class InsertQueryStringGenerator {

    public static String buildInsertStatement(String tableName, String[] columns, Object[] values) {
        if (tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("Table name is required.");
        }

        if (columns == null || values == null) {
            throw new IllegalArgumentException("Columns and values must not be null.");
        }

        if (columns.length != values.length) {
            throw new IllegalArgumentException("Columns and values must have the same number of elements.");
        }

        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ")
                .append(tableName)
                .append(" (");

        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i]);
            if (i < columns.length - 1) {
                builder.append(", ");
            }
        }

        builder.append(") VALUES (");

        for (int i = 0; i < values.length; i++) {
            builder.append(formatValue(values[i]));
            if (i < values.length - 1) {
                builder.append(", ");
            }
        }

        builder.append(");");

        return builder.toString();
    }

    private static String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        } else if (value instanceof String) {
            return "'" + value.toString() + "'";
        } else if (value instanceof Date) {
            return "'" + value.toString() + "'";
        } else {
            return Objects.toString(value);
        }
    }
}

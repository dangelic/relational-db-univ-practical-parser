package queryBuilderStandardEntities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import java.text.DecimalFormat;

public class QueryBuilderStandard {

    public static List<String> getInsertQueriesForCommonEntity(
            List<HashMap<String, List<String>>> data,
            HashMap<String, String> dataTypeMapping,
            String entityName,
            String idName
    ) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);
        List<String> queryList = new ArrayList<>();
        HashSet<String> uniqueIds = new HashSet<>(); // Track unique ids

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> idValues = hashMap.get(idName); // Get values for idName

            String idValue = (idValues != null && !idValues.isEmpty()) ? idValues.get(0) : null; // Assume single value for idName

            if (idValue == null || idValue.isEmpty() || !uniqueIds.contains(idValue)) {
                // Add idValue to set of unique ids if it's null, empty, or not already present
                uniqueIds.add(idValue);
            } else {
                continue; // Skip if idValue is already added
            }

            Object[] values = new Object[columns.length];
            String[] mappedColumns = new String[columns.length];

            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];
                String dataType = dataTypeMapping.get(column);
                String columnName = getColumnName(dataType);
                List<String> columnData = hashMap.get(column);
                values[i] = getColumnValue(columnData, dataType);
                mappedColumns[i] = columnName;
            }

            String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values);
            queryList.add(sql);
        }

        return queryList;
    }

    public static List<String> getInsertQueriesForCommonEntity2(
            List<HashMap<String, List<String>>> data,
            HashMap<String, String> dataTypeMapping,
            String entityName,
            String idName
    ) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);
        List<String> queryList = new ArrayList<>();

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> idValues = hashMap.get(idName); // Get values for idName

            String idValue = (idValues != null && !idValues.isEmpty()) ? idValues.get(0) : null; // Assume single value for idName

            if (idValue == null || idValue.isEmpty()) {
                // Skip the entry if idValue is null or empty
                continue;
            }

            Object[] values = new Object[columns.length];
            String[] mappedColumns = new String[columns.length];

            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];
                String dataType = dataTypeMapping.get(column);
                String columnName = getColumnName(dataType);
                List<String> columnData = hashMap.get(column);
                values[i] = getColumnValue(columnData, dataType);
                mappedColumns[i] = columnName;
            }

            String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values);
            queryList.add(sql);
        }

        return queryList;
    }




    public static List<String> getInsertQueriesForCommonEntityGenId(List<HashMap<String, List<String>>> data, HashMap<String, String> dataTypeMapping, String entityName, String idName, int idStart) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);
        List<String> queryList = new ArrayList<>();
        int currentId = idStart;

        for (HashMap<String, List<String>> hashMap : data) {
            Object[] values = new Object[columns.length + 1]; // Add one for the generated id
            String[] mappedColumns = new String[columns.length + 1]; // Add one for the generated id

            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];
                String dataType = dataTypeMapping.get(column);
                String columnName = getColumnName(dataType);
                List<String> columnData = hashMap.get(column);
                values[i] = getColumnValue(columnData, dataType);
                mappedColumns[i] = columnName; // Store mapped column names
            }

            // Add generated id column and value
            String idColumnName = getColumnName(idName);
            values[columns.length] = String.valueOf(currentId++); // Assuming idStart is an int
            mappedColumns[columns.length] = idColumnName;

            String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values); // Use mapped column names
            queryList.add(sql);
        }

        return queryList;
    }


    public static List<String> getInsertQueriesForCommonEntityGenIdFilter(List<HashMap<String, List<String>>> data, HashMap<String, String> dataTypeMapping, String entityName, String idName, int idStart, String filter) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);
        List<String> queryList = new ArrayList<>();
        int currentId = idStart;

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> pgroupValues = hashMap.get("pgroup");
            if (pgroupValues == null || pgroupValues.isEmpty() || !pgroupValues.contains(filter)) {
                continue; // Skip iteration if "pgroup" attribute is missing or doesn't contain the filter value
            }

            Object[] values = new Object[columns.length + 1]; // Add one for the generated id
            String[] mappedColumns = new String[columns.length + 1]; // Add one for the generated id

            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];
                String dataType = dataTypeMapping.get(column);
                String columnName = getColumnName(dataType);
                List<String> columnData = hashMap.get(column);
                values[i] = getColumnValue(columnData, dataType);
                mappedColumns[i] = columnName; // Store mapped column names
            }

            // Add generated id column and value
            String idColumnName = getColumnName(idName);
            values[columns.length] = String.valueOf(currentId++); // Assuming idStart is an int
            mappedColumns[columns.length] = idColumnName;

            String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values); // Use mapped column names
            queryList.add(sql);
        }

        return queryList;
    }



    public static List<String> getInsertQueriesForNestedEntityGenId(List<HashMap<String, List<String>>> data, HashMap<String, String> dataTypeMapping, String entityName, String fieldName, Integer idStart, String idName) {
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
                            values[i + 1] = escapeString(fieldItem); // Escape the fieldItem
                        } else {
                            values[i + 1] = getColumnValue(columnData, dataType);
                        }

                        mappedColumns[i + 1] = columnName;
                    }

                    String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values);
                    queryList.add(sql);

                    id++;
                }
            }
        }

        return queryList;
    }

    public static List<String> getInsertQueriesForNestedEntity(
            List<HashMap<String, List<String>>> data,
            HashMap<String, String> dataTypeMapping,
            String entityName,
            String fieldName
    ) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);
        List<String> queryList = new ArrayList<>();

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> fieldItems = hashMap.get(fieldName);

            if (fieldItems != null && !fieldItems.isEmpty()) {
                for (String fieldItem : fieldItems) {
                    Object[] values = new Object[columns.length];
                    String[] mappedColumns = new String[columns.length];

                    for (int i = 0; i < columns.length; i++) {
                        String column = columns[i];
                        String dataType = dataTypeMapping.get(column);
                        String columnName = getColumnName(dataType);
                        List<String> columnData = hashMap.get(column);

                        if (column.equals(fieldName)) {
                            values[i] = escapeString(fieldItem); // Escape the fieldItem
                        } else {
                            values[i] = getColumnValue(columnData, dataType);
                        }

                        mappedColumns[i] = columnName;
                    }

                    String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values);
                    queryList.add(sql);
                }
            }
        }

        return queryList;
    }




    public static List<String> getInsertQueriesForNestedEntitySuppressDuplicatesGenId(List<HashMap<String, List<String>>> data, HashMap<String, String> dataTypeMapping, String entityName, String fieldName, Integer idStart, String idName) {
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
                            values[i + 1] = escapeString(fieldItem); // Escape the fieldItem
                        } else {
                            values[i + 1] = getColumnValue(columnData, dataType);
                        }

                        mappedColumns[i + 1] = columnName;
                    }

                    String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values);
                    queryList.add(sql);

                    insertedTitles.add(fieldItem); // Add inserted field item to the set
                    id++;
                }
            }
        }

        return queryList;
    }



    public static List<String> getInsertQueriesForNestedEntitySuppressDuplicatesMultipleValuesGenId(
            List<HashMap<String, List<String>>> data,
            HashMap<String, String> dataTypeMapping,
            String entityName,
            List<String> fieldNames,
            Integer idStart,
            String idName
    ) {
        String[] columns = dataTypeMapping.keySet().toArray(new String[0]);
        List<String> queryList = new ArrayList<>();
        Set<List<String>> insertedItems = new HashSet<>();
        int id = idStart;

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> combinedFieldItems = new ArrayList<>();
            for (String fieldName : fieldNames) {
                List<String> fieldItems = hashMap.get(fieldName);
                if (fieldItems != null) {
                    combinedFieldItems.addAll(fieldItems);
                }
            }

            if (!combinedFieldItems.isEmpty()) {
                for (String fieldItem : combinedFieldItems) {
                    List<String> key = new ArrayList<>();
                    key.add(fieldItem);
                    if (insertedItems.contains(key)) {
                        continue; // Skip duplicate item
                    }

                    Object[] values = new Object[columns.length + fieldNames.size() + 1];
                    String[] mappedColumns = new String[columns.length + fieldNames.size() + 1];
                    values[0] = id;
                    mappedColumns[0] = idName;

                    int columnIndex = 1;
                    for (int i = 0; i < columns.length; i++) {
                        String column = columns[i];
                        String dataType = dataTypeMapping.get(column);
                        String columnName = getColumnName(dataType);
                        List<String> columnData = hashMap.get(column);

                        if (fieldNames.contains(column)) {
                            values[columnIndex] = escapeString(fieldItem); // Escape the fieldItem
                            mappedColumns[columnIndex] = columnName;
                            columnIndex++;
                        } else {
                            values[columnIndex + fieldNames.size()] = getColumnValue(columnData, dataType);
                            mappedColumns[columnIndex + fieldNames.size()] = columnName;
                        }
                    }

                    for (String fieldName : fieldNames) {
                        values[columnIndex] = getColumnValue(hashMap.get(fieldName), dataTypeMapping.get(fieldName));
                        mappedColumns[columnIndex] = getColumnName(dataTypeMapping.get(fieldName));
                        columnIndex++;
                    }

                    String sql = InsertQueryStringGenerator.buildInsertStatement(entityName, mappedColumns, values);
                    queryList.add(sql);

                    insertedItems.add(key); // Add inserted field item to the set
                    id++;
                }
            }
        }

        return queryList;
    }





    private static String escapeString(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("\"", "\"\"").replace("'", "''");
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
                } else if ("float".equalsIgnoreCase(dataType)) {
                    return convertToFloat(value);
                } else {
                    return escapeString(value); // Escape the value
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

    private static Float convertToFloat(String value) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setParseBigDecimal(true);
            return decimalFormat.parse(value).floatValue();
        } catch (NumberFormatException | ParseException e) {
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

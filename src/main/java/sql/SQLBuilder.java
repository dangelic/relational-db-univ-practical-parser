package sql;

import java.sql.Date;
import java.util.Objects;

public class SQLBuilder {

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

        builder.append(")");

        return builder.toString();
    }

    private static String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        } else if (value instanceof String) {
            return "'" + value.toString().replaceAll("'", "''") + "'";
        } else if (value instanceof Date) {
            return "'" + value.toString() + "'";
        } else {
            return Objects.toString(value);
        }
    }
}

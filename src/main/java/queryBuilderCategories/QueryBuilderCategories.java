package queryBuilderCategories;

import java.util.List;
import parserCategoriesXML.Category;

public class QueryBuilderCategories {

    private static int categoryId = 1;

    public static String generateInsertSQL(String tableName, List<Category> categories, Integer parentCategoryId) {
        StringBuilder sqlBuilder = new StringBuilder();

        for (Category category : categories) {
            String sql = generateInsertSQLRecursive(tableName, category, parentCategoryId);
            sqlBuilder.append(sql).append("\n");
        }

        return sqlBuilder.toString();
    }

    private static String generateInsertSQLRecursive(String tableName, Category category, Integer parentCategoryId) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ")
                .append(tableName)
                .append(" (category_id, parent_category_id, name) VALUES (")
                .append(categoryId++)
                .append(", ")
                .append(parentCategoryId != null ? parentCategoryId : "NULL")
                .append(", '")
                .append(category.getName())
                .append("');");

        for (Category subCategory : category.getSubCategories()) {
            String subCategorySQL = generateInsertSQLRecursive(tableName, subCategory, categoryId - 1);
            sqlBuilder.append("\n").append(subCategorySQL);
        }
        // System.out.println(sqlBuilder.toString());
        return sqlBuilder.toString();
    }
}

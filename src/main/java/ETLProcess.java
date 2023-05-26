import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import categoriesLogic.ParserToMapToGenerateSQLCategories;
import helper.sqlParser;
import dbConnection.PostgresConnector;
import java.io.File;

import parserProductsXML.XMLParsingProducts;

import queryBuilderCommonEntities.QueryBuilderCommon;

import entityMap.EntityFieldDTMappings;

import java.sql.SQLException;

import queryBuilderJunctions2.QueryBuilderJunction2;


public class ETLProcess {
    public static void main(String[] args) {

        initializeDatabaseScheme("./sql/drop_tables_casc.sql",  "./sql/create_tables.sql", "./sql/add_constraints.sql");
        List<HashMap<String, List<String>>> parsedDataProductsMerged = parseProductsFromShopsMerged("./data/raw/xml/leipzig_transformed.xml", "./data/raw/xml/dresden.xml");
        loadCommonData(parsedDataProductsMerged);
        // loadCategoriesData("./data/raw/xml/categories.xml");



//

    }

    private static void initializeDatabaseScheme(String dropSQLFilePath, String createSQLFilePath, String constraintsSQLFilePath) {

        String pathToLogFile = "./logs/setup_database.log";

        File logFile = new File(pathToLogFile);
        if (logFile.exists()) logFile.delete();

        PostgresConnector dbConnection = new PostgresConnector("postgres", "");
        try {
            dbConnection.connect();

            System.out.println("Initialize Database Scheme...");

            System.out.println("DROP ALL TABLES...");
            List<String> dropSQLStatements = sqlParser.parseSQLFile(dropSQLFilePath);
            dbConnection.executeSQLQueryBatch(dropSQLStatements, pathToLogFile);
            System.out.println("DONE.");

            System.out.println("CREATE TABLES...");
            List<String> creationSQLStatements = sqlParser.parseSQLFile(createSQLFilePath);
            dbConnection.executeSQLQueryBatch(creationSQLStatements, pathToLogFile);
            System.out.println("DONE.");

            System.out.println("ADD TABLE CONSTRAINTS...");
            List<String> constraintSQLStatements = sqlParser.parseSQLFile(constraintsSQLFilePath);
            dbConnection.executeSQLQueryBatch(constraintSQLStatements, pathToLogFile);
            System.out.println("DONE.");

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<HashMap<String, List<String>>> parseProductsFromShopsMerged(String pathToLeipzigRawXML, String pathToDresdenRawXML) {

        // Parse Leipzig
        List<String> leipzig = new ArrayList<>();
        leipzig.add("LEIPZIG");
        List<HashMap<String, List<String>>> parsedXMLProductDataLeipzig = XMLParsingProducts.parseXMLFile(pathToLeipzigRawXML, leipzig);
        // Parse Dresden
        List<String> dresden = new ArrayList<>();
        dresden.add("DRESDEN");
        List<HashMap<String, List<String>>> parsedXMLProductDataDresden = XMLParsingProducts.parseXMLFile(pathToDresdenRawXML, dresden);

        // Merge the two lists
        parsedXMLProductDataLeipzig.addAll(parsedXMLProductDataDresden);
        List<HashMap<String, List<String>>> parsedXMLProductDataMerged = parsedXMLProductDataLeipzig;
        return parsedXMLProductDataMerged;
    }

    public static void loadCommonData(List<HashMap<String, List<String>>> parsedXMLProductDataMerged) {
        String pathToLogFile = "./logs/demoload.log";
        File logFile = new File(pathToLogFile);
        if (logFile.exists()) logFile.delete();

        HashMap<String, String> dataTypeMapping;
        List<String> fillingData;

        PostgresConnector dbConnection = new PostgresConnector("postgres", "");

        try {
            dbConnection.connect();

//            dataTypeMapping = EntityFieldDTMappings.getProductsEntityFieldDTMappings();
//            fillingData = QueryBuilderCommon.getInsertQueriesForCommonEntity(parsedXMLProductDataMerged, dataTypeMapping, "products");
//            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);
//
//            dataTypeMapping = EntityFieldDTMappings.getBooksEntityFieldDTMappings();
//            fillingData = QueryBuilderCommon.getInsertQueriesForCommonEntity(parsedXMLProductDataMerged, dataTypeMapping, "books");
//            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);
//
//            dataTypeMapping = EntityFieldDTMappings.getAuthorsEntityFieldDTMappings();
//            fillingData = QueryBuilderCommon.getInsertQueriesForNestedEntitySuppressDuplicates(parsedXMLProductDataMerged, dataTypeMapping, "authors", "authors", 1, "author_id");
//            dbConnection.executeSQLQueryBatch(fillingData, "./failed_authors.log");

            dataTypeMapping = EntityFieldDTMappings.getPriceinfosEntityFieldDTMappings();
            fillingData = QueryBuilderCommon.getInsertQueriesForCommonEntityWithGeneratedId(parsedXMLProductDataMerged, dataTypeMapping, "priceinfos", "priceinfo_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadCategoriesData(String filePath) {
        String pathToLogFile = "./logs/categories_test.log";
        File logFile = new File(pathToLogFile);
        if (logFile.exists()) logFile.delete();

        List<String> fillingData;



        PostgresConnector dbConnection = new PostgresConnector("postgres", "");

        try {
            dbConnection.connect();


            System.out.println("LOAD CATEGORIES DATA...");
            ParserToMapToGenerateSQLCategories parserToMapToGenerateSQLCategories = new ParserToMapToGenerateSQLCategories();
            fillingData = parserToMapToGenerateSQLCategories.generateSQLCategoryEntity(filePath);
            dbConnection.executeSQLQueryBatch(fillingData, "./logs/categories_test.log");
            System.out.println("DONE.");

            System.out.println("LOAD JUNCTIONS DATA PRODUCTS CATEGORIES...");
            fillingData = parserToMapToGenerateSQLCategories.generateSQLJunction(filePath);
            dbConnection.executeSQLQueryBatch(fillingData, "./logs/categories_test.log");
            System.out.println("DONE.");


            dbConnection.disconnect();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

}

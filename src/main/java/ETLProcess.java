import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.sqlParser;
import dbConnection.PostgresConnector;
import java.io.File;

import parserProductsXML.XMLParsingProducts;

import parserCategoriesXML.XMLParsingCategories;
import parserCategoriesXML.Category;
import queryBuilderCategories.QueryBuilderCategories;

import queryBuilderCommonEntities.QueryBuilderCommon;

import entityMap.EntityFieldDTMappings;
import org.xml.sax.SAXException;
import java.sql.SQLException;




import parserShopsXML.XMLParsingShops;
import queryBuilderJunctions.QueryBuilderJunctions;


public class ETLProcess {
    public static void main(String[] args) {

        // initializeDatabaseScheme();
        List<HashMap<String, List<String>>> parsedXMLProductDataMerged = parseProductsFromShopsMerged();
        //loadCommonData(parsedXMLProductDataMerged);

        try {
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged,
                    "authors",
                    "asin",
                    "name",
                    "author_id",
                    "books",
                    "authors",
                    "junction",
                    "a_key",
                    "b_key");
        } catch (SQLException e) {
            // Handle the SQLException here
            e.printStackTrace();
        }

    }

    private static void initializeDatabaseScheme() {

        String pathToLogFile = "./logs/setup_database.log";

        File logFile = new File(pathToLogFile);
        if (logFile.exists()) logFile.delete();

        PostgresConnector dbConnection = new PostgresConnector("postgres", "");
        try {
            dbConnection.connect();

            System.out.println("Initialize Database Scheme...");

            System.out.println("DROP ALL TABLES...");
            String dropSQLFilePath = "./sql/drop_tables_casc.sql";
            List<String> dropSQLStatements = sqlParser.parseSQLFile(dropSQLFilePath);
            dbConnection.executeSQLQueryBatch(dropSQLStatements, pathToLogFile);
            System.out.println("DONE.");

            System.out.println("CREATE TABLES...");
            String createSQLFilePath = "./sql/create_tables.sql";
            List<String> creationSQLStatements = sqlParser.parseSQLFile(createSQLFilePath);
            dbConnection.executeSQLQueryBatch(creationSQLStatements, pathToLogFile);
            System.out.println("DONE.");

            System.out.println("ADD TABLE CONSTRAINTS...");
            String constraintsSQLFilePath = "./sql/add_constraints.sql";
            List<String> constraintSQLStatements = sqlParser.parseSQLFile(constraintsSQLFilePath);
            dbConnection.executeSQLQueryBatch(constraintSQLStatements, pathToLogFile);
            System.out.println("DONE.");

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static List<HashMap<String, List<String>>> parseProductsFromShopsMerged() {

        // Parse Leipzig
        String pathToLeipzigRawXML = "./data/raw/xml/leipzig_transformed.xml";
        List<String> leipzig = new ArrayList<>();
        leipzig.add("LEIPZIG");
        List<HashMap<String, List<String>>> parsedXMLProductDataLeipzig = XMLParsingProducts.parseXMLFile(pathToLeipzigRawXML, leipzig);
        // Parse Dresden
        String pathToDresdenRawXML = "./data/raw/xml/dresden.xml";
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

            dataTypeMapping = EntityFieldDTMappings.getProductsEntityFieldDTMappings();
            fillingData = QueryBuilderCommon.getInsertQueriesForCommonEntity(parsedXMLProductDataMerged, dataTypeMapping, "products");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            dataTypeMapping = EntityFieldDTMappings.getBooksEntityFieldDTMappings();
            fillingData = QueryBuilderCommon.getInsertQueriesForCommonEntity(parsedXMLProductDataMerged, dataTypeMapping, "books");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            dataTypeMapping = EntityFieldDTMappings.getAuthorsEntityFieldDTMappings();
            fillingData = QueryBuilderCommon.getInsertQueriesForNestedEntitySuppressDuplicates(parsedXMLProductDataMerged, dataTypeMapping, "authors", "authors", 1, "author_id");
            dbConnection.executeSQLQueryBatch(fillingData, "./failed_authors.log");

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.sqlParser;
import dbConnection.PostgresConnector;
import java.io.File;

import parserProductsXML.XMLParsingProducts;


public class ETLProcess {
    public static void main(String[] args) {

        initializeDatabaseScheme();





        //initializeDatabaseScheme(sqlStatementTableDeletion, sqlStatementTableCreation, sqlStatementsConstraints);

        // ## Parse Shops XML for Leipzig and Dresden
        // Parse Leipzig
        //String pathToLeipzigRawXML = "./data/raw/xml/leipzig_transformed.xml";
        //List<String> leipzig = new ArrayList<>();
        //leipzig.add("LEIPZIG");
        //List<HashMap<String, List<String>>> parsedXMLProductDataLeipzig = XMLParsingProducts.parseXMLFile(pathToLeipzigRawXML, leipzig);
        // Parse Dresden
        //String pathToDresdenRawXML = "./data/raw/xml/dresden.xml";
        //List<String> dresden = new ArrayList<>();
        //dresden.add("DRESDEN");
        // List<HashMap<String, List<String>>> parsedXMLProductDataDresden = XMLParsingProducts.parseXMLFile(pathToDresdenRawXML, dresden);

        // Merge the two lists
        // parsedXMLProductDataLeipzig.addAll(parsedXMLProductDataDresden);
        // List<HashMap<String, List<String>>> parsedXMLProductDataMerged = parsedXMLProductDataLeipzig;

        HashMap<String, String> dataTypeMapping = new HashMap<>();
        dataTypeMapping.put("asin", "asin@string");
        dataTypeMapping.put("tracks", "tracks@string");
        dataTypeMapping.put("pgroup", "pgroup2@string");

        // QueryBuilder.getInsertQueriesForNestedEntitySuppressDuplicates(parsedXMLProductDataMerged, dataTypeMapping, "products", "tracks", 1, "my_id");
    }

    private static void initializeDatabaseScheme() {

        String pathToLogFile = "./logs/setup_database.log";

        File logFile = new File(pathToLogFile);
        if (logFile.exists()) logFile.delete();

        System.out.println("Initialize Database Scheme...");

        System.out.println("DROP ALL TABLES...");
        String dropSQLFilePath = "./sql/drop_tables_casc.sql";
        List<String> dropSQLStatements = sqlParser.parseSQLFile(dropSQLFilePath);
        PostgresConnector.executeSQLQueryBatch(dropSQLStatements, pathToLogFile);
        System.out.println("DONE.");

        System.out.println("CREATE TABLES...");
        String createSQLFilePath = "./sql/create_tables.sql";
        List<String> creationSQLStatements = sqlParser.parseSQLFile(createSQLFilePath);
        PostgresConnector.executeSQLQueryBatch(creationSQLStatements, pathToLogFile);
        System.out.println("DONE.");

        System.out.println("ADD TABLE CONSTRAINTS...");
        String constraintsSQLFilePath = "./sql/add_constraints.sql";
        List<String> constraintSQLStatements = sqlParser.parseSQLFile(constraintsSQLFilePath);
        PostgresConnector.executeSQLQueryBatch(constraintSQLStatements, pathToLogFile);
        System.out.println("DONE.");
    }
}
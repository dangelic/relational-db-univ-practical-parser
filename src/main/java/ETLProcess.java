import LogGenerationRejection.LogGenerator;
import parserShopsXML.XMLParsingShops;
import parserProductsXML.XMLParsingProducts;
import entityMap.EntityFieldDTMappings;
import parserUserReviewsCSV.CSVParsingReviews;
import queryBuilderStandardEntities.QueryBuilderStandard;
import categoriesLogic.ParserToMapToGenerateSQLCategories;
import helper.sqlParser;
import dataCleanUp.CleanUpOperations;
import dbConnection.PostgresConnector;
import queryBuilderJunctions.QueryBuilderJunctions;


import static java.lang.System.out;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;

public class ETLProcess {

    private static String pathToLogFile = "./logs/constraint_violations.log";
    private static String pathToLogFileDebugDB = "./logs/debug_db.log";
    private static String pathToLeipzigShopXML = "./data/raw/xml/leipzig_transformed.xml";
    private static String pathToDresdenShopXML = "./data/raw/xml/dresden.xml";
    private static String pathToCategoriesXML = "./data/raw/xml/categories.xml";
    private static String pathToUserReviewsCSV = "./data/raw/csv/reviews.csv";

    static PostgresConnector dbConnection = new PostgresConnector("postgres", "");

    public static void main(String[] args) {

        File logFileConstraintViolations = new File(pathToLogFile);
        if (logFileConstraintViolations.exists()) logFileConstraintViolations.delete();
        File logFileDB = new File(pathToLogFileDebugDB);
        if (logFileDB.exists()) logFileDB.delete();

        //initializeDatabaseScheme();
        //loadShopData();

        List < HashMap < String, List < String >>> parsedXMLProductDataMerged = parseProductsFromShopsMerged();
        // List < HashMap < String, List < String >>> parsedXMLProductDataMergedCleanPre = runCleanUpTasksPre(parsedXMLProductDataMerged);

        List < HashMap < String, List < String >>> parsedCSVReviewsFromUsers = CSVParsingReviews.parseCSVCRegisteredUserReviews(pathToUserReviewsCSV);
        List < HashMap < String, List < String >>> parsedCSVReviewsFromGuests = CSVParsingReviews.parseCSVGuestReviews(pathToUserReviewsCSV);
        // Merge the two lists
        parsedCSVReviewsFromUsers.addAll(parsedCSVReviewsFromGuests);
        List < HashMap < String, List < String >>> parsedCSVReviewsDataMerged = parsedCSVReviewsFromUsers;


        LogGenerator.check(parsedXMLProductDataMerged, parsedCSVReviewsDataMerged);

        //loadCommonData(parsedXMLProductDataMergedCleanPre);
        //loadNormalizationTablesData(parsedXMLProductDataMergedCleanPre);
        //loadJunctionData(parsedXMLProductDataMergedCleanPre);

        //loadCategoriesData("./data/raw/xml/categories.xml");

        //loadUsersAndReviewsData();

    }

    /**
     * Initializes the database schema based on the specified SQL files in ./sql. Relevant initialization commands are divided into four files.
     *
     * @param dropSQLFilePath         The file path to the SQL file that contains the DROP commands for all tables.
     * @param createSQLFilePath       The file path to the SQL file that contains the CREATE commands for all tables.
     * @param constraintsSQLFilePath  The file path to the SQL file that contains the CONSTRAINT commands for all tables.
     *                                add
     */
    private static void initializeDatabaseScheme() {

        String dropSQLFilePath = "./sql/drop_tables_casc.sql";
        String createSQLFilePath = "./sql/create_tables.sql";
        String constraintsSQLFilePath = "./sql/add_constraints.sql";
        String indexSQLFilePath = "./sql/add_index.sql";

        try {
            dbConnection.connect();
            out.println("Initialize Database Scheme...");

            out.println("DROP ALL TABLES...");
            List < String > dropSQLStatements = sqlParser.parseSQLFile(dropSQLFilePath); // Parse statements from file with helper method.
            dbConnection.executeSQLQueryBatch(dropSQLStatements, pathToLogFileDebugDB);
            out.println("DONE.");

            out.println("CREATE TABLES...");
            List < String > creationSQLStatements = sqlParser.parseSQLFile(createSQLFilePath);
            dbConnection.executeSQLQueryBatch(creationSQLStatements, pathToLogFileDebugDB);
            out.println("DONE.");

            out.println("ADD TABLE CONSTRAINTS...");
            List < String > constraintSQLStatements = sqlParser.parseSQLFile(constraintsSQLFilePath);
            dbConnection.executeSQLQueryBatch(constraintSQLStatements, pathToLogFileDebugDB);
            out.println("DONE.");

            out.println("ADD INDEXES...");
            List < String > idxStatements = sqlParser.parseSQLFile(indexSQLFilePath);
            dbConnection.executeSQLQueryBatch(idxStatements, pathToLogFileDebugDB);
            out.println("DONE.");

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the product data from both shops and merges them into one single List of HashMaps
     * The structure of the Lists/ HashMaps is explained in XMLParsingProducts Class.
     *
     * @param pathToLeipzigRawXML   The file path to the raw XML file for Leipzig.
     * @param pathToDresdenRawXML   The file path to the raw XML file for Dresden.
     * @return The merged product data parsed from the XML shop files.
     */
    private static List < HashMap < String, List < String >>> parseProductsFromShopsMerged() {

        // Parse Leipzig
        List < String > leipzig = new ArrayList < > (); // Pass in a ArrayList with one Value (and no simple String) as everything in the parser is handled as a List.
        leipzig.add("LEIPZIG");
        List < HashMap < String, List < String >>> parsedXMLProductDataLeipzig = XMLParsingProducts.parseXMLFile(pathToLeipzigShopXML, leipzig);

        // Parse Dresden
        List < String > dresden = new ArrayList < > ();
        dresden.add("DRESDEN");
        List < HashMap < String, List < String >>> parsedXMLProductDataDresden = XMLParsingProducts.parseXMLFile(pathToDresdenShopXML, dresden);

        // Merge the two lists
        parsedXMLProductDataLeipzig.addAll(parsedXMLProductDataDresden);
        List < HashMap < String, List < String >>> parsedXMLProductDataMerged = parsedXMLProductDataLeipzig;

        return parsedXMLProductDataMerged;
    }

    private static List < HashMap < String, List < String >>> parseShopDataMerged(String pathToLeipzigRawXML, String pathToDresdenRawXML) {
        // Parse Leipzig
        List < HashMap < String, List < String >>> shopDataLeipzig = XMLParsingShops.parseXMLFile(pathToLeipzigRawXML);
        // Parse Dresden
        List < HashMap < String, List < String >>> shopDataDresden = XMLParsingShops.parseXMLFile(pathToDresdenRawXML);

        // Merge the two lists
        shopDataLeipzig.addAll(shopDataDresden);
        List < HashMap < String, List < String >>> parsedXMLProductDataMerged = shopDataLeipzig;

        return parsedXMLProductDataMerged;
    }

    private static List < HashMap < String, List < String >>> runCleanUpTasksPre(List < HashMap < String, List < String >>> parsedXMLProductDataMerged) {

        List < HashMap < String, List < String >>> parsedXMLProductDataMergedCleanedPre;
        out.println("Run clean jobs on product data...");
        parsedXMLProductDataMergedCleanedPre = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMerged, "title");
        parsedXMLProductDataMergedCleanedPre = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPre, "studios");
        parsedXMLProductDataMergedCleanedPre = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPre, "similars");
        parsedXMLProductDataMergedCleanedPre = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPre, "labels");
        parsedXMLProductDataMergedCleanedPre = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPre, "tracks");
        parsedXMLProductDataMergedCleanedPre = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPre, "authors");
        parsedXMLProductDataMergedCleanedPre = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPre, "publishers");
        parsedXMLProductDataMergedCleanedPre = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPre, "listmania_lists");
        parsedXMLProductDataMergedCleanedPre = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPre, "creators");
        out.println("DONE.");
        out.println(parsedXMLProductDataMergedCleanedPre);
        return parsedXMLProductDataMergedCleanedPre;
    }

    public static void loadShopData() {

        List < HashMap < String, List < String >>> shopDataMerged = parseShopDataMerged("./data/raw/xml/leipzig_transformed.xml", "./data/raw/xml/dresden.xml");


        HashMap < String, String > dataTypeMapping;
        List < String > fillingData;


        try {
            dbConnection.connect();

            out.println("Filling shops entity...");
            dataTypeMapping = EntityFieldDTMappings.getShopsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntity(shopDataMerged, dataTypeMapping, "shops", "shop_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling shopaddresses entity...");
            dataTypeMapping = EntityFieldDTMappings.getShopaddressesEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityGenId(shopDataMerged, dataTypeMapping, "shopaddresses", "shopaddress_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            dbConnection.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads data for common* entities by applying logic on the merged List - which contains all Products from both shops.
     * Common entities are products, books, authors, priceinfos, ...
     * Junction Tables, categories, ... are handled separatly.
     *
     * @param parsedXMLProductDataMerged
     * @return The merged product data parsed from the XML shop files.
     */
    public static void loadCommonData(List < HashMap < String, List < String >>> parsedXMLProductDataMerged) {


        HashMap < String, String > dataTypeMapping;
        List < String > fillingData;

        try {
            dbConnection.connect();

            out.println("Filling products entity...");
            dataTypeMapping = EntityFieldDTMappings.getProductsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntity(parsedXMLProductDataMerged, dataTypeMapping, "products", "asin");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling books entity...");
            dataTypeMapping = EntityFieldDTMappings.getBooksEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntity(parsedXMLProductDataMerged, dataTypeMapping, "books", "asin");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling cds entity...");
            dataTypeMapping = EntityFieldDTMappings.getCdsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntity(parsedXMLProductDataMerged, dataTypeMapping, "cds", "asin");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling dvds entity...");
            dataTypeMapping = EntityFieldDTMappings.getDvdsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntity(parsedXMLProductDataMerged, dataTypeMapping, "dvds", "asin");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);


            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadNormalizationTablesData(List < HashMap < String, List < String >>> parsedXMLProductDataMerged) {

        HashMap < String, String > dataTypeMapping;
        List < String > fillingData;


        try {
            dbConnection.connect();

            out.println("Filling dvdformats entity...");
            dataTypeMapping = EntityFieldDTMappings.getDvdformatsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntityGenId(parsedXMLProductDataMerged, dataTypeMapping, "dvdformats", "dvdspec_format", 1, "dvdformat_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling similars entity...");
            dataTypeMapping = EntityFieldDTMappings.getProductsSimilarsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntity(parsedXMLProductDataMerged, dataTypeMapping, "products_similars", "similars");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling listmanialists entity...");
            dataTypeMapping = EntityFieldDTMappings.getListmanialistsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "listmanialists", "listmania_lists", 1, "listmanialist_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling tracks entity...");
            dataTypeMapping = EntityFieldDTMappings.getTracksEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityGenId(parsedXMLProductDataMerged, dataTypeMapping, "tracks", "track_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling authors entity...");
            dataTypeMapping = EntityFieldDTMappings.getAuthorsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "authors", "authors", 1, "author_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling creators entity...");
            dataTypeMapping = EntityFieldDTMappings.getCreatorsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "creators", "creators", 1, "creator_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling labels entity...");
            dataTypeMapping = EntityFieldDTMappings.getLabelsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "labels", "labels", 1, "label_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling studios entity...");
            dataTypeMapping = EntityFieldDTMappings.getStudiosEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "studios", "studios", 1, "studio_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling actors entity...");
            dataTypeMapping = EntityFieldDTMappings.getActorsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "actors", "actors", 1, "actor_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling publishers entity...");
            dataTypeMapping = EntityFieldDTMappings.getPublishersEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "publishers", "publishers", 1, "publisher_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling priceinfos entity...");
            dataTypeMapping = EntityFieldDTMappings.getPriceinfosEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityGenId(parsedXMLProductDataMerged, dataTypeMapping, "priceinfos", "priceinfo_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void loadJunctionData(List < HashMap < String, List < String >>> parsedXMLProductDataMerged) {

        List < String > fillingData;

        try {

            out.println("Filling junction_books_authors entity...");
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "authors", "asin", "name", "author_id", "books", "authors", "junction_books_authors", "books_asin", "authors_author_id");
            out.println("Filling junction_books_publishers entity...");
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "publishers", "asin", "name", "publisher_id", "books", "publishers", "junction_books_publishers", "books_asin", "publishers_publisher_id");
            out.println("Filling junction_dvds_actors entity...");
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "actors", "asin", "name", "actor_id", "dvds", "actors", "junction_dvds_actors", "dvds_asin", "actors_actor_id");
            out.println("Filling junction_dvds_studios entity...");
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "studios", "asin", "name", "studio_id", "dvds", "studios", "junction_dvds_studios", "dvds_asin", "studios_studio_id");
            out.println("Filling junction_dvds_formats entity...");
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "formats", "asin", "name", "format_id", "dvds", "formats", "junction_dvds_formats", "dvds_asin", "formats_format_id");
            // TODO: Audiotexts
            out.println("Filling junction_cds_labels entity...");
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "labels", "asin", "name", "label_id", "cds", "labels", "junction_cds_labels", "cds_asin", "labels_label_id");
            out.println("Filling junction_products_creators entity...");
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "creators", "asin", "name", "creator_id", "products", "creators", "junction_products_creators", "products_asin", "creators_creator_id");
            out.println("Filling junction_products_listmanialists entity...");
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "listmania_lists", "asin", "name", "listmanialist_id", "products", "listmanialists", "junction_products_listmanialists", "products_asin", "listmanialists_listmanialist_id");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadCategoriesData() {

        List < String > fillingData;



        PostgresConnector dbConnection = new PostgresConnector("postgres", "");

        try {
            dbConnection.connect();


            System.out.println("LOAD CATEGORIES DATA...");
            ParserToMapToGenerateSQLCategories parserToMapToGenerateSQLCategories = new ParserToMapToGenerateSQLCategories();
            fillingData = parserToMapToGenerateSQLCategories.generateSQLCategoryEntity(pathToCategoriesXML);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);
            System.out.println("DONE.");

            System.out.println("LOAD JUNCTIONS DATA PRODUCTS CATEGORIES...");
            fillingData = parserToMapToGenerateSQLCategories.generateSQLJunction(pathToCategoriesXML);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);
            System.out.println("DONE.");


            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadUsersAndReviewsData() {

        List < HashMap < String, List < String >>> parsedCSVReviewsFromUsers = CSVParsingReviews.parseCSVCRegisteredUserReviews(pathToUserReviewsCSV);
        List < HashMap < String, List < String >>> parsedCSVReviewsFromGuests = CSVParsingReviews.parseCSVGuestReviews(pathToUserReviewsCSV);
        out.println(parsedCSVReviewsFromUsers);

        List < String > fillingData;

        HashMap < String, String > dataTypeMapping;

        PostgresConnector dbConnection = new PostgresConnector("postgres", "");

        try {
            dbConnection.connect();

           out.println("Filling users entity...");
            dataTypeMapping = EntityFieldDTMappings.getUsersEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntity(parsedCSVReviewsFromUsers, dataTypeMapping, "users", "username");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling userreviews entity...");
            dataTypeMapping = EntityFieldDTMappings.getUserreviewsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityGenId(parsedCSVReviewsFromUsers, dataTypeMapping, "userreviews", "userreview_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            out.println("Filling questreviews entity...");
            dataTypeMapping = EntityFieldDTMappings.getGuestReviewsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityGenId(parsedCSVReviewsFromGuests, dataTypeMapping, "guestreviews", "guestreview_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFile);

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
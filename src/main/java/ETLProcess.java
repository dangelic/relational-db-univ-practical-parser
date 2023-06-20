import LogGenerationRejection.LogGenerator;
import parserShopsXML.XMLParsingShops;
import parserProductsXML.XMLParsingProducts;
import entityMap.EntityFieldDTMappings;
import parserUserReviewsCSV.CSVParsingReviews;
import queryBuilderStandardEntities.QueryBuilderStandard;
import helper.sqlParser;
import dataCleanUp.CleanUpOperations;
import dbConnection.PostgresConnector;
import queryBuilderJunctions.QueryBuilderJunctions;
import categoryHandler.CategoryTransformAndGenerateSQL;

import static java.lang.System.out;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;

public class ETLProcess {
    // Define Color-Codes for Console-Output.
    private static String success = "\u001B[32m"; // Green color string for successful queries
    private static String info = "\u001B[34m"; // Blue color string for info messages
    private static String end = "\u001B[0m";

    // Define paths.
    private static String pathToLogFileDebugQueries = "./logs/debug_queries.log";
    private static String pathToLogFileDebugDB = "./logs/debug_db.log";
    private static String pathToLogFileFinalRejectionsErrors = "./logs/rejections_errors.log";
    private static String pathToLeipzigShopXML = "./data/raw/xml/leipzig_transformed.xml";
    private static String pathToDresdenShopXML = "./data/raw/xml/dresden.xml";
    private static String pathToCategoriesXML = "./data/raw/xml/categories.xml";
    private static String pathToUserReviewsCSV = "./data/raw/csv/reviews.csv";

    private static String dropSQLFilePath = "./sql/drop_tables_casc.sql";
    private static String createSQLFilePath = "./sql/create_tables.sql";
    private static String indexSQLFilePath = "./sql/add_index.sql";

    static PostgresConnector dbConnection = new PostgresConnector("postgres", "admin");

    public static void main(String[] args) {
        // Resets log files.
        resetLogFiles();
        // Initializes the database scheme to start from ETL from zero.
        initializeDatabaseScheme();
        // Parses XML product data from both shops, merges data and returns a list of HashMaps containing all parsed values separately in Hashmaps.
        List < HashMap < String, List < String >>> parsedXMLProductDataMerged = parseProductsFromShopsMerged();
        // Run preprocessing on the list of HashMaps. This includes replacement for missing chars if the same product exists again with proper chars in strings.
        List < HashMap < String, List < String >>> parsedXMLProductDataMergedPreprocessed = runCleanUpTasksPre(parsedXMLProductDataMerged);
        // Load the Shop Data including Name, Zip, ...
        loadShopData();
        // Load common data using the parsed and preprocessed XML product data. Common Data includes Products, CDs, DVDs and Books entities.
        loadCommonData(parsedXMLProductDataMergedPreprocessed);
        // Load normalization tables data using the parsed and preprocessed XML product data. Normalitation tables are e.g. Authors, Creators, Studios, or Labels.
        loadNormalizationTablesData(parsedXMLProductDataMergedPreprocessed);
        // Load junction data using the parsed and preprocessed XML product data. Junction Data is the data to connect m:m relations.
        loadJunctionData(parsedXMLProductDataMergedPreprocessed);
        // Load users and their reviews data.
        loadUsersAndReviewsData();
        // Load categories data.
        loadCategoriesData();
        // Generate a Log File based on rejected data from the parsed files which is written in ./logs directory.
        generateRejectionsLogFile(); // Generates a log file for rejections
    }

    /**
     * Resets the log files.
     * If the log files exist, they are deleted.
     */
    private static void resetLogFiles() {
        File logFileConstraintViolations = new File(pathToLogFileDebugQueries);
        if (logFileConstraintViolations.exists()) {
            out.println(info + "Log file " + pathToLogFileDebugQueries + " already exists. Deleted it." + end);
            logFileConstraintViolations.delete();
        }
        File logFileDB = new File(pathToLogFileDebugDB);
        if (logFileDB.exists()) {
            out.println(info + "Log file " + pathToLogFileDebugDB + " already exists. Deleted it." + end);
            logFileDB.delete();
        }
        File logFileFinal = new File(pathToLogFileFinalRejectionsErrors);
        if (logFileFinal.exists()) {
            out.println(info + "Log file " + pathToLogFileFinalRejectionsErrors + " already exists. Deleted it." + end);
            logFileFinal.delete();
        }

    }

    /**
     * Initializes the database scheme.
     * Connects to the database, drops all tables, creates tables with constraints, adds indexes, and disconnects from the database.
     */
    private static void initializeDatabaseScheme() {
        try {
            dbConnection.connect();
            out.println(info + "Initialize Database Scheme..." + end);

            out.println(info + "DROP ALL TABLES..." + end);
            List<String> dropSQLStatements = sqlParser.parseSQLFile(dropSQLFilePath); // Parse statements from file with helper method.
            dbConnection.executeSQLQueryBatch(dropSQLStatements, pathToLogFileDebugDB);
            out.println(info + "DONE." + end);

            out.println(info + "CREATE TABLES WITH CONSTRAINTS..." + end);
            List<String> creationSQLStatements = sqlParser.parseSQLFile(createSQLFilePath);
            dbConnection.executeSQLQueryBatch(creationSQLStatements, pathToLogFileDebugDB);
            out.println(info + "DONE." + end);

            out.println(info + "ADD INDEXES..." + end);
            List<String> idxStatements = sqlParser.parseSQLFile(indexSQLFilePath);
            dbConnection.executeSQLQueryBatch(idxStatements, pathToLogFileDebugDB);
            out.println(info + "DONE." + end);

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses products from merged shops.
     *
     * @return A list of HashMaps containing parsed XML product data from the merged shops.
     */
    private static List<HashMap<String, List<String>>> parseProductsFromShopsMerged() {

        // Parse Leipzig.
        List<String> leipzig = new ArrayList<>(); // Pass in an ArrayList with one value (and no simple String) as everything in the parser is handled as a List.
        leipzig.add("LEIPZIG");
        List<HashMap<String, List<String>>> parsedXMLProductDataLeipzig = XMLParsingProducts.parseXMLFile(pathToLeipzigShopXML, leipzig);

        // Parse Dresden.
        List<String> dresden = new ArrayList<>();
        dresden.add("DRESDEN");
        List<HashMap<String, List<String>>> parsedXMLProductDataDresden = XMLParsingProducts.parseXMLFile(pathToDresdenShopXML, dresden);

        // Merge the two lists to process them as unit.
        parsedXMLProductDataLeipzig.addAll(parsedXMLProductDataDresden);
        List<HashMap<String, List<String>>> parsedXMLProductDataMerged = parsedXMLProductDataLeipzig;

        return parsedXMLProductDataMerged;
    }

    /**
     * Parses merged shop data.
     *
     * @param pathToLeipzigRawXML  The path to the Leipzig raw XML file.
     * @param pathToDresdenRawXML The path to the Dresden raw XML file.
     * @return A list of HashMaps containing parsed shop data from the merged sources.
     */
    private static List<HashMap<String, List<String>>> parseShopDataMerged(String pathToLeipzigRawXML, String pathToDresdenRawXML) {
        // Parse Leipzig.
        List<HashMap<String, List<String>>> shopDataLeipzig = XMLParsingShops.parseXMLFile(pathToLeipzigRawXML);
        // Parse Dresden.
        List<HashMap<String, List<String>>> shopDataDresden = XMLParsingShops.parseXMLFile(pathToDresdenRawXML);

        // Merge the two lists to process them as unit.
        shopDataLeipzig.addAll(shopDataDresden);
        List<HashMap<String, List<String>>> parsedXMLProductDataMerged = shopDataLeipzig;

        return parsedXMLProductDataMerged;
    }

    /**
     * Runs clean-up tasks on the merged product data, replacing special characters (such as ö, ä, ü, etc.) in products
     * that have a sibling with a matching string containing these special characters.
     * Also, this replaces wrong pgroup properties.
     *
     * @param parsedXMLProductDataMerged The merged product data to be cleaned and preprocessed.
     * @return The cleaned and preprocessed merged product data.
     */
    private static List<HashMap<String, List<String>>> runCleanUpTasksPre(List<HashMap<String, List<String>>> parsedXMLProductDataMerged) {

        List<HashMap<String, List<String>>> parsedXMLProductDataMergedCleanedPreprocessed;
        out.println(info + "RUN CLEAN JOBS ON PRODUCT DATA..." + end);
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMerged, "title");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPreprocessed, "artists");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPreprocessed, "studios");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPreprocessed, "similars");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPreprocessed, "labels");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPreprocessed, "tracks");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPreprocessed, "authors");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPreprocessed, "publishers");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPreprocessed, "listmania_lists");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceMissingCharacters(parsedXMLProductDataMergedCleanedPreprocessed, "creators");
        parsedXMLProductDataMergedCleanedPreprocessed = CleanUpOperations.replaceInvalidPgroup(parsedXMLProductDataMergedCleanedPreprocessed);
        out.println(parsedXMLProductDataMergedCleanedPreprocessed);
        out.println(info + "DONE." + end);
        return parsedXMLProductDataMergedCleanedPreprocessed;
    }

    /**
     * Loads shop data (name, zip, ...) into the database. This also fills the shopaddresses table.
     */
    public static void loadShopData() {
        List<HashMap<String, List<String>>> shopDataMerged = parseShopDataMerged("./data/raw/xml/leipzig_transformed.xml", "./data/raw/xml/dresden.xml");

        HashMap<String, String> dataTypeMapping;
        List<String> fillingData;

        try {
            dbConnection.connect();

            out.println(info + "FILLING SHOPS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getShopsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntity(shopDataMerged, dataTypeMapping, "shops", "shop_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING SHOPADDRESSES ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getShopaddressesEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityGenId(shopDataMerged, dataTypeMapping, "shopaddresses", "shopaddress_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            dbConnection.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads common data into the database.
     *
     * @param parsedXMLProductDataMerged The merged product data to be loaded.
     */
    public static void loadCommonData(List<HashMap<String, List<String>>> parsedXMLProductDataMerged) {
        HashMap<String, String> dataTypeMapping;
        List<String> fillingData;

        try {
            dbConnection.connect();

            out.println(info + "FILLING PRODUCTS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getProductsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntity(parsedXMLProductDataMerged, dataTypeMapping, "products", "asin");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING BOOKS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getBooksEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityFilter(parsedXMLProductDataMerged, dataTypeMapping, "books", "asin", "pgroup", "Book");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING CDS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getCdsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityFilter(parsedXMLProductDataMerged, dataTypeMapping, "cds", "asin", "pgroup", "Music");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING DVDS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getDvdsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityFilter(parsedXMLProductDataMerged, dataTypeMapping, "dvds", "asin", "pgroup", "DVD");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads normalization tables data into the database.
     *
     * @param parsedXMLProductDataMerged The merged product data to be loaded.
     */
    public static void loadNormalizationTablesData(List<HashMap<String, List<String>>> parsedXMLProductDataMerged) {
        HashMap<String, String> dataTypeMapping;
        List<String> fillingData;

        try {
            dbConnection.connect();

            out.println(info + "FILLING DVDFORMATS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getDvdformatsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "dvdformats", "dvdspec_format", 1, "dvdformat_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING SIMILARS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getProductsSimilarsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntity(parsedXMLProductDataMerged, dataTypeMapping, "products_similars", "similars");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING LISTMANIALISTS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getListmanialistsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "listmanialists", "listmania_lists", 1, "listmanialist_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING TRACKS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getTracksEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntityGenId(parsedXMLProductDataMerged, dataTypeMapping, "tracks", "tracks", 1, "track_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING AUTHORS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getAuthorsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "authors", "authors", 1, "author_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING CREATORS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getCreatorsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "creators", "creators", 1, "creator_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING AUDIOTEXTS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getAudiotextsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "audiotexts", "audiotext", 1, "audiotext_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING LABELS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getLabelsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "labels", "labels", 1, "label_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING STUDIOS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getStudiosEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "studios", "studios", 1, "studio_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING ARTISTS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getArtistsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "artists", "artists", 1, "artist_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING PUBLISHERS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getPublishersEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForNestedEntitySuppressDuplicatesGenId(parsedXMLProductDataMerged, dataTypeMapping, "publishers", "publishers", 1, "publisher_id");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING PRICEINFOS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getPriceinfosEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityGenId(parsedXMLProductDataMerged, dataTypeMapping, "priceinfos", "priceinfo_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads junction data into the database.
     *
     * @param parsedXMLProductDataMerged The merged product data to be loaded.
     */
    public static void loadJunctionData(List<HashMap<String, List<String>>> parsedXMLProductDataMerged) {
        try {
            out.println(info + "FILLING JUNCTION_BOOKS_AUTHORS ENTITY..." + end);
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "authors", "asin", "name", "author_id", "books", "authors", "junction_books_authors", "books_asin", "authors_author_id");

            out.println(info + "FILLING JUNCTION_BOOKS_PUBLISHERS ENTITY..." + end);
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "publishers", "asin", "name", "publisher_id", "books", "publishers", "junction_books_publishers", "books_asin", "publishers_publisher_id");

            out.println(info + "FILLING JUNCTION_DVDS_STUDIOS ENTITY..." + end);
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "studios", "asin", "name", "studio_id", "dvds", "studios", "junction_dvds_studios", "dvds_asin", "studios_studio_id");

            out.println(info + "FILLING JUNCTION_DVDS_FORMATS ENTITY..." + end);
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "dvdspec_format", "asin", "name", "dvdformat_id", "dvds", "dvdformats", "junction_dvds_dvdformats", "dvds_asin", "dvdformats_dvdformat_id");

            out.println(info + "FILLING JUNCTION_DVDS_AUDIOTEXTS ENTITY..." + end);
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "audiotext", "asin", "media_properties", "audiotext_id", "dvds", "audiotexts", "junction_dvds_audiotexts", "dvds_asin", "audiotexts_audiotext_id");

            out.println(info + "FILLING JUNCTION_CDS_LABELS ENTITY..." + end);
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "labels", "asin", "name", "label_id", "cds", "labels", "junction_cds_labels", "cds_asin", "labels_label_id");

            out.println(info + "FILLING JUNCTION_PRODUCTS_CREATORS ENTITY..." + end);
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "creators", "asin", "name", "creator_id", "products", "creators", "junction_products_creators", "products_asin", "creators_creator_id");

            out.println(info + "FILLING JUNCTION_PRODUCTS_ARTISTS ENTITY..." + end);
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "artists", "asin", "name", "artist_id", "products", "artists", "junction_products_artists", "products_asin", "artists_artist_id");

            out.println(info + "FILLING JUNCTION_PRODUCTS_LISTMANIALISTS ENTITY..." + end);
            QueryBuilderJunctions.executeQuery(parsedXMLProductDataMerged, "listmania_lists", "asin", "name", "listmanialist_id", "products", "listmanialists", "junction_products_listmanialists", "products_asin", "listmanialists_listmanialist_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads categories data into the database which are parsed from XML. This method also generates a clean variant of the categories.xml to work with.
     */
    public static void loadCategoriesData() {
        List<String> fillingData;


        try {
            dbConnection.connect();

            out.println(info + "LOADING CATEGORIES DATA..." + end);
            CategoryTransformAndGenerateSQL categoryHandler = new CategoryTransformAndGenerateSQL();
            categoryHandler.transformAndCreateFormattedXML(pathToCategoriesXML, "./data/transformed/tagged_cats.xml");
            fillingData = categoryHandler.buildInsertStatementsForCategories();
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "LOADING JUNCTIONS DATA PRODUCTS CATEGORIES..." + end);
            fillingData = categoryHandler.buildInsertStatementsForProductsCatJunction();
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads users and reviews data into the database which are parsed from CSV in this method.
     */
    public static void loadUsersAndReviewsData() {
        List<HashMap<String, List<String>>> parsedCSVReviewsFromUsers = CSVParsingReviews.parseCSVCRegisteredUserReviews(pathToUserReviewsCSV);
        List<HashMap<String, List<String>>> parsedCSVReviewsFromGuests = CSVParsingReviews.parseCSVGuestReviews(pathToUserReviewsCSV);
        out.println(parsedCSVReviewsFromUsers);

        List<String> fillingData;

        HashMap<String, String> dataTypeMapping;

        try {
            dbConnection.connect();

            out.println(info + "FILLING USERS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getUsersEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntity(parsedCSVReviewsFromUsers, dataTypeMapping, "users", "username");
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING USERREVIEWS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getUserreviewsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityGenId(parsedCSVReviewsFromUsers, dataTypeMapping, "userreviews", "userreview_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            out.println(info + "FILLING GUESTREVIEWS ENTITY..." + end);
            dataTypeMapping = EntityFieldDTMappings.getGuestReviewsEntityFieldDTMappings();
            fillingData = QueryBuilderStandard.getInsertQueriesForCommonEntityGenId(parsedCSVReviewsFromGuests, dataTypeMapping, "guestreviews", "guestreview_id", 1);
            dbConnection.executeSQLQueryBatch(fillingData, pathToLogFileDebugQueries);

            dbConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates the rejections log file.
     */
    private static void generateRejectionsLogFile() {
        List<HashMap<String, List<String>>> parsedCSVReviewsFromUsers = CSVParsingReviews.parseCSVCRegisteredUserReviews(pathToUserReviewsCSV);
        List<HashMap<String, List<String>>> parsedCSVReviewsFromGuests = CSVParsingReviews.parseCSVGuestReviews(pathToUserReviewsCSV);
        // Merge the two lists
        parsedCSVReviewsFromUsers.addAll(parsedCSVReviewsFromGuests);
        List<HashMap<String, List<String>>> parsedCSVReviewsDataMerged = parsedCSVReviewsFromUsers;

        out.println(info + "GENERATING REJECTIONS LOG FILE..." + end);
        LogGenerator.check(parseProductsFromShopsMerged(), parsedCSVReviewsDataMerged);
    }
}
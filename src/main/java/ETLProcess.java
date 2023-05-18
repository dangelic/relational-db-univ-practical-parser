import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import xmlParsing.XMLParsingProducts;
import queryBuilder.QueryBuilder;

public class ETLProcess {
    public static void main(String[] args) {
        // ## Parse Shops XML for Leipzig and Dresden
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

        HashMap<String, String> dataTypeMapping = new HashMap<>();
        dataTypeMapping.put("asin", "name@string");
        dataTypeMapping.put("tracks", "tracks@string");

        QueryBuilder.getInsertQueriesForNestedEntitySuppressDuplicates(parsedXMLProductDataMerged, dataTypeMapping, "products", "tracks", 1, "my_id");
    }
}
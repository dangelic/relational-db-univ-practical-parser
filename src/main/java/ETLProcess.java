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
        List<String> arrayList = new ArrayList<>();
        arrayList.add("LEIPZIG");
        List<HashMap<String, List<String>>> dataLeipzig = XMLParsingProducts.parseXMLFile(pathToLeipzigRawXML, arrayList);
        String pathToDresdenRawXML = "./data/raw/xml/dresden.xml";
        //XMLParsingProducts.parseXMLFile(pathToDresdenRawXML, "DRESDEN");

        HashMap<String, String> dataTypeMapping = new HashMap<>();
        dataTypeMapping.put("asin", "name@string");
        dataTypeMapping.put("tracks", "tracks@string");

        QueryBuilder.getInsertQueries2(dataLeipzig, dataTypeMapping, "products", "tracks", 1, "my_id");
    }
}
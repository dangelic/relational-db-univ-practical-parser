import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import xmlParsing.XMLParsingProducts;
import map.MapperDataTables;

public class ETLProcess {
    public static void main(String[] args) {
        // ## Parse Shops XML for Leipzig and Dresden
        // Parse Leipzig
        String pathToLeipzigRawXML = "./data/raw/xml/leipzig_transformed.xml";
        List<String> arrayList = new ArrayList<>();
        arrayList.add("DRESDEN");
        List<HashMap<String, List<String>>> dataLeipzig = XMLParsingProducts.parseXMLFile(pathToLeipzigRawXML, arrayList);
        String pathToDresdenRawXML = "./data/raw/xml/dresden.xml";
        //XMLParsingProducts.parseXMLFile(pathToDresdenRawXML, "DRESDEN");

        MapperDataTables.mapProductsTable(dataLeipzig);
    }
}
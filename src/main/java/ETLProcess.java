import java.map.MapperDataTables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.xmlParsing.XMLParserShops;

public class ETLProcess {
    public static void main(String[] args) {
        // ## Parse Shops XML for Leipzig and Dresden
        // Parse Leipzig
        String pathToLeipzigRawXML = "./data/raw/xml/dresden.xml";
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("DRESDEN");
        List<HashMap<String, List<String>>> dataLeipzig = XMLParserShops.parseXMLFile(pathToLeipzigRawXML, arrayList);
        String pathToDresdenRawXML = "./data/raw/xml/dresden.xml";
        // XMLParserShops.parseXMLFile(pathToDresdenRawXML, "DRESDEN");


        MapperDataTables.mapProductsTable(dataLeipzig);
    }
}
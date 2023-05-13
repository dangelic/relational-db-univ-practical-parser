
import com.parser.XMLParserShops;

public class ETLProcess {
    public static void main(String[] args) {
        String filePath = "./data/raw/xml/leipzig_transformed.xml";
        String outputPath = "./etl/extracted/xml/leipzig_parsed.txt";
        String configPath = "./mappings/xml_mappings/leipzig_xml_attributes.properties";

        XMLParserShops.parseXML(filePath, outputPath, configPath);
    }
}
import com.parser.XMLParser;
import com.mapper.ObjectMapper;

public class ETLProcess {

    public static void main(String[] args) {
        try {
            parseLeipzigXML();
            parseDresdenXML();

            // Call the ObjectMapper class
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.mapAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseLeipzigXML() {
        XMLParser parser = new XMLParser();
        try {
            parser.parseXML(
                    "./data/raw/xml/leipzig_transformed.xml",
                    XMLParser.getFile1Attributes(),
                    "./etl/extract_fails/leipzig_extract_fails.log",
                    "./etl/parsed_to_json_unmapped/leipzig_parsed_unmapped.json"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseDresdenXML() {
        XMLParser parser = new XMLParser();
        try {
            parser.parseXML(
                    "./data/raw/xml/dresden.xml",
                    XMLParser.getFile2Attributes(),
                    "./etl/extract_fails/dresden_extract_fails.log",
                    "./etl/parsed_to_json_unmapped/dresden_parsed_unmapped.json"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

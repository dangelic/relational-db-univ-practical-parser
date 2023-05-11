import com.parser.XMLParser;

public class ETLProcess {

    public static void main(String[] args) {
        XMLParser parser = new XMLParser();

        try {
            parser.parseXML("./data/raw/xml/leipzig_transformed.xml", XMLParser.getFile1Attributes(), XMLParser.EXTRACT_FAILS_LOG, "file1.json");
            parser.parseXML("./data/raw/xml/dresden.xml", XMLParser.getFile2Attributes(), XMLParser.EXTRACT_FAILS_LOG, "file2.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
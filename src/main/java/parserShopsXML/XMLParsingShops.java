package parserShopsXML;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple parser just for the shops addresses, zips, ... matching the logic in the products parser.
 */
public class XMLParsingShops {

    public static List<HashMap<String, List<String>>> parseXMLFile(String filePath) {

        List<Shop> shops = parseXML(filePath);
        List<HashMap<String, List<String>>> shopList = new ArrayList<>();

        for (Shop shop : shops) {
            HashMap<String, List<String>> shopMap = new HashMap<>();

            shopMap.put("shop_id", shop.getShopId());
            shopMap.put("name", shop.getName());
            shopMap.put("zip", shop.getZip());
            shopMap.put("street", shop.getStreet());

            shopList.add(shopMap);
        }
        System.out.println(shopList);
        return shopList;
    }

    private static List<Shop> parseXML(String filePath) {
        List<Shop> shops = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Specify the character encoding when reading the XML file
            InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8);
            Document doc = dBuilder.parse(new InputSource(reader));
            doc.getDocumentElement().normalize();

            NodeList shopList = doc.getElementsByTagName("shop");

            if (shopList.getLength() > 0) {
                Node shopNode = shopList.item(0);
                if (shopNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element shopElement = (Element) shopNode;
                    List<String> shopId = getTagAttributeDataVal(shopElement, "", "name");
                    shopId.add(0, shopId.get(0).toString().toUpperCase());
                    List<String> name = getTagAttributeDataVal(shopElement, "", "name");
                    List<String> zip = getTagAttributeDataVal(shopElement, "", "zip");
                    List<String> street = getTagAttributeDataVal(shopElement, "", "street");

                    Shop shop = new Shop(shopId, name, zip, street);
                    shops.add(shop);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return shops;
    }

    private static List<String> escapeStrings(List<String> strings) {
        List<String> escapedStrings = new ArrayList<>();
        for (String string : strings) {
            escapedStrings.add(escapeString(string));
        }
        return escapedStrings;
    }

    private static String escapeString(String string) {
        string = string.replace("'", "\\'");
        string = string.replace("\"", "\\\"");
        string = string.replace("[", "\\[");
        string = string.replace("]", "\\]");
        string = string.replace("&", "\\&");
        string = string.replace(";", "\\;");
        // return "\"" + string + "\"";
        return string;
    }

    private static List<String> getTagAttributeDataVal(Element element, String path, String attributeName) {
        List<String> tagAttributeValues = new ArrayList<>();
        if (path.isEmpty()) {
            String attributeValue = element.getAttribute(attributeName);
            if (!attributeValue.isEmpty()) {
                tagAttributeValues.add(attributeValue);
            }
        } else {
            String[] tags = path.split("/");
            traversTagAttributeDataValRecursive(element, tags, 0, attributeName, tagAttributeValues);
        }
        tagAttributeValues = escapeStrings(tagAttributeValues);
        return tagAttributeValues;
    }

    private static void traversTagAttributeDataValRecursive(
            Element element, String[] tags, int index, String attributeName, List<String> tagAttributeValues) {
        if (index >= tags.length) {
            return;
        }

        String tag = tags[index];
        NodeList nodeList = element.getElementsByTagName(tag);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                traversTagAttributeDataValRecursive(childElement, tags, index + 1, attributeName, tagAttributeValues);
                String attributeValue = childElement.getAttribute(attributeName);
                if (!attributeValue.isEmpty()) {
                    tagAttributeValues.add(attributeValue);
                }
            }
        }
    }
}

class Shop {

    private List<String> shopId;
    private List<String> name;
    private List<String> zip;
    private List<String> street;

    public Shop(List<String> shopId, List<String> name, List<String> zip, List<String> street) {
        this.shopId = shopId;
        this.name = name;
        this.zip = zip;
        this.street = street;
    }

    public List<String> getShopId() {
        return shopId;
    }

    public List<String> getName() {
        return name;
    }

    public List<String> getZip() {
        return zip;
    }

    public List<String> getStreet() {
        return street;
    }
}

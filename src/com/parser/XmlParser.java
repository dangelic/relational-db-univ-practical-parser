package com.parser;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser {

    public static void main(String[] args) {

        try {
            File xmlFile = new File("./data/raw/xml/leipzig_transformed.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("item");
            List<Item> items = new ArrayList<Item>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    try {
                        Item item = new Item();
                        item.asin = element.getAttribute("asin");
                        item.title = element.getElementsByTagName("title").item(0).getTextContent();
                        item.binding = element.getElementsByTagName("binding").item(0).getTextContent();
                        item.format = element.getElementsByTagName("format").item(0).getAttributes().getNamedItem("value").getTextContent();
                        item.numDiscs = element.getElementsByTagName("num_discs").item(0).getTextContent();
                        item.releaseDate = element.getElementsByTagName("releasedate").item(0).getTextContent();
                        item.upc = element.getElementsByTagName("upc").item(0).getTextContent();
                        items.add(item);
                    } catch (NullPointerException e) {
                        // handle inconsistent elements here
                    }
                }
            }

            // Create SQL insertion script
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO items (asin, title, binding, format, num_discs, release_date, upc) VALUES ");
            for (Item item : items) {
                sb.append("(");
                sb.append("'" + item.asin.replaceAll("'", "''") + "', ");
                sb.append("'" + item.title.replaceAll("'", "''") + "', ");
                sb.append("'" + item.binding.replaceAll("'", "''") + "', ");
                sb.append("'" + item.format.replaceAll("'", "''") + "', ");
                sb.append("'" + item.numDiscs.replaceAll("'", "''") + "', ");
                sb.append("'" + item.releaseDate.replaceAll("'", "''") + "', ");
                sb.append("'" + item.upc.replaceAll("'", "''") + "'");
                sb.append("), ");
            }
            sb.delete(sb.length() - 2, sb.length() - 1);
            String sqlScript = sb.toString();
            System.out.println(sqlScript);

            // Write SQL insertion script to a file
            File outputFile = new File("./parsedXmlOutput/leipzig_insert.sql");
            FileWriter writer = new FileWriter(outputFile);
            writer.write(sqlScript);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class Item {
        String asin;
        String title;
        String binding;
        String format;
        String numDiscs;
        String releaseDate;
        String upc;
    }
}

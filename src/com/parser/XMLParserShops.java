package com.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParserShops {
    public static void parseXML(String filePath, String outputPath, String configPath) {
        try {
            // Laden der Konfigurationseigenschaften
            Properties config = new Properties();
            config.load(new FileInputStream(configPath));

            // Öffnen des XML-Dokuments
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            // Erstellen des Output-Dateischreibers
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));

            // Iterieren über alle item-Elemente im XML-Dokument
            NodeList itemList = document.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                writer.newLine();

                // Iterieren über alle in der Konfiguration angegebenen Attribute
                for (String attribute : config.getProperty("attributes").split(",")) {
                    attribute = attribute.trim();

                    // Überprüfen, ob das Attribut im XML-Element vorhanden ist
                    NodeList nodes = item.getElementsByTagName(attribute);
                    if (nodes.getLength() > 0) {
                        if (attribute.equals("tracks")) {
                            List<String> trackList = new ArrayList<>();
                            NodeList trackNodes = nodes.item(0).getChildNodes();
                            for (int j = 0; j < trackNodes.getLength(); j++) {
                                Node trackNode = trackNodes.item(j);
                                if (trackNode.getNodeType() == Node.ELEMENT_NODE) {
                                    String track = escapeString(trackNode.getTextContent().trim());
                                    trackList.add("\"" + track + "\"");
                                }
                            }
                            writer.write(attribute + "=[" + String.join(", ", trackList) + "]");
                        } else if (attribute.equals("listmania")) {
                            List<String> listmaniaList = new ArrayList<>();
                            NodeList listNodes = nodes.item(0).getChildNodes();
                            for (int j = 0; j < listNodes.getLength(); j++) {
                                Node listNode = listNodes.item(j);
                                if (listNode.getNodeType() == Node.ELEMENT_NODE && listNode.getNodeName().equals("list")) {
                                    Element listElement = (Element) listNode;
                                    String listName = listElement.getAttribute("name");
                                    listmaniaList.add("\"" + escapeString(listName) + "\"");
                                }
                            }
                            writer.write(attribute + "=[" + String.join(", ", listmaniaList) + "]");
                        } else {
                            String value = escapeString(nodes.item(0).getTextContent().trim());
                            writer.write(attribute + "=\"" + value + "\"");
                            writer.newLine();
                        }
                    } else {
                        writer.write(attribute + "=\"NULL\"");
                        writer.newLine();
                    }
                }
            }

            // Schließen des Dateischreibers
            writer.close();

            System.out.println("XML-Parser abgeschlossen. Ausgabedatei: " + outputPath);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    public static String escapeString(String str) {
        return str.replace("\"", "\\\"").replace("'", "\\'");
    }
}

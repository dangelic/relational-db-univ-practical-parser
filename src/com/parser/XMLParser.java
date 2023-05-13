package com.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public class XMLParser {

    public static String[] getFile1Attributes() {
        return FILE_1_ATTRIBUTES;
    }

    public static String[] getFile2Attributes() {
        return FILE_2_ATTRIBUTES;
    }

    private static final String[] FILE_1_ATTRIBUTES;
    private static final String[] FILE_2_ATTRIBUTES;

    public static final String EXTRACT_FAILS_LOG = "./logs/extract_fails.log";

    static {
        FILE_1_ATTRIBUTES = loadAttributes("config.properties", "file1.attributes");
        FILE_2_ATTRIBUTES = loadAttributes("config.properties", "file2.attributes");
    }

    public void parseXML(String filePath, String[] attributes, String extractFailsLog, String jsonOutputFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(filePath));
        document.getDocumentElement().normalize();

        NodeList itemList = document.getElementsByTagName("item");

        LocalDateTime currentDateTime = LocalDateTime.now();

        try (PrintWriter extractFailsWriter = new PrintWriter(new FileWriter(extractFailsLog))) {
            Map<String, Map<String, Object>> jsonResult = new HashMap<>();

            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;
                    String asin = itemElement.getAttribute("asin");

                    System.out.println("ASIN: " + asin);

                    if (asin.isEmpty()) {
                        String title = getElementValue(itemElement, "title");
                        // First Log
                        extractFailsWriter.println(currentDateTime + " " + "Item without ASIN found in " + filePath + ". Title: " + title);
                    }

                    Map<String, Object> attributeValues = jsonResult.getOrDefault(asin, new HashMap<>());
                    for (String attribute : attributes) {
                        NodeList attributeList = itemElement.getElementsByTagName(attribute);
                        if (attributeList.getLength() > 0) {
                            Element attributeElement = (Element) attributeList.item(0);
                            String value = attributeElement.getTextContent();
                            System.out.println(attribute + ": " + value);
                            attributeValues.put(attribute, value);
                        } else {
                            System.out.println(attribute + ": NULL");
                            attributeValues.put(attribute, null);
                        }
                    }

                    // Add empty array for missing attributes
                    for (String attribute : attributes) {
                        if (!attributeValues.containsKey(attribute)) {
                            attributeValues.put(attribute, new ArrayList<>());
                        }
                    }

                    jsonResult.put(asin, attributeValues);
                    System.out.println();
                }
            }

            writeJsonFile(jsonOutputFile, jsonResult);
        }
    }

    private static void writeJsonObject(PrintWriter writer, Map<String, Object> jsonObject) {
        writer.println("{");
        int count = 0;
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            writer.print("    \"" + escapeJsonString(key) + "\": ");
            if (value instanceof Map) {
                writeJsonObject(writer, (Map<String, Object>) value);
            } else if (value != null) {
                writer.print("\"" + escapeJsonString(value.toString()) + "\"");
            } else {
                writer.print("null");
            }
            if (++count < jsonObject.size()) {
                writer.println(",");
            } else {
                writer.println();
            }
        }
        writer.print("  }");
    }

    private static String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }

    private static void writeJsonFile(String filePath, Map<String, Map<String, Object>> jsonResult) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            String json = gson.toJson(jsonResult);
            writer.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] loadAttributes(String configFile, String propertyKey) {
        Properties properties = new Properties();
        try (InputStream inputStream = XMLParser.class.getResourceAsStream(configFile)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String attributesString = properties.getProperty(propertyKey);
        return attributesString.split(",");
    }


    private static String escapeJsonString(String value) {
        value = value.replace("\\", "\\\\"); // Escape backslashes
        value = value.replace("\"", "\\\""); // Escape double quotes
        value = value.replace("'", "\\'"); // Escape single quotes
        return value;
    }
}


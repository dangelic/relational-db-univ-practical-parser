package xmlParsingCategories;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLParsingCategories {
    public static void main(String[] args) {
        String filePath = "./data/raw/xml/categories.xml";

        try {
            // Etree-Package initialisieren
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(filePath);

            // Tabellen leeren vor erneutem Einfuegen
            System.out.println("DELETE FROM Produkt_Kategorie;");
            System.out.println("DELETE FROM Kategorie;");
            System.out.println("DELETE FROM Produkt;");

            // Einfügen
            NodeList mainCategories = doc.getDocumentElement().getChildNodes();
            int idCounter = 0;
            for (int i = 0; i < mainCategories.getLength(); i++) {
                Node mainCategoryNode = mainCategories.item(i);
                if (mainCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element mainCategoryElement = (Element) mainCategoryNode;
                    idCounter++;
                    int categoryId = Integer.parseInt("1" + idCounter);
                    String categoryName = mainCategoryElement.getTextContent();

                    System.out.println("INSERT INTO Kategorie (KatID, Kategoriename, Oberkategorie) VALUES (" +
                            categoryId + ", '" + categoryName + "', NULL);");

                    processSubCategories(mainCategoryElement, categoryId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processSubCategories(Element categoryElement, int parentCategoryId) {
        NodeList subCategories = categoryElement.getElementsByTagName("category");
        int idCounter = 0;
        for (int i = 0; i < subCategories.getLength(); i++) {
            Node subCategoryNode = subCategories.item(i);
            if (subCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
                Element subCategoryElement = (Element) subCategoryNode;
                idCounter++;
                int categoryId = Integer.parseInt("2" + idCounter);
                String categoryName = subCategoryElement.getTextContent();

                System.out.println("INSERT INTO Kategorie (KatID, Kategoriename, Oberkategorie) VALUES (" +
                        categoryId + ", '" + categoryName + "', " + parentCategoryId + ");");

                processItems(subCategoryElement, categoryId);
                processSubCategories(subCategoryElement, categoryId);
            }
        }
    }

    private static void processItems(Element categoryElement, int categoryId) {
        NodeList items = categoryElement.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            Node itemNode = items.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;
                String itemName = itemElement.getTextContent();

                System.out.println("INSERT INTO Produkt (PID, Titel, Rating, Verkaufsrang, Bild) VALUES ('" +
                        itemName + "', NULL, NULL, NULL, NULL);");

                System.out.println("INSERT INTO Produkt_Kategorie (KatID, PID) VALUES (" +
                        categoryId + ", '" + itemName + "');");
            }
        }
    }
}

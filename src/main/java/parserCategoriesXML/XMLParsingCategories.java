package parserCategoriesXML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParsingCategories {
    private static int categoryId = 1;

    public static List<Category> parseCategories(String filePath) {
        List<Category> categories = new ArrayList<>();

        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element rootElement = doc.getDocumentElement();
            NodeList categoryNodes = rootElement.getElementsByTagName("category");

            for (int i = 0; i < categoryNodes.getLength(); i++) {
                Node categoryNode = categoryNodes.item(i);
                if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element categoryElement = (Element) categoryNode;
                    Category category = parseCategory(categoryElement, null);
                    categories.add(category);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    private static Category parseCategory(Element categoryElement, Category parentCategory) {
        Category category = new Category();

        // Assign an ID to the category
        category.setId(categoryId++);

        // Get the category name from the first child text node
        String categoryName = categoryElement.getTextContent().trim();
        category.setName(categoryName);

        NodeList itemNodes = categoryElement.getElementsByTagName("item");
        for (int i = 0; i < itemNodes.getLength(); i++) {
            Node itemNode = itemNodes.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;
                String itemName = itemElement.getTextContent().trim();
                category.addItem(itemName);
            }
        }

        NodeList subCategoryNodes = categoryElement.getElementsByTagName("category");
        if (subCategoryNodes.getLength() > 0) {
            for (int i = 0; i < subCategoryNodes.getLength(); i++) {
                Node subCategoryNode = subCategoryNodes.item(i);
                if (subCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element subCategoryElement = (Element) subCategoryNode;
                    Category subCategory = parseCategory(subCategoryElement, category);

                    // Check if the sub-category already exists in the current category or any of its parent categories
                    boolean exists = categoryExistsInParents(subCategory, category);

                    // Add the sub-category only if it doesn't already exist
                    if (!exists) {
                        category.addSubCategory(subCategory);
                    }
                }
            }
        }

        return category;
    }

    private static boolean categoryExistsInParents(Category subCategory, Category category) {
        if (subCategory == null || category.getId() == subCategory.getId()) {
            return true;
        }

        for (Category parentCategory : category.getSubCategories()) {
            if (categoryExistsInParents(subCategory, parentCategory)) {
                return true;
            }
        }

        return false;
    }

    public static void printCategories(List<Category> categories) {
        for (Category category : categories) {
            printCategory(category, 0);
        }
    }

    private static void printCategory(Category category, int indentLevel) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        System.out.println(indent + "Category: " + category.getName());
        System.out.println(indent + "ID: " + category.getId());
        for (String item : category.getItems()) {
            System.out.println(indent + "  - " + item);
        }

        for (Category subCategory : category.getSubCategories()) {
            printCategory(subCategory, indentLevel + 1);
        }
    }

    public static void printCategoriesToFile(List<Category> categories, String filePath) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            for (Category category : categories) {
                writeCategoryToFile(category, 0, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close(); // Closing the FileWriter
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeCategoryToFile(Category category, int indentLevel, FileWriter writer) throws IOException {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        writer.write(indent + "Category: " + category.getName() + "\n");
        writer.write(indent + "ID: " + category.getId() + "\n");
        for (String item : category.getItems()) {
            writer.write(indent + "  - " + item + "\n");
        }

        for (Category subCategory : category.getSubCategories()) {
            writeCategoryToFile(subCategory, indentLevel + 1, writer);
        }
    }
}

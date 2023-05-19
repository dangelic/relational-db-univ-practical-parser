package xmlParsingCategories;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import javax.xml.parsers.*;


import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class XMLParsingCategories {
    public static void main(String[] args) {
        String filePath = "./data/raw/xml/categories.xml";
        ArrayList<Category> categories = parseCategories(filePath);

        // Print the category structure
        printCategories(categories, 0);
    }

    public static ArrayList<Category> parseCategories(String filePath) {
        ArrayList<Category> categories = new ArrayList<>();

        try {
            // Read the XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
            document.getDocumentElement().normalize();

            // Parse the categories recursively
            Element root = document.getDocumentElement();
            parseCategory(root, null, categories);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    public static void parseCategory(Element categoryElement, Category parentCategory, ArrayList<Category> categories) {
        // Extract the category name
        String categoryName = categoryElement.getTextContent().trim();

        // Create a new category object
        Category category = new Category(categoryName, parentCategory);
        categories.add(category);

        NodeList childNodes = categoryElement.getChildNodes();

        // Parse the child items and sub-categories
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;

                if (childElement.getTagName().equals("item")) {
                    // Extract the item value and add it to the category
                    String itemValue = childElement.getTextContent().trim();
                    category.addItem(itemValue);
                } else if (childElement.getTagName().equals("category")) {
                    // Recursively parse the sub-category
                    parseCategory(childElement, category, categories);
                }
            }
        }
    }

    public static void printCategories(ArrayList<Category> categories, int indentationLevel) {
        for (Category category : categories) {
            String indentation = getIndentation(indentationLevel);
            System.out.println(indentation + category.getName());

            // Print the items in the category
            for (String item : category.getItems()) {
                System.out.println(indentation + "  - " + item);
            }

            // Recursively print the sub-categories
            printCategories(category.getSubcategories(), indentationLevel + 1);
        }
    }

    public static String getIndentation(int level) {
        StringBuilder indentation = new StringBuilder();

        for (int i = 0; i < level; i++) {
            indentation.append("  ");
        }

        return indentation.toString();
    }
}

class Category {
    private String name;
    private Category parentCategory;
    private ArrayList<String> items;
    private ArrayList<Category> subcategories;

    public Category(String name, Category parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
        this.items = new ArrayList<>();
        this.subcategories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public ArrayList<Category> getSubcategories() {
        return subcategories;
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void addSubcategory(Category subcategory) {
        subcategories.add(subcategory);
    }
}

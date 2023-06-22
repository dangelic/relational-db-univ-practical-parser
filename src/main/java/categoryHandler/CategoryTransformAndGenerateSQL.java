package categoryHandler;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * The CategoryTransformAndGenerateSQL class is responsible for transforming XML data and generating SQL statements
 * for categories and products.
 */
public class CategoryTransformAndGenerateSQL {
    private Document document;

    /**
     * Transforms the XML file at the specified path and creates a formatted XML file.
     *
     * @param pathToXMLFile          The path to the XML file to transform.
     * @param pathToFormattedXMLFile The path to save the formatted XML file.
     */
    public void transformAndCreateFormattedXML(String pathToXMLFile, String pathToFormattedXMLFile) {
        File xmlFile = new File(pathToXMLFile);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);

            addCatnameTags(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

            DOMSource source = new DOMSource(document);

            // Specify the output file
            File outputFile = new File(pathToFormattedXMLFile);
            OutputStream outputStream = new FileOutputStream(outputFile);
            StreamResult result = new StreamResult(outputStream);

            transformer.transform(source, result);
            outputStream.close();

            System.out.println("Formatted XML written to: " + outputFile.getAbsolutePath());
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds insert statements for categories based on the transformed XML document.
     *
     * @return A list of insert statements for categories.
     */
    public List<String> buildInsertStatementsForCategories() {
        CategoryHierarchyBuilder hierarchyBuilder = new CategoryHierarchyBuilder();
        Map<String, CategoryInfo> categoryIds = hierarchyBuilder.buildCategoryHierarchy(document.getDocumentElement());

        List<String> insertStatements = new ArrayList<>();
        for (Map.Entry<String, CategoryInfo> entry : categoryIds.entrySet()) {
            String categoryName = getCategoryName(entry.getKey());
            int categoryId = entry.getValue().getId();
            int parentId = entry.getValue().getParentId();

            String insertStatement;

            if (parentId != 0) {
                insertStatement = "INSERT INTO categories (category_id, parent_category_id, name) VALUES (" +
                        categoryId + ", " + parentId + ", '" + categoryName + "');";
            } else {
                insertStatement = "INSERT INTO categories (category_id, parent_category_id, name) VALUES (" +
                        categoryId + ", " + null + ", '" + categoryName + "');";
            }

            insertStatements.add(insertStatement);
        }

        return insertStatements;
    }

    private String getCategoryName(String categoryId) {
        NodeList catnameNodes = document.getElementsByTagName("catname");
        for (int i = 0; i < catnameNodes.getLength(); i++) {
            Element catnameElement = (Element) catnameNodes.item(i);
            String catId = catnameElement.getAttribute("cat_id");
            if (catId.equals(categoryId)) {
                return catnameElement.getTextContent().trim();
            }
        }
        return "";
    }


    public List<String> buildInsertStatementsForProductsCatJunction() {
        List<ProductCategoryInfo> productCategories = new ArrayList<>();

        traverseCategoriesForProducts(document.getDocumentElement(), productCategories);

        List<String> insertStatements = new ArrayList<>();
        for (ProductCategoryInfo productCategory : productCategories) {
            List<String> categoryIds = productCategory.getCategoryIds();
            List<String> asins = productCategory.getAsins();

            for (String categoryId : categoryIds) {
                String insertStatement = "INSERT INTO junction_products_categories (products_asin, categories_category_id) VALUES ";

                for (String asin : asins) {
                    insertStatement += "('" + asin + "', " + categoryId + "), ";
                }

                // Remove the trailing comma and space
                insertStatement = insertStatement.substring(0, insertStatement.length() - 2) + ";";
                insertStatements.add(insertStatement);
            }
        }

        return insertStatements;
    }

    private void traverseCategoriesForProducts(Element element, List<ProductCategoryInfo> productCategories) {
        NodeList childNodes = element.getChildNodes();
        List<String> categoryIds = new ArrayList<>();
        List<String> asins = new ArrayList<>();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                Element childElement = (Element) node;
                String tagName = childElement.getTagName();

                if (tagName.equals("catname")) {
                    String categoryId = childElement.getAttribute("cat_id");
                    if (!categoryId.isEmpty()) {
                        categoryIds.add(categoryId);
                    }
                } else if (tagName.equals("item")) {
                    String asin = childElement.getTextContent().trim();
                    if (!asin.isEmpty()) {
                        asins.add(asin);
                    }
                } else if (tagName.equals("category")) {
                    traverseCategoriesForProducts(childElement, productCategories);
                }
            }
        }

        if (!categoryIds.isEmpty() && !asins.isEmpty()) {
            productCategories.add(new ProductCategoryInfo(categoryIds, asins));
        }
    }




    private int catIdCounter = 1; // Counter for generating the cat_id

    private void addCatnameTags(Node node) {
        if (node instanceof Text) {
            Element parentElement = (Element) node.getParentNode();
            if (parentElement.getTagName().equals("category") && !isNullOrWhitespace(node.getNodeValue())) {
                // Check if the parent element is <category> and the text is not empty or whitespace
                Element catnameElement = node.getOwnerDocument().createElement("catname");
                catnameElement.setAttribute("cat_id", String.valueOf(catIdCounter));
                catIdCounter++;
                node.getParentNode().insertBefore(catnameElement, node);
                catnameElement.appendChild(node);
            }
        } else {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                addCatnameTags(childNodes.item(i));
            }
        }
    }


    private boolean isNullOrWhitespace(String value) {
        return value == null || value.trim().isEmpty();
    }
}

class CategoryHierarchyBuilder {
    private int currentId;
    private Map<String, CategoryInfo> categoryIds;

    public Map<String, CategoryInfo> buildCategoryHierarchy(Element rootElement) {
        currentId = 1;
        categoryIds = new HashMap<>();

        traverseCategories(rootElement, 0);

        return categoryIds;
    }

    private void traverseCategories(Element element, int parentId) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                Element childElement = (Element) node;
                int categoryId = getCategoryId(childElement);

                if (categoryId != -1) {
                    categoryIds.put(String.valueOf(categoryId), new CategoryInfo(categoryId, parentId));

                    traverseCategories(childElement, categoryId);
                }
            }
        }
    }


    private String getCategoryName(Element element) {
        NodeList catnameNodes = element.getElementsByTagName("catname");
        if (catnameNodes.getLength() > 0) {
            Element catnameElement = (Element) catnameNodes.item(0);
            return catnameElement.getTextContent().trim();
        }
        return "";
    }

    private int getCategoryId(Element element) {
        NodeList catnameNodes = element.getElementsByTagName("catname");
        if (catnameNodes.getLength() > 0) {
            Element catnameElement = (Element) catnameNodes.item(0);
            String categoryId = catnameElement.getAttribute("cat_id");
            if (!categoryId.isEmpty()) {
                try {
                    return Integer.parseInt(categoryId);
                } catch (NumberFormatException e) {
                    // Handle parsing error if necessary
                    e.printStackTrace();
                }
            }
        }
        // Return a default value or throw an exception if the category ID is not found
        return -1;
    }
}



class CategoryInfo {
    private int id;
    private int parentId;

    public CategoryInfo(int id, int parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }
}

class ProductCategoryBuilder {
    public List<ProductCategoryInfo> buildProductCategories(Element rootElement) {
        List<ProductCategoryInfo> productCategories = new ArrayList<>();
        traverseCategories(rootElement, productCategories);

        return productCategories;
    }

    private void traverseCategories(Element element, List<ProductCategoryInfo> productCategories) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                Element childElement = (Element) node;
                String categoryName = getCategoryName(childElement);

                if (!categoryName.isEmpty()) {
                    List<String> asins = new ArrayList<>();
                    NodeList itemNodes = childElement.getElementsByTagName("item");
                    for (int j = 0; j < itemNodes.getLength(); j++) {
                        Element itemElement = (Element) itemNodes.item(j);
                        String asin = itemElement.getTextContent().trim();
                        asins.add(asin);
                    }

                    List<String> categoryNames = new ArrayList<>();
                    categoryNames.add(categoryName);

                    productCategories.add(new ProductCategoryInfo(asins, categoryNames));
                    traverseCategories(childElement, productCategories);
                }
            }
        }
    }




    private String getCategoryName(Element element) {
        NodeList catnameNodes = element.getElementsByTagName("catname");
        if (catnameNodes.getLength() > 0) {
            Element catnameElement = (Element) catnameNodes.item(0);
            return catnameElement.getTextContent().trim();
        }
        return "";
    }
}

class ProductCategoryInfo {
    private List<String> categoryIds;
    private List<String> asins;

    public ProductCategoryInfo(List<String> categoryIds, List<String> asins) {
        this.categoryIds = categoryIds;
        this.asins = asins;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public List<String> getAsins() {
        return asins;
    }
}

class CategoryIdGenerator {
    private static int currentId = 1;
    private static Map<String, Integer> categoryIds = new HashMap<>();

    public static int getCategoryId(String categoryName) {
        if (categoryIds.containsKey(categoryName)) {
            return categoryIds.get(categoryName);
        } else {
            int categoryId = currentId++;
            categoryIds.put(categoryName, categoryId);
            return categoryId;
        }
    }
}

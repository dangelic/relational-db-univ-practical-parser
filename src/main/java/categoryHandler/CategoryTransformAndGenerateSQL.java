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

public class CategoryTransformAndGenerateSQL {
    private Document document;

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

    public List<String> buildInsertStatementsForCategories() {
        CategoryHierarchyBuilder hierarchyBuilder = new CategoryHierarchyBuilder();
        Map<String, CategoryInfo> categoryIds = hierarchyBuilder.buildCategoryHierarchy(document.getDocumentElement());

        List<String> insertStatements = new ArrayList<>();
        for (Map.Entry<String, CategoryInfo> entry : categoryIds.entrySet()) {
            String categoryName = entry.getKey();
            int categoryId = entry.getValue().getId();
            int parentId = entry.getValue().getParentId();

            String insertStatement = "INSERT INTO categories (category_id, parent_category_id, name) VALUES (" +
                    categoryId + ", " + parentId + ", '" + categoryName + "');";

            insertStatements.add(insertStatement);
        }

        return insertStatements;
    }

    public List<String> buildInsertStatementsForProducts() {
        List<ProductCategoryInfo> productCategories = new ArrayList<>();

        traverseCategoriesForProducts(document.getDocumentElement(), productCategories);

        List<String> insertStatements = new ArrayList<>();
        for (ProductCategoryInfo productCategory : productCategories) {
            List<String> categoryNames = productCategory.getCategoryNames();
            List<String> asins = productCategory.getAsins();

            for (String categoryName : categoryNames) {
                String insertStatement = "INSERT INTO junction_products_categories (products_asin, category_id) VALUES ";

                for (String asin : asins) {
                    insertStatement += "('" + categoryName + "', " + "(SELECT category_id FROM categories WHERE name = '" +
                            asin + "')), ";
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
        List<String> categoryNames = new ArrayList<>();
        List<String> asins = new ArrayList<>();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                Element childElement = (Element) node;
                String tagName = childElement.getTagName();

                if (tagName.equals("catname")) {
                    String categoryName = childElement.getTextContent().trim();
                    if (!categoryName.isEmpty()) {
                        categoryNames.add(categoryName);
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

        if (!categoryNames.isEmpty() && !asins.isEmpty()) {
            productCategories.add(new ProductCategoryInfo(categoryNames, asins));
        }
    }




    private void addCatnameTags(Node node) {
        if (node instanceof Text) {
            Element parentElement = (Element) node.getParentNode();
            if (parentElement.getTagName().equals("category") && !isNullOrWhitespace(node.getNodeValue())) {
                // Check if the parent element is <category> and the text is not empty or whitespace
                Element catnameElement = node.getOwnerDocument().createElement("catname");
                node.getParentNode().insertBefore(catnameElement, node);
                catnameElement.appendChild(node);
            }
        } else {
            NodeList childNodes = node.getChildNodes();
            for (int i = childNodes.getLength() - 1; i >= 0; i--) {
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
                String categoryName = getCategoryName(childElement);

                if (!categoryName.isEmpty()) {
                    categoryIds.put(categoryName, new CategoryInfo(currentId, parentId));
                    currentId++;

                    traverseCategories(childElement, categoryIds.get(categoryName).getId());
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
    private List<String> categoryNames;
    private List<String> asins;

    public ProductCategoryInfo(List<String> asins, List<String> categoryNames) {
        this.asins = asins;
        this.categoryNames = categoryNames;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
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

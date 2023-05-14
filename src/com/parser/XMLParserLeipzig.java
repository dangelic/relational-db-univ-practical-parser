package com.parser;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLParserLeipzig {
    public static void main(String[] args) {
        String filePath = "./data/raw/xml/leipzig_transformed.xml";
        List<Item> items = parseXML(filePath);
        for (Item item : items) {
            System.out.println("pgroup: " + item.getPgroup());
            System.out.println("asin: " + item.getAsin());

            System.out.println("bookspec binding: " + item.getBookspecBinding());
            System.out.println("bookspec edition: " + item.getBookspecEdition());
            System.out.println("bookspec isbn: " + item.getBookspecISBN());
            System.out.println("bookspec weight: " + item.getBookspecWeight());
            System.out.println("bookspec height: " + item.getBookspecHight());
            System.out.println("bookspec length: " + item.getBookspecLength());
            System.out.println("bookspec pages: " + item.getBookspecPages());
            System.out.println("bookspec publication date: " + item.getBookspecPublicationDate());

            System.out.println("musicspec binding: " + item.getMusicspecBinding());
            System.out.println("musicspec format: " + item.getMusicspecFormat());
            System.out.println("musicspec num discs: " + item.getMusicspecNumDiscs());
            System.out.println("musicspec release date: " + item.getMusicspecReleaseDate());
            System.out.println("musicspec upc: " + item.getMusicspecUpc());

            System.out.println("similars: " + item.getSimilars());
            System.out.println("tracks: " + item.getTracks());
            System.out.println("salesrank: " + item.getSalesRank());
            System.out.println("picture: " + item.getPicture());
            System.out.println("detailpage: " + item.getDetailPage());
            System.out.println("ean: " + item.getEan());
            System.out.println("----------------------------------");
        }
    }

    public static List<Item> parseXML(String filePath) {
        List<Item> items = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList itemList = doc.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;
                    String pgroup = itemElement.getAttribute("pgroup");
                    String asin = itemElement.getAttribute("asin");

                    String bookspecBinding = getTagValue(itemElement, "bookspec/binding");
                    String bookspecEdition = getTagAttributeValue(itemElement, "edition", "val");
                    String bookspecISBN = getTagAttributeValue(itemElement, "isbn", "val");
                    String bookspecWeight = getTagValue(itemElement, "package/weight");
                    String bookspecHeight = getTagValue(itemElement, "package/height");
                    String bookspecLength = getTagValue(itemElement, "package/length");
                    String bookspecPages = getTagValue(itemElement, "bookspec/pages");
                    String bookspecPublicationDate = getTagAttributeValue(itemElement, "publication", "date");

                    String musicspecBinding = getTagValue(itemElement, "musicspec/binding");
                    String musicspecFormat = getTagValue(itemElement, "musicspec/format");
                    String musicspecNumDiscs = getTagValue(itemElement, "musicspec/num_discs");
                    String musicspecReleaseDate = getTagValue(itemElement, "musicspec/releasedate");
                    String musicspecUpc = getTagValue(itemElement, "musicspec/upc");
                    String similars = getSimilars(itemElement);
                    List<String> tracks = getTracks(itemElement);
                    String salesRank = itemElement.getAttribute("salesrank");
                    String picture = itemElement.getAttribute("picture");
                    String detailPage = itemElement.getAttribute("detailpage");
                    String ean = itemElement.getAttribute("ean");

                    Item item = new Item(pgroup,
                            asin,
                            bookspecBinding,
                            bookspecEdition,
                            bookspecISBN,
                            bookspecWeight,
                            bookspecHeight,
                            bookspecLength,
                            bookspecPages,
                            bookspecPublicationDate,
                            musicspecBinding,
                            musicspecFormat,
                            musicspecNumDiscs,
                            musicspecReleaseDate,
                            musicspecUpc,
                            similars,
                            tracks,
                            salesRank,
                            picture,
                            detailPage,
                            ean);
                    items.add(item);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    private static String getTagAttributeValue(Element element, String tagName, String attribute) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Element tagElement = (Element) nodeList.item(0);
            return tagElement.getAttribute(attribute);
        }
        return "";
    }

    private static String getTagValue(Element element, String tagName) {
        String[] tagNames = tagName.split("/");
        Node node = element;
        for (String name : tagNames) {
            NodeList nodeList = ((Node) node).getChildNodes();
            node = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node tempNode = nodeList.item(i);
                if (tempNode.getNodeName().equals(name)) {
                    node = tempNode;
                    break;
                }
            }
            if (node == null) {
                return "";
            }
        }
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element tagElement = (Element) node;
            if (tagElement.hasAttribute("value")) {
                return tagElement.getAttribute("value");
            } else if (tagElement.hasAttribute("val")) {
                return tagElement.getAttribute("val");
            } else if (tagElement.getTagName().equals("package")) {
                String weight = tagElement.getAttribute("weight");
                String height = tagElement.getAttribute("height");
                String length = tagElement.getAttribute("length");
                StringBuilder attributeValues = new StringBuilder();
                if (!weight.isEmpty()) {
                    attributeValues.append("Weight: ").append(weight).append(", ");
                }
                if (!height.isEmpty()) {
                    attributeValues.append("Height: ").append(height).append(", ");
                }
                if (!length.isEmpty()) {
                    attributeValues.append("Length: ").append(length).append(", ");
                }
                return attributeValues.toString();
            } else if (tagElement.hasAttributes()) {
                NamedNodeMap attributes = tagElement.getAttributes();
                StringBuilder attributeValues = new StringBuilder();
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attribute = attributes.item(i);
                    attributeValues.append(attribute.getNodeName()).append("=").append(attribute.getNodeValue()).append(", ");
                }
                return attributeValues.toString();
            } else {
                return tagElement.getTextContent();
            }
        }
        return "";
    }

    private static String getSimilars(Element element) {
        StringBuilder sb = new StringBuilder();
        NodeList similarsList = element.getElementsByTagName("sim_product");
        for (int i = 0; i < similarsList.getLength(); i++) {
            Node similarNode = similarsList.item(i);
            if (similarNode.getNodeType() == Node.ELEMENT_NODE) {
                Element similarElement = (Element) similarNode;
                String asin = getTagValue(similarElement, "asin");
                String title = getTagValue(similarElement, "title");
                sb.append("ASIN: ").append(asin).append(", Title: ").append(title).append("; ");
            }
        }
        return sb.toString();
    }

    private static List<String> getTracks(Element element) {
        List<String> tracks = new ArrayList<>();
        NodeList tracksList = element.getElementsByTagName("tracks");
        if (tracksList.getLength() > 0) {
            Element tracksElement = (Element) tracksList.item(0);
            NodeList trackList = tracksElement.getElementsByTagName("title");
            for (int i = 0; i < trackList.getLength(); i++) {
                Node trackNode = trackList.item(i);
                if (trackNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element trackElement = (Element) trackNode;
                    String trackTitle = trackElement.getTextContent();
                    tracks.add(trackTitle);
                }
            }
        }
        return tracks;
    }
}

class Item {
    private String pgroup;
    private String asin;

    private String bookspecBinding;
    private String bookspecEdition;
    private String bookspecISBN;
    private String bookspecWeight;
    private String bookspecHight;

    private String bookspecLength;
    private String bookspecPages;

    private String bookspecPublicationDate;
    private String musicspecBinding;
    private String musicspecFormat;
    private String musicspecNumDiscs;
    private String musicspecReleaseDate;
    private String musicspecUpc;
    private String similars;
    private List<String> tracks;
    private String salesRank;
    private String picture;
    private String detailPage;
    private String ean;

    public Item(String pgroup,
                String asin,
                String bookspecBinding,
                String bookspecEdition,
                String bookspecISBN,
                String bookspecWeight,
                String bookspecHight,
                String bookspecLength,
                String bookspecPages,
                String bookspecPublicationDate,
                String musicspecBinding,
                String musicspecFormat,
                String musicspecNumDiscs,
                String musicspecReleaseDate,
                String musicspecUpc,
                String similars,
                List<String> tracks,
                String salesRank,
                String picture,
                String detailPage,
                String ean) {
        this.pgroup = pgroup;
        this.asin = asin;
        this.bookspecBinding = bookspecBinding;
        this.bookspecEdition = bookspecEdition;
        this.bookspecISBN = bookspecISBN;
        this.bookspecWeight = bookspecWeight;
        this.bookspecHight = bookspecHight;
        this.bookspecLength = bookspecLength;
        this.bookspecPages = bookspecPages;
        this.bookspecPublicationDate = bookspecPublicationDate;
        this.musicspecBinding = musicspecBinding;
        this.musicspecFormat = musicspecFormat;
        this.musicspecNumDiscs = musicspecNumDiscs;
        this.musicspecReleaseDate = musicspecReleaseDate;
        this.musicspecUpc = musicspecUpc;
        this.similars = similars;
        this.tracks = tracks;
        this.salesRank = salesRank;
        this.picture = picture;
        this.detailPage = detailPage;
        this.ean = ean;
    }

    public String getPgroup() {
        return pgroup;
    }

    public String getAsin() {
        return asin;
    }

    public String getBookspecBinding() {
        return bookspecBinding;
    }

    public String getBookspecEdition() {
        return bookspecEdition;
    }

    public String getBookspecISBN() {
        return bookspecISBN;
    }

    public String getBookspecWeight() {
        return bookspecWeight;
    }

    public String getBookspecHight() {
        return bookspecHight;
    }

    public String getBookspecLength() {
        return bookspecLength;
    }


    public String getBookspecPages() {
        return bookspecPages;
    }

    public String getBookspecPublicationDate() {
        return bookspecPublicationDate;
    }


    public String getMusicspecBinding() {
        return musicspecBinding;
    }


    public String getMusicspecFormat() {
        return musicspecFormat;
    }

    public String getMusicspecNumDiscs() {
        return musicspecNumDiscs;
    }

    public String getMusicspecReleaseDate() {
        return musicspecReleaseDate;
    }

    public String getMusicspecUpc() {
        return musicspecUpc;
    }

    public String getSimilars() {
        return similars;
    }

    public List<String> getTracks() {
        return tracks;
    }

    public String getSalesRank() {
        return salesRank;
    }

    public String getPicture() {
        return picture;
    }

    public String getDetailPage() {
        return detailPage;
    }

    public String getEan() {
        return ean;
    }
}
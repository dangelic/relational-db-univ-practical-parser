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
            System.out.println("musicspec binding: " + item.getMusicspecBinding());
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
                    String musicspecBinding = getTagValue(itemElement, "musicspec/binding");
                    String similars = getSimilars(itemElement);
                    List<String> tracks = getTracks(itemElement);
                    String salesRank = itemElement.getAttribute("salesrank");
                    String picture = itemElement.getAttribute("picture");
                    String detailPage = itemElement.getAttribute("detailpage");
                    String ean = itemElement.getAttribute("ean");

                    Item item = new Item(pgroup, asin, musicspecBinding, similars, tracks, salesRank, picture, detailPage, ean);
                    items.add(item);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return items;
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
            return tagElement.getTextContent();
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
    private String musicspecBinding;
    private String similars;
    private List<String> tracks;
    private String salesRank;
    private String picture;
    private String detailPage;
    private String ean;

    public Item(String pgroup, String asin, String musicspecBinding, String similars, List<String> tracks, String salesRank, String picture, String detailPage, String ean) {
        this.pgroup = pgroup;
        this.asin = asin;
        this.musicspecBinding = musicspecBinding;
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

    public String getMusicspecBinding() {
        return musicspecBinding;
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
package com.parser;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLParserShops {

    public static void parseXMLFile(String filePath, String SHOP_MODE) {
        List<Item> items = parseXML(filePath, SHOP_MODE);


        for (Item item : items) {
            System.out.println("SHOP: " + SHOP_MODE);
            System.out.println("pgroup: " + item.getPgroup());
            System.out.println("asin: " + item.getAsin());
            System.out.println("product title: " + item.getProductTitle());

            System.out.println("price: " + item.getPrice());
            System.out.println("price multiplier: " + item.getPriceMult());
            System.out.println("price state: " + item.getPriceState());
            System.out.println("price currency: " + item.getPriceCurrency());


            System.out.println("dvdspec aspect ratio: " + item.getDvdspecAspectRatio());
            System.out.println("dvdspec format: " + item.getDvdspecFormat());
            System.out.println("dvdspec region code: " + item.getDvdspecRegionCode());
            System.out.println("dvdspec release date: " + item.getDvdspecReleaseDate());
            System.out.println("dvdspec running time: " + item.getDvdspecRunningTime());
            System.out.println("dvdspec theatr release: " + item.getDvdspecTheatrRelease());
            System.out.println("dvdspec upc: " + item.getDvdspecUpc());

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
            System.out.println("labels: " + item.getLabels());

            System.out.println("creators: " + item.getCreators());
            System.out.println("authors: " + item.getAuthors());
            System.out.println("directors: " + item.getDirectors());
            System.out.println("artists: " + item.getArtists());
            System.out.println("listmania lists: " + item.getListmanialists());
            System.out.println("publishers: " + item.getPublishers());
            System.out.println("studios: " + item.getStudios());

            System.out.println("----------------------------------");
        }
    }

    private static List<Item> parseXML(String filePath, String SHOP_MODE) {
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
                    String productTitle = getTagValue(itemElement, "title");

                    String price = getCharacterData(itemElement, "price");
                    String priceMult = getTagAttributeValue(itemElement, "price", "mult");
                    String priceState = getTagAttributeValue(itemElement, "price", "state");
                    String priceCurrency = getTagAttributeValue(itemElement, "price", "currency");

                    String dvdspecAspectRatio = getCharacterData(itemElement, "dvdspec/price");
                    String dvdspecFormat = getCharacterData(itemElement, "dvdspec/format");
                    String dvdspecRegionCode = getCharacterData(itemElement, "dvdspec/regioncode");
                    String dvdspecReleaseDate = getCharacterData(itemElement, "dvdspec/releasedate");
                    String dvdspecRunningTime = getCharacterData(itemElement, "dvdspec/runningtime");
                    String dvdspecTheatrRelease = getCharacterData(itemElement, "dvdspec/theatr_release");
                    String dvdspecUpc = getTagValue(itemElement, "dvdspec/upc");


                    String bookspecBinding = getTagValue(itemElement, "bookspec/binding");
                    String bookspecEdition = getTagAttributeValue(itemElement, "edition", "val");
                    String bookspecISBN = getTagAttributeValue(itemElement, "isbn", "val");
                    List<String> bookspecWeight = getTagValuesList(itemElement, "bookspec", "package", "weight");
                    List<String> bookspecHeight = getTagValuesList(itemElement, "bookspec", "package", "height");
                    List<String> bookspecLength = getTagValuesList(itemElement, "bookspec", "package", "length");
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

                    String picture = "";
                    String detailPage ="";


                    if (SHOP_MODE.equals("LEIPZIG")) {
                        picture = itemElement.getAttribute("picture");
                        detailPage = itemElement.getAttribute("detailpage");

                        // TODO: REWORK!
                    }

                    if (SHOP_MODE.equals("DRESDEN")) {
                        picture = itemElement.getAttribute("picture");
                        detailPage = itemElement.getAttribute("detailpage");
                    }


                    String ean = itemElement.getAttribute("ean");

                    List<String> labels = getTagValuesList(itemElement, "labels", "label","name");
                    List<String> creators = getTagValuesList(itemElement, "creators", "creator","name");
                    List<String> authors = getTagValuesList(itemElement, "authors", "author","name");
                    List<String> directors = getTagValuesList(itemElement, "directors", "director","name");
                    List<String> artists = getTagValuesList(itemElement, "artists", "artist","name");
                    List<String> listmanialists = getTagValuesList(itemElement, "listmania", "list","name");
                    List<String> publishers = getTagValuesList(itemElement, "publishers", "publisher","name");
                    List<String> studios = getTagValuesList(itemElement, "studios", "studio","name");


                    Item item = new Item(pgroup,
                            asin,
                            productTitle,
                            price,
                            priceMult,
                            priceState,
                            priceCurrency,
                            dvdspecAspectRatio,
                            dvdspecFormat,
                            dvdspecRegionCode,
                            dvdspecReleaseDate,
                            dvdspecRunningTime,
                            dvdspecTheatrRelease,
                            dvdspecUpc,
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
                            ean,
                            labels,
                            creators,
                            authors,
                            directors,
                            artists,
                            listmanialists,
                            publishers,
                            studios
                    );
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

    private static String getCharacterData(Element element, String attribute) {
        String[] attributePath = attribute.split("/");
        Node currentNode = element;

        for (String attr : attributePath) {
            NodeList nodeList = ((Element) currentNode).getElementsByTagName(attr);
            if (nodeList.getLength() > 0) {
                currentNode = nodeList.item(0);
            } else {
                return "";
            }
        }

        return currentNode.getTextContent().trim();
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

    private static List<String> getTagValuesList(Element element, String overallTagName, String subTagName, String val) {
        List<String> labelValues = new ArrayList<>();
        NodeList labelsList = element.getElementsByTagName(overallTagName);
        if (labelsList.getLength() > 0) {
            Element labelsElement = (Element) labelsList.item(0);
            NodeList labelList = labelsElement.getElementsByTagName(subTagName);
            for (int i = 0; i < labelList.getLength(); i++) {
                Node labelNode = labelList.item(i);
                if (labelNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element labelElement = (Element) labelNode;
                    String labelValue = labelElement.getAttribute(val);
                    labelValues.add(labelValue);
                }
            }
        }
        return labelValues;
    }
}



class Item {
    private String pgroup;
    private String asin;

    private String productTitle;

    private String price;
    private String priceMult;
    private String priceState;
    private String priceCurrency;

    private String dvdspecAspectRatio;
    private String dvdspecFormat;
    private String dvdspecRegionCode;
    private String dvdspecReleaseDate;
    private String dvdspecRunningTime;
    private String dvdspecTheatrRelease;
    private String dvdspecUpc;

    private String bookspecBinding;
    private String bookspecEdition;
    private String bookspecISBN;
    private List<String>  bookspecWeight;
    private List<String>  bookspecHight;

    private List<String>  bookspecLength;
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
    private List<String> labels;

    private List<String> creators;
    private List<String> authors;
    private List<String> directors;
    private List<String> artists;
    private List<String> listmanialists;

    private List<String> publishers;

    private List<String> studios;


    public Item(String pgroup,
                String asin,
                String productTitle,
                String price,
                String priceMult,
                String priceState,
                String priceCurrency,
                String dvdspecAspectRatio,
                String dvdspecFormat,
                String dvdspecRegionCode,
                String dvdspecReleaseDate,
                String dvdspecRunningTime,
                String dvdspecTheatrRelease,
                String dvdspecUpc,
                String bookspecBinding,
                String bookspecEdition,
                String bookspecISBN,
                List<String>  bookspecWeight,
                List<String>  bookspecHight,
                List<String>  bookspecLength,
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
                String ean,
                List<String> labels,
                List<String> creators,
                List<String> authors,
                List<String> directors,
                List<String> artists,
                List<String> listmanialists,
                List<String> publishers,
                List<String> studios
    ) {
        this.pgroup = pgroup;
        this.asin = asin;
        this.productTitle = productTitle;
        this.price = price;
        this.priceMult = priceMult;
        this.priceState = priceState;
        this.priceCurrency = priceCurrency;
        this.productTitle = productTitle;
        this.dvdspecAspectRatio = dvdspecAspectRatio;
        this.dvdspecFormat = dvdspecFormat;
        this.dvdspecRegionCode = dvdspecRegionCode;
        this.dvdspecReleaseDate = dvdspecReleaseDate;
        this.dvdspecRunningTime = dvdspecRunningTime;
        this.dvdspecTheatrRelease = dvdspecTheatrRelease;
        this.dvdspecUpc = dvdspecUpc;
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
        this.labels = labels;
        this.creators = creators;
        this.authors = authors;
        this.directors = directors;
        this.artists = artists;
        this.listmanialists = listmanialists;
        this.publishers = publishers;
        this.studios = studios;
    }

    public String getPgroup() {
        return pgroup;
    }

    public String getAsin() {
        return asin;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceMult() {
        return priceMult;
    }

    public String getPriceState() {
        return priceState;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public String getDvdspecAspectRatio() {
        return dvdspecAspectRatio;
    }

    public String getDvdspecFormat() {
        return dvdspecFormat;
    }

    public String getDvdspecRegionCode() {
        return dvdspecRegionCode;
    }

    public String getDvdspecReleaseDate() {
        return dvdspecReleaseDate;
    }

    public String getDvdspecRunningTime() {
        return dvdspecRunningTime;
    }

    public String getDvdspecTheatrRelease() {
        return dvdspecTheatrRelease;
    }

    public String getDvdspecUpc() {
        return dvdspecUpc;
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

    public List<String>  getBookspecWeight() {
        return bookspecWeight;
    }

    public List<String>  getBookspecHight() {
        return bookspecHight;
    }

    public List<String>  getBookspecLength() {
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

    public List<String> getLabels() {
        return labels;
    }

    public List<String> getCreators() {
        return creators;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getArtists() {
        return artists;
    }

    public List<String> getListmanialists() {
        return listmanialists;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public List<String> getStudios() {
        return studios;
    }
}
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

            System.out.println("audiotext type: " + item.getAudiotextType());
            System.out.println("audiotext language " + item.getAudiotextLanguage());
            System.out.println("audiotext audio format: " + item.getAudiotextAudioformat());

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
                    // ## Common data which is labeled the same in XML for both LEIPZIG and DRESDEN

                    // ITEM overall
                    List<String> asin =  getTagAttributeDataVal(itemElement, "", "asin");
                    List<String> pgroup =  getTagAttributeDataVal(itemElement, "", "pgroup");
                    List<String> salesRank =  getTagAttributeDataVal(itemElement, "", "salesrank");
                    List<String> ean =  getTagAttributeDataVal(itemElement, "", "ean");

                    // Title
                    List<String> productTitle = getCharacterDataVal(itemElement, "title");

                    // Priceinfos
                    List<String> price = getCharacterDataVal(itemElement, "price");
                    List<String> priceMult = getTagAttributeDataVal(itemElement, "price", "mult");
                    List<String> priceState = getTagAttributeDataVal(itemElement, "price", "state");
                    List<String> priceCurrency = getTagAttributeDataVal(itemElement, "price", "currency");

                    // DVDSpec
                    List<String> dvdspecAspectRatio = getCharacterDataVal(itemElement, "dvdspec/price");
                    List<String> dvdspecFormat = getCharacterDataVal(itemElement, "dvdspec/format");
                    List<String> dvdspecRegionCode = getCharacterDataVal(itemElement, "dvdspec/regioncode");
                    List<String> dvdspecReleaseDate = getCharacterDataVal(itemElement, "dvdspec/releasedate");
                    List<String> dvdspecRunningTime = getCharacterDataVal(itemElement, "dvdspec/runningtime");
                    List<String> dvdspecTheatrRelease = getCharacterDataVal(itemElement, "dvdspec/theatr_release");
                    List<String> dvdspecUpc = getTagAttributeDataVal(itemElement, "dvdspec", "val");

                    // BookSpec
                    List<String> bookspecBinding = getCharacterDataVal(itemElement, "bookspec/binding");
                    List<String> bookspecEdition = getTagAttributeDataVal(itemElement, "edition", "val");
                    List<String> bookspecISBN = getTagAttributeDataVal(itemElement, "isbn", "val");
                    List<String> bookspecWeight = getTagAttributeDataVal(itemElement, "bookspec/package",  "weight");
                    List<String> bookspecHeight = getTagAttributeDataVal(itemElement, "bookspec/package",  "height");
                    List<String> bookspecLength = getTagAttributeDataVal(itemElement, "bookspec/package", "length");
                    List<String> bookspecPages = getCharacterDataVal(itemElement, "bookspec/pages");
                    List<String> bookspecPublicationDate = getTagAttributeDataVal(itemElement, "publication", "date");

                    // MusicSpec
                    List<String> musicspecBinding = getCharacterDataVal(itemElement, "musicspec/binding");
                    List<String> musicspecFormat = getTagAttributeDataVal(itemElement, "musicspec/format", "value");
                    List<String> musicspecNumDiscs = getCharacterDataVal(itemElement, "musicspec/num_discs");
                    List<String> musicspecReleaseDate = getCharacterDataVal(itemElement, "musicspec/releasedate");
                    List<String> musicspecUpc = getCharacterDataVal(itemElement, "musicspec/upc");

                    // Audiotext
                    List<String> audiotextType = getTagAttributeDataVal(itemElement, "audiotext/language", "type");
                    List<String> audiotextLanguage = getCharacterDataVal(itemElement, "audiotext/language");
                    List<String> audiotextAudioformat = getCharacterDataVal(itemElement, "audiotext/audioformat");


                    // Multiples
                    List<String> labels =  getTagAttributeDataVal(itemElement, "labels/label", "name");
                    List<String> creators = getTagAttributeDataVal(itemElement, "creators/creator" ,"name");
                    List<String> authors = getTagAttributeDataVal(itemElement, "authors/author", "name");
                    List<String> directors = getTagAttributeDataVal(itemElement, "directors/director", "name");
                    List<String> artists = getTagAttributeDataVal(itemElement, "artists/artist", "name");
                    List<String> listmanialists = getTagAttributeDataVal(itemElement, "listmania/list", "name");
                    List<String> publishers = getTagAttributeDataVal(itemElement, "publishers/publisher", "name");
                    List<String> studios = getTagAttributeDataVal(itemElement, "studios/studio", "name");
                    List<String> similars = getCharacterDataVal(itemElement, "similars/sim_product/asin");
                    List<String> tracks = getCharacterDataVal(itemElement, "tracks/title");


                    // ## Data which is labeled different in XML for LEIPZIG or DRESDEN

                    List<String> picture = getTagAttributeDataVal(itemElement, "", "picture");
                    List<String> detailPage = getTagAttributeDataVal(itemElement, "", "detailpage");


                    if (SHOP_MODE.equals("LEIPZIG")) {
                        picture = getTagAttributeDataVal(itemElement, "", "picture");
                        detailPage = getTagAttributeDataVal(itemElement, "", "detailpage");

                        // TODO: REWORK!
                    }

                    if (SHOP_MODE.equals("DRESDEN")) {

                    }

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
                            audiotextType,
                            audiotextLanguage,
                            audiotextAudioformat,
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

    private static List<String> escapeStrings(List<String> strings) {
        List<String> escapedStrings = new ArrayList<>();
        for (String string : strings) {
            escapedStrings.add(escapeString(string));
        }
        return escapedStrings;
    }

    private static String escapeString(String string) {
        string = string.replace("'", "\\'");
        string = string.replace("\"", "\\\"");
        string = string.replace("[", "\\[");
        string = string.replace("]", "\\]");
        return "'" + string + "'";
    }

    private static List<String> getCharacterDataVal(Element element, String path) {
        List<String> characterDataValues = new ArrayList<>();
        String[] tags = path.split("/");
        traversCharacterDataValRecursive(element, tags, 0, characterDataValues);
        characterDataValues = escapeStrings(characterDataValues);
        return characterDataValues;
    }

    private static void traversCharacterDataValRecursive(Element element, String[] tags, int index, List<String> characterDataValues) {
        if (index >= tags.length) {
            // Reached the end of the path, collect character data value
            String characterData = element.getTextContent().trim();
            if (!characterData.isEmpty()) {
                characterDataValues.add(characterData);
            }
            return;
        }

        String tag = tags[index];
        NodeList nodeList = element.getElementsByTagName(tag);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                traversCharacterDataValRecursive(childElement, tags, index + 1, characterDataValues);
            }
        }
    }

    private static List<String> getTagAttributeDataVal (Element element, String path, String attributeName) {
        List<String> tagAttributeValues = new ArrayList<>();
        if (path.isEmpty()) {
            String attributeValue = element.getAttribute(attributeName);
            if (!attributeValue.isEmpty()) {
                tagAttributeValues.add(attributeValue);
            }
        } else {
            String[] tags = path.split("/");
            traversTagAttributeDataValRecursive(element, tags, 0, attributeName, tagAttributeValues);
        }
        tagAttributeValues = escapeStrings(tagAttributeValues);
        return tagAttributeValues;
    }

    private static void traversTagAttributeDataValRecursive (Element element, String[] tags, int index, String attributeName, List<String> tagAttributeValues) {
        if (index >= tags.length) {
            return;
        }

        String tag = tags[index];
        NodeList nodeList = element.getElementsByTagName(tag);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                traversTagAttributeDataValRecursive(childElement, tags, index + 1, attributeName, tagAttributeValues);
                String attributeValue = childElement.getAttribute(attributeName);
                if (!attributeValue.isEmpty()) {
                    tagAttributeValues.add(attributeValue);
                }
            }
        }
    }
}

class Item {
    private List<String> pgroup;
    private List<String> asin;

    private List<String> productTitle;

    private List<String> price;
    private List<String> priceMult;
    private List<String> priceState;
    private List<String> priceCurrency;

    private List<String> dvdspecAspectRatio;
    private List<String> dvdspecFormat;
    private List<String> dvdspecRegionCode;
    private List<String> dvdspecReleaseDate;
    private List<String> dvdspecRunningTime;
    private List<String> dvdspecTheatrRelease;

    private List<String> audiotextType;

    private List<String> audiotextLanguage;

    private List<String> audiotextAudioformat;
    private List<String> dvdspecUpc;

    private List<String> bookspecBinding;
    private List<String> bookspecEdition;
    private List<String> bookspecISBN;
    private List<String>  bookspecWeight;
    private List<String>  bookspecHight;

    private List<String>  bookspecLength;
    private List<String> bookspecPages;

    private List<String> bookspecPublicationDate;
    private List<String> musicspecBinding;
    private List<String> musicspecFormat;
    private List<String> musicspecNumDiscs;
    private List<String> musicspecReleaseDate;
    private List<String> musicspecUpc;
    private List<String> similars;
    private List<String> tracks;
    private List<String> salesRank;
    private List<String> picture;
    private List<String> detailPage;
    private List<String> ean;
    private List<String> labels;

    private List<String> creators;
    private List<String> authors;
    private List<String> directors;
    private List<String> artists;
    private List<String> listmanialists;

    private List<String> publishers;

    private List<String> studios;


    public Item(List<String> pgroup,
                List<String> asin,
                List<String> productTitle,
                List<String> price,
                List<String> priceMult,
                List<String> priceState,
                List<String> priceCurrency,
                List<String> dvdspecAspectRatio,
                List<String> dvdspecFormat,
                List<String> dvdspecRegionCode,
                List<String> dvdspecReleaseDate,
                List<String> dvdspecRunningTime,
                List<String> dvdspecTheatrRelease,
                List<String> dvdspecUpc,
                List<String> bookspecBinding,
                List<String> bookspecEdition,
                List<String> bookspecISBN,
                List<String> bookspecWeight,
                List<String> bookspecHight,
                List<String> bookspecLength,
                List<String> bookspecPages,
                List<String> bookspecPublicationDate,
                List<String> musicspecBinding,
                List<String> musicspecFormat,
                List<String> musicspecNumDiscs,
                List<String> musicspecReleaseDate,
                List<String> musicspecUpc,
                List<String> audiotextLanguage,
                List<String> audiotextType,
                List<String> audiotextAudioformat,
                List<String> similars,
                List<String> tracks,
                List<String> salesRank,
                List<String> picture,
                List<String> detailPage,
                List<String> ean,
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
        this.audiotextLanguage = audiotextLanguage;
        this.audiotextType = audiotextType;
        this.audiotextAudioformat = audiotextAudioformat;
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

    public List<String> getPgroup() {
        return pgroup;
    }

    public List<String> getAsin() {
        return asin;
    }

    public List<String> getProductTitle() {
        return productTitle;
    }

    public List<String> getPrice() {
        return price;
    }

    public List<String> getPriceMult() {
        return priceMult;
    }

    public List<String> getPriceState() {
        return priceState;
    }

    public List<String> getPriceCurrency() {
        return priceCurrency;
    }

    public List<String> getDvdspecAspectRatio() { return dvdspecAspectRatio; }

    public List<String> getDvdspecFormat() {
        return dvdspecFormat;
    }

    public List<String> getDvdspecRegionCode() {
        return dvdspecRegionCode;
    }

    public List <String> getDvdspecReleaseDate() { return dvdspecReleaseDate;}

    public List<String> getDvdspecRunningTime() { return dvdspecRunningTime;}

    public List<String> getDvdspecTheatrRelease() {
        return dvdspecTheatrRelease;
    }

    public List<String> getDvdspecUpc() {
        return dvdspecUpc;
    }

    public List<String> getAudiotextType() {
        return audiotextType;
    }

    public List<String> getAudiotextLanguage() {
        return audiotextLanguage;
    }

    public List<String> getAudiotextAudioformat() {
        return audiotextAudioformat;
    }

    public List<String> getBookspecBinding() {
        return bookspecBinding;
    }

    public List<String> getBookspecEdition() {
        return bookspecEdition;
    }

    public List<String> getBookspecISBN() {
        return bookspecISBN;
    }

    public List<String>  getBookspecWeight() {
        return bookspecWeight;
    }

    public List<String>  getBookspecHight() {
        return bookspecHight;
    }

    public List<String>  getBookspecLength() { return bookspecLength; }

    public List<String> getBookspecPages() {
        return bookspecPages;
    }

    public List<String> getBookspecPublicationDate() {
        return bookspecPublicationDate;
    }


    public List<String> getMusicspecBinding() {
        return musicspecBinding;
    }
    public List<String> getMusicspecFormat() {
        return musicspecFormat;
    }

    public List<String> getMusicspecNumDiscs() {
        return musicspecNumDiscs;
    }

    public List<String> getMusicspecReleaseDate() {
        return musicspecReleaseDate;
    }

    public List<String> getMusicspecUpc() {
        return musicspecUpc;
    }

    public List<String> getSimilars() {
        return similars;
    }

    public List<String> getTracks() {
        return tracks;
    }

    public List<String> getSalesRank() {
        return salesRank;
    }

    public List<String> getPicture() {
        return picture;
    }

    public List<String> getDetailPage() {
        return detailPage;
    }

    public List<String> getEan() {
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
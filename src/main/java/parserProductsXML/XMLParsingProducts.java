package parserProductsXML;

import dataCleanUp.CleanUpOperations;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class XMLParsingProducts {

    public static List<HashMap<String, List<String>>> parseXMLFile(String filePath, List<String> SHOP_MODE) {

        // if (SHOP_MODE.equals("DRESDEN")) convertFileEncoding(filePath, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8);

        List<Item> items = parseXML(filePath, SHOP_MODE);

        List<HashMap<String, List<String>>> itemList = new ArrayList<>();

        int itemIndex = 0;
        for (Item item : items) {
            // Debugging
            // if (itemIndex >= 2) break;

            HashMap<String, List<String>> itemMap = new HashMap<>();
            ArrayList<String> indexMap = new ArrayList<>();
            indexMap.add(String.valueOf(itemIndex));

            itemMap.put("__INDEX__", indexMap);
            itemMap.put("shop_id", SHOP_MODE);
            itemMap.put("pgroup", item.getPgroup());
            itemMap.put("asin", item.getAsin());
            itemMap.put("ptitle", item.getProductTitle());
            itemMap.put("upc", item.getUpc());


            itemMap.put("price", item.getPrice());
            itemMap.put("price_multiplier", item.getPriceMult());
            itemMap.put("price_state", item.getPriceState());
            itemMap.put("price_currency", item.getPriceCurrency());

            itemMap.put("dvdspec_aspect_ratio", item.getDvdspecAspectRatio());
            itemMap.put("dvdspec_format", item.getDvdspecFormat());
            itemMap.put("dvdspec_region_code", item.getDvdspecRegionCode());
            itemMap.put("dvdspec_release_date", item.getDvdspecReleaseDate());
            itemMap.put("dvdspec_running_time", item.getDvdspecRunningTime());
            itemMap.put("dvdspec_theatr_release", item.getDvdspecTheatrRelease());
            itemMap.put("dvdspec_upc", item.getDvdspecUpc());

            itemMap.put("bookspec_binding", item.getBookspecBinding());
            itemMap.put("bookspec_edition", item.getBookspecEdition());
            itemMap.put("bookspec_isbn", item.getBookspecISBN());
            itemMap.put("bookspec_weight", item.getBookspecWeight());
            itemMap.put("bookspec_height", item.getBookspecHight());
            itemMap.put("bookspec_length", item.getBookspecLength());
            itemMap.put("bookspec_pages", item.getBookspecPages());
            itemMap.put("bookspec_publication_date", item.getBookspecPublicationDate());

            itemMap.put("musicspec_binding", item.getMusicspecBinding());
            itemMap.put("musicspec_format", item.getMusicspecFormat());
            itemMap.put("musicspec_num_discs", item.getMusicspecNumDiscs());
            itemMap.put("musicspec_release_date", item.getMusicspecReleaseDate());
            itemMap.put("musicspec_upc", item.getMusicspecUpc());

            itemMap.put("audiotext_type", item.getAudiotextType());
            itemMap.put("audiotext_language", item.getAudiotextLanguage());
            itemMap.put("audiotext_audio_format", item.getAudiotextAudioformat());

            itemMap.put("similars", item.getSimilars());
            itemMap.put("tracks", item.getTracks());
            itemMap.put("salesrank", item.getSalesRank());
            itemMap.put("image_url", item.getPicture());
            itemMap.put("detailpage_url", item.getDetailPage());
            itemMap.put("ean", item.getEan());
            itemMap.put("labels", item.getLabels());

            itemMap.put("creators", item.getCreators());
            itemMap.put("authors", item.getAuthors());
            itemMap.put("directors", item.getDirectors());
            itemMap.put("artists", item.getArtists());
            itemMap.put("listmania_lists", item.getListmanialists());
            itemMap.put("publishers", item.getPublishers());
            itemMap.put("studios", item.getStudios());

            itemList.add(itemMap);

            //System.out.println(itemMap);
            // System.out.println("----------------------------------");

            itemIndex++;
        }
        return itemList;
    }

    private static List<Item> parseXML(String filePath, List<String> SHOP_MODE) {
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

                    // Title
                    List<String> productTitle =  getCharacterDataVal(itemElement, "title");

                    // Priceinfos
                    List<String> price = getCharacterDataVal(itemElement, "price");
                    List<String> priceMult = getTagAttributeDataVal(itemElement, "price", "mult");
                    List<String> priceState = getTagAttributeDataVal(itemElement, "price", "state");
                    List<String> priceCurrency = getTagAttributeDataVal(itemElement, "price", "currency");

                    // DVDSpec
                    List<String> dvdspecAspectRatio = getCharacterDataVal(itemElement, "dvdspec/aspectratio");
                    List<String> dvdspecFormat = getCharacterDataVal(itemElement, "dvdspec/format");
                    dvdspecFormat = CleanUpOperations.splitStringIntoPieces(dvdspecFormat);
                    // dvdspecFormat = CleanUpOperations.removeInvalidsDvdFormat(dvdspecFormat);
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
                    List<String> musicspecNumDiscs = getCharacterDataVal(itemElement, "musicspec/num_discs");
                    List<String> musicspecReleaseDate = getCharacterDataVal(itemElement, "musicspec/releasedate");
                    List<String> musicspecUpc = getCharacterDataVal(itemElement, "musicspec/upc");

                    // Audiotext
                    List<String> audiotextType = getTagAttributeDataVal(itemElement, "audiotext/language", "type");
                    List<String> audiotextLanguage = getCharacterDataVal(itemElement, "audiotext/language");
                    List<String> audiotextAudioformat = getCharacterDataVal(itemElement, "audiotext/audioformat");

                    // Tracks
                    List<String> tracks = getCharacterDataVal(itemElement, "tracks/title");

                    // ## Data which is labeled different in XML for LEIPZIG or DRESDEN

                    List<String> picture;
                    List<String> detailPage;
                    List<String> musicspecFormat;
                    List<String> ean;
                    List<String> listmanialists;
                    List<String> labels;
                    List<String> creators;
                    List<String> authors;
                    List<String> directors;
                    List<String> artists;
                    List<String> publishers;
                    List<String> studios;
                    List<String> similars;

                    if (SHOP_MODE.get(0).equals("LEIPZIG")) {
                        picture = getTagAttributeDataVal(itemElement, "", "picture");
                        detailPage = getTagAttributeDataVal(itemElement, "", "detailpage");
                        musicspecFormat = getTagAttributeDataVal(itemElement, "musicspec/format", "value");
                        musicspecFormat = CleanUpOperations.removeRedundancies(musicspecFormat);
                        ean = getTagAttributeDataVal(itemElement, "", "ean");
                        listmanialists = getTagAttributeDataVal(itemElement, "listmania/list", "name");
                        labels =  getTagAttributeDataVal(itemElement, "labels/label", "name");
                        creators = getTagAttributeDataVal(itemElement, "creators/creator" ,"name");
                        authors = getTagAttributeDataVal(itemElement, "authors/author", "name");
                        directors = getTagAttributeDataVal(itemElement, "directors/director", "name");
                        artists = getTagAttributeDataVal(itemElement, "artists/artist", "name");
                        publishers = getTagAttributeDataVal(itemElement, "publishers/publisher", "name");
                        studios = getTagAttributeDataVal(itemElement, "studios/studio", "name");
                        similars = getCharacterDataVal(itemElement, "similars/sim_product/asin");

                    } else { // DRESDEN
                        picture = getTagAttributeDataVal(itemElement, "details", "img");
                        detailPage = getCharacterDataVal(itemElement, "details");
                        musicspecFormat = getCharacterDataVal(itemElement, "musicspec/format");
                        musicspecFormat = CleanUpOperations.removeRedundancies(musicspecFormat);
                        ean = getCharacterDataVal(itemElement, "ean");
                        listmanialists = getCharacterDataVal(itemElement, "listmania/list");
                        labels =  getCharacterDataVal(itemElement, "labels/label");
                        creators = getCharacterDataVal(itemElement, "creators/creator");
                        authors = getCharacterDataVal(itemElement, "authors/author");
                        directors = getCharacterDataVal(itemElement, "directors/director");
                        artists = getCharacterDataVal(itemElement, "artists/artist");
                        publishers = getCharacterDataVal(itemElement, "publishers/publisher");
                        studios = getCharacterDataVal(itemElement, "studios/studio");
                        similars = getTagAttributeDataVal(itemElement, "similars/item", "asin");
                    }

                    // Combine UPC from CDs and DVDs to normalize the values in products table
                    List<String> upc = new ArrayList<>();
                    upc.addAll(dvdspecUpc);
                    upc.addAll(musicspecUpc);


                    Item item = new Item(pgroup,
                            asin,
                            productTitle,
                            upc,
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

    private static String escapeString(String value) {
        if (value == null) {
            return null;
        }
        return value
                .replace("\"", "\"\"")
                .replace("'", "''")
                .replace("\\[", "[")
                .replace("\\]", "]")
                .replace("\\;", ";")
                .replace("\\&", "&");
    }


    private static List<String> getCharacterDataVal(Element element, String path) {
        List<String> characterDataValues = new ArrayList<>();
        String[] tags = path.split("/");
        // Suppresses a recursion for single elements on hierarchy 1
        if (tags.length == 1) {
            NodeList nodeList = element.getElementsByTagName(tags[0]);
            if (nodeList.getLength() > 0) {
                Node node = nodeList.item(0);
                String ele = node.getTextContent().trim();
                if (!ele.isEmpty()) {
                    characterDataValues.add(ele);
                }
            }
            characterDataValues = escapeStrings(characterDataValues);
            return characterDataValues;
        }
        traverseCharacterDataValRecursive(element, tags, 0, characterDataValues);
        characterDataValues = escapeStrings(characterDataValues);
        return characterDataValues;
    }

    private static void traverseCharacterDataValRecursive(Element element, String[] tags, int index, List<String> characterDataValues) {
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
                traverseCharacterDataValRecursive(childElement, tags, index + 1, characterDataValues);
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

    private List<String> upc;

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
                List<String> upc,
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
        this.upc = upc;
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
    public List<String> getUpc() {
        return upc;
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
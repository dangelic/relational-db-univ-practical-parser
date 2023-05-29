package entityMap;

import java.util.HashMap;

/**
 * The EntityFieldDTMappings class provides mappings between entity fields and their corresponding data types for the mapping system.
 */
public  class EntityFieldDTMappings {
    public static HashMap<String, String> getProductsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "asin@stringtext");
        FieldDTMappings.put("ptitle", "ptitle@stringtext");
        FieldDTMappings.put("pgroup", "pgroup@stringtext");
        FieldDTMappings.put("ean", "ean@stringtext");
        FieldDTMappings.put("image_url", "image_url@stringtext");
        FieldDTMappings.put("detailpage_url", "detailpage_url@stringtext");
        FieldDTMappings.put("salesrank", "salesrank@integer");
        FieldDTMappings.put("upc", "upc@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getBankinfosEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("bankinfo_id", "bankinfo_id@stringtext");
        FieldDTMappings.put("user", "users_username@stringtext");
        FieldDTMappings.put("account_number", "account_number@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getUsersEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("user", "username@stringtext");
        return FieldDTMappings;
    }


    public static HashMap<String, String> getCreatorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("creators", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getListmanialistsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("listmania_lists", "name@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getShopaddressesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("shop_id", "shops_shop_id@stringtext");
        FieldDTMappings.put("street", "street@stringtext");
        FieldDTMappings.put("zip", "zip@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getShopsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("shop_id", "shop_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getPriceinfosEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "products_asin@stringtext");
        FieldDTMappings.put("shop_id", "shops_shop_id@stringtext");
        FieldDTMappings.put("price", "price@integer");
        FieldDTMappings.put("price_multiplier", "multiplier@float");
        FieldDTMappings.put("price_currency", "currency@stringtext");
        FieldDTMappings.put("price_state", "state@stringtext");
        return FieldDTMappings;
    }
    public static HashMap<String, String> getUserreviewsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("reviewdate", "review_date@date");
        FieldDTMappings.put("rating", "rating@float");
        FieldDTMappings.put("helpful", "helpful_votes@integer");
        FieldDTMappings.put("product", "products_asin@stringtext");
        FieldDTMappings.put("summary", "summary@stringtext");
        FieldDTMappings.put("content", "content@stringtext");
        FieldDTMappings.put("user", "users_username@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getGuestReviewsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("reviewdate", "review_date@date");
        FieldDTMappings.put("rating", "rating@float");
        FieldDTMappings.put("helpful", "helpful_votes@integer");
        FieldDTMappings.put("product", "products_asin@stringtext");
        FieldDTMappings.put("summary", "summary@stringtext");
        FieldDTMappings.put("content", "content@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getPurchasesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("purchase_id", "purchase_id@stringtext");
        FieldDTMappings.put("purchase_date", "purchase_date@date");
        FieldDTMappings.put("purchase_quantity", "purchase_quantity@integer");
        FieldDTMappings.put("purchase_price", "purchase_price@float(2)");
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("users_username", "users_username@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getDeliveryaddressesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("deliveryaddress_id", "deliveryaddress_id@stringtext");
        FieldDTMappings.put("street", "street@stringtext");
        FieldDTMappings.put("zip", "zip@stringtext");
        FieldDTMappings.put("users_username", "users_username@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getAuthorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("authors", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getPublishersEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("publishers", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getActorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("actors", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getArtistsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("artists", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getStudiosEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("studios", "name@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getAudiotextsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("audiotext", "media_properties@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getLabelsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("labels", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getBooksEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "asin@stringtext");
        FieldDTMappings.put("bookspec_isbn", "isbn@stringtext");
        FieldDTMappings.put("bookspec_weight", "weight@float");
        FieldDTMappings.put("bookspec_height", "height@float");
        FieldDTMappings.put("bookspec_length", "length@float");
        FieldDTMappings.put("bookspec_pages", "pages@integer");
        FieldDTMappings.put("bookspec_publication_date", "publication_date@date");
        FieldDTMappings.put("bookspec_edition", "edition@stringtext");
        FieldDTMappings.put("bookspec_binding", "binding@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getCdsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "asin@stringtext");
        FieldDTMappings.put("musicspec_binding", "binding@stringtext");
        FieldDTMappings.put("musicspec_format", "cd_format@integer");
        FieldDTMappings.put("musicspec_num_discs", "num_discs@integer");
        FieldDTMappings.put("musicspec_release_date", "release_date@date");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getDvdsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "asin@stringtext");
        FieldDTMappings.put("dvdspec_aspect_ratio", "aspect_ratio@stringtext");
        FieldDTMappings.put("dvdspec_region_code", "region_code@stringtext");
        FieldDTMappings.put("dvdspec_release_date", "release_date@date");
        FieldDTMappings.put("dvdspec_running_time", "running_time@integer");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getDvdformatsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("dvdspec_format", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getTracksEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "cds_asin@stringtext");
        FieldDTMappings.put("tracks", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getProductsSimilarsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "products_asin@stringtext");
        FieldDTMappings.put("similars", "similar_product_asin@stringtext");
        return FieldDTMappings;
    }
}
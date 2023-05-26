package entityMap;

import java.util.HashMap;

public  class EntityFieldDTMappings {
    // TODO: Change the source field names appropriately to the parser values
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
        FieldDTMappings.put("account_number", "account_number@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getUsersEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("username", "username@stringtext");
        FieldDTMappings.put("banks_bankinfo_id", "banks_bankinfo_id@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getCategoriesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("category_id", "category_id@stringtext");
        FieldDTMappings.put("parent_category_id", "parent_category_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
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
        FieldDTMappings.put("street", "street@stringtext");
        FieldDTMappings.put("zip", "zip@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getShopsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
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
        FieldDTMappings.put("review_rating", "review_rating@float");
        FieldDTMappings.put("helpful", "helpful_votes@integer");
        FieldDTMappings.put("product", "products_asin@stringtext");
        FieldDTMappings.put("summary", "summary@stringtext");
        FieldDTMappings.put("content", "content@stringtext");
        FieldDTMappings.put("user", "username@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getGuestReviewsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("reviewdate", "review_date@date");
        FieldDTMappings.put("review_rating", "review_rating@float");
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

    public static HashMap<String, String> getStudiosEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("studios", "name@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getAudiotextsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("audiotext_id", "audiotext_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
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
        FieldDTMappings.put("musicspec_format", "format@integer");
        FieldDTMappings.put("musicspec_num_discs", "num_discs@integer");
        FieldDTMappings.put("musicspec_release_date", "release_date@date");
        FieldDTMappings.put("musicspec_upc", "upc@integer"); // TODO: How to solve this?
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionCdsLabelsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("cds_cd_id", "cds_cd_id@stringtext");
        FieldDTMappings.put("labels_label_id", "labels_label_id@stringtext");
        return FieldDTMappings;
    }
    // TODO: Create format and junction table!
    public static HashMap<String, String> getDvdsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "asin@stringtext");
        FieldDTMappings.put("dvdspec_aspect_ratio", "aspect_ratio@stringtext");
        FieldDTMappings.put("dvdspec_region_code", "region_code@stringtext");
        FieldDTMappings.put("dvdspec_release_date", "release_date@date");
        FieldDTMappings.put("dvdspec_running_time", "running_time@integer");
        FieldDTMappings.put("dvdspec_upc", "upc@stringtext"); // TODO: Normalize this.
        return FieldDTMappings;
    }

    public static HashMap<String, String> getDvdformatsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("dvdspec_format", "name@stringtext");
        return FieldDTMappings;
    }

    // TODO: Rework!
    public static HashMap<String, String> getJunctionDvdsDvdformatsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("books_book_id", "books_book_id@stringtext");
        FieldDTMappings.put("authors_author_id", "authors_author_id@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionBooksAuthorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("books_book_id", "books_book_id@stringtext");
        FieldDTMappings.put("authors_author_id", "authors_author_id@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionBooksPublishersEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("books_book_id", "books_book_id@stringtext");
        FieldDTMappings.put("publishers_publisher_id", "publishers_publisher_id@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionDvdsActorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("dvds_dvd_id", "dvds_dvd_id@stringtext");
        FieldDTMappings.put("actors_actor_id", "actors_actor_id@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionDvdsStudiosEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("dvds_dvd_id", "dvds_dvd_id@stringtext");
        FieldDTMappings.put("studios_studio_id", "studios_studio_id@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionDvdsAudiotextsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("dvds_dvd_id", "dvds_dvd_id@stringtext");
        FieldDTMappings.put("audiotexts_audiotext_id", "audiotexts_audiotext_id@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getTracksEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "cds_asin@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public static HashMap<String, String> getProductsSimilarsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("asin", "products_asin@stringtext");
        FieldDTMappings.put("similars", "similar_product_asin@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionProductsCategoriesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("categories_category_id", "categories_category_id@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionProductsListmanialistsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("listmanialists_listmanialist_id", "listmanialists_listmanialist_id@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionProductsCreatorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("creators_creator_id", "creators_creator_id@stringtext");
        return FieldDTMappings;
    }
    // TODO: Rework!
    public static HashMap<String, String> getJunctionUsersDeliveryaddressesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("users_username", "users_username@stringtext");
        FieldDTMappings.put("deliveryaddresses_deliveryaddress_id", "deliveryaddresses_deliveryaddress_id@stringtext");
        return FieldDTMappings;
    }
}
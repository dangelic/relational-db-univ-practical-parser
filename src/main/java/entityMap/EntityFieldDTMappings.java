package entityMap;

import java.util.HashMap;

public class EntityFieldDTMappings {
    // TODO: Change the source field names appropriately to the parser values
    public HashMap<String, String> getProductsEntityFieldDTMappings() {
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

    public HashMap<String, String> getBankinfosEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("bankinfo_id", "bankinfo_id@stringtext");
        FieldDTMappings.put("account_number", "account_number@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getUsersEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("username", "username@stringtext");
        FieldDTMappings.put("banks_bankinfo_id", "banks_bankinfo_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getCategoriesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("category_id", "category_id@stringtext");
        FieldDTMappings.put("parent_category_id", "parent_category_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getCreatorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("creator_id", "creator_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getListmanialistsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("listmanialist_id", "listmanialist_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getStoreaddressesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("storeaddress_id", "storeaddress_id@stringtext");
        FieldDTMappings.put("street", "street@stringtext");
        FieldDTMappings.put("zip", "zip@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getStoresEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("store_id", "store_id@stringtext");
        FieldDTMappings.put("storeaddresses_storeaddress_id", "storeaddresses_storeaddress_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getPriceinfosEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("priceinfo_id", "priceinfo_id@stringtext");
        FieldDTMappings.put("price", "price@float(2)");
        FieldDTMappings.put("multiplier", "multiplier@float(2)");
        FieldDTMappings.put("currency", "currency@stringtext");
        FieldDTMappings.put("stores_store_id", "stores_store_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionProductsPriceinfosStoresEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("priceinfos_priceinfo_id", "priceinfos_priceinfo_id@stringtext");
        FieldDTMappings.put("stores_store_id", "stores_store_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getUserreviewsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("review_id", "review_id@stringtext");
        FieldDTMappings.put("review_date", "review_date@date");
        FieldDTMappings.put("review_rating", "review_rating@float(1)");
        FieldDTMappings.put("review_votes", "review_votes@integer");
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getGuestReviewsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("guestreview_id", "guestreview_id@stringtext");
        FieldDTMappings.put("guestreview_date", "guestreview_date@date");
        FieldDTMappings.put("guestreview_rating", "guestreview_rating@float(1)");
        FieldDTMappings.put("guestreview_votes", "guestreview_votes@integer");
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getPurchasesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("purchase_id", "purchase_id@stringtext");
        FieldDTMappings.put("purchase_date", "purchase_date@date");
        FieldDTMappings.put("purchase_quantity", "purchase_quantity@integer");
        FieldDTMappings.put("purchase_price", "purchase_price@float(2)");
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("users_username", "users_username@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getDeliveryaddressesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("deliveryaddress_id", "deliveryaddress_id@stringtext");
        FieldDTMappings.put("street", "street@stringtext");
        FieldDTMappings.put("zip", "zip@stringtext");
        FieldDTMappings.put("users_username", "users_username@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getAuthorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("author_id", "author_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getPublishersEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("publisher_id", "publisher_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getActorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("actor_id", "actor_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getStudiosEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("studio_id", "studio_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getAudiotextsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("audiotext_id", "audiotext_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getLabelsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("label_id", "label_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getBooksEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("book_id", "book_id@stringtext");
        FieldDTMappings.put("title", "title@stringtext");
        FieldDTMappings.put("isbn", "isbn@stringtext");
        FieldDTMappings.put("authors_author_id", "authors_author_id@stringtext");
        FieldDTMappings.put("publishers_publisher_id", "publishers_publisher_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getCdsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("cd_id", "cd_id@stringtext");
        FieldDTMappings.put("title", "title@stringtext");
        FieldDTMappings.put("tracks", "tracks@integer");
        FieldDTMappings.put("labels_label_id", "labels_label_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionCdsLabelsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("cds_cd_id", "cds_cd_id@stringtext");
        FieldDTMappings.put("labels_label_id", "labels_label_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getDvdsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("dvd_id", "dvd_id@stringtext");
        FieldDTMappings.put("title", "title@stringtext");
        FieldDTMappings.put("actors_actor_id", "actors_actor_id@stringtext");
        FieldDTMappings.put("studios_studio_id", "studios_studio_id@stringtext");
        FieldDTMappings.put("audiotexts_audiotext_id", "audiotexts_audiotext_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionBooksAuthorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("books_book_id", "books_book_id@stringtext");
        FieldDTMappings.put("authors_author_id", "authors_author_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionBooksPublishersEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("books_book_id", "books_book_id@stringtext");
        FieldDTMappings.put("publishers_publisher_id", "publishers_publisher_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionDvdsActorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("dvds_dvd_id", "dvds_dvd_id@stringtext");
        FieldDTMappings.put("actors_actor_id", "actors_actor_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionDvdsStudiosEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("dvds_dvd_id", "dvds_dvd_id@stringtext");
        FieldDTMappings.put("studios_studio_id", "studios_studio_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionDvdsAudiotextsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("dvds_dvd_id", "dvds_dvd_id@stringtext");
        FieldDTMappings.put("audiotexts_audiotext_id", "audiotexts_audiotext_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getTracksEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("track_id", "track_id@stringtext");
        FieldDTMappings.put("name", "name@stringtext");
        FieldDTMappings.put("cds_cd_id", "cds_cd_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getProductsSimilarsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("similar_asin", "similar_asin@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionProductsCategoriesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("categories_category_id", "categories_category_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionProductsListmanialistsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("listmanialists_listmanialist_id", "listmanialists_listmanialist_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionProductsCreatorsEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("products_asin", "products_asin@stringtext");
        FieldDTMappings.put("creators_creator_id", "creators_creator_id@stringtext");
        return FieldDTMappings;
    }

    public HashMap<String, String> getJunctionUsersDeliveryaddressesEntityFieldDTMappings() {
        HashMap<String, String> FieldDTMappings = new HashMap<>();
        FieldDTMappings.put("users_username", "users_username@stringtext");
        FieldDTMappings.put("deliveryaddresses_deliveryaddress_id", "deliveryaddresses_deliveryaddress_id@stringtext");
        return FieldDTMappings;
    }
}
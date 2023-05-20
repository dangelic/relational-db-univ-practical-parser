-- Add NOT NULL and UNIQUE constraint

-- #### products
ALTER TABLE products
ADD CONSTRAINT unique_asin UNIQUE (asin),
ADD CONSTRAINT unique_upc UNIQUE (upc),
ADD CONSTRAINT unique_ean UNIQUE (ean),
MODIFY asin VARCHAR(12) NOT NULL,
MODIFY ptitle VARCHAR(255) NOT NULL,
MODIFY pgroup VARCHAR(5) NOT NULL;


-- #### bankinfos
ALTER TABLE bankinfos
ADD CONSTRAINT unique_bankinfos_id UNIQUE (bankinfo_id),
MODIFY bankinfo_id VARCHAR(9) NOT NULL,
MODIFY account_number VARCHAR(9) NOT NULL;


-- #### users
ALTER TABLE users
ADD CONSTRAINT unique_username UNIQUE (username),
MODIFY username VARCHAR(30) NOT NULL;


-- #### categories
ALTER TABLE categories
ADD CONSTRAINT unique_category_id UNIQUE (category_id),
MODIFY category_id VARCHAR(9) NOT NULL,
MODIFY name VARCHAR(9) NOT NULL;

-- #### creators
ALTER TABLE creators
ADD CONSTRAINT unique_creator_id UNIQUE (creator_id),
MODIFY creator_id VARCHAR(9) NOT NULL,
MODIFY name VARCHAR(9) NOT NULL;


-- #### listmanialists
ALTER TABLE listmanialists
ADD CONSTRAINT unique_listmanialist_id UNIQUE (listmanialist_id),
MODIFY listmanialist_id VARCHAR(9) NOT NULL,
MODIFY name VARCHAR(9) NOT NULL;


-- #### storeaddresses
ALTER TABLE storeaddresses
ADD CONSTRAINT unique_address_id UNIQUE (address_id),
MODIFY address_id VARCHAR(9) NOT NULL,
MODIFY street VARCHAR(255) NOT NULL,
MODIFY zip VARCHAR(9) NOT NULL;

-- #### stores
ALTER TABLE stores
ADD CONSTRAINT unique_store_id UNIQUE (store_id),
MODIFY store_id VARCHAR(9) NOT NULL,
MODIFY storeaddresses_storeaddress_id VARCHAR(9) NOT NULL,
MODIFY name VARCHAR(9) NOT NULL;

-- #### priceinfos
ALTER TABLE priceinfos
ADD CONSTRAINT unique_priceinfo_id UNIQUE (priceinfo_id),
MODIFY priceinfo_id VARCHAR(9) NOT NULL;

-- #### map_products_priceinfos_stores
ALTER TABLE map_products_priceinfos_stores
ADD CONSTRAINT unique_mapping_products_priceinfos_stores UNIQUE (products_asin, priceinfos_priceinfo_id, stores_store_id),
MODIFY products_asin VARCHAR(12) NOT NULL,
MODIFY priceinfos_priceinfo_id VARCHAR(9) NOT NULL,
MODIFY stores_store_id VARCHAR(9) NOT NULL;

-- #### userreviews
ALTER TABLE userreviews
ADD CONSTRAINT unique_asin_username_in_userreviews UNIQUE (products_asin, users_username),
MODIFY products_asin VARCHAR(12) NOT NULL,
MODIFY users_username VARCHAR(30) NOT NULL,
MODIFY rating INTEGER NOT NULL,
MODIFY helpful_rating INTEGER NOT NULL,
MODIFY summary TEXT NOT NULL,
MODIFY content TEXT NOT NULL,
MODIFY review_date DATE NOT NULL;

-- #### guestreviews
ALTER TABLE guestreviews
ADD CONSTRAINT unique_guestreview_id UNIQUE (questreview_id),
MODIFY questreview_id VARCHAR(9) NOT NULL,
MODIFY products_asin VARCHAR(12) NOT NULL,
MODIFY rating INTEGER NOT NULL,
MODIFY helpful_rating INTEGER NOT NULL,
MODIFY summary TEXT NOT NULL,
MODIFY content TEXT NOT NULL,
MODIFY review_date DATE NOT NULL;

-- #### purchases
ALTER TABLE purchases
ADD CONSTRAINT unique_purchase_id UNIQUE (purchase_id),
MODIFY purchase_id VARCHAR(12) NOT NULL,
MODIFY products_asin VARCHAR(12) NOT NULL,
MODIFY priceinfos_priceinfo_id VARCHAR(9) NOT NULL,
MODIFY stores_store_id VARCHAR(9) NOT NULL,
MODIFY users_name VARCHAR(30) NOT NULL;

-- #### deliveraddresses
ALTER TABLE deliveryaddresses
ADD CONSTRAINT unique_deliveryaddress_id UNIQUE (deliveryaddress_id),
MODIFY deliveryaddress_id VARCHAR(9) NOT NULL,
MODIFY zip VARCHAR(9) NOT NULL,
MODIFY street VARCHAR(255) NOT NULL;


-- #### authors
ALTER TABLE authors
ADD CONSTRAINT unique_author_id UNIQUE (author_id),
MODIFY author_id VARCHAR(9) NOT NULL;

-- #### publishers
ALTER TABLE publishers
ADD CONSTRAINT unique_publisher_id UNIQUE (publisher_id),
MODIFY publisher_id VARCHAR(9) NOT NULL;

-- #### actors
ALTER TABLE actors
ADD CONSTRAINT unique_actor_id UNIQUE (actor_id),
MODIFY actor_id VARCHAR(9) NOT NULL;

-- #### studios
ALTER TABLE studios
ADD CONSTRAINT unique_studio_id UNIQUE (studio_id),
MODIFY studio_id VARCHAR(9) NOT NULL;

-- #### audiotexts
ALTER TABLE audiotexts
ADD CONSTRAINT unique_audiotext_id UNIQUE (audiotext_id),
MODIFY audiotext_id VARCHAR(9) NOT NULL;

-- #### labels
ALTER TABLE labels
ADD CONSTRAINT unique_label_id UNIQUE (label_id),
MODIFY label_id VARCHAR(9) NOT NULL;

-- #### books
ALTER TABLE books
ADD CONSTRAINT unique_books_asin UNIQUE (asin),
MODIFY asin VARCHAR(12) NOT NULL;

-- #### cds
ALTER TABLE cds
ADD CONSTRAINT unique_cds_asin UNIQUE (asin),
MODIFY asin VARCHAR(12) NOT NULL;

-- #### dvds
ALTER TABLE dvds
ADD CONSTRAINT unique_dvds_asin (asin),
MODIFY asin VARCHAR(12) NOT NULL;

-- #### map_books_authors
ALTER TABLE map_books_authors
ADD CONSTRAINT unique_mapping_books_authors UNIQUE (books_asin, authors_author_id),
MODIFY books_asin VARCHAR(12) NOT NULL,
MODIFY authors_author_id VARCHAR(9) NOT NULL;

-- #### map_books_publishers
ALTER TABLE map_books_publishers
ADD CONSTRAINT unique_mapping_books_publishers UNIQUE (books_asin, publishers_publisher_id),
MODIFY books_asin VARCHAR(12) NOT NULL,
MODIFY publishers_publisher_id VARCHAR(9) NOT NULL;

-- #### map_dvds_actors
ALTER TABLE map_dvds_actors
ADD CONSTRAINT unique_mapping_dvds_actors UNIQUE (dvds_asin, actors_actor_id),
MODIFY dvds_asin VARCHAR(12) NOT NULL,
MODIFY actors_actor_id VARCHAR(9) NOT NULL;

-- #### map_dvds_studios
ALTER TABLE map_dvds_studios
ADD CONSTRAINT unique_mapping_dvds_studios UNIQUE (dvds_asin, studios_studio_id),
MODIFY dvds_asin VARCHAR(12) NOT NULL,
MODIFY studios_studio_id VARCHAR(9) NOT NULL;

-- #### map_dvds_audiotexts
ALTER TABLE map_dvds_audiotexts
ADD CONSTRAINT unique_mapping_dvds_audiotexts UNIQUE (dvds_asin, audiotexts_audiotext_id),
MODIFY dvds_asin VARCHAR(12) NOT NULL,
MODIFY audiotexts_audiotext_id VARCHAR(9) NOT NULL;

-- #### map_cds_labels
ALTER TABLE map_cds_labels
ADD CONSTRAINT unique_mapping_cds_labels UNIQUE (books_asin, labels_label_id),
MODIFY cds_asin VARCHAR(12) NOT NULL,
MODIFY labels_label_id VARCHAR(9) NOT NULL;

-- #### tracks
ALTER TABLE tracks
ADD CONSTRAINT unique_trackname_cd UNIQUE (cds_asin, labels_label_id),
MODIFY name VARCHAR(9) NOT NULL,
MODIFY cds_asin VARCHAR(12) NOT NULL;

-- #### products_similars
ALTER TABLE products_similars
ADD CONSTRAINT unique_mapping_product_similarproduct UNIQUE (products_asin, similars_asin),
MODIFY products_asin VARCHAR(12) NOT NULL,
MODIFY similars_asin VARCHAR(12) NOT NULL;

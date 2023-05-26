-- Add NOT NULL and UNIQUE constraints

-- #### products
ALTER TABLE products
ADD CONSTRAINT unique_asin UNIQUE (asin),
ADD CONSTRAINT unique_upc UNIQUE (upc),
ADD CONSTRAINT unique_ean UNIQUE (ean),
ADD CONSTRAINT check_ean_length CHECK (length(ean) <= 14),
ADD CONSTRAINT check_upc_length CHECK (length(ean) <= 13);

ALTER TABLE products
ALTER COLUMN asin SET NOT NULL,
ALTER COLUMN ptitle SET NOT NULL,
ALTER COLUMN pgroup SET NOT NULL;

-- #### bankinfos
ALTER TABLE bankinfos
ADD CONSTRAINT unique_bankinfos_id UNIQUE (bankinfo_id);

ALTER TABLE bankinfos
ALTER COLUMN bankinfo_id SET NOT NULL,
ALTER COLUMN account_number SET NOT NULL;

-- #### users
ALTER TABLE users
ADD CONSTRAINT unique_username UNIQUE (username);

ALTER TABLE users
ALTER COLUMN username SET NOT NULL;

-- #### categories
ALTER TABLE categories
ADD CONSTRAINT unique_category_id UNIQUE (category_id),
ADD CONSTRAINT unique_name_parent_id UNIQUE (name, parent_category_id);

ALTER TABLE categories
ALTER COLUMN category_id SET NOT NULL,
ALTER COLUMN name SET NOT NULL;

-- #### creators
ALTER TABLE creators
ADD CONSTRAINT unique_creator_id UNIQUE (creator_id);

ALTER TABLE creators
ALTER COLUMN creator_id SET NOT NULL,
ALTER COLUMN name SET NOT NULL;

-- #### listmanialists
ALTER TABLE listmanialists
ADD CONSTRAINT unique_listmanialist_id UNIQUE (listmanialist_id);

ALTER TABLE listmanialists
ALTER COLUMN listmanialist_id SET NOT NULL,
ALTER COLUMN name SET NOT NULL;

-- #### shopaddresses
ALTER TABLE shopaddresses
ADD CONSTRAINT unique_address_id UNIQUE (shopaddress_id);

ALTER TABLE shopaddresses
ALTER COLUMN shopaddress_id SET NOT NULL,
ALTER COLUMN shops_shop_id SET NOT NULL,
ALTER COLUMN street SET NOT NULL,
ALTER COLUMN zip SET NOT NULL;

-- #### shops
ALTER TABLE shops
ADD CONSTRAINT unique_shop_id UNIQUE (shop_id);

ALTER TABLE shops
ALTER COLUMN shop_id SET NOT NULL,
ALTER COLUMN name SET NOT NULL;

-- #### priceinfos
ALTER TABLE priceinfos
ADD CONSTRAINT unique_priceinfo_id UNIQUE (priceinfo_id),
ADD CONSTRAINT unique_priceinfo_constellation UNIQUE (shops_shop_id, products_asin, multiplier, price, currency, state);

ALTER TABLE priceinfos
ALTER COLUMN priceinfo_id SET NOT NULL,
ALTER COLUMN products_asin SET NOT NULL,
ALTER COLUMN shops_shop_id SET NOT NULL;

-- #### userreviews
ALTER TABLE userreviews
ADD CONSTRAINT unique_userreview_id UNIQUE (userreview_id),
ADD CONSTRAINT unique_asin_username_in_userreviews UNIQUE (products_asin, users_username),
ADD CONSTRAINT check_rating_range CHECK (rating >= 0 AND rating <= 5);

ALTER TABLE userreviews
ALTER COLUMN products_asin SET NOT NULL,
ALTER COLUMN users_username SET NOT NULL,
ALTER COLUMN rating SET NOT NULL,
ALTER COLUMN helpful_votes SET NOT NULL,
ALTER COLUMN summary SET NOT NULL,
ALTER COLUMN content SET NOT NULL,
ALTER COLUMN review_date SET NOT NULL;

-- #### guestreviews
ALTER TABLE guestreviews
ADD CONSTRAINT unique_guestreview_id UNIQUE (guestreview_id),
ADD CONSTRAINT check_rating_range_guestreviews CHECK (rating >= 0 AND rating <= 5);

ALTER TABLE guestreviews
ALTER COLUMN guestreview_id SET NOT NULL,
ALTER COLUMN products_asin SET NOT NULL,
ALTER COLUMN rating SET NOT NULL,
ALTER COLUMN helpful_votes SET NOT NULL,
ALTER COLUMN summary SET NOT NULL,
ALTER COLUMN content SET NOT NULL,
ALTER COLUMN review_date SET NOT NULL;

-- #### purchases
ALTER TABLE purchases
ADD CONSTRAINT unique_purchase_id UNIQUE (purchase_id);

ALTER TABLE purchases
ALTER COLUMN purchase_id SET NOT NULL,
ALTER COLUMN products_asin SET NOT NULL,
ALTER COLUMN priceinfos_priceinfo_id SET NOT NULL,
ALTER COLUMN users_username SET NOT NULL;

-- #### deliveryaddresses
ALTER TABLE deliveryaddresses
ADD CONSTRAINT unique_deliveryaddress_id UNIQUE (deliveryaddress_id);

ALTER TABLE deliveryaddresses
ALTER COLUMN deliveryaddress_id SET NOT NULL,
ALTER COLUMN zip SET NOT NULL,
ALTER COLUMN street SET NOT NULL;

-- #### authors
ALTER TABLE authors
ADD CONSTRAINT unique_author_id UNIQUE (author_id);

ALTER TABLE authors
ALTER COLUMN author_id SET NOT NULL;

-- #### publishers
ALTER TABLE publishers
ADD CONSTRAINT unique_publisher_id UNIQUE (publisher_id);

ALTER TABLE publishers
ALTER COLUMN publisher_id SET NOT NULL;

-- #### actors
ALTER TABLE actors
ADD CONSTRAINT unique_actor_id UNIQUE (actor_id);

ALTER TABLE actors
ALTER COLUMN actor_id SET NOT NULL;

-- #### studios
ALTER TABLE studios
ADD CONSTRAINT unique_studio_id UNIQUE (studio_id);

ALTER TABLE studios
ALTER COLUMN studio_id SET NOT NULL;

-- #### audiotexts
ALTER TABLE audiotexts
ADD CONSTRAINT unique_audiotext_id UNIQUE (audiotext_id);

ALTER TABLE audiotexts
ALTER COLUMN audiotext_id SET NOT NULL;

-- #### labels
ALTER TABLE labels
ADD CONSTRAINT unique_label_id UNIQUE (label_id);

ALTER TABLE labels
ALTER COLUMN label_id SET NOT NULL;

-- #### dvdformats
ALTER TABLE dvdformats
ADD CONSTRAINT unique_dvdformat_id UNIQUE (dvdformat_id);

ALTER TABLE dvdformats
ALTER COLUMN dvdformat_id SET NOT NULL;

-- #### books
ALTER TABLE books
ADD CONSTRAINT unique_books_asin UNIQUE (asin);

ALTER TABLE books
ALTER COLUMN asin SET NOT NULL;

-- #### cds
ALTER TABLE cds
ADD CONSTRAINT unique_cds_asin UNIQUE (asin);

ALTER TABLE cds
ALTER COLUMN asin SET NOT NULL;

-- #### dvds
ALTER TABLE dvds
ADD CONSTRAINT unique_dvds_asin UNIQUE (asin);

ALTER TABLE dvds
ALTER COLUMN asin SET NOT NULL;

-- #### junction_books_authors
ALTER TABLE junction_books_authors
ADD CONSTRAINT unique_junction_books_authors UNIQUE (books_asin, authors_author_id);

ALTER TABLE junction_books_authors
ALTER COLUMN books_asin SET NOT NULL,
ALTER COLUMN authors_author_id SET NOT NULL;

-- #### junction_books_publishers
ALTER TABLE junction_books_publishers
ADD CONSTRAINT unique_junction_books_publishers UNIQUE (books_asin, publishers_publisher_id);

ALTER TABLE junction_books_publishers
ALTER COLUMN books_asin SET NOT NULL,
ALTER COLUMN publishers_publisher_id SET NOT NULL;

-- #### junction_dvds_actors
ALTER TABLE junction_dvds_actors
ADD CONSTRAINT unique_junction_dvds_actors UNIQUE (dvds_asin, actors_actor_id);

ALTER TABLE junction_dvds_actors
ALTER COLUMN dvds_asin SET NOT NULL,
ALTER COLUMN actors_actor_id SET NOT NULL;

-- #### junction_dvds_studios
ALTER TABLE junction_dvds_studios
ADD CONSTRAINT unique_junction_dvds_studios UNIQUE (dvds_asin, studios_studio_id);

ALTER TABLE junction_dvds_studios
ALTER COLUMN dvds_asin SET NOT NULL,
ALTER COLUMN studios_studio_id SET NOT NULL;

-- #### junction_dvds_dvdformats
ALTER TABLE junction_dvds_dvdformats
ADD CONSTRAINT unique_junction_dvds_formats UNIQUE (dvds_asin, dvdformats_dvdformat_id);

ALTER TABLE junction_dvds_dvdformats
ALTER COLUMN dvds_asin SET NOT NULL,
ALTER COLUMN dvdformats_dvdformat_id SET NOT NULL;

-- #### junction_dvds_audiotexts
ALTER TABLE junction_dvds_audiotexts
ADD CONSTRAINT unique_junction_dvds_audiotexts UNIQUE (dvds_asin, audiotexts_audiotext_id);

ALTER TABLE junction_dvds_audiotexts
ALTER COLUMN dvds_asin SET NOT NULL,
ALTER COLUMN audiotexts_audiotext_id SET NOT NULL;

-- #### junction_cds_labels
ALTER TABLE junction_cds_labels
ADD CONSTRAINT unique_junction_cds_labels UNIQUE (cds_asin, labels_label_id);

ALTER TABLE junction_cds_labels
ALTER COLUMN cds_asin SET NOT NULL,
ALTER COLUMN labels_label_id SET NOT NULL;

-- #### tracks
ALTER TABLE tracks
ADD CONSTRAINT unique_track_id UNIQUE (track_id),
ADD CONSTRAINT unique_trackname_cd UNIQUE (name, cds_asin);

ALTER TABLE tracks
ALTER COLUMN name SET NOT NULL,
ALTER COLUMN cds_asin SET NOT NULL;

-- #### products_similars
ALTER TABLE products_similars
ADD CONSTRAINT unique_junction_product_similarproduct UNIQUE (products_asin, similar_product_asin);

ALTER TABLE products_similars
ALTER COLUMN products_asin SET NOT NULL,
ALTER COLUMN similar_product_asin SET NOT NULL;
CREATE TABLE products (
  asin VARCHAR(12) NOT NULL,
  ptitle VARCHAR(255) NOT NULL,
  pgroup VARCHAR(5) NOT NULL,
  ean VARCHAR(14),
  image_url TEXT,
  detailpage_url TEXT,
  salesrank INTEGER,
  upc VARCHAR(13),
  PRIMARY KEY (asin),
  CONSTRAINT unique_upc UNIQUE (upc),
  CONSTRAINT unique_ean UNIQUE (ean)
);

CREATE TABLE users (
  username VARCHAR(30) NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE bankinfos (
  bankinfo_id VARCHAR(9) NOT NULL,
  users_username VARCHAR(9),
  account_number VARCHAR(255) NOT NULL,
  PRIMARY KEY (bankinfo_id),
  CONSTRAINT fk_bankinfos_users FOREIGN KEY (users_username) REFERENCES users(username)
);

CREATE TABLE categories (
  category_id VARCHAR(9) NOT NULL,
  parent_category_id VARCHAR(9),
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (category_id),
  CONSTRAINT unique_name_parent_id UNIQUE (name, parent_category_id)
);


CREATE TABLE creators (
  creator_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (creator_id)
);

CREATE TABLE listmanialists (
  listmanialist_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (listmanialist_id)
);

CREATE TABLE shops (
  shop_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (shop_id)
);

CREATE TABLE shopaddresses (
  shopaddress_id VARCHAR(9) NOT NULL,
  shops_shop_id VARCHAR(9) NOT NULL,
  street VARCHAR(255) NOT NULL,
  zip VARCHAR(10) NOT NULL,
  PRIMARY KEY (shopaddress_id),
  CONSTRAINT fk_shopaddresses_shops FOREIGN KEY (shops_shop_id) REFERENCES shops(shop_id)
);

CREATE TABLE priceinfos (
  priceinfo_id VARCHAR(9) NOT NULL,
  products_asin VARCHAR(12) NOT NULL,
  shops_shop_id VARCHAR(9) NOT NULL,
  price FLOAT(2),
  multiplier FLOAT(2),
  currency VARCHAR(9),
  state VARCHAR(15),
  PRIMARY KEY (priceinfo_id),
  CONSTRAINT fk_priceinfos_products FOREIGN KEY (products_asin) REFERENCES products(asin),
  CONSTRAINT fk_priceinfos_shops FOREIGN KEY (shops_shop_id) REFERENCES shops(shop_id),
  CONSTRAINT unique_priceinfo_constellation UNIQUE (shops_shop_id, products_asin, multiplier, price, currency, state)
);

CREATE TABLE userreviews (
  userreview_id VARCHAR(9) NOT NULL,
  products_asin VARCHAR(12) NOT NULL,
  users_username VARCHAR(30) NOT NULL,
  rating INTEGER NOT NULL,
  helpful_votes INTEGER NOT NULL,
  summary TEXT NOT NULL,
  content TEXT NOT NULL,
  review_date DATE NOT NULL,
  PRIMARY KEY (userreview_id),
  CONSTRAINT fk_userreviews_products FOREIGN KEY (products_asin) REFERENCES products(asin),
  CONSTRAINT fk_userreviews_users FOREIGN KEY (users_username) REFERENCES users(username),
  CONSTRAINT unique_asin_username_in_userreviews UNIQUE (products_asin, users_username),
  CONSTRAINT check_rating_range CHECK (rating >= 0 AND rating <= 5),
  -- Must be younger than Amazon Foundry.
  CONSTRAINT check_review_date CHECK (review_date > '1994-05-07')
);

CREATE TABLE guestreviews (
  guestreview_id VARCHAR(9) NOT NULL,
  products_asin VARCHAR(12) NOT NULL,
  rating INTEGER NOT NULL,
  helpful_votes INTEGER NOT NULL,
  summary TEXT NOT NULL,
  content TEXT NOT NULL,
  review_date DATE NOT NULL,
  PRIMARY KEY (guestreview_id),
  CONSTRAINT check_rating_range_guestreviews CHECK (rating >= 0 AND rating <= 5),
  CONSTRAINT check_review_date CHECK (review_date > '1994-05-07'),
  CONSTRAINT fk_guestreviews_products FOREIGN KEY (products_asin) REFERENCES products(asin)
);

CREATE TABLE purchases (
  purchase_id VARCHAR(12) NOT NULL,
  products_asin VARCHAR(12) NOT NULL,
  priceinfos_priceinfo_id VARCHAR(9) NOT NULL,
  users_username VARCHAR(30) NOT NULL,
  purchase_date DATE NOT NULL,
  PRIMARY KEY (purchase_id),
  CONSTRAINT fk_purchases_products FOREIGN KEY (products_asin) REFERENCES products(asin),
  CONSTRAINT fk_purchases_priceinfos FOREIGN KEY (priceinfos_priceinfo_id) REFERENCES priceinfos(priceinfo_id),
  CONSTRAINT fk_purchases_users FOREIGN KEY (users_username) REFERENCES users(username)
);

CREATE TABLE deliveryaddresses (
  deliveryaddress_id VARCHAR(9) NOT NULL,
  street VARCHAR(255) NOT NULL,
  zip VARCHAR(255) NOT NULL,
  PRIMARY KEY (deliveryaddress_id)
);

CREATE TABLE authors (
  author_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (author_id)
);

CREATE TABLE publishers (
  publisher_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (publisher_id)
);

CREATE TABLE actors (
  actor_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (actor_id)
);

CREATE TABLE studios (
  studio_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (studio_id)
);

CREATE TABLE audiotexts (
  audiotext_id VARCHAR(9) NOT NULL,
  name VARCHAR(255),
  language VARCHAR(30),
  language_type VARCHAR(50),
  audio_format VARCHAR(50),
  PRIMARY KEY (audiotext_id)
);

CREATE TABLE labels (
  label_id VARCHAR(9),
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (label_id)
);

CREATE TABLE dvdformats (
  dvdformat_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (dvdformat_id)
);

CREATE TABLE dvds (
  asin VARCHAR(12) NOT NULL,
  aspect_ratio VARCHAR(7),
  running_time INTEGER,
  theatr_release DATE,
  release_date DATE,
  region_code VARCHAR(3),
  PRIMARY KEY (asin),
  CONSTRAINT fk_dvds_products_asin FOREIGN KEY (asin) REFERENCES products(asin)
);

CREATE TABLE books (
  asin VARCHAR(12) NOT NULL,
  isbn VARCHAR(13),
  edition VARCHAR(255),
  binding VARCHAR(255),
  weight FLOAT(2),
  height FLOAT(2),
  length FLOAT(2),
  pages INTEGER,
  publication_date DATE,
  PRIMARY KEY (asin),
  CONSTRAINT fk_books_products_asin FOREIGN KEY (asin) REFERENCES products(asin)
);

CREATE TABLE cds (
  asin VARCHAR(12) NOT NULL,
  binding VARCHAR(20),
  cd_format VARCHAR(50),
  num_discs INTEGER,
  release_date DATE,
  PRIMARY KEY (asin),
  CONSTRAINT fk_cds_products_asin FOREIGN KEY (asin) REFERENCES products(asin)
);

CREATE TABLE junction_cds_labels (
  cds_asin VARCHAR(12),
  labels_label_id VARCHAR(9),
  PRIMARY KEY (cds_asin, labels_label_id),
  CONSTRAINT fk_junction_cds_labels_cds FOREIGN KEY (cds_asin) REFERENCES cds(asin),
  CONSTRAINT fk_junction_cds_labels_labels FOREIGN KEY (labels_label_id) REFERENCES labels(label_id)
);

CREATE TABLE junction_books_authors (
  books_asin VARCHAR(12) NOT NULL,
  authors_author_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (books_asin, authors_author_id),
  CONSTRAINT fk_junction_books_authors_books FOREIGN KEY (books_asin) REFERENCES books(asin),
  CONSTRAINT fk_junction_books_authors_authors FOREIGN KEY (authors_author_id) REFERENCES authors(author_id)
);

CREATE TABLE junction_books_publishers (
  books_asin VARCHAR(12) NOT NULL,
  publishers_publisher_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (books_asin, publishers_publisher_id),
  CONSTRAINT fk_junction_books_publishers_books FOREIGN KEY (books_asin) REFERENCES books(asin),
  CONSTRAINT fk_junction_books_publishers_publishers FOREIGN KEY (publishers_publisher_id) REFERENCES publishers(publisher_id)
);

CREATE TABLE junction_dvds_actors (
  dvds_asin VARCHAR(12) NOT NULL,
  actors_actor_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (dvds_asin, actors_actor_id),
  CONSTRAINT fk_junction_dvds_actors_dvds FOREIGN KEY (dvds_asin) REFERENCES dvds(asin),
  CONSTRAINT fk_junction_dvds_actors_actors FOREIGN KEY (actors_actor_id) REFERENCES actors(actor_id)
);

CREATE TABLE junction_dvds_studios (
  dvds_asin VARCHAR(12) NOT NULL,
  studios_studio_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (dvds_asin, studios_studio_id),
  CONSTRAINT fk_junction_dvds_studios_dvds FOREIGN KEY (dvds_asin) REFERENCES dvds(asin),
  CONSTRAINT fk_junction_dvds_studios_studios FOREIGN KEY (studios_studio_id) REFERENCES studios(studio_id)
);

CREATE TABLE junction_dvds_audiotexts (
  dvds_asin VARCHAR(12) NOT NULL,
  audiotexts_audiotext_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (dvds_asin, audiotexts_audiotext_id),
  CONSTRAINT fk_junction_dvds_audiotexts_dvds FOREIGN KEY (dvds_asin) REFERENCES dvds(asin),
  CONSTRAINT fk_junction_dvds_audiotexts_audiotexts FOREIGN KEY (audiotexts_audiotext_id) REFERENCES audiotexts(audiotext_id)
);

CREATE TABLE junction_dvds_dvdformats (
  dvds_asin VARCHAR(12) NOT NULL,
  dvdformats_dvdformat_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (dvds_asin, dvdformats_dvdformat_id),
  CONSTRAINT fk_junction_dvds_dvdformats_dvds FOREIGN KEY (dvds_asin) REFERENCES dvds(asin),
  CONSTRAINT fk_junction_dvds_dvdformats_dvdformats FOREIGN KEY (dvdformats_dvdformat_id) REFERENCES dvdformats(dvdformat_id)
);

CREATE TABLE tracks (
  track_id VARCHAR(9) NOT NULL,
  cds_asin VARCHAR(12) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (track_id),
  CONSTRAINT fk_tracks_cds FOREIGN KEY (cds_asin) REFERENCES cds(asin),
  CONSTRAINT unique_trackname_cd UNIQUE (name, cds_asin)
);

CREATE TABLE products_similars (
  products_asin VARCHAR(12) NOT NULL,
  similar_product_asin VARCHAR(12) NOT NULL,
  PRIMARY KEY (products_asin, similar_product_asin),
  CONSTRAINT fk_products_similars_products FOREIGN KEY (products_asin) REFERENCES products(asin),
  CONSTRAINT fk_products_similars_similar_product FOREIGN KEY (similar_product_asin) REFERENCES products(asin)
);

CREATE TABLE junction_products_categories (
  products_asin VARCHAR(12) NOT NULL,
  categories_category_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (products_asin, categories_category_id),
  CONSTRAINT fk_junction_products_categories_products FOREIGN KEY (products_asin) REFERENCES products(asin),
  CONSTRAINT fk_junction_products_categories_categories FOREIGN KEY (categories_category_id) REFERENCES categories(category_id)
);

CREATE TABLE junction_products_listmanialists (
  products_asin VARCHAR(12) NOT NULL,
  listmanialists_listmanialist_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (products_asin, listmanialists_listmanialist_id),
  CONSTRAINT fk_junction_products_listmanialists_products FOREIGN KEY (products_asin) REFERENCES products(asin),
  CONSTRAINT fk_junction_products_listmanialists_listmanialists FOREIGN KEY (listmanialists_listmanialist_id) REFERENCES listmanialists(listmanialist_id)
);

CREATE TABLE junction_products_creators (
  products_asin VARCHAR(12) NOT NULL,
  creators_creator_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (products_asin, creators_creator_id),
  CONSTRAINT fk_junction_products_creators_products FOREIGN KEY (products_asin) REFERENCES products(asin),
  CONSTRAINT fk_junction_products_creators_creators FOREIGN KEY (creators_creator_id) REFERENCES creators(creator_id)
);

CREATE TABLE junction_users_deliveryaddresses (
  users_username VARCHAR(30) NOT NULL,
  deliveryaddresses_deliveryaddress_id VARCHAR(9) NOT NULL,
  PRIMARY KEY (users_username, deliveryaddresses_deliveryaddress_id),
  CONSTRAINT fk_junction_users_deliveryaddresses_users FOREIGN KEY (users_username) REFERENCES users(username),
  CONSTRAINT fk_junction_users_deliveryaddresses_deliveryaddresses FOREIGN KEY (deliveryaddresses_deliveryaddress_id) REFERENCES deliveryaddresses(deliveryaddress_id)
);
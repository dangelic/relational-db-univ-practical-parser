CREATE TABLE products (
  asin VARCHAR(12) ,
  ptitle VARCHAR(255) ,
  pgroup VARCHAR(5) ,
  ean VARCHAR(13),
  image_url TEXT,
  detailpage_url TEXT,
  salesrank INTEGER,
  upc VARCHAR(12),
  PRIMARY KEY (asin)
);

CREATE TABLE bankinfos (
  bankinfo_id VARCHAR(9) ,
  account_number VARCHAR(255) ,
  PRIMARY KEY (bankinfo_id)
);

CREATE TABLE users (
  username VARCHAR(30) ,
  banks_bankinfo_id VARCHAR(9)  REFERENCES bankinfos(bankinfo_id),
  PRIMARY KEY (username)
);

CREATE TABLE categories (
  category_id VARCHAR(9) ,
  parent_category_id VARCHAR(9) REFERENCES categories(category_id),
  name VARCHAR(255) ,
  PRIMARY KEY (category_id)
);

CREATE TABLE creators (
  creator_id VARCHAR(9) ,
  name VARCHAR(255) ,
  PRIMARY KEY (creator_id)
);

CREATE TABLE listmanialists (
  listmanialist_id VARCHAR(9) ,
  name VARCHAR(255) ,
  PRIMARY KEY (listmanialist_id)
);



CREATE TABLE storeaddresses (
  storeaddress_id VARCHAR(9) ,
  street VARCHAR(255) ,
  zip VARCHAR(255) ,
  PRIMARY KEY (storeaddress_id)
);

CREATE TABLE stores (
  store_id VARCHAR(9) ,
  storeaddresses_storeaddress_id VARCHAR(9)  REFERENCES storeaddresses(storeaddress_id),
  name VARCHAR(255) ,
  PRIMARY KEY (store_id)
);

CREATE TABLE priceinfos (
  priceinfo_id VARCHAR(9),
  price FLOAT(2),
  multiplier FLOAT(2),
  currency VARCHAR(9),
  state VARCHAR(15),
  PRIMARY KEY (priceinfo_id)
);

CREATE TABLE junction_products_priceinfos_stores (
  products_asin VARCHAR(12)  REFERENCES products(asin),
  priceinfos_priceinfo_id VARCHAR(9)  REFERENCES priceinfos(priceinfo_id),
  stores_store_id VARCHAR(9)  REFERENCES stores(store_id),
  PRIMARY KEY (products_asin, priceinfos_priceinfo_id, stores_store_id)
);


CREATE TABLE userreviews (
  products_asin VARCHAR(12)  REFERENCES products(asin),
  users_username VARCHAR(30)  REFERENCES users(username),
  rating INTEGER,
  helpful_votes INTEGER,
  summary TEXT,
  content TEXT,
  review_date DATE,
  PRIMARY KEY (products_asin, users_username)
);

CREATE TABLE guestreviews (
  guestreview_id VARCHAR(9),
  products_asin VARCHAR(12)  REFERENCES products(asin),
  rating INTEGER,
  helpful_votes INTEGER,
  summary TEXT,
  content TEXT,
  review_date DATE,
  PRIMARY KEY (guestreview_id)
);

CREATE TABLE purchases (
  purchase_id VARCHAR(12),
  products_asin VARCHAR(12),
  priceinfos_priceinfo_id VARCHAR(9),
  stores_store_id VARCHAR(9),
  users_username VARCHAR(30)  REFERENCES users(username),
  purchase_date DATE,
  FOREIGN KEY (products_asin, priceinfos_priceinfo_id, stores_store_id) REFERENCES junction_products_priceinfos_stores(products_asin, priceinfos_priceinfo_id, stores_store_id),
  PRIMARY KEY (purchase_id)
);


CREATE TABLE deliveryaddresses (
  deliveryaddress_id VARCHAR(9) ,
  street VARCHAR(255),
  zip VARCHAR(255),
  PRIMARY KEY (deliveryaddress_id)
);



CREATE TABLE authors (
  author_id VARCHAR(9) ,
  name VARCHAR(255),
  PRIMARY KEY (author_id)
);

CREATE TABLE publishers (
  publisher_id VARCHAR(9) ,
  name VARCHAR(255),
  PRIMARY KEY (publisher_id)
);

CREATE TABLE actors (
  actor_id VARCHAR(9) ,
  name VARCHAR(255),
  PRIMARY KEY (actor_id)
);

CREATE TABLE studios (
  studio_id VARCHAR(9) ,
  name VARCHAR(255),
  PRIMARY KEY (studio_id)
);

CREATE TABLE audiotexts (
  audiotext_id VARCHAR(9) ,
  name VARCHAR(255),
  language VARCHAR(30),
  language_type VARCHAR(50),
  audio_format VARCHAR(50),
  PRIMARY KEY (audiotext_id)
);

CREATE TABLE labels (
  label_id VARCHAR(9) ,
  name VARCHAR(255),
  PRIMARY KEY (label_id)
);

CREATE TABLE dvdformats (
  dvdformat_id VARCHAR(9) ,
  name VARCHAR(255),
  PRIMARY KEY (dvdformat_id)
);

CREATE TABLE books (
  asin VARCHAR(12)  REFERENCES products(asin),
  isbn VARCHAR(13),
  edition VARCHAR(255),
  binding VARCHAR(255),
  weight FLOAT(2),
  height FLOAT(2),
  length FLOAT(2),
  pages INTEGER,
  publication_date DATE,
  PRIMARY KEY (asin)
);

CREATE TABLE cds (
  asin VARCHAR(12)  REFERENCES products(asin),
  binding VARCHAR(20),
  cd_format VARCHAR(20),
  num_discs INTEGER,
  release_date DATE,
  PRIMARY KEY (asin)
);

CREATE TABLE junction_cds_labels (
  cds_asin VARCHAR(12)  REFERENCES cds(asin),
  labels_label_id VARCHAR(9)  REFERENCES labels(label_id),
  PRIMARY KEY (cds_asin, labels_label_id)
);

CREATE TABLE dvds (
  asin VARCHAR(12)  REFERENCES products(asin),
  dvd_format VARCHAR(30),
  apsect_ratio VARCHAR(7),
  running_time INTEGER,
  theatr_release DATE,
  release_date DATE,
  region_code VARCHAR(3),
  PRIMARY KEY (asin)
);

CREATE TABLE junction_books_authors (
  books_asin VARCHAR(12)  REFERENCES books(asin),
  authors_author_id VARCHAR(9)  REFERENCES authors(author_id),
  PRIMARY KEY (books_asin, authors_author_id)
);

CREATE TABLE junction_books_publishers (
  books_asin VARCHAR(12)  REFERENCES books(asin),
  publishers_publisher_id VARCHAR(9)  REFERENCES publishers(publisher_id),
  PRIMARY KEY (books_asin, publishers_publisher_id)
);

CREATE TABLE junction_dvds_actors (
  dvds_asin VARCHAR(12)  REFERENCES dvds(asin),
  actors_actor_id VARCHAR(9)  REFERENCES actors(actor_id),
  PRIMARY KEY (dvds_asin, actors_actor_id)
);

CREATE TABLE junction_dvds_studios (
  dvds_asin VARCHAR(12)  REFERENCES dvds(asin),
  studios_studio_id VARCHAR(9)  REFERENCES studios(studio_id),
  PRIMARY KEY (dvds_asin, studios_studio_id)
);

CREATE TABLE junction_dvds_audiotexts (
  dvds_asin VARCHAR(12)  REFERENCES dvds(asin),
  audiotexts_audiotext_id VARCHAR(9)  REFERENCES audiotexts(audiotext_id),
  PRIMARY KEY (dvds_asin, audiotexts_audiotext_id)
);

CREATE TABLE junction_dvds_dvdformats (
  dvds_asin VARCHAR(12)  REFERENCES dvds(asin),
  dvdformats_dvdformat_id VARCHAR(9)  REFERENCES dvdformats(dvdformat_id),
  PRIMARY KEY (dvds_asin, dvdformats_dvdformat_id)
);


CREATE TABLE tracks (
  cds_asin VARCHAR(12)  REFERENCES cds(asin),
  name VARCHAR(255),
  PRIMARY KEY (cds_asin, name)
);


CREATE TABLE products_similars (
  products_asin VARCHAR(12)  REFERENCES products(asin),
  similar_product_asin VARCHAR(12)  REFERENCES products(asin),
  PRIMARY KEY (products_asin, similar_product_asin)
);

CREATE TABLE junction_products_categories (
  products_asin VARCHAR(12)  REFERENCES products(asin),
  categories_category_id VARCHAR(9)  REFERENCES categories(category_id),
  PRIMARY KEY (products_asin, categories_category_id)
);

CREATE TABLE junction_products_listmanialists (
  products_asin VARCHAR(12)  REFERENCES products(asin),
  listmanialists_listmanialist_id VARCHAR(9)  REFERENCES listmanialists(listmanialist_id),
  PRIMARY KEY (products_asin, listmanialists_listmanialist_id)
);

CREATE TABLE junction_products_creators (
  products_asin VARCHAR(12)  REFERENCES products(asin),
  creators_creator_id VARCHAR(9)  REFERENCES creators(creator_id),
  PRIMARY KEY (products_asin, creators_creator_id)
);

CREATE TABLE junction_users_deliveryaddresses (
  users_username VARCHAR(30)  REFERENCES users(username),
  deliveryaddresses_deliveryaddress_id VARCHAR(9)  REFERENCES deliveryaddresses(deliveryaddress_id),
  PRIMARY KEY (users_username, deliveryaddresses_deliveryaddress_id)
);
CREATE TABLE products (
  asin VARCHAR(12) NOT NULL,
  ptitle VARCHAR(255) NOT NULL,
  pgroup VARCHAR(5) NOT NULL,
  ean VARCHAR(13),
  image_url TEXT,
  detailpage_url TEXT,
  salesrank INTEGER,
  upc VARCHAR(12),
  PRIMARY KEY (asin)
);

CREATE TABLE bankinfos (
  bankinfo_id VARCHAR(10) NOT NULL,
  account_number VARCHAR(255) NOT NULL,
  PRIMARY KEY (bankinfo_id)
);

CREATE TABLE customers (
  name VARCHAR(30) NOT NULL,
  banks_bankinfo_id VARCHAR(10) NOT NULL REFERENCES bankinfos(bankinfo_id),
  PRIMARY KEY (name)
);

CREATE TABLE categories (
  category_id VARCHAR(10) NOT NULL,
  parent_category_id VARCHAR(10) REFERENCES categories(category_id),
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (category_id)
);

CREATE TABLE creators (
  creator_id VARCHAR(10) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (creator_id)
);

CREATE TABLE listmanialists (
  listmanialist_id VARCHAR(10) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (listmanialist_id)
);



CREATE TABLE storeaddresses (
  address_id VARCHAR(10) NOT NULL,
  street VARCHAR(255) NOT NULL,
  zip VARCHAR(255) NOT NULL,
  PRIMARY KEY (address_id)
);

CREATE TABLE stores (
  store_id VARCHAR(10) NOT NULL,
  addresses_address_id VARCHAR(10) NOT NULL REFERENCES storeaddresses(address_id),
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (store_id)
);

CREATE TABLE priceinfos (
  priceinfo_id VARCHAR(10) NOT NULL,
  price FLOAT(2),
  multiplier FLOAT(2),
  currency VARCHAR(10),
  state VARCHAR(15),
  PRIMARY KEY (priceinfo_id)
);

CREATE TABLE map_products_priceinfos_stores (
  products_asin VARCHAR(12) NOT NULL REFERENCES products(asin),
  priceinfos_priceinfo_id VARCHAR(10) NOT NULL REFERENCES priceinfos(priceinfo_id),
  stores_store_id VARCHAR(10) NOT NULL REFERENCES stores(store_id),
  PRIMARY KEY (products_asin, priceinfos_priceinfo_id, stores_store_id)
);


CREATE TABLE reviews (
  products_asin VARCHAR(12) NOT NULL REFERENCES products(asin),
  customers_name VARCHAR(30) NOT NULL REFERENCES customers(name),
  rating VARCHAR(12) NOT NULL,
  helpful_rating VARCHAR(255) NOT NULL,
  summary TEXT NOT NULL,
  content TEXT NOT NULL,
  review_date DATE NOT NULL
);

CREATE TABLE purchases (
  purchase_id VARCHAR(12) PRIMARY KEY,
  products_asin VARCHAR(12) NOT NULL,
  priceinfos_priceinfo_id VARCHAR(10) NOT NULL,
  stores_store_id VARCHAR(10) NOT NULL,
  customers_name VARCHAR(30) NOT NULL REFERENCES customers(name),
  purchase_date DATE NOT NULL,

  FOREIGN KEY (products_asin, priceinfos_priceinfo_id, stores_store_id) REFERENCES map_products_priceinfos_stores(products_asin, priceinfos_priceinfo_id, stores_store_id)
);


CREATE TABLE deliveraddresses (
  deliveraddress_id VARCHAR(10) NOT NULL,
  PRIMARY KEY (deliveraddress_id)
);



CREATE TABLE authors (
  author_id VARCHAR(10) NOT NULL,
  name VARCHAR(255),
  PRIMARY KEY (author_id)
);

CREATE TABLE publishers (
  publisher_id VARCHAR(10) NOT NULL,
  name VARCHAR(255),
  PRIMARY KEY (publisher_id)
);

CREATE TABLE actors (
  actor_id VARCHAR(10) NOT NULL,
  name VARCHAR(255),
  PRIMARY KEY (actor_id)
);

CREATE TABLE studios (
  studio_id VARCHAR(10) NOT NULL,
  name VARCHAR(255),
  PRIMARY KEY (studio_id)
);

CREATE TABLE audiotexts (
  audiotext_id VARCHAR(10) NOT NULL,
  name VARCHAR(255),
  language VARCHAR(30),
  language_type VARCHAR(50),
  audio_format VARCHAR(50),
  PRIMARY KEY (audiotext_id)
);

CREATE TABLE labels (
  label_id VARCHAR(10) NOT NULL,
  name VARCHAR(255),
  PRIMARY KEY (label_id)
);

CREATE TABLE books (
  asin VARCHAR(12) NOT NULL REFERENCES products(asin),
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
  asin VARCHAR(12) NOT NULL REFERENCES products(asin),
  binding VARCHAR(20),
  cd_format VARCHAR(20),
  num_discs INTEGER,
  release_date DATE,
  PRIMARY KEY (asin)
);

CREATE TABLE dvds (
  asin VARCHAR(12) NOT NULL REFERENCES products(asin),
  dvd_format VARCHAR(30),
  apsect_ratio VARCHAR(7),
  running_time INTEGER,
  theatr_release DATE,
  release_date DATE,
  region_code VARCHAR(3),
  PRIMARY KEY (asin)
);

CREATE TABLE map_books_authors (
  books_asin VARCHAR(12) NOT NULL REFERENCES books(asin),
  authors_author_id VARCHAR(10) NOT NULL REFERENCES authors(author_id),
  PRIMARY KEY (books_asin, authors_author_id)
);

CREATE TABLE map_books_publishers (
  books_asin VARCHAR(12) NOT NULL REFERENCES books(asin),
  publishers_publisher_id VARCHAR(10) NOT NULL REFERENCES publishers(publisher_id),
  PRIMARY KEY (books_asin, publishers_publisher_id)
);

CREATE TABLE map_dvds_actors (
  dvds_asin VARCHAR(12) NOT NULL REFERENCES dvds(asin),
  actors_actor_id VARCHAR(10) NOT NULL REFERENCES actors(actor_id),
  PRIMARY KEY (dvds_asin, actors_actor_id)
);

CREATE TABLE map_dvds_studios (
  dvds_asin VARCHAR(12) NOT NULL REFERENCES dvds(asin),
  studios_studio_id VARCHAR(10) NOT NULL REFERENCES studios(studio_id),
  PRIMARY KEY (dvds_asin, studios_studio_id)
);

CREATE TABLE map_dvds_audiotexts (
  dvds_asin VARCHAR(12) NOT NULL REFERENCES dvds(asin),
  audiotexts_audiotext_id VARCHAR(10) NOT NULL REFERENCES audiotexts(audiotext_id),
  PRIMARY KEY (dvds_asin, audiotexts_audiotext_id)
);

CREATE TABLE map_dvds_labels (
  dvds_asin VARCHAR(12) NOT NULL REFERENCES dvds(asin),
  labels_label_id VARCHAR(10) NOT NULL REFERENCES labels(label_id),
  PRIMARY KEY (dvds_asin, labels_label_id)
);

CREATE TABLE tracks (
  track_id VARCHAR(10) NOT NULL,
  cds_asin VARCHAR(12) NOT NULL REFERENCES cds(asin),
  name VARCHAR(255),
  PRIMARY KEY (track_id)
);





CREATE TABLE products_similars (
  products_asin VARCHAR(12) NOT NULL REFERENCES products(asin),
  similar_product_asin VARCHAR(12) NOT NULL REFERENCES products(asin),
  PRIMARY KEY (products_asin, similar_product_asin)
);

CREATE TABLE map_products_categories (
  products_asin VARCHAR(12) NOT NULL REFERENCES products(asin),
  categories_category_id VARCHAR(10) NOT NULL REFERENCES categories(category_id),
  PRIMARY KEY (products_asin, categories_category_id)
);

CREATE TABLE map_products_listmanialists (
  products_asin VARCHAR(12) NOT NULL REFERENCES products(asin),
  listmanialists_listmanialist_id VARCHAR(10) NOT NULL REFERENCES listmanialists(listmanialist_id),
  PRIMARY KEY (products_asin, listmanialists_listmanialist_id)
);

CREATE TABLE map_products_creators (
  products_asin VARCHAR(12) NOT NULL REFERENCES products(asin),
  creators_creator_id VARCHAR(10) NOT NULL REFERENCES creators(creator_id),
  PRIMARY KEY (products_asin, creators_creator_id)
);

CREATE TABLE map_customers_deliveraddresses (
  customers_name VARCHAR(30) NOT NULL REFERENCES customers(name),
  deliveraddresses_deliveraddress_id VARCHAR(10) NOT NULL REFERENCES deliveraddresses(deliveraddress_id),
  PRIMARY KEY (customers_name, deliveraddresses_deliveraddress_id)
);
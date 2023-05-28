

CREATE TABLE products (
  asin VARCHAR(12) NOT NULL,
  ptitle VARCHAR(255) NOT NULL,
  pgroup VARCHAR(5) NOT NULL,
  ean VARCHAR(255),
  image_url TEXT,
  detailpage_url TEXT,
  salesrank INTEGER,
  upc VARCHAR(255),
  PRIMARY KEY (asin),
  CONSTRAINT unique_asin UNIQUE (asin),
  CONSTRAINT unique_upc UNIQUE (upc),
  CONSTRAINT unique_ean UNIQUE (ean),
  CONSTRAINT check_ean_length CHECK (length(ean) <= 14),
  CONSTRAINT check_upc_length CHECK (length(upc) <= 13)
);

CREATE TABLE bankinfos (
  bankinfo_id VARCHAR(9) NOT NULL,
  account_number VARCHAR(255) NOT NULL,
  PRIMARY KEY (bankinfo_id),
  CONSTRAINT unique_bankinfo_id UNIQUE (bankinfo_id)
);

CREATE TABLE users (
  username VARCHAR(30) NOT NULL,
  banks_bankinfo_id VARCHAR(9) REFERENCES bankinfos(bankinfo_id),
  PRIMARY KEY (username),
  CONSTRAINT unique_username UNIQUE (username)
);


CREATE TABLE categories (
  category_id VARCHAR(9) NOT NULL,
  parent_category_id VARCHAR(9),
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (category_id),
  CONSTRAINT unique_category_id UNIQUE (category_id),
  CONSTRAINT unique_name_parent_id UNIQUE (name, parent_category_id)
);


CREATE TABLE creators (
  creator_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (creator_id),
  CONSTRAINT unique_creator_id UNIQUE (creator_id)
);

CREATE TABLE listmanialists (
  listmanialist_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (listmanialist_id),
  CONSTRAINT unique_listmanialist_id UNIQUE (listmanialist_id)
);

CREATE TABLE shops (
  shop_id VARCHAR(9) NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (shop_id),
  CONSTRAINT unique_shop_id UNIQUE (shop_id)
);

CREATE TABLE shopaddresses (
  shopaddress_id VARCHAR(9) ,
  shops_shop_id VARCHAR(9) REFERENCES shops(shop_id),
  street VARCHAR(255) ,
  zip VARCHAR(255) ,
  PRIMARY KEY (shopaddress_id)
);

--------- HERE :-)

CREATE TABLE priceinfos (
  priceinfo_id VARCHAR(9),
  products_asin VARCHAR(12)  REFERENCES products(asin),
  shops_shop_id VARCHAR(9)  REFERENCES shops(shop_id),
  price FLOAT(2),
  multiplier FLOAT(2),
  currency VARCHAR(9),
  state VARCHAR(15),
  PRIMARY KEY (priceinfo_id)
);

CREATE TABLE userreviews (
  userreview_id VARCHAR(9),
  products_asin VARCHAR(12)  REFERENCES products(asin),
  users_username VARCHAR(30)  REFERENCES users(username),
  rating INTEGER,
  helpful_votes INTEGER,
  summary TEXT,
  content TEXT,
  review_date DATE,
  PRIMARY KEY (userreview_id)
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
  products_asin VARCHAR(12) REFERENCES products(asin),
  priceinfos_priceinfo_id VARCHAR(9) REFERENCES priceinfos(priceinfo_id),
  users_username VARCHAR(30)  REFERENCES users(username),
  purchase_date DATE,
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
  cd_format VARCHAR(50),
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
  aspect_ratio VARCHAR(7),
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
  track_id VARCHAR(9),
  cds_asin VARCHAR(12)  REFERENCES cds(asin),
  name VARCHAR(255),
  PRIMARY KEY (track_id)
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
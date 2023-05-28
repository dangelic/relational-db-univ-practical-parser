-- Indexes for the "products" table
CREATE INDEX idx_products_ptitle ON products (ptitle);
CREATE INDEX idx_products_pgroup ON products (pgroup);
CREATE INDEX idx_products_salesrank ON products (salesrank);

-- Indexes for the "users" table
CREATE INDEX idx_users_banks_bankinfo_id ON users (banks_bankinfo_id);

-- Indexes for the "priceinfos" table
CREATE INDEX idx_priceinfos_products_asin ON priceinfos (products_asin);
CREATE INDEX idx_priceinfos_shops_shop_id ON priceinfos (shops_shop_id);

-- Indexes for the "userreviews" table
CREATE INDEX idx_userreviews_products_asin ON userreviews (products_asin);
CREATE INDEX idx_userreviews_users_username ON userreviews (users_username);

-- Indexes for the "purchases" table
CREATE INDEX idx_purchases_products_asin ON purchases (products_asin);
CREATE INDEX idx_purchases_users_username ON purchases (users_username);

-- Indexes for the "books" table
CREATE INDEX idx_books_isbn ON books (isbn);

-- Indexes for the "cds" table
CREATE INDEX idx_cds_binding ON cds (binding);

-- Indexes for the "dvds" table
CREATE INDEX idx_dvds_running_time ON dvds (running_time);

-- Indexes for the "products_similars" table
CREATE INDEX idx_products_similars_products_asin ON products_similars (products_asin);
CREATE INDEX idx_products_similars_similar_product_asin ON products_similars (similar_product_asin);

-- Indexes for the "junction_products_categories" table
CREATE INDEX idx_junction_products_categories_products_asin ON junction_products_categories (products_asin);
CREATE INDEX idx_junction_products_categories_categories_category_id ON junction_products_categories (categories_category_id);

-- Indexes for the "junction_products_creators" table
CREATE INDEX idx_junction_products_creators_products_asin ON junction_products_creators (products_asin);
CREATE INDEX idx_junction_products_creators_creators_creator_id ON junction_products_creators (creators_creator_id);

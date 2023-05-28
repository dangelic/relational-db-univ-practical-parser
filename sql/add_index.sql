-- Index for getProduct method
-- This index improves the retrieval performance of getProduct using the product ID as the search criterion.
CREATE INDEX idx_getProduct ON products (asin);

-- Index for getProducts method
-- This index improves the performance of searching for products by title using the LIKE operator.
CREATE INDEX idx_getProducts ON products (ptitle);

-- Index for getCategoryTree method
-- This index facilitates the hierarchical traversal of getCategoryTree by retrieving the root node and its subcategories.
CREATE INDEX idx_getCategoryTree ON categories (parent_category_id);

-- Index for getProductsByCategoryPath method
-- This index improves the search performance for products belonging to a specific category path.
CREATE INDEX idx_getProductsByCategoryPath ON categories (name, parent_category_id);

-- Index for getTopProducts method
-- This index efficiently identifies the top-rated products.
CREATE INDEX idx_getTopProducts ON userreviews (rating);

-- Index for getSimilarCheaperProduct method
-- This index helps optimize the search for cheaper products similar to the specified one.
CREATE INDEX idx_getSimilarCheaperProduct ON priceinfos (price);

-- Index for addNewReview method
-- This index improves the performance of adding new reviews.
CREATE INDEX idx_addNewReview ON userreviews (products_asin, users_username);

-- Index for getTrolls method
-- This index enhances the search performance to identify users with an average rating below a specified value.
CREATE INDEX idx_getTrolls ON userreviews (users_username);

-- Index for getOffers method
-- This index optimizes the search to retrieve all available offers for a specific product.
CREATE INDEX idx_getOffers ON priceinfos (products_asin);
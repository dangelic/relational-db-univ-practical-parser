-- Fills the the average_rating column for each product in the "products" table
-- Fetches data from both "userreviews" and "guestreviews" tables to calculate the average rating over a UNION.
-- This function is called initially to fill the data and also after every UPDATE of the reviews' tables - as defined by triggers
CREATE OR REPLACE FUNCTION calculate_average_ratings()
RETURNS TRIGGER AS $$
BEGIN
    -- Perform an UPDATE statement on the "products" table
    UPDATE products
    SET average_rating = (
        -- Calculate the average rating using a subquery
        SELECT AVG(rating)
        FROM (
            -- Combine the ratings from both "userreviews" and "guestreviews" tables for a specific product
            SELECT rating FROM userreviews WHERE products_asin = products.asin
            UNION ALL
            SELECT rating FROM guestreviews WHERE products_asin = products.asin
        ) AS subquery
    );
    -- Return the NEW row (required for trigger function)
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Call the function
SELECT calculate_average_ratings();

CREATE TRIGGER update_average_rating_on_update_userreviews_trigger
AFTER INSERT ON userreviews
FOR EACH ROW
EXECUTE FUNCTION calculate_average_ratings();

CREATE TRIGGER update_average_rating_on_update_guestreviews_trigger
AFTER INSERT ON guestreviews
FOR EACH ROW
EXECUTE FUNCTION calculate_average_ratings();


INSERT INTO guestreviews (guestreview_id, products_asin, rating, helpful_votes, summary, content, review_date)
VALUES ('9999999', 'B00068C824', 5, 12345, 'Let''s trigger an average_rating calculation in products entity...', 'This Album is better than rated on this site.', NOW());

-- TODO: ADAPT ME.
INSERT INTO userreviews (guestreview_id, products_asin, rating, helpful_votes, summary, content, review_date)
VALUES ('9999999', 'B00068C824', 5, 12345, 'Let''s trigger an average_rating calculation in products entity...', 'This Album is better than rated on this site.', NOW());
-- TASK 2b)
-- This code below implements the functionality to keep average_rating integrity if a new userreview or guestreview gets added (or deleted)

-- A PL/pgsql-function is used:
--      Fills the the average_rating column for each product in the "products" table
--      Fetches data from both "userreviews" and "guestreviews" tables to calculate the average rating over a UNION.
--      This function is on UPDATE of the reviews' tables - as defined by triggers below.
CREATE OR REPLACE FUNCTION calculate_average_ratings_ontrigger()
RETURNS TRIGGER AS $$
BEGIN
    -- Perform an UPDATE statement on the "products" table
    UPDATE products
    SET average_rating = (
        -- Calculate the average rating using a subquery
        SELECT AVG(rating)
        FROM (
            -- Combine the ratings from both "userreviews" and "guestreviews" tables for a specific product.
            SELECT rating FROM userreviews WHERE products_asin = products.asin
            UNION ALL
            SELECT rating FROM guestreviews WHERE products_asin = products.asin
        ) AS subquery
    );
    -- Return the NEW row (required for trigger function).
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Add on-update-trigger for guestreviews
CREATE TRIGGER update_average_rating_on_update_guestreviews_trigger
AFTER INSERT ON guestreviews
FOR EACH ROW
EXECUTE FUNCTION calculate_average_ratings_ontrigger();

-- Add on-update-trigger for userreviews
CREATE TRIGGER update_average_rating_on_update_userreviews_trigger
AFTER INSERT ON userreviews
FOR EACH ROW
EXECUTE FUNCTION calculate_average_ratings_ontrigger();

-- Workaround:
--      To initially call the function without altering a value, we need the same function but with return type "void".
--      This function can be called with "SELECT calculate_average_ratings();" from the code to fill average_ratings initially.
CREATE OR REPLACE FUNCTION calculate_average_ratings()
RETURNS void AS $$
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
END;
$$ LANGUAGE plpgsql;

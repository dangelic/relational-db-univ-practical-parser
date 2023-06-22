-- Example for average_rating calculation via triggered function.

-- Select product with asin = B00068C824 -> It has a rating of 3.5 initially, calculated as average from the union of guest- and userreviews.
SELECT asin, ptitle, average_rating FROM products WHERE asin LIKE 'B00068C824';

-- Insert a positive 5* review (exemplary in guestreviews) and see the average_rating rising.
INSERT INTO guestreviews (guestreview_id, products_asin, rating, helpful_votes, summary, content, review_date)
VALUES ('123456789', 'B00068C824', 5, 999, 'WOW!', 'This product has me feeling like I am on a Kylie Minogue concert stage—confident, glamorous, and ready to shine brighter than a disco ball!', NOW());

SELECT asin, ptitle, average_rating FROM products WHERE asin LIKE 'B00068C824';

-- Insert a negative 1* review (exemplary in userreviews) and see the average_rating shrinking.
INSERT INTO userreviews (userrreview_id, users_username, products_asin, rating, helpful_votes, summary, content, review_date)
VALUES ('987654321','sunny82', 'B00068C824', 1, 0, 'Absurdity...', 'Listening to that album was a cringe-fest of musical proportions!', NOW());

SELECT asin, ptitle, average_rating FROM products WHERE asin LIKE 'B00068C824';

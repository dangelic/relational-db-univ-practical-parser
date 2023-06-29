-- Beispiel für die Berechnung des Durchschnittsratings über einen Trigger-Funktion.

-- Wähle das Produkt mit der asin = B00068C824 aus -> Es hat anfangs ein Rating von 3,5, das als Durchschnitt aus den Gast- und Benutzerbewertungen berechnet wird.
SELECT asin, ptitle, average_rating FROM products WHERE asin LIKE 'B00068C824';
/*
+--------------+----------------------------------+------------------+
|    "asin"    |             "ptitle"             | "average_rating" |
+--------------+----------------------------------+------------------+
| "B00068C824" | "Kylie Minogue - Ultimate Kylie" |              3.5 |
+--------------+----------------------------------+------------------+
*/

-- Füge eine positive 5*-Bewertung ein (exemplarisch in den Gastbewertungen) und beobachte, wie sich das Durchschnittsrating erhöht.
INSERT INTO guestreviews (guestreview_id, products_asin, rating, helpful_votes, summary, content, review_date)
VALUES ('123456789', 'B00068C824', 5, 999, 'WOW!', 'This product has me feeling like I am on a Kylie Minogue concert stage—confident, glamorous, and ready to shine brighter than a disco ball!', NOW());

SELECT asin, ptitle, average_rating FROM products WHERE asin LIKE 'B00068C824';
/*
+--------------+----------------------------------+------------------+
|    "asin"    |             "ptitle"             | "average_rating" |
+--------------+----------------------------------+------------------+
| "B00068C824" | "Kylie Minogue - Ultimate Kylie" |              3.8 |
+--------------+----------------------------------+------------------+
*/

-- Füge eine negative 1*-Bewertung ein (exemplarisch in den Userbewertungen) und beobachte, wie sich das Durchschnittsrating verringert.
INSERT INTO userreviews (userreview_id, users_username, products_asin, rating, helpful_votes, summary, content, review_date)
VALUES ('987654321','sunny82', 'B00068C824', 1, 0, 'Absurdity...', 'Listening to that album was a cringe-fest of musical proportions!', NOW());

SELECT asin, ptitle, average_rating FROM products WHERE asin LIKE 'B00068C824';
/*
+--------------+----------------------------------+------------------+
|    "asin"    |             "ptitle"             | "average_rating" |
+--------------+----------------------------------+------------------+
| "B00068C824" | "Kylie Minogue - Ultimate Kylie" |            3.334 |
+--------------+----------------------------------+------------------+
*/

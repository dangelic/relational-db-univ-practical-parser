-- 2 a)

-- #1
SELECT
    -- Calculate the total count of products in the 'Book' category and label the result as "Anzahl Bücher"
    SUM(CASE WHEN pgroup = 'Book' THEN 1 ELSE 0 END) AS "Anzahl Bücher",
    
    -- Calculate the total count of products in the 'Music' category and label the result as "Anzahl Musik-CDs"
    SUM(CASE WHEN pgroup = 'Music' THEN 1 ELSE 0 END) AS "Anzahl Musik-CDs",
    
    -- Calculate the total count of products in the 'DVD' category and label the result as "Anzahl DVDs"
    SUM(CASE WHEN pgroup = 'DVD' THEN 1 ELSE 0 END) AS "Anzahl DVDs"
FROM
    products;

-- Result:
/*
+-------------------+-----------------------+------------------+
| "Number of Books" | "Number of Musik-CDs" | "Number of DVDs" |
+-------------------+-----------------------+------------------+
|               586 |                  1810 |              665 |
+-------------------+-----------------------+------------------+
*/


-- #2

-- Logic Summary:
/*
    1. Create the intermediate table "ranked_products" to store information about products, ratings, and ranking positions.
    2. Calculate the average ratings for each product, regardless of whether they come from "userreviews" or "guestreviews" table.
    3. Join the "products" table with the average ratings to retrieve relevant product details.
    4. Use the "ROW_NUMBER" function to assign a rank position to each product within its product group based on the average rating.
    5. Retrieve the top 5 products per product group based on the rank position.
    6. Order the results by product group and ranking.
*/

WITH ranked_products AS (
    -- The intermediate table "ranked_products" contains information about the products and their ratings,
    -- along with a rank position for each product based on the average rating.
    SELECT products.pgroup AS Type, products.asin AS asin, ROUND(avg_ratings.rating, 2) AS Rating,
           ROW_NUMBER() OVER (PARTITION BY products.pgroup ORDER BY avg_ratings.rating DESC) AS ranking
    FROM products
    JOIN (
        -- The subquery "avg_ratings" calculates the average ratings for each product, regardless of whether they come from the "userreviews" or "guestreviews" table.
        SELECT reviews.products_asin, AVG(reviews.rating) AS rating
        FROM (
            -- The subquery "reviews" combines all the ratings from the "userreviews" and "guestreviews" tables.
            SELECT products_asin, rating FROM userreviews
            UNION ALL
            SELECT products_asin, rating FROM guestreviews
        ) reviews
        GROUP BY reviews.products_asin
    ) avg_ratings ON products.asin = avg_ratings.products_asin
)
-- The main query retrieves the top-rated products for each type based on the rank position.
SELECT Type, asin, Rating
FROM ranked_products
-- Only retrieve the top 5 products per type
WHERE ranking <= 5
ORDER BY Type, ranking;

-- Result:
/*
+---------+-----------------+----------+
| "type"  |     "asin"      | "rating" |
+---------+-----------------+----------+
| "Book"  | "3491886120"    |     5.00 |
| "Book"  | "3720527069"    |     5.00 |
| "Book"  | "3570007928"    |     5.00 |
| "Book"  | "342362115X"    |     5.00 |
| "Book"  | "3785535929"    |     5.00 |
| "DVD"   | "B00007BKGQ"    |     5.00 |
| "DVD"   | "B00002ZMNV"    |     5.00 |
| "DVD"   | "B0009K33LY"    |     5.00 |
| "DVD"   | "B000053ZL2"    |     5.00 |
| "DVD"   | "B00009PBJA"    |     5.00 |
| "Music" | "B000026EM7"    |     5.00 |
| "Music" | "B00008DCR8"    |     5.00 |
| "Music" | "B000009OGR"    |     5.00 |
| "Music" | "B00005UMTW"    |     5.00 |
| "Music" | "B000068VXD"    |     5.00 |
+---------+-----------------+----------+
*/

------ 3

SELECT DISTINCT pi.products_asin -- Selektiere die eindeutigen products_asin mit Distinct
FROM priceinfos pi
WHERE NOT EXISTS (
    -- Subquery zur Überprüfung der Bedingung, ob für ein Produkt ein Eintrag mit price ≠ 0 besteht
    -- Nach unserer Miniwelt sind dies die Produkte, für die kein Angebot besteht
    SELECT 1
    FROM priceinfos pi2
    WHERE pi.products_asin = pi2.products_asin -- Vergleiche die ASIN mit jedem Wert aus der Mainquery
    AND pi2.price IS NOT NULL
);


----- 4

SELECT pi.products_asin -- Selektiere die products_asin, um die betreffenden Produkte zu identifizieren
FROM priceinfos pi
WHERE pi.price = ( -- Selektiere nur die Zeilen, bei denen der Preis gleich dem...
    SELECT MAX(price) -- ...höchsten Preis für das betreffende Produkt ist
    FROM priceinfos -- Unterabfrage, um den höchsten Preis zu ermitteln
    WHERE products_asin = pi.products_asin -- Vergleiche das products_asin der Hauptabfrage mit dem der Unterabfrage
)
GROUP BY pi.products_asin -- Gruppiere die Ergebnisse nach products_asin, um jedes Produkt einmal aufzulisten
HAVING MAX(price) > 2 * MIN(price); -- Filtere die Ergebnisse, bei denen der höchste Preis mehr als das Doppelte des niedrigsten Preises ist


------- 5

SELECT reviews.products_asin -- Wähle die ASIN der Produkte aus
FROM (
    SELECT products_asin, rating -- Selektiere ASIN und Bewertung aus den Tabellen guestreviews und userreviews
    FROM guestreviews
    UNION
    SELECT products_asin, rating
    FROM userreviews
) AS reviews -- Kombiniere die Bewertungen aus beiden Tabellen
WHERE reviews.rating = 1 OR reviews.rating = 5 -- Filtere nach Bewertungen mit Punktzahl 1 oder 5
GROUP BY reviews.products_asin -- Gruppiere nach ASIN, um jedes Produkt nur einmal aufzulisten
HAVING COUNT(DISTINCT CASE WHEN reviews.rating = 1 THEN reviews.products_asin END) > 0 -- Prüfe, ob es mindestens eine Bewertung mit Punktzahl 1 gibt
   AND COUNT(DISTINCT CASE WHEN reviews.rating = 5 THEN reviews.products_asin END) > 0 -- Prüfe, ob es mindestens eine Bewertung mit Punktzahl 5 gibt

------- 6

--- Count wird genutzt
SELECT COUNT(p.asin) AS product_count -- Zähle die Produkte, für die keine Rezension existiert
FROM products p
LEFT JOIN (SELECT products_asin FROM guestreviews
           UNION
           SELECT products_asin FROM userreviews) AS r
    ON p.asin = r.products_asin -- Verbinde die products Tabelle mit der UNION-Abfrage
WHERE r.products_asin IS NULL -- Filtere nach Produkten, für die keine Rezension existiert


------- 7

-- Schließe Guests aus
SELECT users_username AS user, COUNT(*) AS review_count -- Selektiere den Rezensenten und zähle die Anzahl der Rezensionen
FROM userreviews
GROUP BY users_username -- Gruppiere nach Rezensenten
HAVING COUNT(*) >= 10 -- Filtere nach Rezensenten mit mindestens 10 Rezensionen

------- 8

-- TODO: IMPLEMENT ME!

------- 9

-- Wichtig: Wir nehmen hier auch CDs mit auf, die keinen Track gelistet haben.
-- Dies macht insofern Sinn, als dass z.B. Ambiente-Musik oder experimentelle Musik keine Tracks listen
-- Somit gehen auch 0 in die Durchschnittsbewertung ein, was wir intendieren.
-- Würden wir das nicht wollen, könnten wir einen simplen COUNT auf gruppierte Werte in der tracks-tabeelle ausführen (da die tracks-tabelle natürlich nur CDs mit mind. einem Track listet)
SELECT AVG(track_count) AS average_track_count
FROM (
    -- Subquery: Zählt die Anzahl der Tracks pro CD und gruppiert sie nach CDs_ASIN
    -- Die Ergebnisse der Subquery werden verwendet, um den Durchschnitt der Trackanzahlen zu berechnen
    SELECT cds_asin, COUNT(*) AS track_count
    FROM tracks
    GROUP BY cds_asin
) AS subquery;


---- 11

-- Die Hauptabfrage, um Produkte zu finden, die in allen Filialen angeboten werden
SELECT products.asin
FROM products
-- Subquery zur Ermittlung der Gesamtanzahl von Filialen
CROSS JOIN (
    -- Subquery zur Berechnung der Gesamtanzahl von Filialen
    SELECT COUNT(DISTINCT priceinfos.shops_shop_id) AS total_shops
    FROM priceinfos
) shop_count
WHERE products.asin IN (
    -- Subquery zur Ermittlung der Produkte, die in allen Filialen angeboten werden
    SELECT priceinfos.products_asin
    FROM priceinfos
    INNER JOIN (
        -- Subquery zur Ermittlung der Produkte, die in allen Filialen angeboten werden
        SELECT priceinfos.products_asin
        FROM priceinfos
        -- Gruppierung nach Produkt-ASIN, um die Anzahl der Filialen pro Produkt zu ermitteln
        GROUP BY priceinfos.products_asin
        -- Bedingung, um Produkte auszuwählen, die in allen Filialen angeboten werden
        HAVING COUNT(DISTINCT priceinfos.shops_shop_id) = shop_count.total_shops
    ) filtered ON priceinfos.products_asin = filtered.products_asin
    -- Bedingung, um Produkte auszuwählen, die einen Preis haben (nicht NULL)
    WHERE priceinfos.price IS NOT NULL
    -- Gruppierung nach Produkt-ASIN, um die Anzahl der Filialen pro Produkt zu ermitteln
    GROUP BY priceinfos.products_asin
    -- Bedingung, um Produkte auszuwählen, die in allen Filialen angeboten werden
    HAVING COUNT(DISTINCT priceinfos.shops_shop_id) = shop_count.total_shops
);











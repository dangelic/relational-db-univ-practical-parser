-- 2 a)

-- #1
-- Zusammenfassung der Logik:
-- Nutze CASE-Statement, um jeweils der passenden pgroup eine 1 zuzuordnen, wenn es zutrifft.
-- Summiere auf und benenne die Ergebnisse entsprechend.

SELECT
    -- SUM(CASE WHEN pgroup = 'Book' THEN 1 ELSE 0 END) berechnet die Gesamtanzahl der Produkte in der Kategorie 'Book' und bezeichnet das Ergebnis als "Anzahl Bücher"
    SUM(CASE WHEN pgroup = 'Book' THEN 1 ELSE 0 END) AS "Anzahl Bücher",
    SUM(CASE WHEN pgroup = 'Music' THEN 1 ELSE 0 END) AS "Anzahl Musik-CDs",
    SUM(CASE WHEN pgroup = 'DVD' THEN 1 ELSE 0 END) AS "Anzahl DVDs"
FROM
    products;

-- Resultat:
/*
+-----------------+--------------------+---------------+
| "Anzahl Bücher" | "Anzahl Musik-CDs" | "Anzahl DVDs" |
+-----------------+--------------------+---------------+
|             586 |               1810 |           665 |
+-----------------+--------------------+---------------+
*/

-- #2
-- Zusammenfassung der Logik:
/*
    1. CTE: Erstelle die Zwischentabelle "ranked_products", um Informationen über Produkte, Bewertungen und Rangpositionen zu speichern.
         	- UNION: Berechne den Durchschnitt der Bewertungen für jedes Produkt, unabhängig davon, ob sie aus der Tabelle "userreviews" oder "guestreviews" stammen.
            - Verknüpfe die Tabelle "products" mit den durchschnittlichen Bewertungen, um relevante Produktdetails abzurufen.
            - ROW_NUMBER OVER A PARTITION: Verwende die Funktion "ROW_NUMBER", um jedem Produkt innerhalb seiner Produktgruppe (Partition) basierend auf der durchschnittlichen Bewertung eine Rangposition zuzuweisen.
            - INNER JOIN: Kombiniert Durchschnittsbewertungen mit den Produktdetails (pgroup)
                    - reviews: Zusammenführen "userreviews" und "guestreviews" über UNION
                    - avg_ratings: Auf basis von Unterabfarge "reviews" wird Durchschnitt der Bewertungen mit AVG berechnet
                    - GROUP BY: Durchschnittsbewertung pro Produktart gruppiert
    2. Hauptabfrage: Referenziere Zwischentabelle, um nur die fünf besten Produkte pro Typ abzurufen (WHERE-Statement)
*/

WITH ranked_products AS (
    SELECT products.pgroup AS typ, products.asin AS produktnr, ROUND(avg_ratings.rating, 2) AS rating,
           ROW_NUMBER() OVER (PARTITION BY products.pgroup ORDER BY avg_ratings.rating DESC) AS rangfolge
    FROM products
    INNER JOIN (
        SELECT reviews.products_asin, AVG(reviews.rating) AS rating
        FROM (
            SELECT products_asin, rating FROM userreviews
            UNION ALL
            SELECT products_asin, rating FROM guestreviews
        ) reviews
        GROUP BY reviews.products_asin
    ) avg_ratings ON products.asin = avg_ratings.products_asin
)
SELECT typ, produktnr, rating
FROM ranked_products
WHERE rangfolge <= 5
ORDER BY typ, rangfolge;

-- Resultat:
/*
+---------+--------------+-------------+
|  "typ"  |  "produktnr" |   "rating"  |
+---------+--------------+-------------+
| "Book"  | "3491886120" |        5.00 |
| "Book"  | "3720527069" |        5.00 |
| "Book"  | "3570007928" |        5.00 |
| "Book"  | "342362115X" |        5.00 |
| "Book"  | "3785535929" |        5.00 |
| "DVD"   | "B00007BKGQ" |        5.00 |
| "DVD"   | "B00002ZMNV" |        5.00 |
| "DVD"   | "B0009K33LY" |        5.00 |
| "DVD"   | "B000053ZL2" |        5.00 |
| "DVD"   | "B00009PBJA" |        5.00 |
| "Music" | "B000026EM7" |        5.00 |
| "Music" | "B00008DCR8" |        5.00 |
| "Music" | "B000009OGR" |        5.00 |
| "Music" | "B00005UMTW" |        5.00 |
| "Music" | "B000068VXD" |        5.00 |
+---------+--------------+-------------+
*/

-- #3
-- Zusammenfassung der Logik:
/*
1. SELECT DISTINCT wird verwendet, um eindeutige Werte der Spalte products_asin aus der Tabelle priceinfos auszuwählen.
2. WHERE NOT EXISTS stellt sicher, dass nur diejenigen Produkte ausgewählt werden, für die keine Einträge in der Tabelle priceinfos mit einem Preis ungleich Null existieren. Dies bedeutet, dass es für diese Produkte kein Angebot gibt.
3. Subquery: Die Subquery überprüft die Bedingung, indem sie nach Einträgen in der Tabelle priceinfos sucht, die mit demselben products_asin-Wert wie der aktuelle Datensatz in der Hauptabfrage übereinstimmen. 
    - AND pi2.price IS NOT NULL stellt sicher, dass nur Einträge ausgewählt werden, bei denen der Preis nicht null ist, was bedeutet, dass es ein Angebot für das betreffende Produkt gibt.
*/

SELECT DISTINCT pi.products_asin
FROM priceinfos pi
WHERE NOT EXISTS (
    SELECT 1
    FROM priceinfos pi2
    WHERE pi.products_asin = pi2.products_asin
    AND pi2.price IS NOT NULL
);

-- Resultat (20 Einträge aus der Ergebnismenge mit 2121 Einträgen)
/*
+-----------------+
| "products_asin" |
+-----------------+
| "3110181460"    |
| "3120101702"    |
| "3125611881"    |
| "3134843080"    |
| "3170189263"    |
| "3190028559"    |
| "3190033137"    |
| "3190033145"    |
| "3190041954"    |
| "3190053588"    |
| "3257008767"    |
| "3401023845"    |
| "3401024434"    |
| "3401041134"    |
| "3401044915"    |
| "3401045512"    |
| "3401045571"    |
| "3401052330"    |
| "3401053698"    |
| "340105371X"    |
+-----------------+
*/


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











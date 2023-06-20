-- 2 a)

-- AUFGABE 1:
-- Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst?
-- Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus.

-- QUERY:
-- Die folgende Abfrage gruppiert die Produkte nach ihrer Produktgruppe und verwendet die SUM-Funktion, um die Anzahl der Produkte in jeder Gruppe zu zählen.
-- Diese relation gibt wie gewünscht drei Spalten aus.
SELECT
    SUM(CASE WHEN pgroup = 'Book' THEN 1 ELSE 0 END) AS "Anzahl Bücher",
    SUM(CASE WHEN pgroup = 'Music' THEN 1 ELSE 0 END) AS "Anzahl Musik-CDs",
    SUM(CASE WHEN pgroup = 'DVD' THEN 1 ELSE 0 END) AS "Anzahl DVDs"
FROM
    products;

-- RESULTAT
/*
   +-----------------+---------------------+-------------+
   | Anzahl Bücher    | Anzahl Musik-CDs     | Anzahl DVDs |
   +-----------------+---------------------+-------------+
   |       586         |         1810             |     665         |
   +-----------------+---------------------+-------------+
*/


---------- 2 TODO: Verstehe mich

WITH ranked_products AS (
    -- Die Zwischentabelle "ranked_products" enthält Informationen über die Produkte und ihre Bewertungen,
    -- sowie eine Rangposition für jedes Produkt basierend auf dem Durchschnittsrating.
    SELECT p.pgroup AS Typ, p.asin AS ProduktNr, r.rating,
           ROW_NUMBER() OVER (PARTITION BY p.pgroup ORDER BY r.rating DESC) AS ranking
    FROM products p
    JOIN (
        -- Die Unterabfrage "r" berechnet den Durchschnitt der Bewertungen für jedes Produkt, unabhängig davon,
        -- ob es aus der Tabelle "userreviews" oder "guestreviews" stammt.
        SELECT products_asin, AVG(rating) AS rating
        FROM (
            -- Die Unterabfrage "sub" kombiniert alle Bewertungen aus den Tabellen "userreviews" und "guestreviews".
            SELECT products_asin, rating FROM userreviews
            UNION ALL
            SELECT products_asin, rating FROM guestreviews
        ) sub
        GROUP BY products_asin
    ) r ON p.asin = r.products_asin
)
-- Die Hauptabfrage gibt die Produkte mit den höchsten Bewertungen für jeden Typ aus, basierend auf der Rangposition.
SELECT Typ, ProduktNr, rating
FROM ranked_products
-- Gebe nur die besten 5 Produkte je Typ aus
WHERE ranking <= 5
ORDER BY Typ, ranking;

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











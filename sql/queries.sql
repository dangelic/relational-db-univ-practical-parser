-- 2 a)

-- #1
-- Aufgabe: 
/*
Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst? 
Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus.Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst? Hinweis: Geben Sie das Ergebnis in einer 3-spaltigen Relation aus.
*/
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
-- Aufgabe:
/*
Nennen Sie die 5 besten Produkte jedes Typs (Buch, Musik-CD, DVD) sortiert nach dem durchschnittlichem Rating. 
Hinweis: Geben Sie das Ergebnis in einer einzigen Relation mit den Attributen Typ, ProduktNr, Rating aus.
*/
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
-- Aufgabe:
/*
Für welche Produkte gibt es im Moment kein Angebot?
*/
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

-- Resultat (20 Einträge aus der Ergebnismenge mit 2121 Einträgen):
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


-- #4
-- Aufgabe:
/*
Für welche Produkte ist das teuerste Angebot mehr als doppelt so teuer wie das preiswerteste?
*/
-- Zusammenfassung der Logik:
/*
1. Hauptabfrage: Nehme aus Produkten mit mehr als einem eindeutigem Preis diejenigen mit max-preis > 2 * min-preis
2. Unterabfrage: Selektiere nur Produkte mit mehr als einen eindeutigen Preis haben
    - WHERE: Bedingungen, dass keine NULL-Werte oder 0-Werte eingefangen werden
    - HAVING COUNT DISTINCT: Mehr als einen eindeutigen Preis (wie vergleichen keine NULL-Werte mit Preisen)
*/
-- Hauptabfrage
SELECT pi.products_asin, MIN(pi.price) AS min_price, MAX(pi.price) AS max_price
FROM priceinfos pi
WHERE pi.products_asin IN (
    -- Unterabfrage
    SELECT products_asin
    FROM priceinfos
    WHERE price IS NOT NULL
      AND price > 0
    GROUP BY products_asin
    HAVING COUNT(DISTINCT price) > 1
)
GROUP BY pi.products_asin
HAVING MAX(pi.price) > 2 * MIN(pi.price);

-- Ergebnis: Leere Menge (Maximal ist ein Produkt rund 1.3x teurer als im billigsten Angebot)

-- #5 
-- Aufgabe:
/*
Welche Produkte haben sowohl mindestens eine sehr schlechte (Punktzahl: 1) 
als auch mindestens eine sehr gute (Punktzahl: 5) Bewertung?
*/

-- Zusammenfassung der Logik:
/*
1. Haupabfrage: Wähle Produkte 
    - WHERE: Filtere nach Bewertungen mit Punktzahl 1 oder 5
    - GROUP-BY: Gruppiere nach ASIN, um jedes Produkt nur einmal aufzulisten
    - HAVING-Statements: Zwei Statement zur Prüfung, ob es mindestens eine Schlechtnote (1) und eine Bestnote (5) gibt
        - DISTINCT CASE WHEN THEN END: Zähle die Fälle, bei denen das Rating 1 (bzw. 5) ist
2. Unterabfrage "reviews": Hole alle Reviews mit der UNION der Tabellen "userreviews" und "guestreviews"
*/
-- Hauptabfrage
SELECT reviews.products_asin
FROM (
    -- Unterabfrage
    SELECT products_asin, rating -- Selektiere ASIN und Bewertung aus den Tabellen guestreviews und userreviews
    FROM guestreviews
    UNION
    SELECT products_asin, rating
    FROM userreviews
) AS reviews
WHERE reviews.rating = 1 OR reviews.rating = 5
GROUP BY reviews.products_asin 
HAVING COUNT(DISTINCT CASE WHEN reviews.rating = 1 THEN reviews.products_asin END) > 0
   AND COUNT(DISTINCT CASE WHEN reviews.rating = 5 THEN reviews.products_asin END) > 0 

-- Resultat (20 Einträge aus der Ergebnismenge mit 118 Einträgen):
/*
+-----------------+
| "products_asin" |
+-----------------+
| "3120101702"    |
| "3401023845"    |
| "3401058371"    |
| "3423055960"    |
| "3451280000"    |
| "3466457513"    |
| "3468266251"    |
| "3520452030"    |
| "3551552002"    |
| "3570016943"    |
| "3570215075"    |
| "3593373912"    |
| "3593375559"    |
| "3765323063"    |
| "3785714130"    |
| "3791504649"    |
| "3791504657"    |
| "3800050870"    |
| "3800050889"    |
| "3800051680"    |
+-----------------+
*/

-- #6
-- Aufgabe:
/*
Für wieviele Produkte gibt es gar keine Rezension?
*/
-- Zusammenfassung der Logik:
/*
1. Umgehe ein JOIN-Statement mit Doppel-NOT-EXIST Statements (Alternative: z.B. LEFT JOIN und dann NULL-Werte filtern)
2. Zähle die Anzahl mit COUNT
*/
SELECT COUNT(products.asin) AS products_without_ratings_count
FROM products
WHERE NOT EXISTS (
    SELECT 1
    FROM guestreviews
    WHERE guestreviews.products_asin = products.asin
)
AND NOT EXISTS (
    SELECT 1
    FROM userreviews
    WHERE userreviews.products_asin = products.asin
);

-- Resultat: 
/*
+----------------------------------+
| "products_without_ratings_count" |
+----------------------------------+
|                             1142 |
+----------------------------------+
*/

-- #7
-- Aufgabe:
/*
Nennen Sie alle Rezensenten, die mindestens 10 Rezensionen geschrieben haben.
*/
-- Zusammenfassung der Logik:
/*
1. SELECT-Statement: Selektiere den Rezensenten und zähle die Anzahl der Rezensionen mit COUNT(*)
    - Wichtig: Guests werden ausgeschlossen
2. GROUP-Statement: Gruppiere nach Rezensenten
3. HAVING-Statement: Wählte nur Rezensenten, bei denen der review_count größergleich 10 ist
*/
SELECT users_username AS user, COUNT(*) AS review_count
FROM userreviews
GROUP BY users_username
HAVING COUNT(*) >= 10

-- Resultat:
/*
+-------------------------+----------------+
|         "user"          | "review_count" |
+-------------------------+----------------+
| "m_oehri_stadtmagazine" |             12 |
| "marccoll11"            |             10 |
| "media-maniade"         |             16 |
| "petethemusicfan"       |             13 |
+-------------------------+----------------+
*/

-- #8
-- Aufgabe:
/*
Geben Sie eine duplikatfreie und alphabetisch sortierte Liste der Namen aller Buchautoren an, 
die auch an DVDs oder Musik-CDs beteiligt sind.
*/
-- Zusammenfassung der Logik:
/*
1. Hauptabfrage: Wählt die Namen der Autoren aus, deren Name auch ein Creator (von DVD, Music) oder ein Actor (DVD) ist
    - DISTINCT: Duplikatfreiheit
    - ORDER BY: Alphabetische Ordnung 
    - WHERE-Klausel: Prüft, ob der Name des Autors in einer der beiden Unterabfragen vorhanden ist.
    - Bereinigung in zweiter Bedingung der WHERE-Klausel (AND): Various Artists fliegen raus
2. Unterabfragen: Vereinigt beide Unterabfragen mit UNION
    - Unterabfrage 1: Wähle Namen aus der Tabelle actors aus (sind Schauspieler auch Autoren?)
    - Unterabfrage 2: Ist komplexer:
           a. Sie verbindet die Tabelle "creators" mit der Junction-Tabelle "junction_products_creators" über die entsprechenden IDs.
           b. Die Verbindung mit der Tabelle "products" erfolgt ebenfalls über die entsprechenden IDs.
           c. Die WHERE-Klausel filtert nur Produkte mit der Kategorie ('DVD', 'Music') (keine Creators von Books sollen gezählt werden)
           d. 
*/
-- Hauptabfrage
SELECT DISTINCT a.name
FROM authors a
WHERE a.name IN (
    -- Unterabfrage 1
    SELECT name
    FROM actors
    UNION
    -- Unterabfrage 2
    SELECT name
    FROM creators c
    JOIN junction_products_creators jpc ON c.creator_id = jpc.creators_creator_id
    JOIN products p ON jpc.products_asin = p.asin
    WHERE p.pgroup IN ('DVD', 'Music')
)
AND a.name not in ('Va')
ORDER BY a.name;

-- Resultat:
/*
+---------------------+
|       "name"        |
+---------------------+
| "Ac"                |
| "Al"                |
| "Alexandre"         |
| "Dav"               |
| "Heino"             |
| "Jürgen"            |
| "Korn"              |
| "Leonard Bernstein" |
| "Nas"               |
| "Nicole"            |
| "Peter"             |
| "Robin"             |
| "Sandra"            |
+---------------------+
*/

-- #9
-- Aufgabe:
/*
Wie hoch ist die durchschnittliche Anzahl von Liedern einer Musik-CD?
*/
-- Zusammenfassung der Logik:
/*

ANNAHME:
Wir nehmen hier auch CDs mit auf, die keinen Track gelistet haben.
Dies macht insofern Sinn, als dass z.B. Ambiente-Musik oder experimentelle Musik keine Tracks listen.
Somit gehen auch 0 in die Durchschnittsbewertung ein, was wir intendieren.
Würden wir das nicht wollen, könnten wir einen simplen COUNT auf gruppierte Werte in der tracks-tabelle ausführen (da die tracks-tabelle natürlich nur CDs mit mind. einem Track listet)

1. Hauptabfrage: Schalte ein AVG (Durschnittswert) auf track_count aus der Unterabfrage
2. Unterabfrage: 
    - COUNT: Zählt die Anzahl der Tracks pro CD und
    - LEFT JOIN: 
            - Bezieht ALLE werte der linken Tabelle (cds) mit aus, auch wenn es keine Übereinstimmung gibt
            - Gibt es keine Übereinstimmung, wird (anders als bei JOIN) ein NULL-Wert ausgebeben
            - Dieser fließt wie intendiert ebenso in die Durschnittsbewertung (als 0) mit ein
    - GROUP: Gruppiert sie nach cds_asin
    - AS subquery: Benenne die Unterabfrage (nötig in pg)
*/
-- Hauptabfrage
SELECT ROUND(AVG(track_count), 2) AS average_track_count
FROM (
    -- Unterabfrage
    SELECT cds.asin AS cds_asin, COUNT(tracks.cds_asin) AS track_count
    FROM cds
    LEFT JOIN tracks ON cds.asin = tracks.cds_asin
    GROUP BY cds.asin
) AS subquery;

-- Resultat:
/*
+-----------------------+
| "average_track_count" |
+-----------------------+
|                 21.25 |
+-----------------------+
*/

-- 10
-- Aufgabe:
/*
Für welche Produkte gibt es ähnliche Produkte in einer anderen Hauptkategorie? 
Hinweis: Eine Hauptkategorie ist eine Produktkategorie ohne Oberkategorie. 
Erstellen Sie eine rekursive Anfrage, die zu jedem Produkt dessen Hauptkategorie bestimmt.
*/
-- Zusammenfassung der Logik:
/*

*/


-- #11
-- Aufgabe:
/*
Welche Produkte werden in allen Filialen angeboten? Hinweis: Ihre Query muss so formuliert werden, dass sie für eine beliebige Anzahl von Filialen funktioniert. Hinweis: 
Beachten Sie, dass ein Produkt mehrfach von einer Filiale angeboten werden kann (z.B. neu und gebraucht).
*/
-- Zusammenfassung der Logik:
/*
Von "innen nach außen":
- Unterabfrage 2: 
    - Es werden die Filialen ausgewählt, für die es einen Eintrag in der Tabelle "priceinfos" gibt, der das entsprechende Produkt repräsentiert und einen nicht-NULL-Preis hat. 
    - Diese SELECT-Anweisung dient dazu, die Filialen zu ermitteln, bei denen das Produkt zu einem bestimmten Preis angeboten wird.
- Unterabfrage 1: 
    - Wähle durch die NOT IN Klausel (auf Unterabfrage 2) alle Filialen aus, bei denen KEIN Wert in den priceinfos besteht, der ein Produkt repräsentiert und einen nicht-NULL-Preis hat.
    - Das bedeutet, dass das Produkt in keinem der Shops angeboten wird
- Hauptabfrage:
    - Durch NOT EXIST werden nur die Produkte ausgewählt, bei denen die Bedingung der Unterabfrage 1 nicht filt
    - Das bedeutet, dass alle Produkte angegeben werden, die in jeder Filiale mindestens einmal (mit Preis, ergo Angebot) vorkommen
2.
*/
-- Hauptabfrage: Produkte, die in allen Filialen angeboten werden
SELECT products.asin, products.ptitle
FROM products
WHERE NOT EXISTS (
    -- Unterabfrage 1: Filialen, in denen das Produkt nicht angeboten wird
    SELECT shops.shop_id
    FROM shops
    WHERE shops.shop_id NOT IN (
        -- Unterabfrage 2: Filialen, in denen das Produkt mit einem Preis vorhanden ist
        SELECT priceinfos.shops_shop_id
        FROM priceinfos
        WHERE priceinfos.products_asin = products.asin
        AND priceinfos.price IS NOT NULL
        AND priceinfos.price > 0
    )
);

-- Resultat (20 Einträge aus der Ergebnismenge mit 115 Einträgen):
/*
+--------------+--------------------------------------------------+
|    "asin"    |                     "ptitle"                     |
+--------------+--------------------------------------------------+
| "B00002DGW7" | "Movie Classics"                                 |
| "B00005UW50" | "Baroque Esprit - Il Pastor Fido"                |
| "B0002TB60W" | "Various Artists - Karaoke: Love Songs, Vol. 01" |
| "B00069KW58" | "Dream on"                                       |
| "B000ANPWYG" | "I'Ve Got My Own Hell to Raise"                  |
| "B000005GWE" | "I Can See Your House from Here"                 |
| "B00004W55E" | "Individuality (Can I Be Me? )"                  |
| "B000BAVWA6" | "We Are Always Searching"                        |
| "B0002PZWQ0" | "Hed Kandi Summer 2004"                          |
| "B000BFHW1E" | "Melissa (25th Anniversary Re-I"                 |
| "B0009WPCJS" | "I Want a Girlfriend"                            |
| "B000BW9BKC" | "Tokyo Project/Introduction"                     |
| "B000B2WK8M" | "Once+Wish I Had An Angel (Collectors Box)"      |
| "B000BFHW1O" | "Abigail (25th Anniversary Re-I"                 |
| "B0008GIXDW" | "I Bruise Easily"                                |
| "B00007B6WD" | "Night of the Proms 2002-d"                      |
| "B0002WS3M8" | "Facts of Life/I Don'T Know Wha"                 |
| "B000B6VUAW" | "Trilogy-Tonight I'M Yours/Atla"                 |
| "B0002Z9ZCW" | "Pure Smooth Jazz"                               |
| "B0009SQ6YW" | "I Love Everybody"                               |
+--------------+--------------------------------------------------+
*/

-- #12
-- Aufgabe:
/*
*/
-- Zusammenfassung der Logik:
/*
ANNAHME: Es werden nur die günstigsten Angebote verglichen, 
d.h. wenn Filiale X Produkt A zu Preis a und b anbietet (in zwei Konditionen), wird der billigste genommen

1. CTE: matching_products_cte -> Gleiche Abfrage wie oben, gibt alle Produkte aus #11 aus
2. CTE: cheapest_price_cte -> 
    - Unterabfrage min_prices: Stellt sicher, dass die minimalen Preise je Produkt (aus matching_products_cte) gruppiert nach asin zurückgegeben werden
    - JOIN:
        - "priceinfos.products_asin = min_prices.products_asin" stellt sicher, dass die Produkte übereinstimmen.
        - "priceinfos.price = min_prices.min_price2 stellt sicher, dass der Preis dem minimalen Preis für jedes Produkt entspricht.
3. CTE: leipzig_count_cte -> Zähle die Einträge, bei denen "LEIPZIG" die Filliale mit dem günstigsten Angebot ist
4. CTE: total_count_cte -> Zählt alle Ergebnisse aus matching_products_cte
5. Hauptabfrage -> Berechnet den Quotienten aus leipzig_count_cte und total_count_cte, um den gerundeten Anteil zu bekommen
*/
WITH matching_products_cte AS (
  SELECT products.asin, products.ptitle
  FROM products
  WHERE NOT EXISTS (
    SELECT shops.shop_id
    FROM shops
    WHERE shops.shop_id NOT IN (
      SELECT priceinfos.shops_shop_id
      FROM priceinfos
      WHERE priceinfos.products_asin = products.asin
      AND priceinfos.price IS NOT NULL
      AND priceinfos.price > 0
    )
  )
),
cheapest_prices_cte AS (
  SELECT priceinfos.products_asin, priceinfos.shops_shop_id, priceinfos.price
  FROM priceinfos
  INNER JOIN (
    - Unterabfrage zu cheapest_price_cte
    SELECT products_asin, MIN(price) AS min_price
    FROM priceinfos
    WHERE products_asin IN (SELECT asin FROM matching_products_cte)
    GROUP BY products_asin
  ) AS min_prices 
    ON priceinfos.products_asin = min_prices.products_asin 
    AND priceinfos.price = min_prices.min_price
),
leipzig_count_cte AS (
  SELECT COUNT(*) AS leipzig_count
  FROM cheapest_prices_cte
  WHERE shops_shop_id = 'LEIPZIG'
),
total_count_cte AS (
  SELECT COUNT(*) AS total_count
  FROM cheapest_prices_cte
)
- Hauptabfrage
SELECT ROUND((leipzig_count_cte.leipzig_count * 100.0 / total_count_cte.total_count), 2) AS leipzig_cheapest_percentage
FROM leipzig_count_cte, total_count_cte;
  
-- Resultat:
/*
+-------------------------------+
| "leipzig_cheapest_percentage" |
+-------------------------------+
|                         50.43 |
+-------------------------------+
*/




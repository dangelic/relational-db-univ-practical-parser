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


----------




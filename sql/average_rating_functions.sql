-- TESTAT II
-- dbprak21
-- b) Code (Beispiel in anderem File)

-- Der folgende Code implementiert die Funktionalität, um die Integrität der Durchschnittsbewertung ("average_rating") aufrechtzuerhalten, wenn eine neue Benutzerbewertung ("userreview") oder Gästebewertung ("guestreview") hinzugefügt oder gelöscht wird.
    -- Eine PL/pgsql-Funktion wird verwendet:
    -- Die Funktion füllt die Spalte "average_rating" für jedes Produkt in der Tabelle "products".
    -- Dabei werden Daten aus den Tabellen "userreviews" und "guestreviews" abgerufen, um die Durchschnittsbewertung über eine UNION-Abfrage zu berechnen.
    -- Diese Funktion wird bei Aktualisierung der Bewertungstabellen durch Trigger aufgerufen.
CREATE OR REPLACE FUNCTION calculate_average_ratings_ontrigger()
RETURNS TRIGGER AS $$
BEGIN
    -- Führe ein UPDATE-Statement auf der Tabelle "products" aus.
    UPDATE products
    SET average_rating = (
        -- Berechne die Durchschnittsbewertung mithilfe einer Unterabfrage.
        SELECT AVG(rating)
        FROM (
            -- Kombiniere die Bewertungen aus den Tabellen "userreviews" und "guestreviews" für ein bestimmtes Produkt.            
            SELECT rating FROM userreviews WHERE products_asin = products.asin
            UNION ALL
            SELECT rating FROM guestreviews WHERE products_asin = products.asin
        ) AS subquery
    );
    --  Gib die aktualisierte Zeile (NEW) zurück (wird für die Trigger-Funktion benötigt).
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Add on-update-trigger für guestreviews
CREATE TRIGGER update_average_rating_on_update_guestreviews_trigger
AFTER INSERT ON guestreviews
FOR EACH ROW
EXECUTE FUNCTION calculate_average_ratings_ontrigger();

-- Add on-update-trigger für userreviews
CREATE TRIGGER update_average_rating_on_update_userreviews_trigger
AFTER INSERT ON userreviews
FOR EACH ROW
EXECUTE FUNCTION calculate_average_ratings_ontrigger();






-- Workaround für initales Triggern der Funktion:
    -- Um die Funktion initial aufzurufen, ohne einen Wert zu ändern, benötigen wir dieselbe Funktion, jedoch mit dem Rückgabetyp "void".
    -- Diese Funktion kann mit "SELECT calculate_average_ratings();" aus dem Code aufgerufen werden, um die Durchschnittsbewertungen initial zu füllen.
CREATE OR REPLACE FUNCTION calculate_average_ratings()
    -- RETURN VOID!
RETURNS void AS $$
BEGIN
    UPDATE products
    SET average_rating = (
        SELECT AVG(rating)
        FROM (
            SELECT rating FROM userreviews WHERE products_asin = products.asin
            UNION ALL
            SELECT rating FROM guestreviews WHERE products_asin = products.asin
        ) AS subquery
    );
END;
$$ LANGUAGE plpgsql;

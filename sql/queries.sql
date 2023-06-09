-- 2 a)
-- 1.


-- Wähle alle Produkte und gruppiere sie bei "pgroup"
SELECT

    pgroup,
    COUNT(*) AS product_count
FROM
    products
GROUP BY
    pgroup;
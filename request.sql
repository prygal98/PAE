-- 1 requette

SELECT
    CASE
        WHEN state = 'OFFERED' THEN 'offert'
        WHEN state = 'CONFIRMED' THEN 'confirmé'
        WHEN state = 'SALE' THEN 'En vente'
        WHEN state = 'SOLD' THEN 'Vendu'
        WHEN state = 'WORKSHOP' THEN 'En atelier'
        WHEN state = 'DENIED' THEN 'Refusé'
        WHEN state = 'WITHDRAWN' THEN 'Retiré'
        WHEN state = 'STORE' THEN 'En magasin'
        ELSE 'État inconnu'
        END AS etat,
    COUNT(*) AS nb_objets
FROM
    project.objects
GROUP BY
    etat;


-- 2 requette

SELECT project.object_types.name, COUNT(project.objects.id_object) AS num_objects
FROM project.object_types
         LEFT JOIN project.objects ON project.object_types.id_type = project.objects.type_object
GROUP BY project.object_types.name, project.object_types.id_type
ORDER BY project.object_types.id_type;


--3 requette

SELECT
        project.registered_users.firstname || ' ' || project.registered_users.lastname AS utilisateur,
        COUNT(*) AS nb_objets
FROM
    project.objects
        JOIN
    project.registered_users ON project.objects.owner_id = project.registered_users.id_user
GROUP BY
    utilisateur;


-- 4 requette

SELECT
    name AS objet,
    description,
    state AS etat,
    offer_date AS date_proposition_objet,
    accept_date AS date_acceptation,
    recovery_date AS date_reception,
    store_deposit_date AS date_depot_magasin,
    selling_date AS date_vente,
    withdraw_date AS date_retrait,
    refusal_reason AS raison_refus
FROM
    project.objects;


--5 requette

SELECT ad.id_availability, ad.availability, COUNT(o.id_object) AS nb_objects
FROM project.availability_dates ad
         LEFT JOIN project.objects o ON o.availability = ad.id_availability
GROUP BY ad.id_availability, ad.availability
ORDER BY ad.availability;


-- 6 requette

SELECT user_role, COUNT(*) as user_count
FROM project.registered_users
GROUP BY user_role
HAVING user_role IN ('USER', 'RESPONSIBLE', 'HELPER');
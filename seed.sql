TRUNCATE TABLE project.notifications_users CASCADE;
TRUNCATE TABLE project.notifications CASCADE;
TRUNCATE TABLE project.objects CASCADE;
TRUNCATE TABLE project.object_types CASCADE;
TRUNCATE TABLE project.availability_dates CASCADE;
TRUNCATE TABLE project.registered_users CASCADE;

-- object_types
INSERT INTO project.object_types (id_type, name)
VALUES
    (1, 'Meuble'),
    (2, 'Table'),
    (3, 'Chaise'),
    (4, 'Fauteuil'),
    (5, 'Lit/sommier'),
    (6, 'Couvertures'),
    (7, 'Matériel de cuisine'),
    (8, 'Vaisselle');

-- registered_users PAS DE PHONE NUMBER
INSERT INTO project.registered_users --password : Jaune
(id_user, password, email, firstname, lastname, registration_date, user_role, picture)
VALUES (1, '$2a$10$1GMVHq/ds1ThvoXqJEVBteCNgyp3VyCGeYnJy8042YBVrqitjpN1q', --mdp "Jaune;10."
        'bert.riez@gmail.be', 'Robert', 'Riez', NOW(), 'RESPONSIBLE',
        '77ac5d4e-e6e5-4d33-be62-6f44f23aaff0.png'),
       (2, '$2a$10$NSW0c3C94Uqv4c8KoTrof.DlSNzSbZPJ2i2BWEI1IZ3XyUjv.3m4O', --mdp "Mauve;7?"
        'fred.muise@gmail.be', 'Alfred', 'Muise', NOW(), 'HELPER',
        '365010a8-0041-4b32-97c4-133292b4c39d.png'),
       (3, '$2a$10$9X395jX5KS2XAnr.20m1q.Shkq1qlwsJ4rvZYY3pfDiZv8HmgRhLa', --mdp "mdpusr.2"
        'caro.line@hotmail.com', 'Caroline', 'Line', NOW(), 'USER',
        'c086899d-f4ca-49be-a316-c192ea0bad9f.png'),
       (4, '$2a$10$9X395jX5KS2XAnr.20m1q.Shkq1qlwsJ4rvZYY3pfDiZv8HmgRhLa', --mdp "mdpusr.2"
        'ach.ile@gmail.com', 'Achille', 'Ile', NOW(), 'USER',
        '382ba20a-a1d7-4fc0-80dd-cc261f314966.png'),
       (5, '$2a$10$9X395jX5KS2XAnr.20m1q.Shkq1qlwsJ4rvZYY3pfDiZv8HmgRhLa', --mdp "mdpusr.2"
        'bas.ile@gmail.be', 'Basile', 'Ile', NOW(), 'USER',
        '682a0ab0-c47b-49cd-b90c-c0d6f4a3c9e8.jpg');

-- availability_dates
INSERT INTO project.availability_dates
    (id_availability, availability, time_slot)
VALUES
    (1, TO_TIMESTAMP('4.03.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'matin'),
    (2, TO_TIMESTAMP('11.03.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'matin'),
    (3, TO_TIMESTAMP('18.03.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'matin'),
    (4, TO_TIMESTAMP('25.03.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'apres-midi'),
    (5, TO_TIMESTAMP('1.04.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'apres-midi'),
    (6, TO_TIMESTAMP('15.04.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'matin'),
    (7, TO_TIMESTAMP('22.04.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'), 'matin');

-- objects
INSERT INTO project.objects
(id_object, name, description, picture, state, accept_date, recovery_date, store_deposit_date,
 selling_price,
 selling_date, type_object, availability, owner_id, phone_owner, refusal_reason)
VALUES (1, 'Chaise', 'Chaise en bois brut avec cousin beige', 'Chaise-woodengbe3bb4b3a_1280.png',
        'SALE', NULL, NULL, TO_TIMESTAMP('18.03.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'),
        2.0, NULL,
        3, 3, 5, '0485/98.86.42', NULL),

       (2, 'Fauteuil', 'Canapé 3 places blanc', 'Fauteuil-sofag99f90fab2_1280.jpg',
        'SOLD', NULL, NULL, TO_TIMESTAMP('18.03.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'),
        3.0, NULL,
        4, 3, 5, '0485/98.86.42', NULL),

       (3, 'Meuble', 'Secrétaire', 'Secretaire.png',
        'DENIED', NULL, NULL,
        NULL, NULL, TO_TIMESTAMP('18.03.2023 00:00:00', 'DD.MM.YYYY HH24:MI:SS'),
        1, 4, NULL, '0496/32.16.54',
        'Ce meuble est magnifique mais fragile pour l’usage qui en sera fait.'),

       (4, 'Vaisselle', '100 assiettes blanches', 'Vaisselle-plate629970_1280.jpg',
        'CONFIRMED', NULL, NULL, NULL,
        NULL, NULL,
        8, 4, 4, '0477/65.32.24 ', NULL),

       (5, 'Fauteuil', 'Grand canapé 4 places bleu us', 'Fauteuil-couchg0f519ec38_1280.png',
        'CONFIRMED', NULL, NULL, NULL,
        NULL, NULL,
        4, 4, 4, '0477/65.32.24 ', NULL),

       (6, 'Fauteuil', 'Fauteuil design très confortable', 'Fauteuil-designgee14e1707_1280.jpg',
        'OFFERED', NULL, NULL, NULL,
        NULL, NULL,
        4, 5, 4, '0477/65.32.24 ', NULL),

       (7, 'Chaise', 'Tabouret de bar en cuir', 'bar-890375_1920.jpg',
        'OFFERED', NULL, NULL, NULL,
        NULL, NULL,
        3, 5, 4, '0477/65.32.24 ', NULL);
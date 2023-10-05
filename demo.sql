DROP SCHEMA IF EXISTS project CASCADE;
CREATE SCHEMA project;


CREATE TABLE project.registered_users
(
    id_user           SERIAL       NOT NULL PRIMARY KEY,
    password          VARCHAR(60)  NOT NULL,
    email             VARCHAR(100) NOT NULL,
    firstname         VARCHAR(100) NOT NULL,
    lastname          VARCHAR(100) NOT NULL,
    registration_date TIMESTAMP    NOT NULL,
    user_role         VARCHAR(15)  NOT NULL,
    picture           VARCHAR(100) NULL,
    phone_number      VARCHAR(10)  NULL
);

CREATE TABLE project.availability_dates
(
    id_availability SERIAL      NOT NULL PRIMARY KEY,
    availability    TIMESTAMP   NOT NULL,
    time_slot       VARCHAR(15) NOT NULL
);

CREATE TABLE project.object_types
(
    id_type SERIAL      NOT NULL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL
);

CREATE TABLE project.objects
(
    id_object          SERIAL           NOT NULL PRIMARY KEY,
    name               VARCHAR(100)     NOT NULL,
    description        VARCHAR(200)     NOT NULL,
    picture            VARCHAR(100)     NULL,
    state              VARCHAR(50)      NOT NULL,
    offer_date         TIMESTAMP        NOT NULL,
    accept_date        TIMESTAMP        NULL,
    recovery_date      TIMESTAMP        NULL,
    store_deposit_date TIMESTAMP        NULL,
    selling_price      DOUBLE PRECISION NULL,
    selling_date       TIMESTAMP        NULL,
    type_object        INTEGER          NOT NULL,
    availability       INTEGER          NOT NULL,
    owner_id           INTEGER          NULL,
    phone_owner        VARCHAR(15)      NULL,
    refusal_reason     VARCHAR(240)     NULL,
    withdraw_date      TIMESTAMP        NULL,

    CONSTRAINT fk_type FOREIGN KEY (type_object)
        REFERENCES project.object_types (id_type),
    CONSTRAINT fk_availability FOREIGN KEY (availability)
        REFERENCES project.availability_dates (id_availability),
    CONSTRAINT fk_user FOREIGN KEY (owner_id)
        REFERENCES project.registered_users (id_user)
);


CREATE TABLE project.notifications
(
    id_notification   SERIAL       NOT NULL PRIMARY KEY,
    content           VARCHAR(240) NOT NULL,
    state             VARCHAR(10)  NOT NULL,
    targeted_object   INTEGER      NULL,
    notification_date TIMESTAMP    NULL,

    CONSTRAINT fk_targeted_object FOREIGN KEY (targeted_object)
        REFERENCES project.objects (id_object)
);



CREATE TABLE project.notifications_users
(
    user_id      INTEGER NOT NULL,
    notification INTEGER NOT NULL,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES project.registered_users (id_user),
    CONSTRAINT fk_notif FOREIGN KEY (notification) REFERENCES project.notifications (id_notification),

    PRIMARY KEY (user_id, notification)
);



-- registered_users PAS DE PHONE NUMBER
INSERT INTO project.registered_users --password : Jaune
(id_user, password, email, firstname, lastname, registration_date, user_role, picture, phone_number)
VALUES (DEFAULT, '$2a$10$1GMVHq/ds1ThvoXqJEVBteCNgyp3VyCGeYnJy8042YBVrqitjpN1q', --mdp "Jaune;10."
        'bert.riez@gmail.be', 'Robert', 'Riez', NOW(), 'RESPONSIBLE',
        'MrRiez.png','0477968547'),
       (DEFAULT, '$2a$10$NSW0c3C94Uqv4c8KoTrof.DlSNzSbZPJ2i2BWEI1IZ3XyUjv.3m4O', --mdp "Mauve;7?"
        'fred.muise@gmail.be', 'Alfred', 'Muise', NOW(), 'HELPER',
        'fred.png','0476963626'),
       (DEFAULT, '$2a$10$Zmm/IxaUUenDkRjYo59d7.Y4G8syncFusJIrqiGe5IytyCD8St5ji', --mdp "mdpusr.4"
        'charline@proximus.be', 'Charles', 'Line', NOW(), 'HELPER',
        'Charline.png','0481356249'),
       (DEFAULT, '$2a$10$9X395jX5KS2XAnr.20m1q.Shkq1qlwsJ4rvZYY3pfDiZv8HmgRhLa', --mdp "mdpusr.2"
        'caro.line@hotmail.com', 'Caroline', 'Line', NOW(), 'USER',
        'caro.png','0487452379'),
       (DEFAULT, '$2a$10$9X395jX5KS2XAnr.20m1q.Shkq1qlwsJ4rvZYY3pfDiZv8HmgRhLa', --mdp "mdpusr.2"
        'ach.ile@gmail.com', 'Achille', 'Ile', NOW(), 'USER',
        'achil.png','0477653224'),
       (DEFAULT, '$2a$10$9X395jX5KS2XAnr.20m1q.Shkq1qlwsJ4rvZYY3pfDiZv8HmgRhLa', --mdp "mdpusr.2"
        'bas.ile@gmail.be', 'Basile', 'Ile', NOW(), 'USER',
        'bazz.jpg','0485988642'),
       (DEFAULT, '$2a$10$9X395jX5KS2XAnr.20m1q.Shkq1qlwsJ4rvZYY3pfDiZv8HmgRhLa', --mdp "mdpusr.2"
        'theo.phile@proximus.be', 'Théophile', 'Ile', NOW(), 'USER',
        'theo.png','0488353389');

-- Insert des disponibilite
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-03-04', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-03-04', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-03-11', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-03-11', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-03-18', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-03-18', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-03-25', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-03-25', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-04-01', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-04-01', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-04-15', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-04-15', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-04-22', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-04-22', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-04-29', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-04-29', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-05-13', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-05-13', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-05-27', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-05-27', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-06-03', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-06-03', 'apres-midi');

INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-06-17', 'matin');
INSERT INTO project.availability_dates VALUES (DEFAULT, '2023-06-17', 'apres-midi');

-- Insert des types d objets
insert into project.object_types
values (DEFAULT, 'Meuble');

insert into project.object_types
values (DEFAULT, 'Table');

insert into project.object_types
values (DEFAULT, 'Chaise');

insert into project.object_types
values (DEFAULT, 'Fauteuil');

insert into project.object_types
values (DEFAULT, 'Lit/Sommier');

insert into project.object_types
values (DEFAULT, 'Matelas');

insert into project.object_types
values (DEFAULT, 'Couvertures');

insert into project.object_types
values (DEFAULT, 'Matériel de cuisine');

insert into project.object_types
values (DEFAULT, 'Vaisselle');


-- Insert into

INSERT INTO project.objects
values (DEFAULT,'Chaise en bois', 'Chaise en bois brut avec coussin beige',
        'Chaise-wooden-gbe3bb4b3a_1280.png', 'SALE','2023-03-15', '2023-03-15', '2023-03-23',
        '2023-03-23', 2.00, null, 3, 5, 6, '0485988642', null, null);

INSERT INTO project.objects
values (DEFAULT,'Canapé 3 places' ,'Canapé 3 places blanc',
        'Fauteuil-sofa-g99f90fab2_1280.jpg', 'SOLD','2023-03-15', '2023-03-15', '2023-03-23',
        '2023-03-23', 3.00, '2023-03-23', 4, 5, 6, '0485988642', null, null);

INSERT INTO project.objects
values (DEFAULT, 'Secrétaire', 'Secrétaire',
        'Secretaire.png', 'DENIED','2023-03-25', null, null,
        null, null, null, 1, 8, null,'0496321654', 'Ce meuble est magnifique mais fragile pour l’usage qui en sera fait.', null);


INSERT INTO project.objects
values (DEFAULT, 'assietes blanches', '100 assiettes blanches',
        'Vaisselle-plate-629970_1280.jpg', 'STORE','2023-03-20', '2023-03-20', null,
        '2023-03-29', null, null, 9, 8, 5, '0477653224', null, null);

INSERT INTO project.objects
values (DEFAULT,'Canapé 4 places', 'Grand canapé 4 places bleu usé',
        'Fauteuil-couch-g0f519ec38_1280.png', 'SOLD','2023-03-20', '2023-03-20', '2023-03-25',
        '2023-03-29', 3.50, '2023-03-29', 4, 8, 5, '0477653224', null, null);

INSERT INTO project.objects
values (DEFAULT,'Fauteuil design', 'Fauteuil design très confortable',
        'Fauteuil-design-gee14e1707_1280.jpg', 'WITHDRAWN','2023-03-15', null, null,
        '2023-03-18', 5.20, null, 4, 7, 5, '0477653224', null, '2023-04-29');


INSERT INTO project.objects
values (DEFAULT,'Chaise', 'Tabouret de bar en cuir',
        'bar-890375_1920.jpg', 'WITHDRAWN','2023-04-01', null, null,
        null, null, null, 3, 10, 5,'0477653224', 'Ceci n’est pas une chaise.', null);


INSERT INTO project.objects
values (DEFAULT,  'Fauteuil ancien','Fauteuil ancien, pieds et accoudoir en bois',
        'Fauteuil-chair-g6374c21b8_1280.jpg', 'WORKSHOP','2023-04-11', '2023-04-11', null,
        null, null, null, 4, 13, 6, '0485988642', null, null);

/*9*/
INSERT INTO project.objects
values (DEFAULT, '6 bols à soupe', '6 bols à soupe', 'Vaisselle-Bol-bowl-469295_1280.jpg', 'STORE',
        '2023-04-11','2023-04-11', '2023-04-22','2023-04-25', null, null , 9,13, 6,null, null, null);

/*10*/
INSERT INTO project.objects
values (DEFAULT, 'Lit cage', 'Lit cage blanc', 'LitEnfant-nursery-g9913b3b19_1280.jpg', 'STORE',
        '2023-04-11', '2023-04-11', '2023-04-22', '2023-04-25', null, null, 1, 14, 7, null, null, null);

/*11*/
INSERT INTO project.objects
values (DEFAULT, 'Epices', '30 pots à épices', 'PotEpices-pharmacy-g01563afff_1280.jpg', 'STORE',
        '2023-04-18', '2023-04-18', '2023-04-29', '2023-05-05', null, null, 9, 15, 7, null, null, null);

/*12*/
INSERT INTO project.objects
values (DEFAULT, 'tasses à café et sous tasses', '4 tasses à café et leurs sous-tasses',
        'Vaisselle-Tassescup-1320578_1280.jpg', 'STORE',
        '2023-04-18', '2023-04-18', '2023-04-29', '2023-05-05', null, null, 9, 15, 4, null, null, null);


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



insert into project.registered_users
(id_user, password, email, firstname, lastname, registration_date, user_role, picture, phone_number)
values (default,
        '$2a$10$LpGcZAJK4zwcMlqVw8xOMuutiYNu64x15ukNkEjbdzfviCulHaq3e', 'raphael@gmail.com',
        'raphael', 'malka', CURRENT_DATE, 'RESPONSIBLE', '77ac5d4e-e6e5-4d33-be62-6f44f23aaff0.png',
        '0484780572');
insert into project.registered_users
(id_user, password, email, firstname, lastname, registration_date, user_role, picture, phone_number)
values (default,
        '$2a$10$LpGcZAJK4zwcMlqVw8xOMuutiYNu64x15ukNkEjbdzfviCulHaq3e', 'raphael2@gmail.com',
        'raphael', 'malka', CURRENT_DATE, 'RESPONSIBLE', '77ac5d4e-e6e5-4d33-be62-6f44f23aaff0.png',
        '0484780572');

insert into project.registered_users
(id_user, password, email, firstname, lastname, registration_date, user_role, picture, phone_number)
values (default,
        '$2a$12$pX33PAxI1Wx.mLYA3rmqgO1xu.Dvmwl.2y/MezXmB2Zwr/AnW1jiO', 'm@gmail.com',
        'Maxime', 'Vlaminck', CURRENT_DATE, 'USER', '77ac5d4e-e6e5-4d33-be62-6f44f23aaff0.png',
        '0484780572');

INSERT INTO project.registered_users --password : Jaune
(id_user, password, email, firstname, lastname, registration_date, user_role, picture, phone_number)
VALUES (default, '$2a$10$1GMVHq/ds1ThvoXqJEVBteCNgyp3VyCGeYnJy8042YBVrqitjpN1q', --mdp "Jaune;10."
        'new10@gmail.com', 'Robert', 'Riez', NOW(), 'RESPONSIBLE',
        '77ac5d4e-e6e5-4d33-be62-6f44f23aaff0.png', '0484780572');

insert into project.availability_dates
values (DEFAULT, '2023-05-15', 'matin');

insert into project.availability_dates
values (DEFAULT, '2023-05-15', 'apres-midi');

insert into project.availability_dates
values (DEFAULT, '2023-05-22', 'matin');

insert into project.availability_dates
values (DEFAULT, '2023-05-22', 'apres-midi');

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

insert into project.objects
values (DEFAULT, 'Armoire', 'Armoire de rangement 2 portes',
        '8ea11f04-2dfe-497d-8a78-80660e72202b.png', 'OFFERED','2023-03-24', null, null,
        '2023-03-24', 35.00, null, 1, 2, 3, '0472823585', null);

insert into project.objects
values (DEFAULT, 'Table basse', 'Table de salon', 'd7dfc64d-c05b-4a2e-a17c-22a9205e62bc.png',
        'OFFERED','2023-03-24', null, null,
        '2023-03-22', 25.00, null, 2, 3, 3, '0472823585', null);

INSERT INTO project.notifications (id_notification, content, state, targeted_object,
                                   notification_date)
VALUES (DEFAULT, 'notif1', 'UNREAD', 1, '4.03.2023 00:00:00'),
       (DEFAULT, 'notif2', 'UNREAD', 2, '5.03.2023 00:00:00');

INSERT INTO project.notifications_users (user_id, notification)
VALUES (1, 1),
       (1, 2);



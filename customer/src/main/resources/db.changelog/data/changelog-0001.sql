--liquibase formatted sql

--changeset author:1
INSERT INTO customer_group (code, name)
VALUES ('VIP', 'Very Important Person'),
       ('REGULAR', 'Regular Customer'),
       ('GUEST', 'Guest Customer'),
       ('NORMAL', 'Normal Customer');



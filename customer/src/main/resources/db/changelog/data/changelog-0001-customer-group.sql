--liquibase formatted sql

--changeset hoangtien2k3:issue-1
insert into customer_group (code, name)
values ('VIP', 'Very Important Person'),
       ('GOLD', 'Gold Customer'),
       ('SILVER', 'Silver Customer'),
       ('REGULAR', 'Standard Customer'),
       ('NEW', 'New Customer');

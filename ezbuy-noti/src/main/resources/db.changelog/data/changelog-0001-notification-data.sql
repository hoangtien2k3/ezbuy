-- insert default categoryType notification_category
INSERT INTO notification_category (id, status, type)
VALUES (uuid_generate_v4()::varchar, 1, 'ANNOUNCEMENT'),
       (uuid_generate_v4()::varchar, 1, 'NEWS'),
       (uuid_generate_v4()::varchar, 1, 'SYSTEM'),
       (uuid_generate_v4()::varchar, 1, 'THONG_BAO'),
       (uuid_generate_v4()::varchar, 1, 'TIN_TUC'),
       (uuid_generate_v4()::varchar, 1, 'HE_THONG');

INSERT INTO channel (id, status, type)
VALUES (uuid_generate_v4()::varchar, 1, 'EMAIL'),
       (uuid_generate_v4()::varchar, 1, 'SMS'),
       (uuid_generate_v4()::varchar, 1, 'REST');

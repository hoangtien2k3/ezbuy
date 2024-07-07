-- POSTGRESQL
-- PRODUCT DATABASE SCHEMA

CREATE TABLE ez_product_image_product_variants
(
    image_id   INT NOT NULL,
    variant_id INT NOT NULL,
    PRIMARY KEY (image_id, variant_id)
);

CREATE TABLE ez_product_variant
(
    id                   SERIAL PRIMARY KEY,
    product_id           INT              NOT NULL,
    tax_category_id      INT              NULL,
    shipping_category_id INT              NULL,
    code                 VARCHAR(255)     NOT NULL,
    created_at           TIMESTAMP        NOT NULL,
    updated_at           TIMESTAMP        NULL,
    on_hold              INT              NOT NULL,
    on_hand              INT              NOT NULL,
    tracked              SERIAL           NOT NULL,
    width                DOUBLE PRECISION NULL,
    height               DOUBLE PRECISION NULL,
    depth                DOUBLE PRECISION NULL,
    weight               DOUBLE PRECISION NULL,
    position             INT              NOT NULL,
    shipping_required    SERIAL           NOT NULL,
    version              INT              NOT NULL DEFAULT 1,
    enabled              SERIAL           NOT NULL
);

ALTER TABLE ez_product_variant
    ADD CONSTRAINT ez_product_variant_code_unique UNIQUE (code);

CREATE INDEX ez_product_variant_product_id_index ON ez_product_variant (product_id);
CREATE INDEX ez_product_variant_tax_category_id_index ON ez_product_variant (tax_category_id);
CREATE INDEX ez_product_variant_shipping_category_id_index ON ez_product_variant (shipping_category_id);

CREATE TABLE ez_product_attribute
(
    id            SERIAL PRIMARY KEY,
    code          VARCHAR(255) NOT NULL,
    type          VARCHAR(255) NOT NULL,
    storage_type  VARCHAR(255) NOT NULL,
    configuration JSONB        NOT NULL,
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP    NULL,
    position      INT          NOT NULL,
    translatable  SMALLINT     NOT NULL DEFAULT 1
);

ALTER TABLE ez_product_attribute
    ADD CONSTRAINT ez_product_attribute_code_unique UNIQUE (code);

CREATE TABLE ez_product_variant_translation
(
    id              SERIAL PRIMARY KEY,
    translatable_id INT          NOT NULL,
    name            VARCHAR(255) NULL,
    locale          VARCHAR(255) NOT NULL
);

ALTER TABLE ez_product_variant_translation
    ADD CONSTRAINT ez_product_variant_translation_translatable_id_locale_unique UNIQUE (translatable_id, locale);

CREATE INDEX ez_product_variant_translation_translatable_id_index
    ON ez_product_variant_translation (translatable_id);

CREATE TABLE ez_product_attribute_value
(
    id             SERIAL PRIMARY KEY,
    product_id     INT              NOT NULL,
    attribute_id   INT              NOT NULL,
    text_value     TEXT             NULL,
    boolean_value  SMALLINT         NULL,
    integer_value  INT              NULL,
    float_value    DOUBLE PRECISION NULL,
    datetime_value TIMESTAMP        NULL,
    date_value     DATE             NULL,
    json_value     JSONB            NULL,
    locale_code    VARCHAR(255)     NULL
);

CREATE INDEX ez_product_attribute_value_product_id_index
    ON ez_product_attribute_value (product_id);

CREATE INDEX ez_product_attribute_value_attribute_id_index
    ON ez_product_attribute_value (attribute_id);

CREATE TABLE ez_product_association
(
    id                  SERIAL PRIMARY KEY,
    association_type_id INT       NOT NULL,
    product_id          INT       NOT NULL,
    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NULL
);

ALTER TABLE ez_product_association
    ADD CONSTRAINT ez_product_association_product_id_association_type_id_unique
        UNIQUE (product_id, association_type_id);

CREATE INDEX ez_product_association_association_type_id_index
    ON ez_product_association (association_type_id);

CREATE INDEX ez_product_association_product_id_index
    ON ez_product_association (product_id);

CREATE TABLE ez_product_association_product
(
    association_id INT NOT NULL,
    product_id     INT NOT NULL,
    PRIMARY KEY (association_id, product_id)
);

CREATE TABLE ez_product_image
(
    id       SERIAL PRIMARY KEY,
    owner_id INT          NOT NULL,
    type     VARCHAR(255) NULL,
    path     VARCHAR(255) NOT NULL
);

CREATE INDEX ez_product_image_owner_id_index
    ON ez_product_image (owner_id);

CREATE TABLE ez_product_variant_option_value
(
    variant_id      INT NOT NULL,
    option_value_id INT NOT NULL,
    PRIMARY KEY (variant_id, option_value_id)
);

CREATE TABLE ez_product
(
    id                       SERIAL PRIMARY KEY,
    main_taxon_id            INT              NULL,
    code                     VARCHAR(255)     NOT NULL,
    created_at               TIMESTAMP        NOT NULL,
    updated_at               TIMESTAMP        NULL,
    enabled                  SMALLINT         NOT NULL DEFAULT 1,
    variant_selection_method VARCHAR(255)     NOT NULL,
    average_rating           DOUBLE PRECISION NOT NULL
);
CREATE INDEX ez_product_main_taxon_id_index
    ON ez_product (main_taxon_id);

ALTER TABLE ez_product
    ADD CONSTRAINT ez_product_code_unique UNIQUE (code);

CREATE TABLE ez_product_taxon
(
    id         SERIAL PRIMARY KEY,
    product_id INT NOT NULL,
    taxon_id   INT NOT NULL,
    position   INT NOT NULL
);

ALTER TABLE ez_product_taxon
    ADD CONSTRAINT ez_product_taxon_product_id_taxon_id_unique
        UNIQUE (product_id, taxon_id);

CREATE INDEX ez_product_taxon_product_id_index
    ON ez_product_taxon (product_id);

CREATE INDEX ez_product_taxon_taxon_id_index
    ON ez_product_taxon (taxon_id);

CREATE TABLE ez_product_option
(
    id         SERIAL PRIMARY KEY,
    code       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NULL,
    position   INT          NOT NULL
);

ALTER TABLE ez_product_option
    ADD CONSTRAINT ez_product_option_code_unique UNIQUE (code);

CREATE TABLE ez_product_options
(
    product_id INT NOT NULL,
    option_id  INT NOT NULL,
    PRIMARY KEY (product_id, option_id)
);

CREATE TABLE ez_product_channels
(
    product_id INT NOT NULL,
    channel_id INT NOT NULL,
    PRIMARY KEY (product_id, channel_id)
);

CREATE TABLE ez_product_option_translation
(
    id              SERIAL PRIMARY KEY,
    translatable_id INT          NOT NULL,
    name            VARCHAR(255) NOT NULL,
    locale          VARCHAR(255) NOT NULL
);

ALTER TABLE ez_product_option_translation
    ADD CONSTRAINT ez_product_option_translation_translatable_id_locale_unique UNIQUE (translatable_id, locale);

CREATE INDEX ez_product_option_translation_translatable_id_index
    ON ez_product_option_translation (translatable_id);

CREATE TABLE ez_product_option_value
(
    id        SERIAL PRIMARY KEY,
    option_id INT          NOT NULL,
    code      VARCHAR(255) NOT NULL
);

CREATE INDEX ez_product_option_value_option_id_index
    ON ez_product_option_value (option_id);

ALTER TABLE ez_product_option_value
    ADD CONSTRAINT ez_product_option_value_code_unique UNIQUE (code);

CREATE TABLE ez_product_option_value_translation
(
    id              SERIAL PRIMARY KEY,
    translatable_id INT          NOT NULL,
    value           VARCHAR(255) NOT NULL,
    locale          VARCHAR(255) NOT NULL
);

ALTER TABLE ez_product_option_value_translation
    ADD CONSTRAINT ez_product_option_value_translatable_id_locale_unique UNIQUE (translatable_id, locale);

CREATE INDEX ez_product_option_value_translation_translatable_id_index
    ON ez_product_option_value_translation (translatable_id);

CREATE TABLE ez_product_association_type
(
    id         SERIAL PRIMARY KEY,
    code       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NULL
);

ALTER TABLE ez_product_association_type
    ADD CONSTRAINT ez_product_association_type_code_unique UNIQUE (code);

CREATE TABLE ez_product_review
(
    id         SERIAL PRIMARY KEY,
    product_id INT          NOT NULL,
    author_id  INT          NOT NULL,
    title      VARCHAR(255),
    rating     INT          NOT NULL,
    comment    TEXT,
    status     VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NULL
);

CREATE INDEX ez_product_review_product_id_index
    ON ez_product_review (product_id);

CREATE INDEX ez_product_review_author_id_index
    ON ez_product_review (author_id);

CREATE TABLE ez_product_association_type_translation
(
    id              SERIAL PRIMARY KEY,
    translatable_id INT          NOT NULL,
    name            VARCHAR(255),
    locale          VARCHAR(255) NOT NULL
);

ALTER TABLE ez_product_association_type_translation
    ADD CONSTRAINT ez_product_association_type_translatable_id_locale_unique UNIQUE (translatable_id, locale);

CREATE INDEX ez_product_association_type_translation_translatable_id_index
    ON ez_product_association_type_translation (translatable_id);

CREATE TABLE ez_product_translation
(
    id                SERIAL PRIMARY KEY,
    translatable_id   INT          NOT NULL,
    name              VARCHAR(255) NOT NULL,
    slug              VARCHAR(255) NOT NULL,
    description       TEXT,
    meta_keywords     VARCHAR(255),
    meta_description  VARCHAR(255),
    short_description TEXT,
    locale            VARCHAR(255) NOT NULL
);

ALTER TABLE ez_product_translation
    ADD CONSTRAINT ez_product_translation_locale_slug_unique UNIQUE (locale, slug);

ALTER TABLE ez_product_translation
    ADD CONSTRAINT ez_product_translation_translatable_id_locale_unique UNIQUE (translatable_id, locale);

CREATE INDEX ez_product_translation_translatable_id_index
    ON ez_product_translation (translatable_id);

CREATE TABLE ez_product_attribute_translation
(
    id              SERIAL PRIMARY KEY,
    translatable_id INT          NOT NULL,
    name            VARCHAR(255) NOT NULL,
    locale          VARCHAR(255) NOT NULL
);

ALTER TABLE ez_product_attribute_translation
    ADD CONSTRAINT ez_product_attribute_translation_translatable_id_locale_unique UNIQUE (translatable_id, locale);

CREATE INDEX ez_product_attribute_translation_translatable_id_index
    ON ez_product_attribute_translation (translatable_id);

-- FOREIGN KEYS
ALTER TABLE
    ez_product_image
    ADD CONSTRAINT ez_product_image_id_foreign FOREIGN KEY (id) REFERENCES ez_product_image_product_variants (image_id);
ALTER TABLE
    ez_product_variant
    ADD CONSTRAINT ez_product_variant_id_foreign FOREIGN KEY (id) REFERENCES ez_product_variant_option_value (variant_id);
ALTER TABLE
    ez_product_attribute_translation
    ADD CONSTRAINT ez_product_attribute_translation_translatable_id_foreign FOREIGN KEY (translatable_id) REFERENCES ez_product_attribute (id);
ALTER TABLE
    ez_product_variant_translation
    ADD CONSTRAINT ez_product_variant_translation_translatable_id_foreign FOREIGN KEY (translatable_id) REFERENCES ez_product_variant (id);
ALTER TABLE
    ez_product_attribute_value
    ADD CONSTRAINT ez_product_attribute_value_attribute_id_foreign FOREIGN KEY (attribute_id) REFERENCES ez_product_attribute (id);
ALTER TABLE
    ez_product_attribute_value
    ADD CONSTRAINT ez_product_attribute_value_product_id_foreign FOREIGN KEY (product_id) REFERENCES ez_product (id);
ALTER TABLE
    ez_product_association
    ADD CONSTRAINT ez_product_association_id_foreign FOREIGN KEY (id) REFERENCES ez_product_association_product (association_id);
ALTER TABLE
    ez_product_image
    ADD CONSTRAINT ez_product_image_owner_id_foreign FOREIGN KEY (owner_id) REFERENCES ez_product (id);
ALTER TABLE
    ez_product_option_value
    ADD CONSTRAINT ez_product_option_value_id_foreign FOREIGN KEY (id) REFERENCES ez_product_variant_option_value (option_value_id);
ALTER TABLE
    ez_product_option_value_translation
    ADD CONSTRAINT ez_product_option_value_translation_translatable_id_foreign FOREIGN KEY (translatable_id) REFERENCES ez_product_option_value (id);
ALTER TABLE
    ez_product_association
    ADD CONSTRAINT ez_product_association_association_type_id_foreign FOREIGN KEY (association_type_id) REFERENCES ez_product_association_type (id);
ALTER TABLE
    ez_product_association_type_translation
    ADD CONSTRAINT ez_product_association_type_translation_translatable_id_foreign FOREIGN KEY (translatable_id) REFERENCES ez_product_association_type (id);
ALTER TABLE
    ez_product_translation
    ADD CONSTRAINT ez_product_translation_translatable_id_foreign FOREIGN KEY (translatable_id) REFERENCES ez_product (id);
ALTER TABLE
    ez_product_option
    ADD CONSTRAINT ez_product_option_id_foreign FOREIGN KEY (id) REFERENCES ez_product_options (option_id);
ALTER TABLE
    ez_product
    ADD CONSTRAINT ez_product_id_foreign FOREIGN KEY (id) REFERENCES ez_product_options (product_id);
ALTER TABLE
    ez_product_taxon
    ADD CONSTRAINT ez_product_taxon_product_id_foreign FOREIGN KEY (product_id) REFERENCES ez_product (id);
ALTER TABLE
    ez_product_review
    ADD CONSTRAINT ez_product_review_product_id_foreign FOREIGN KEY (product_id) REFERENCES ez_product (id);
ALTER TABLE
    ez_product
    ADD CONSTRAINT ez_product_id_foreign FOREIGN KEY (id) REFERENCES ez_product_channels (product_id);
ALTER TABLE
    ez_product_option_translation
    ADD CONSTRAINT ez_product_option_translation_translatable_id_foreign FOREIGN KEY (translatable_id) REFERENCES ez_product_option (id);
ALTER TABLE
    ez_product_association
    ADD CONSTRAINT ez_product_association_product_id_foreign FOREIGN KEY (product_id) REFERENCES ez_product (id);
ALTER TABLE
    ez_product_variant
    ADD CONSTRAINT ez_product_variant_product_id_foreign FOREIGN KEY (product_id) REFERENCES ez_product (id);

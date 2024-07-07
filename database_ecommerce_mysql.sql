CREATE TABLE `sylius_catalog_promotion_action`
(
    `id`                   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `catalog_promotion_id` INT          NULL,
    `type`                 VARCHAR(255) NOT NULL,
    `configuration`        JSON         NOT NULL
);
ALTER TABLE
    `sylius_catalog_promotion_action`
    ADD INDEX `sylius_catalog_promotion_action_catalog_promotion_id_index` (`catalog_promotion_id`);
CREATE TABLE `sylius_avatar_image`
(
    `id`       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `owner_id` INT          NOT NULL,
    `type`     VARCHAR(255) NULL,
    `path`     VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_avatar_image`
    ADD UNIQUE `sylius_avatar_image_owner_id_unique` (`owner_id`);
CREATE TABLE `sylius_product_image_product_variants`
(
    `image_id`   INT NOT NULL,
    `variant_id` INT NOT NULL,
    PRIMARY KEY (`image_id`)
);
ALTER TABLE
    `sylius_product_image_product_variants`
    ADD PRIMARY KEY (`variant_id`);
CREATE TABLE `sylius_product_variant`
(
    `id`                   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id`           INT          NOT NULL,
    `tax_category_id`      INT          NULL,
    `shipping_category_id` INT          NULL,
    `code`                 VARCHAR(255) NOT NULL,
    `created_at`           DATETIME     NOT NULL,
    `updated_at`           DATETIME     NULL,
    `on_hold`              INT          NOT NULL,
    `on_hand`              INT          NOT NULL,
    `tracked`              TINYINT      NOT NULL AUTO_INCREMENT,
    `width`                DOUBLE       NULL,
    `height`               DOUBLE       NULL,
    `depth`                DOUBLE       NULL,
    `weight`               DOUBLE       NULL,
    `position`             INT          NOT NULL,
    `shipping_required`    TINYINT      NOT NULL AUTO_INCREMENT,
    `version`              INT          NOT NULL DEFAULT '1',
    `enabled`              TINYINT      NOT NULL AUTO_INCREMENT
);
ALTER TABLE
    `sylius_product_variant`
    ADD INDEX `sylius_product_variant_product_id_index` (`product_id`);
ALTER TABLE
    `sylius_product_variant`
    ADD INDEX `sylius_product_variant_tax_category_id_index` (`tax_category_id`);
ALTER TABLE
    `sylius_product_variant`
    ADD INDEX `sylius_product_variant_shipping_category_id_index` (`shipping_category_id`);
ALTER TABLE
    `sylius_product_variant`
    ADD UNIQUE `sylius_product_variant_code_unique` (`code`);
CREATE TABLE `sylius_payment_method_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `name`            VARCHAR(255) NOT NULL,
    `description`     LONGTEXT     NULL,
    `instructions`    LONGTEXT     NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_payment_method_translation`
    ADD UNIQUE `sylius_payment_method_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_payment_method_translation`
    ADD INDEX `sylius_payment_method_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_gateway_config`
(
    `id`           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `config`       JSON         NOT NULL,
    `gateway_name` VARCHAR(255) NOT NULL,
    `factory_name` VARCHAR(255) NOT NULL
);
CREATE TABLE `sylius_product_attribute`
(
    `id`            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`          VARCHAR(255) NOT NULL,
    `type`          VARCHAR(255) NOT NULL,
    `storage_type`  VARCHAR(255) NOT NULL,
    `configuration` JSON         NOT NULL,
    `created_at`    DATETIME     NOT NULL,
    `updated_at`    DATETIME     NULL,
    `position`      INT          NOT NULL,
    `translatable`  TINYINT      NOT NULL DEFAULT '1' AUTO_INCREMENT
);
ALTER TABLE
    `sylius_product_attribute`
    ADD UNIQUE `sylius_product_attribute_code_unique` (`code`);
CREATE TABLE `sylius_promotion`
(
    `id`                    INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`                  VARCHAR(255) NOT NULL,
    `name`                  VARCHAR(255) NOT NULL,
    `description`           VARCHAR(255) NULL,
    `priority`              INT          NOT NULL,
    `exclusive`             TINYINT      NOT NULL AUTO_INCREMENT,
    `usage_limit`           INT          NULL,
    `used`                  INT          NOT NULL,
    `coupon_based`          TINYINT      NOT NULL AUTO_INCREMENT,
    `starts_at`             DATETIME     NULL,
    `ends_at`               DATETIME     NULL,
    `created_at`            DATETIME     NOT NULL,
    `updated_at`            DATETIME     NULL,
    `applies_to_discounted` TINYINT      NOT NULL DEFAULT '1' AUTO_INCREMENT,
    `archived_at`           DATETIME     NULL
);
ALTER TABLE
    `sylius_promotion`
    ADD UNIQUE `sylius_promotion_code_unique` (`code`);
CREATE TABLE `sylius_payment`
(
    `id`            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `method_id`     INT          NULL,
    `order_id`      INT          NOT NULL,
    `currency_code` VARCHAR(3)   NOT NULL,
    `amount`        INT          NOT NULL,
    `state`         VARCHAR(255) NOT NULL,
    `details`       JSON         NOT NULL,
    `created_at`    DATETIME     NOT NULL,
    `updated_at`    DATETIME     NULL
);
ALTER TABLE
    `sylius_payment`
    ADD INDEX `sylius_payment_method_id_index` (`method_id`);
ALTER TABLE
    `sylius_payment`
    ADD INDEX `sylius_payment_order_id_index` (`order_id`);
CREATE TABLE `sylius_payment_method_channels`
(
    `payment_method_id` INT NOT NULL,
    `channel_id`        INT NOT NULL,
    PRIMARY KEY (`payment_method_id`)
);
ALTER TABLE
    `sylius_payment_method_channels`
    ADD PRIMARY KEY (`channel_id`);
CREATE TABLE `sylius_catalog_promotion_scope`
(
    `id`            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `promotion_id`  INT          NULL,
    `type`          VARCHAR(255) NOT NULL,
    `configuration` JSON         NOT NULL
);
ALTER TABLE
    `sylius_catalog_promotion_scope`
    ADD INDEX `sylius_catalog_promotion_scope_promotion_id_index` (`promotion_id`);
CREATE TABLE `sylius_product_variant_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `name`            VARCHAR(255) NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_product_variant_translation`
    ADD UNIQUE `sylius_product_variant_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_product_variant_translation`
    ADD INDEX `sylius_product_variant_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_product_attribute_value`
(
    `id`             INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id`     INT          NOT NULL,
    `attribute_id`   INT          NOT NULL,
    `text_value`     LONGTEXT     NULL,
    `boolean_value`  TINYINT      NULL AUTO_INCREMENT,
    `integer_value`  INT          NULL,
    `float_value`    DOUBLE       NULL,
    `datetime_value` DATETIME     NULL,
    `date_value`     DATE         NULL,
    `json_value`     JSON         NULL,
    `locale_code`    VARCHAR(255) NULL
);
ALTER TABLE
    `sylius_product_attribute_value`
    ADD INDEX `sylius_product_attribute_value_product_id_index` (`product_id`);
ALTER TABLE
    `sylius_product_attribute_value`
    ADD INDEX `sylius_product_attribute_value_attribute_id_index` (`attribute_id`);
CREATE TABLE `sylius_shop_billing_data`
(
    `id`           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `company`      VARCHAR(255) NULL,
    `tax_id`       VARCHAR(255) NULL,
    `country_code` VARCHAR(255) NULL,
    `street`       VARCHAR(255) NULL,
    `city`         VARCHAR(255) NULL,
    `postcode`     VARCHAR(255) NULL
);
CREATE TABLE `sylius_country`
(
    `id`      INT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`    VARCHAR(2) NOT NULL,
    `enabled` TINYINT    NOT NULL AUTO_INCREMENT
);
ALTER TABLE
    `sylius_country`
    ADD UNIQUE `sylius_country_code_unique` (`code`);
CREATE TABLE `sylius_currency`
(
    `id`         INT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`       VARCHAR(3) NOT NULL,
    `created_at` DATETIME   NOT NULL,
    `updated_at` DATETIME   NULL
);
ALTER TABLE
    `sylius_currency`
    ADD UNIQUE `sylius_currency_code_unique` (`code`);
CREATE TABLE `sylius_province`
(
    `id`           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `country_id`   INT          NOT NULL,
    `code`         VARCHAR(255) NOT NULL,
    `name`         VARCHAR(255) NOT NULL,
    `abbreviation` VARCHAR(255) NULL
);
ALTER TABLE
    `sylius_province`
    ADD UNIQUE `sylius_province_country_id_name_unique` (`country_id`, `name`);
ALTER TABLE
    `sylius_province`
    ADD INDEX `sylius_province_country_id_index` (`country_id`);
ALTER TABLE
    `sylius_province`
    ADD UNIQUE `sylius_province_code_unique` (`code`);
CREATE TABLE `sylius_product_association_product`
(
    `association_id` INT NOT NULL,
    `product_id`     INT NOT NULL,
    PRIMARY KEY (`association_id`)
);
ALTER TABLE
    `sylius_product_association_product`
    ADD PRIMARY KEY (`product_id`);
CREATE TABLE `sylius_shipping_method_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `name`            VARCHAR(255) NOT NULL,
    `description`     VARCHAR(255) NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_shipping_method_translation`
    ADD UNIQUE `sylius_shipping_method_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_shipping_method_translation`
    ADD INDEX `sylius_shipping_method_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_product_image`
(
    `id`       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `owner_id` INT          NOT NULL,
    `type`     VARCHAR(255) NULL,
    `path`     VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_product_image`
    ADD INDEX `sylius_product_image_owner_id_index` (`owner_id`);
CREATE TABLE `sylius_product_variant_option_value`
(
    `variant_id`      INT NOT NULL,
    `option_value_id` INT NOT NULL,
    PRIMARY KEY (`variant_id`)
);
ALTER TABLE
    `sylius_product_variant_option_value`
    ADD PRIMARY KEY (`option_value_id`);
CREATE TABLE `sylius_locale`
(
    `id`         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`       VARCHAR(12) NOT NULL,
    `created_at` DATETIME    NOT NULL,
    `updated_at` DATETIME    NULL
);
ALTER TABLE
    `sylius_locale`
    ADD UNIQUE `sylius_locale_code_unique` (`code`);
CREATE TABLE `sylius_tax_category`
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`        VARCHAR(255) NOT NULL,
    `name`        VARCHAR(255) NOT NULL,
    `description` LONGTEXT     NULL,
    `created_at`  DATETIME     NOT NULL,
    `updated_at`  DATETIME     NULL
);
ALTER TABLE
    `sylius_tax_category`
    ADD UNIQUE `sylius_tax_category_code_unique` (`code`);
CREATE TABLE `sylius_shipping_method`
(
    `id`                   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `category_id`          INT          NULL,
    `zone_id`              INT          NOT NULL,
    `tax_category_id`      INT          NULL,
    `code`                 VARCHAR(255) NOT NULL,
    `configuration`        JSON         NOT NULL,
    `category_requirement` INT          NOT NULL,
    `calculator`           VARCHAR(255) NOT NULL,
    `is_enabled`           TINYINT      NOT NULL AUTO_INCREMENT,
    `position`             INT          NOT NULL,
    `created_at`           DATETIME     NOT NULL,
    `updated_at`           DATETIME     NULL,
    `archived_at`          DATETIME     NULL
);
ALTER TABLE
    `sylius_shipping_method`
    ADD INDEX `sylius_shipping_method_category_id_index` (`category_id`);
ALTER TABLE
    `sylius_shipping_method`
    ADD INDEX `sylius_shipping_method_zone_id_index` (`zone_id`);
ALTER TABLE
    `sylius_shipping_method`
    ADD INDEX `sylius_shipping_method_tax_category_id_index` (`tax_category_id`);
ALTER TABLE
    `sylius_shipping_method`
    ADD UNIQUE `sylius_shipping_method_code_unique` (`code`);
CREATE TABLE `sylius_product`
(
    `id`                       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `main_taxon_id`            INT          NULL,
    `code`                     VARCHAR(255) NOT NULL,
    `created_at`               DATETIME     NOT NULL,
    `updated_at`               DATETIME     NULL,
    `enabled`                  TINYINT      NOT NULL AUTO_INCREMENT,
    `variant_selection_method` VARCHAR(255) NOT NULL,
    `average_rating`           DOUBLE       NOT NULL
);
ALTER TABLE
    `sylius_product`
    ADD INDEX `sylius_product_main_taxon_id_index` (`main_taxon_id`);
ALTER TABLE
    `sylius_product`
    ADD UNIQUE `sylius_product_code_unique` (`code`);
CREATE TABLE `sylius_shop_user`
(
    `id`                       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `customer_id`              INT          NOT NULL,
    `username`                 VARCHAR(255) NULL,
    `username_canonical`       VARCHAR(255) NULL,
    `enabled`                  TINYINT      NOT NULL AUTO_INCREMENT,
    `salt`                     VARCHAR(255) NOT NULL,
    `password`                 VARCHAR(255) NULL,
    `last_login`               DATETIME     NULL,
    `password_reset_token`     VARCHAR(255) NULL,
    `password_requested_at`    DATETIME     NULL,
    `email_verification_token` VARCHAR(255) NULL,
    `verified_at`              DATETIME     NULL,
    `locked`                   TINYINT      NOT NULL AUTO_INCREMENT,
    `expires_at`               DATETIME     NULL,
    `credentials_expire_at`    DATETIME     NULL,
    `roles`                    JSON         NOT NULL,
    `email`                    VARCHAR(255) NULL,
    `email_canonical`          VARCHAR(255) NULL,
    `created_at`               DATETIME     NOT NULL,
    `updated_at`               DATETIME     NULL,
    `encoder_name`             VARCHAR(255) NULL
);
ALTER TABLE
    `sylius_shop_user`
    ADD UNIQUE `sylius_shop_user_customer_id_unique` (`customer_id`);
ALTER TABLE
    `sylius_shop_user`
    ADD UNIQUE `sylius_shop_user_password_reset_token_unique` (`password_reset_token`);
ALTER TABLE
    `sylius_shop_user`
    ADD UNIQUE `sylius_shop_user_email_verification_token_unique` (`email_verification_token`);
CREATE TABLE `sylius_channel_locales`
(
    `channel_id` INT NOT NULL,
    `locale_id`  INT NOT NULL,
    PRIMARY KEY (`channel_id`)
);
ALTER TABLE
    `sylius_channel_locales`
    ADD PRIMARY KEY (`locale_id`);
CREATE TABLE `sylius_order`
(
    `id`                    INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `shipping_address_id`   INT          NULL,
    `billing_address_id`    INT          NULL,
    `channel_id`            INT          NULL,
    `promotion_coupon_id`   INT          NULL,
    `customer_id`           INT          NULL,
    `number`                VARCHAR(255) NULL,
    `notes`                 LONGTEXT     NULL,
    `state`                 VARCHAR(255) NOT NULL,
    `checkout_completed_at` DATETIME     NULL,
    `items_total`           INT          NOT NULL,
    `adjustments_total`     INT          NOT NULL,
    `total`                 INT          NOT NULL,
    `created_at`            DATETIME     NOT NULL,
    `updated_at`            DATETIME     NULL,
    `currency_code`         VARCHAR(3)   NOT NULL,
    `locale_code`           VARCHAR(255) NOT NULL,
    `checkout_state`        VARCHAR(255) NOT NULL,
    `payment_state`         VARCHAR(255) NOT NULL,
    `shipping_state`        VARCHAR(255) NOT NULL,
    `token_value`           VARCHAR(255) NULL,
    `customer_ip`           VARCHAR(255) NULL,
    `created_by_guest`      TINYINT      NOT NULL DEFAULT '1' AUTO_INCREMENT
);
ALTER TABLE
    `sylius_order`
    ADD INDEX `sylius_order_state_updated_at_index` (`state`, `updated_at`);
ALTER TABLE
    `sylius_order`
    ADD UNIQUE `sylius_order_shipping_address_id_unique` (`shipping_address_id`);
ALTER TABLE
    `sylius_order`
    ADD UNIQUE `sylius_order_billing_address_id_unique` (`billing_address_id`);
ALTER TABLE
    `sylius_order`
    ADD INDEX `sylius_order_channel_id_index` (`channel_id`);
ALTER TABLE
    `sylius_order`
    ADD INDEX `sylius_order_promotion_coupon_id_index` (`promotion_coupon_id`);
ALTER TABLE
    `sylius_order`
    ADD INDEX `sylius_order_customer_id_index` (`customer_id`);
ALTER TABLE
    `sylius_order`
    ADD UNIQUE `sylius_order_number_unique` (`number`);
ALTER TABLE
    `sylius_order`
    ADD UNIQUE `sylius_order_token_value_unique` (`token_value`);
CREATE TABLE `sylius_channel_pricing_catalog_promotions`
(
    `channel_pricing_id`   INT NOT NULL,
    `catalog_promotion_id` INT NOT NULL,
    PRIMARY KEY (`channel_pricing_id`)
);
ALTER TABLE
    `sylius_channel_pricing_catalog_promotions`
    ADD PRIMARY KEY (`catalog_promotion_id`);
CREATE TABLE `sylius_product_taxon`
(
    `id`         INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` INT NOT NULL,
    `taxon_id`   INT NOT NULL,
    `position`   INT NOT NULL
);
ALTER TABLE
    `sylius_product_taxon`
    ADD UNIQUE `sylius_product_taxon_product_id_taxon_id_unique` (`product_id`, `taxon_id`);
ALTER TABLE
    `sylius_product_taxon`
    ADD INDEX `sylius_product_taxon_product_id_index` (`product_id`);
ALTER TABLE
    `sylius_product_taxon`
    ADD INDEX `sylius_product_taxon_taxon_id_index` (`taxon_id`);
CREATE TABLE `sylius_shipment`
(
    `id`                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `method_id`         INT          NOT NULL,
    `order_id`          INT          NOT NULL,
    `state`             VARCHAR(255) NOT NULL,
    `tracking`          VARCHAR(255) NULL,
    `created_at`        DATETIME     NOT NULL,
    `updated_at`        DATETIME     NULL,
    `shipped_at`        DATETIME     NULL,
    `adjustments_total` INT          NOT NULL
);
ALTER TABLE
    `sylius_shipment`
    ADD INDEX `sylius_shipment_method_id_index` (`method_id`);
ALTER TABLE
    `sylius_shipment`
    ADD INDEX `sylius_shipment_order_id_index` (`order_id`);
CREATE TABLE `sylius_promotion_action`
(
    `id`            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `promotion_id`  INT          NULL,
    `type`          VARCHAR(255) NOT NULL,
    `configuration` JSON         NOT NULL
);
ALTER TABLE
    `sylius_promotion_action`
    ADD INDEX `sylius_promotion_action_promotion_id_index` (`promotion_id`);
CREATE TABLE `sylius_tax_rate`
(
    `id`                INT            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `category_id`       INT            NOT NULL,
    `zone_id`           INT            NOT NULL,
    `code`              VARCHAR(255)   NOT NULL,
    `name`              VARCHAR(255)   NOT NULL,
    `amount`            DECIMAL(10, 5) NOT NULL,
    `included_in_price` TINYINT        NOT NULL AUTO_INCREMENT,
    `calculator`        VARCHAR(255)   NOT NULL,
    `created_at`        DATETIME       NOT NULL,
    `updated_at`        DATETIME       NULL,
    `start_date`        DATETIME       NULL,
    `end_date`          DATETIME       NULL
);
ALTER TABLE
    `sylius_tax_rate`
    ADD INDEX `sylius_tax_rate_category_id_index` (`category_id`);
ALTER TABLE
    `sylius_tax_rate`
    ADD INDEX `sylius_tax_rate_zone_id_index` (`zone_id`);
ALTER TABLE
    `sylius_tax_rate`
    ADD UNIQUE `sylius_tax_rate_code_unique` (`code`);
CREATE TABLE `sylius_paypal_plugin_pay_pal_credentials`
(
    `id`                VARCHAR(255) NOT NULL,
    `payment_method_id` INT          NULL,
    `access_token`      VARCHAR(255) NOT NULL,
    `creation_time`     DATETIME     NOT NULL,
    `expiration_time`   DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);
ALTER TABLE
    `sylius_paypal_plugin_pay_pal_credentials`
    ADD INDEX `sylius_paypal_plugin_pay_pal_credentials_payment_method_id_index` (`payment_method_id`);
CREATE TABLE `sylius_channel_price_history_config`
(
    `id`                                                   INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `lowest_price_for_discounted_products_checking_period` INT     NOT NULL DEFAULT '30',
    `lowest_price_for_discounted_products_visible`         TINYINT NOT NULL DEFAULT '1' AUTO_INCREMENT
);
CREATE TABLE `sylius_product_options`
(
    `product_id` INT NOT NULL,
    `option_id`  INT NOT NULL,
    PRIMARY KEY (`product_id`)
);
ALTER TABLE
    `sylius_product_options`
    ADD PRIMARY KEY (`option_id`);
CREATE TABLE `sylius_channel_currencies`
(
    `channel_id`  INT NOT NULL,
    `currency_id` INT NOT NULL,
    PRIMARY KEY (`channel_id`)
);
ALTER TABLE
    `sylius_channel_currencies`
    ADD PRIMARY KEY (`currency_id`);
CREATE TABLE `sylius_address`
(
    `id`            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `customer_id`   INT          NULL,
    `first_name`    VARCHAR(255) NOT NULL,
    `last_name`     VARCHAR(255) NOT NULL,
    `phone_number`  VARCHAR(255) NULL,
    `street`        VARCHAR(255) NOT NULL,
    `company`       VARCHAR(255) NULL,
    `city`          VARCHAR(255) NOT NULL,
    `postcode`      VARCHAR(255) NOT NULL,
    `created_at`    DATETIME     NOT NULL,
    `updated_at`    DATETIME     NULL,
    `country_code`  VARCHAR(255) NOT NULL,
    `province_code` VARCHAR(255) NULL,
    `province_name` VARCHAR(255) NULL
);
ALTER TABLE
    `sylius_address`
    ADD INDEX `sylius_address_customer_id_index` (`customer_id`);
CREATE TABLE `sylius_order_sequence`
(
    `id`      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `idx`     INT NOT NULL,
    `version` INT NOT NULL DEFAULT '1'
);
CREATE TABLE `sylius_product_option`
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NULL,
    `position`   INT          NOT NULL
);
ALTER TABLE
    `sylius_product_option`
    ADD UNIQUE `sylius_product_option_code_unique` (`code`);
CREATE TABLE `sylius_channel_countries`
(
    `channel_id` INT NOT NULL,
    `country_id` INT NOT NULL,
    PRIMARY KEY (`channel_id`)
);
ALTER TABLE
    `sylius_channel_countries`
    ADD PRIMARY KEY (`country_id`);
CREATE TABLE `sylius_order_item`
(
    `id`                  INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id`            INT          NOT NULL,
    `variant_id`          INT          NOT NULL,
    `quantity`            INT          NOT NULL,
    `unit_price`          INT          NOT NULL,
    `units_total`         INT          NOT NULL,
    `adjustments_total`   INT          NOT NULL,
    `total`               INT          NOT NULL,
    `is_immutable`        TINYINT      NOT NULL AUTO_INCREMENT,
    `product_name`        VARCHAR(255) NULL,
    `variant_name`        VARCHAR(255) NULL,
    `version`             INT          NOT NULL DEFAULT '1',
    `original_unit_price` INT          NULL
);
ALTER TABLE
    `sylius_order_item`
    ADD INDEX `sylius_order_item_order_id_index` (`order_id`);
ALTER TABLE
    `sylius_order_item`
    ADD INDEX `sylius_order_item_variant_id_index` (`variant_id`);
CREATE TABLE `sylius_channel_pricing_log_entry`
(
    `id`                 INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `channel_pricing_id` INT      NOT NULL,
    `price`              INT      NOT NULL,
    `original_price`     INT      NULL,
    `logged_at`          DATETIME NOT NULL
);
ALTER TABLE
    `sylius_channel_pricing_log_entry`
    ADD INDEX `sylius_channel_pricing_log_entry_channel_pricing_id_index` (`channel_pricing_id`);
CREATE TABLE `sylius_product_association`
(
    `id`                  INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `association_type_id` INT      NOT NULL,
    `product_id`          INT      NOT NULL,
    `created_at`          DATETIME NOT NULL,
    `updated_at`          DATETIME NULL
);
ALTER TABLE
    `sylius_product_association`
    ADD UNIQUE `sylius_product_association_product_id_association_type_id_unique` (
                                                                                   `product_id`,
                                                                                   `association_type_id`
        );
ALTER TABLE
    `sylius_product_association`
    ADD INDEX `sylius_product_association_association_type_id_index` (`association_type_id`);
ALTER TABLE
    `sylius_product_association`
    ADD INDEX `sylius_product_association_product_id_index` (`product_id`);
CREATE TABLE `sylius_shipping_method_channels`
(
    `shipping_method_id` INT NOT NULL,
    `channel_id`         INT NOT NULL,
    PRIMARY KEY (`shipping_method_id`)
);
ALTER TABLE
    `sylius_shipping_method_channels`
    ADD PRIMARY KEY (`channel_id`);
CREATE TABLE `sylius_channel_price_history_config_excluded_taxons`
(
    `channel_id` INT NOT NULL,
    `taxon_id`   INT NOT NULL,
    PRIMARY KEY (`channel_id`)
);
ALTER TABLE
    `sylius_channel_price_history_config_excluded_taxons`
    ADD PRIMARY KEY (`taxon_id`);
CREATE TABLE `sylius_customer`
(
    `id`                       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `customer_group_id`        INT          NULL,
    `default_address_id`       INT          NULL,
    `email`                    VARCHAR(255) NOT NULL,
    `email_canonical`          VARCHAR(255) NOT NULL,
    `first_name`               VARCHAR(255) NULL,
    `last_name`                VARCHAR(255) NULL,
    `birthday`                 DATETIME     NULL,
    `gender`                   VARCHAR(1)   NOT NULL DEFAULT 'u',
    `created_at`               DATETIME     NOT NULL,
    `updated_at`               DATETIME     NULL,
    `phone_number`             VARCHAR(255) NULL,
    `subscribed_to_newsletter` TINYINT      NOT NULL AUTO_INCREMENT
);
ALTER TABLE
    `sylius_customer`
    ADD INDEX `sylius_customer_customer_group_id_index` (`customer_group_id`);
ALTER TABLE
    `sylius_customer`
    ADD UNIQUE `sylius_customer_default_address_id_unique` (`default_address_id`);
ALTER TABLE
    `sylius_customer`
    ADD UNIQUE `sylius_customer_email_unique` (`email`);
ALTER TABLE
    `sylius_customer`
    ADD UNIQUE `sylius_customer_email_canonical_unique` (`email_canonical`);
ALTER TABLE
    `sylius_customer`
    ADD INDEX `sylius_customer_created_at_index` (`created_at`);
CREATE TABLE `sylius_product_channels`
(
    `product_id` INT NOT NULL,
    `channel_id` INT NOT NULL,
    PRIMARY KEY (`product_id`)
);
ALTER TABLE
    `sylius_product_channels`
    ADD PRIMARY KEY (`channel_id`);
CREATE TABLE `sylius_migrations`
(
    `version`        VARCHAR(191) NOT NULL,
    `executed_at`    DATETIME     NULL,
    `execution_time` INT          NULL,
    PRIMARY KEY (`version`)
);
CREATE TABLE `sylius_promotion_channels`
(
    `promotion_id` INT NOT NULL,
    `channel_id`   INT NOT NULL,
    PRIMARY KEY (`promotion_id`)
);
ALTER TABLE
    `sylius_promotion_channels`
    ADD PRIMARY KEY (`channel_id`);
CREATE TABLE `sylius_channel_pricing`
(
    `id`                           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_variant_id`           INT          NOT NULL,
    `price`                        INT          NULL,
    `channel_code`                 VARCHAR(255) NOT NULL,
    `original_price`               INT          NULL,
    `minimum_price`                INT          NULL,
    `lowest_price_before_discount` INT          NULL
);
ALTER TABLE
    `sylius_channel_pricing`
    ADD UNIQUE `sylius_channel_pricing_product_variant_id_channel_code_unique` (
                                                                                `product_variant_id`,
                                                                                `channel_code`
        );
ALTER TABLE
    `sylius_channel_pricing`
    ADD INDEX `sylius_channel_pricing_product_variant_id_index` (`product_variant_id`);
CREATE TABLE `sylius_product_option_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `name`            VARCHAR(255) NOT NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_product_option_translation`
    ADD UNIQUE `sylius_product_option_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_product_option_translation`
    ADD INDEX `sylius_product_option_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_product_option_value`
(
    `id`        INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `option_id` INT          NOT NULL,
    `code`      VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_product_option_value`
    ADD INDEX `sylius_product_option_value_option_id_index` (`option_id`);
ALTER TABLE
    `sylius_product_option_value`
    ADD UNIQUE `sylius_product_option_value_code_unique` (`code`);
CREATE TABLE `sylius_product_option_value_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `value`           VARCHAR(255) NOT NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_product_option_value_translation`
    ADD UNIQUE `sylius_product_option_value_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_product_option_value_translation`
    ADD INDEX `sylius_product_option_value_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_shipping_method_rule`
(
    `id`                 INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `shipping_method_id` INT          NULL,
    `type`               VARCHAR(255) NOT NULL,
    `configuration`      JSON         NOT NULL
);
ALTER TABLE
    `sylius_shipping_method_rule`
    ADD INDEX `sylius_shipping_method_rule_shipping_method_id_index` (`shipping_method_id`);
CREATE TABLE `sylius_payment_security_token`
(
    `hash`         VARCHAR(255) NOT NULL,
    `details`      LONGTEXT     NULL COMMENT '( DC2Type:object )',
    `after_url`    LONGTEXT     NULL,
    `target_url`   LONGTEXT     NOT NULL,
    `gateway_name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`hash`)
);
CREATE TABLE `sylius_payment_method`
(
    `id`                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`              VARCHAR(255) NOT NULL,
    `environment`       VARCHAR(255) NULL,
    `is_enabled`        TINYINT      NOT NULL AUTO_INCREMENT,
    `position`          INT          NOT NULL,
    `created_at`        DATETIME     NOT NULL,
    `updated_at`        DATETIME     NULL,
    `gateway_config_id` INT          NULL
);
ALTER TABLE
    `sylius_payment_method`
    ADD UNIQUE `sylius_payment_method_code_unique` (`code`);
ALTER TABLE
    `sylius_payment_method`
    ADD INDEX `sylius_payment_method_gateway_config_id_index` (`gateway_config_id`);
CREATE TABLE `sylius_customer_group`
(
    `id`   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_customer_group`
    ADD UNIQUE `sylius_customer_group_code_unique` (`code`);
CREATE TABLE `sylius_taxon`
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `tree_root`  INT          NULL,
    `parent_id`  INT          NULL,
    `code`       VARCHAR(255) NOT NULL,
    `tree_left`  INT          NOT NULL,
    `tree_right` INT          NOT NULL,
    `tree_level` INT          NOT NULL,
    `position`   INT          NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NULL,
    `enabled`    TINYINT      NOT NULL AUTO_INCREMENT
);
ALTER TABLE
    `sylius_taxon`
    ADD INDEX `sylius_taxon_tree_root_index` (`tree_root`);
ALTER TABLE
    `sylius_taxon`
    ADD INDEX `sylius_taxon_parent_id_index` (`parent_id`);
ALTER TABLE
    `sylius_taxon`
    ADD UNIQUE `sylius_taxon_code_unique` (`code`);
CREATE TABLE `sylius_user_oauth`
(
    `id`            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`       INT          NULL,
    `provider`      VARCHAR(255) NOT NULL,
    `identifier`    VARCHAR(255) NOT NULL,
    `access_token`  TEXT         NULL,
    `refresh_token` TEXT         NULL
);
ALTER TABLE
    `sylius_user_oauth`
    ADD UNIQUE `sylius_user_oauth_user_id_provider_unique` (`user_id`, `provider`);
ALTER TABLE
    `sylius_user_oauth`
    ADD INDEX `sylius_user_oauth_user_id_index` (`user_id`);
CREATE TABLE `sylius_catalog_promotion_channels`
(
    `catalog_promotion_id` INT NOT NULL,
    `channel_id`           INT NOT NULL,
    PRIMARY KEY (`catalog_promotion_id`)
);
ALTER TABLE
    `sylius_catalog_promotion_channels`
    ADD PRIMARY KEY (`channel_id`);
CREATE TABLE `sylius_zone`
(
    `id`    INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`  VARCHAR(255) NOT NULL,
    `name`  VARCHAR(255) NOT NULL,
    `type`  VARCHAR(8)   NOT NULL,
    `scope` VARCHAR(255) NULL
);
ALTER TABLE
    `sylius_zone`
    ADD UNIQUE `sylius_zone_code_unique` (`code`);
CREATE TABLE `sylius_promotion_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `label`           VARCHAR(255) NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_promotion_translation`
    ADD UNIQUE `sylius_promotion_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_promotion_translation`
    ADD INDEX `sylius_promotion_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_catalog_promotion`
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`       VARCHAR(255) NOT NULL,
    `name`       VARCHAR(255) NOT NULL,
    `enabled`    TINYINT      NOT NULL AUTO_INCREMENT,
    `start_date` DATETIME     NULL,
    `end_date`   DATETIME     NULL,
    `state`      VARCHAR(255) NOT NULL,
    `priority`   INT          NOT NULL,
    `exclusive`  TINYINT      NOT NULL AUTO_INCREMENT
);
ALTER TABLE
    `sylius_catalog_promotion`
    ADD UNIQUE `sylius_catalog_promotion_code_unique` (`code`);
CREATE TABLE `sylius_product_association_type`
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NULL
);
ALTER TABLE
    `sylius_product_association_type`
    ADD UNIQUE `sylius_product_association_type_code_unique` (`code`);
CREATE TABLE `sylius_product_review`
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` INT          NOT NULL,
    `author_id`  INT          NOT NULL,
    `title`      VARCHAR(255) NULL,
    `rating`     INT          NOT NULL,
    `comment`    LONGTEXT     NULL,
    `status`     VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NULL
);
ALTER TABLE
    `sylius_product_review`
    ADD INDEX `sylius_product_review_product_id_index` (`product_id`);
ALTER TABLE
    `sylius_product_review`
    ADD INDEX `sylius_product_review_author_id_index` (`author_id`);
CREATE TABLE `sylius_address_log_entries`
(
    `id`           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `action`       VARCHAR(255) NOT NULL,
    `logged_at`    DATETIME     NOT NULL,
    `object_id`    VARCHAR(64)  NULL,
    `object_class` VARCHAR(255) NOT NULL,
    `version`      INT          NOT NULL,
    `data`         JSON         NULL,
    `username`     VARCHAR(255) NULL
);
CREATE TABLE `messenger_messages`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `body`         LONGTEXT     NOT NULL,
    `headers`      LONGTEXT     NOT NULL,
    `queue_name`   VARCHAR(190) NOT NULL,
    `created_at`   DATETIME     NOT NULL,
    `available_at` DATETIME     NOT NULL,
    `delivered_at` DATETIME     NULL
);
ALTER TABLE
    `messenger_messages`
    ADD INDEX `messenger_messages_queue_name_index` (`queue_name`);
ALTER TABLE
    `messenger_messages`
    ADD INDEX `messenger_messages_available_at_index` (`available_at`);
ALTER TABLE
    `messenger_messages`
    ADD INDEX `messenger_messages_delivered_at_index` (`delivered_at`);
CREATE TABLE `sylius_taxon_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `name`            VARCHAR(255) NOT NULL,
    `slug`            VARCHAR(255) NOT NULL,
    `description`     LONGTEXT     NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_taxon_translation`
    ADD UNIQUE `sylius_taxon_translation_locale_slug_unique` (`locale`, `slug`);
ALTER TABLE
    `sylius_taxon_translation`
    ADD UNIQUE `sylius_taxon_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_taxon_translation`
    ADD INDEX `sylius_taxon_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_promotion_order`
(
    `order_id`     INT NOT NULL,
    `promotion_id` INT NOT NULL,
    PRIMARY KEY (`order_id`)
);
ALTER TABLE
    `sylius_promotion_order`
    ADD PRIMARY KEY (`promotion_id`);
CREATE TABLE `sylius_product_association_type_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `name`            VARCHAR(255) NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_product_association_type_translation`
    ADD UNIQUE `sylius_product_association_type_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_product_association_type_translation`
    ADD INDEX `sylius_product_association_type_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_exchange_rate`
(
    `id`              INT            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `source_currency` INT            NOT NULL,
    `target_currency` INT            NOT NULL,
    `ratio`           DECIMAL(10, 5) NOT NULL,
    `created_at`      DATETIME       NOT NULL,
    `updated_at`      DATETIME       NULL
);
ALTER TABLE
    `sylius_exchange_rate`
    ADD UNIQUE `sylius_exchange_rate_source_currency_target_currency_unique` (
                                                                              `source_currency`,
                                                                              `target_currency`
        );
ALTER TABLE
    `sylius_exchange_rate`
    ADD INDEX `sylius_exchange_rate_source_currency_index` (`source_currency`);
ALTER TABLE
    `sylius_exchange_rate`
    ADD INDEX `sylius_exchange_rate_target_currency_index` (`target_currency`);
CREATE TABLE `sylius_shipping_category`
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`        VARCHAR(255) NOT NULL,
    `name`        VARCHAR(255) NOT NULL,
    `description` LONGTEXT     NULL,
    `created_at`  DATETIME     NOT NULL,
    `updated_at`  DATETIME     NULL
);
ALTER TABLE
    `sylius_shipping_category`
    ADD UNIQUE `sylius_shipping_category_code_unique` (`code`);
CREATE TABLE `sylius_promotion_coupon`
(
    `id`                             INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `promotion_id`                   INT          NULL,
    `code`                           VARCHAR(255) NOT NULL,
    `usage_limit`                    INT          NULL,
    `used`                           INT          NOT NULL,
    `expires_at`                     DATETIME     NULL,
    `created_at`                     DATETIME     NOT NULL,
    `updated_at`                     DATETIME     NULL,
    `per_customer_usage_limit`       INT          NULL,
    `reusable_from_cancelled_orders` TINYINT      NOT NULL DEFAULT '1' AUTO_INCREMENT
);
ALTER TABLE
    `sylius_promotion_coupon`
    ADD INDEX `sylius_promotion_coupon_promotion_id_index` (`promotion_id`);
ALTER TABLE
    `sylius_promotion_coupon`
    ADD UNIQUE `sylius_promotion_coupon_code_unique` (`code`);
CREATE TABLE `sylius_taxon_image`
(
    `id`       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `owner_id` INT          NOT NULL,
    `type`     VARCHAR(255) NULL,
    `path`     VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_taxon_image`
    ADD INDEX `sylius_taxon_image_owner_id_index` (`owner_id`);
CREATE TABLE `sylius_order_item_unit`
(
    `id`                INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_item_id`     INT      NOT NULL,
    `shipment_id`       INT      NULL,
    `adjustments_total` INT      NOT NULL,
    `created_at`        DATETIME NOT NULL,
    `updated_at`        DATETIME NULL
);
ALTER TABLE
    `sylius_order_item_unit`
    ADD INDEX `sylius_order_item_unit_order_item_id_index` (`order_item_id`);
ALTER TABLE
    `sylius_order_item_unit`
    ADD INDEX `sylius_order_item_unit_shipment_id_index` (`shipment_id`);
CREATE TABLE `sylius_adjustment`
(
    `id`                 INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id`           INT          NULL,
    `order_item_id`      INT          NULL,
    `order_item_unit_id` INT          NULL,
    `type`               VARCHAR(255) NOT NULL,
    `label`              VARCHAR(255) NULL,
    `amount`             INT          NOT NULL,
    `is_neutral`         TINYINT      NOT NULL AUTO_INCREMENT,
    `is_locked`          TINYINT      NOT NULL AUTO_INCREMENT,
    `origin_code`        VARCHAR(255) NULL,
    `created_at`         DATETIME     NOT NULL,
    `updated_at`         DATETIME     NULL,
    `shipment_id`        INT          NULL,
    `details`            JSON         NOT NULL
);
ALTER TABLE
    `sylius_adjustment`
    ADD INDEX `sylius_adjustment_order_id_index` (`order_id`);
ALTER TABLE
    `sylius_adjustment`
    ADD INDEX `sylius_adjustment_order_item_id_index` (`order_item_id`);
ALTER TABLE
    `sylius_adjustment`
    ADD INDEX `sylius_adjustment_order_item_unit_id_index` (`order_item_unit_id`);
ALTER TABLE
    `sylius_adjustment`
    ADD INDEX `sylius_adjustment_shipment_id_index` (`shipment_id`);
CREATE TABLE `sylius_product_translation`
(
    `id`                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id`   INT          NOT NULL,
    `name`              VARCHAR(255) NOT NULL,
    `slug`              VARCHAR(255) NOT NULL,
    `description`       LONGTEXT     NULL,
    `meta_keywords`     VARCHAR(255) NULL,
    `meta_description`  VARCHAR(255) NULL,
    `short_description` LONGTEXT     NULL,
    `locale`            VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_product_translation`
    ADD UNIQUE `sylius_product_translation_locale_slug_unique` (`locale`, `slug`);
ALTER TABLE
    `sylius_product_translation`
    ADD UNIQUE `sylius_product_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_product_translation`
    ADD INDEX `sylius_product_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_zone_member`
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `belongs_to` INT          NULL,
    `code`       VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_zone_member`
    ADD UNIQUE `sylius_zone_member_belongs_to_code_unique` (`belongs_to`, `code`);
ALTER TABLE
    `sylius_zone_member`
    ADD INDEX `sylius_zone_member_belongs_to_index` (`belongs_to`);
CREATE TABLE `sylius_catalog_promotion_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `label`           VARCHAR(255) NULL,
    `description`     VARCHAR(255) NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_catalog_promotion_translation`
    ADD UNIQUE `sylius_catalog_promotion_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_catalog_promotion_translation`
    ADD INDEX `sylius_catalog_promotion_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_admin_user`
(
    `id`                       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username`                 VARCHAR(255) NULL,
    `username_canonical`       VARCHAR(255) NULL,
    `enabled`                  TINYINT      NOT NULL AUTO_INCREMENT,
    `salt`                     VARCHAR(255) NOT NULL,
    `password`                 VARCHAR(255) NULL,
    `last_login`               DATETIME     NULL,
    `password_reset_token`     VARCHAR(255) NULL,
    `password_requested_at`    DATETIME     NULL,
    `email_verification_token` VARCHAR(255) NULL,
    `verified_at`              DATETIME     NULL,
    `locked`                   TINYINT      NOT NULL AUTO_INCREMENT,
    `expires_at`               DATETIME     NULL,
    `credentials_expire_at`    DATETIME     NULL,
    `roles`                    JSON         NOT NULL,
    `email`                    VARCHAR(255) NULL,
    `email_canonical`          VARCHAR(255) NULL,
    `created_at`               DATETIME     NOT NULL,
    `updated_at`               DATETIME     NULL,
    `first_name`               VARCHAR(255) NULL,
    `last_name`                VARCHAR(255) NULL,
    `locale_code`              VARCHAR(12)  NOT NULL,
    `encoder_name`             VARCHAR(255) NULL
);
ALTER TABLE
    `sylius_admin_user`
    ADD UNIQUE `sylius_admin_user_password_reset_token_unique` (`password_reset_token`);
ALTER TABLE
    `sylius_admin_user`
    ADD UNIQUE `sylius_admin_user_email_verification_token_unique` (`email_verification_token`);
CREATE TABLE `sylius_product_attribute_translation`
(
    `id`              INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `translatable_id` INT          NOT NULL,
    `name`            VARCHAR(255) NOT NULL,
    `locale`          VARCHAR(255) NOT NULL
);
ALTER TABLE
    `sylius_product_attribute_translation`
    ADD UNIQUE `sylius_product_attribute_translation_translatable_id_locale_unique` (`translatable_id`, `locale`);
ALTER TABLE
    `sylius_product_attribute_translation`
    ADD INDEX `sylius_product_attribute_translation_translatable_id_index` (`translatable_id`);
CREATE TABLE `sylius_promotion_rule`
(
    `id`            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `promotion_id`  INT          NULL,
    `type`          VARCHAR(255) NOT NULL,
    `configuration` JSON         NOT NULL
);
ALTER TABLE
    `sylius_promotion_rule`
    ADD INDEX `sylius_promotion_rule_promotion_id_index` (`promotion_id`);
CREATE TABLE `sylius_channel`
(
    `id`                                    INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `default_locale_id`                     INT          NOT NULL,
    `base_currency_id`                      INT          NOT NULL,
    `default_tax_zone_id`                   INT          NULL,
    `code`                                  VARCHAR(255) NOT NULL,
    `name`                                  VARCHAR(255) NOT NULL,
    `color`                                 VARCHAR(255) NULL,
    `description`                           LONGTEXT     NULL,
    `enabled`                               TINYINT      NOT NULL AUTO_INCREMENT,
    `hostname`                              VARCHAR(255) NULL,
    `created_at`                            DATETIME     NOT NULL,
    `updated_at`                            DATETIME     NULL,
    `theme_name`                            VARCHAR(255) NULL,
    `tax_calculation_strategy`              VARCHAR(255) NOT NULL,
    `contact_email`                         VARCHAR(255) NULL,
    `skipping_shipping_step_allowed`        TINYINT      NOT NULL AUTO_INCREMENT,
    `account_verification_required`         TINYINT      NOT NULL AUTO_INCREMENT,
    `skipping_payment_step_allowed`         TINYINT      NOT NULL AUTO_INCREMENT,
    `shop_billing_data_id`                  INT          NULL,
    `menu_taxon_id`                         INT          NULL,
    `contact_phone_number`                  VARCHAR(255) NULL,
    `shipping_address_in_checkout_required` TINYINT      NOT NULL AUTO_INCREMENT,
    `channel_price_history_config_id`       INT          NULL
);
ALTER TABLE
    `sylius_channel`
    ADD INDEX `sylius_channel_default_locale_id_index` (`default_locale_id`);
ALTER TABLE
    `sylius_channel`
    ADD INDEX `sylius_channel_base_currency_id_index` (`base_currency_id`);
ALTER TABLE
    `sylius_channel`
    ADD INDEX `sylius_channel_default_tax_zone_id_index` (`default_tax_zone_id`);
ALTER TABLE
    `sylius_channel`
    ADD UNIQUE `sylius_channel_code_unique` (`code`);
ALTER TABLE
    `sylius_channel`
    ADD INDEX `sylius_channel_hostname_index` (`hostname`);
ALTER TABLE
    `sylius_channel`
    ADD UNIQUE `sylius_channel_shop_billing_data_id_unique` (`shop_billing_data_id`);
ALTER TABLE
    `sylius_channel`
    ADD INDEX `sylius_channel_menu_taxon_id_index` (`menu_taxon_id`);
ALTER TABLE
    `sylius_channel`
    ADD UNIQUE `sylius_channel_channel_price_history_config_id_unique` (
                                                                        `channel_price_history_config_id`
        );
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_default_tax_zone_id_foreign` FOREIGN KEY (`default_tax_zone_id`) REFERENCES `sylius_zone` (`id`);
ALTER TABLE
    `sylius_catalog_promotion_action`
    ADD CONSTRAINT `sylius_catalog_promotion_action_catalog_promotion_id_foreign` FOREIGN KEY (`catalog_promotion_id`) REFERENCES `sylius_catalog_promotion` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_payment_method_channels` (`channel_id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_catalog_promotion_channels` (`channel_id`);
ALTER TABLE
    `sylius_product_association`
    ADD CONSTRAINT `sylius_product_association_product_id_foreign` FOREIGN KEY (`product_id`) REFERENCES `sylius_product` (`id`);
ALTER TABLE
    `sylius_product_association`
    ADD CONSTRAINT `sylius_product_association_association_type_id_foreign` FOREIGN KEY (`association_type_id`) REFERENCES `sylius_product_association_type` (`id`);
ALTER TABLE
    `sylius_product`
    ADD CONSTRAINT `sylius_product_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_options` (`product_id`);
ALTER TABLE
    `sylius_exchange_rate`
    ADD CONSTRAINT `sylius_exchange_rate_target_currency_foreign` FOREIGN KEY (`target_currency`) REFERENCES `sylius_currency` (`id`);
ALTER TABLE
    `sylius_product_translation`
    ADD CONSTRAINT `sylius_product_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_product` (`id`);
ALTER TABLE
    `sylius_product_association_type_translation`
    ADD CONSTRAINT `sylius_product_association_type_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_product_association_type` (`id`);
ALTER TABLE
    `sylius_order`
    ADD CONSTRAINT `sylius_order_shipping_address_id_foreign` FOREIGN KEY (`shipping_address_id`) REFERENCES `sylius_address` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_shop_billing_data_id_foreign` FOREIGN KEY (`shop_billing_data_id`) REFERENCES `sylius_shop_billing_data` (`id`);
ALTER TABLE
    `sylius_product`
    ADD CONSTRAINT `sylius_product_main_taxon_id_foreign` FOREIGN KEY (`main_taxon_id`) REFERENCES `sylius_taxon` (`id`);
ALTER TABLE
    `sylius_payment_method`
    ADD CONSTRAINT `sylius_payment_method_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_payment_method_channels` (`payment_method_id`);
ALTER TABLE
    `sylius_product_option`
    ADD CONSTRAINT `sylius_product_option_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_options` (`option_id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_base_currency_id_foreign` FOREIGN KEY (`base_currency_id`) REFERENCES `sylius_currency` (`id`);
ALTER TABLE
    `sylius_taxon`
    ADD CONSTRAINT `sylius_taxon_parent_id_foreign` FOREIGN KEY (`parent_id`) REFERENCES `sylius_taxon` (`id`);
ALTER TABLE
    `sylius_product_option_value_translation`
    ADD CONSTRAINT `sylius_product_option_value_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_product_option_value` (`id`);
ALTER TABLE
    `sylius_product`
    ADD CONSTRAINT `sylius_product_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_channels` (`product_id`);
ALTER TABLE
    `sylius_product_variant_translation`
    ADD CONSTRAINT `sylius_product_variant_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_product_variant` (`id`);
ALTER TABLE
    `sylius_order`
    ADD CONSTRAINT `sylius_order_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_promotion_order` (`order_id`);
ALTER TABLE
    `sylius_adjustment`
    ADD CONSTRAINT `sylius_adjustment_order_item_unit_id_foreign` FOREIGN KEY (`order_item_unit_id`) REFERENCES `sylius_order_item_unit` (`id`);
ALTER TABLE
    `sylius_order`
    ADD CONSTRAINT `sylius_order_promotion_coupon_id_foreign` FOREIGN KEY (`promotion_coupon_id`) REFERENCES `sylius_promotion_coupon` (`id`);
ALTER TABLE
    `sylius_country`
    ADD CONSTRAINT `sylius_country_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_countries` (`country_id`);
ALTER TABLE
    `sylius_promotion_translation`
    ADD CONSTRAINT `sylius_promotion_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_promotion` (`id`);
ALTER TABLE
    `sylius_order`
    ADD CONSTRAINT `sylius_order_billing_address_id_foreign` FOREIGN KEY (`billing_address_id`) REFERENCES `sylius_address` (`id`);
ALTER TABLE
    `sylius_catalog_promotion_scope`
    ADD CONSTRAINT `sylius_catalog_promotion_scope_promotion_id_foreign` FOREIGN KEY (`promotion_id`) REFERENCES `sylius_catalog_promotion` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_promotion_channels` (`channel_id`);
ALTER TABLE
    `sylius_payment_method_translation`
    ADD CONSTRAINT `sylius_payment_method_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_payment_method` (`id`);
ALTER TABLE
    `sylius_product_variant`
    ADD CONSTRAINT `sylius_product_variant_tax_category_id_foreign` FOREIGN KEY (`tax_category_id`) REFERENCES `sylius_tax_category` (`id`);
ALTER TABLE
    `sylius_product_variant`
    ADD CONSTRAINT `sylius_product_variant_shipping_category_id_foreign` FOREIGN KEY (`shipping_category_id`) REFERENCES `sylius_shipping_category` (`id`);
ALTER TABLE
    `sylius_product_option_value`
    ADD CONSTRAINT `sylius_product_option_value_option_id_foreign` FOREIGN KEY (`option_id`) REFERENCES `sylius_product_option` (`id`);
ALTER TABLE
    `sylius_adjustment`
    ADD CONSTRAINT `sylius_adjustment_shipment_id_foreign` FOREIGN KEY (`shipment_id`) REFERENCES `sylius_shipment` (`id`);
ALTER TABLE
    `sylius_order`
    ADD CONSTRAINT `sylius_order_customer_id_foreign` FOREIGN KEY (`customer_id`) REFERENCES `sylius_customer` (`id`);
ALTER TABLE
    `sylius_product_review`
    ADD CONSTRAINT `sylius_product_review_author_id_foreign` FOREIGN KEY (`author_id`) REFERENCES `sylius_customer` (`id`);
ALTER TABLE
    `sylius_product_review`
    ADD CONSTRAINT `sylius_product_review_product_id_foreign` FOREIGN KEY (`product_id`) REFERENCES `sylius_product` (`id`);
ALTER TABLE
    `sylius_adjustment`
    ADD CONSTRAINT `sylius_adjustment_order_item_id_foreign` FOREIGN KEY (`order_item_id`) REFERENCES `sylius_order_item` (`id`);
ALTER TABLE
    `sylius_product_taxon`
    ADD CONSTRAINT `sylius_product_taxon_taxon_id_foreign` FOREIGN KEY (`taxon_id`) REFERENCES `sylius_taxon` (`id`);
ALTER TABLE
    `sylius_currency`
    ADD CONSTRAINT `sylius_currency_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_currencies` (`currency_id`);
ALTER TABLE
    `sylius_catalog_promotion`
    ADD CONSTRAINT `sylius_catalog_promotion_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_catalog_promotion_channels` (`catalog_promotion_id`);
ALTER TABLE
    `sylius_promotion_rule`
    ADD CONSTRAINT `sylius_promotion_rule_promotion_id_foreign` FOREIGN KEY (`promotion_id`) REFERENCES `sylius_promotion` (`id`);
ALTER TABLE
    `sylius_taxon`
    ADD CONSTRAINT `sylius_taxon_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_price_history_config_excluded_taxons` (`taxon_id`);
ALTER TABLE
    `sylius_order_item_unit`
    ADD CONSTRAINT `sylius_order_item_unit_shipment_id_foreign` FOREIGN KEY (`shipment_id`) REFERENCES `sylius_shipment` (`id`);
ALTER TABLE
    `sylius_customer`
    ADD CONSTRAINT `sylius_customer_default_address_id_foreign` FOREIGN KEY (`default_address_id`) REFERENCES `sylius_address` (`id`);
ALTER TABLE
    `sylius_taxon_translation`
    ADD CONSTRAINT `sylius_taxon_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_taxon` (`id`);
ALTER TABLE
    `sylius_product_image`
    ADD CONSTRAINT `sylius_product_image_owner_id_foreign` FOREIGN KEY (`owner_id`) REFERENCES `sylius_product` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_default_locale_id_foreign` FOREIGN KEY (`default_locale_id`) REFERENCES `sylius_locale` (`id`);
ALTER TABLE
    `sylius_product_attribute_translation`
    ADD CONSTRAINT `sylius_product_attribute_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_product_attribute` (`id`);
ALTER TABLE
    `sylius_product_taxon`
    ADD CONSTRAINT `sylius_product_taxon_product_id_foreign` FOREIGN KEY (`product_id`) REFERENCES `sylius_product` (`id`);
ALTER TABLE
    `sylius_shipping_method_rule`
    ADD CONSTRAINT `sylius_shipping_method_rule_shipping_method_id_foreign` FOREIGN KEY (`shipping_method_id`) REFERENCES `sylius_shipping_method` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_shipping_method_channels` (`channel_id`);
ALTER TABLE
    `sylius_promotion`
    ADD CONSTRAINT `sylius_promotion_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_promotion_channels` (`promotion_id`);
ALTER TABLE
    `sylius_payment_method`
    ADD CONSTRAINT `sylius_payment_method_gateway_config_id_foreign` FOREIGN KEY (`gateway_config_id`) REFERENCES `sylius_gateway_config` (`id`);
ALTER TABLE
    `sylius_user_oauth`
    ADD CONSTRAINT `sylius_user_oauth_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `sylius_shop_user` (`id`);
ALTER TABLE
    `sylius_shipping_method_translation`
    ADD CONSTRAINT `sylius_shipping_method_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_shipping_method` (`id`);
ALTER TABLE
    `sylius_channel_pricing`
    ADD CONSTRAINT `sylius_channel_pricing_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_pricing_catalog_promotions` (`channel_pricing_id`);
ALTER TABLE
    `sylius_catalog_promotion`
    ADD CONSTRAINT `sylius_catalog_promotion_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_pricing_catalog_promotions` (`catalog_promotion_id`);
ALTER TABLE
    `sylius_tax_rate`
    ADD CONSTRAINT `sylius_tax_rate_zone_id_foreign` FOREIGN KEY (`zone_id`) REFERENCES `sylius_zone` (`id`);
ALTER TABLE
    `sylius_shipping_method`
    ADD CONSTRAINT `sylius_shipping_method_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_shipping_method_channels` (`shipping_method_id`);
ALTER TABLE
    `sylius_promotion_coupon`
    ADD CONSTRAINT `sylius_promotion_coupon_promotion_id_foreign` FOREIGN KEY (`promotion_id`) REFERENCES `sylius_promotion` (`id`);
ALTER TABLE
    `sylius_product_attribute_value`
    ADD CONSTRAINT `sylius_product_attribute_value_attribute_id_foreign` FOREIGN KEY (`attribute_id`) REFERENCES `sylius_product_attribute` (`id`);
ALTER TABLE
    `sylius_product_image`
    ADD CONSTRAINT `sylius_product_image_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_image_product_variants` (`image_id`);
ALTER TABLE
    `sylius_order_item_unit`
    ADD CONSTRAINT `sylius_order_item_unit_order_item_id_foreign` FOREIGN KEY (`order_item_id`) REFERENCES `sylius_order_item` (`id`);
ALTER TABLE
    `sylius_product_attribute_value`
    ADD CONSTRAINT `sylius_product_attribute_value_product_id_foreign` FOREIGN KEY (`product_id`) REFERENCES `sylius_product` (`id`);
ALTER TABLE
    `sylius_shipping_method`
    ADD CONSTRAINT `sylius_shipping_method_tax_category_id_foreign` FOREIGN KEY (`tax_category_id`) REFERENCES `sylius_tax_category` (`id`);
ALTER TABLE
    `sylius_product_option_translation`
    ADD CONSTRAINT `sylius_product_option_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_product_option` (`id`);
ALTER TABLE
    `sylius_shipping_method`
    ADD CONSTRAINT `sylius_shipping_method_zone_id_foreign` FOREIGN KEY (`zone_id`) REFERENCES `sylius_zone` (`id`);
ALTER TABLE
    `sylius_order_item`
    ADD CONSTRAINT `sylius_order_item_order_id_foreign` FOREIGN KEY (`order_id`) REFERENCES `sylius_order` (`id`);
ALTER TABLE
    `sylius_payment`
    ADD CONSTRAINT `sylius_payment_order_id_foreign` FOREIGN KEY (`order_id`) REFERENCES `sylius_order` (`id`);
ALTER TABLE
    `sylius_exchange_rate`
    ADD CONSTRAINT `sylius_exchange_rate_source_currency_foreign` FOREIGN KEY (`source_currency`) REFERENCES `sylius_currency` (`id`);
ALTER TABLE
    `sylius_customer`
    ADD CONSTRAINT `sylius_customer_customer_group_id_foreign` FOREIGN KEY (`customer_group_id`) REFERENCES `sylius_customer_group` (`id`);
ALTER TABLE
    `sylius_paypal_plugin_pay_pal_credentials`
    ADD CONSTRAINT `sylius_paypal_plugin_pay_pal_credentials_payment_method_id_foreign` FOREIGN KEY (`payment_method_id`) REFERENCES `sylius_payment_method` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_channels` (`channel_id`);
ALTER TABLE
    `sylius_product_variant`
    ADD CONSTRAINT `sylius_product_variant_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_variant_option_value` (`variant_id`);
ALTER TABLE
    `sylius_payment`
    ADD CONSTRAINT `sylius_payment_method_id_foreign` FOREIGN KEY (`method_id`) REFERENCES `sylius_payment_method` (`id`);
ALTER TABLE
    `sylius_province`
    ADD CONSTRAINT `sylius_province_country_id_foreign` FOREIGN KEY (`country_id`) REFERENCES `sylius_country` (`id`);
ALTER TABLE
    `sylius_avatar_image`
    ADD CONSTRAINT `sylius_avatar_image_owner_id_foreign` FOREIGN KEY (`owner_id`) REFERENCES `sylius_admin_user` (`id`);
ALTER TABLE
    `sylius_product_association`
    ADD CONSTRAINT `sylius_product_association_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_association_product` (`association_id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_channel_price_history_config_id_foreign` FOREIGN KEY (
                                                                                         `channel_price_history_config_id`
        ) REFERENCES `sylius_channel_price_history_config` (`id`);
ALTER TABLE
    `sylius_shop_user`
    ADD CONSTRAINT `sylius_shop_user_customer_id_foreign` FOREIGN KEY (`customer_id`) REFERENCES `sylius_customer` (`id`);
ALTER TABLE
    `sylius_shipping_method`
    ADD CONSTRAINT `sylius_shipping_method_category_id_foreign` FOREIGN KEY (`category_id`) REFERENCES `sylius_shipping_category` (`id`);
ALTER TABLE
    `sylius_promotion_action`
    ADD CONSTRAINT `sylius_promotion_action_promotion_id_foreign` FOREIGN KEY (`promotion_id`) REFERENCES `sylius_promotion` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_currencies` (`channel_id`);
ALTER TABLE
    `sylius_order_item`
    ADD CONSTRAINT `sylius_order_item_variant_id_foreign` FOREIGN KEY (`variant_id`) REFERENCES `sylius_product_variant` (`id`);
ALTER TABLE
    `sylius_tax_rate`
    ADD CONSTRAINT `sylius_tax_rate_category_id_foreign` FOREIGN KEY (`category_id`) REFERENCES `sylius_tax_category` (`id`);
ALTER TABLE
    `sylius_product`
    ADD CONSTRAINT `sylius_product_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_association_product` (`product_id`);
ALTER TABLE
    `sylius_channel_pricing`
    ADD CONSTRAINT `sylius_channel_pricing_product_variant_id_foreign` FOREIGN KEY (`product_variant_id`) REFERENCES `sylius_product_variant` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_countries` (`channel_id`);
ALTER TABLE
    `sylius_product_variant`
    ADD CONSTRAINT `sylius_product_variant_product_id_foreign` FOREIGN KEY (`product_id`) REFERENCES `sylius_product` (`id`);
ALTER TABLE
    `sylius_shipment`
    ADD CONSTRAINT `sylius_shipment_order_id_foreign` FOREIGN KEY (`order_id`) REFERENCES `sylius_order` (`id`);
ALTER TABLE
    `sylius_zone_member`
    ADD CONSTRAINT `sylius_zone_member_belongs_to_foreign` FOREIGN KEY (`belongs_to`) REFERENCES `sylius_zone` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_locales` (`channel_id`);
ALTER TABLE
    `sylius_locale`
    ADD CONSTRAINT `sylius_locale_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_locales` (`locale_id`);
ALTER TABLE
    `sylius_product_option_value`
    ADD CONSTRAINT `sylius_product_option_value_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_variant_option_value` (`option_value_id`);
ALTER TABLE
    `sylius_catalog_promotion_translation`
    ADD CONSTRAINT `sylius_catalog_promotion_translation_translatable_id_foreign` FOREIGN KEY (`translatable_id`) REFERENCES `sylius_catalog_promotion` (`id`);
ALTER TABLE
    `sylius_order`
    ADD CONSTRAINT `sylius_order_channel_id_foreign` FOREIGN KEY (`channel_id`) REFERENCES `sylius_channel` (`id`);
ALTER TABLE
    `sylius_promotion`
    ADD CONSTRAINT `sylius_promotion_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_promotion_order` (`promotion_id`);
ALTER TABLE
    `sylius_taxon`
    ADD CONSTRAINT `sylius_taxon_tree_root_foreign` FOREIGN KEY (`tree_root`) REFERENCES `sylius_taxon` (`id`);
ALTER TABLE
    `sylius_shipment`
    ADD CONSTRAINT `sylius_shipment_method_id_foreign` FOREIGN KEY (`method_id`) REFERENCES `sylius_shipping_method` (`id`);
ALTER TABLE
    `sylius_channel`
    ADD CONSTRAINT `sylius_channel_menu_taxon_id_foreign` FOREIGN KEY (`menu_taxon_id`) REFERENCES `sylius_taxon` (`id`);
ALTER TABLE
    `sylius_adjustment`
    ADD CONSTRAINT `sylius_adjustment_order_id_foreign` FOREIGN KEY (`order_id`) REFERENCES `sylius_order` (`id`);
ALTER TABLE
    `sylius_product_variant`
    ADD CONSTRAINT `sylius_product_variant_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_product_image_product_variants` (`variant_id`);
ALTER TABLE
    `sylius_channel_pricing_log_entry`
    ADD CONSTRAINT `sylius_channel_pricing_log_entry_channel_pricing_id_foreign` FOREIGN KEY (`channel_pricing_id`) REFERENCES `sylius_channel_pricing` (`id`);
ALTER TABLE
    `sylius_address`
    ADD CONSTRAINT `sylius_address_customer_id_foreign` FOREIGN KEY (`customer_id`) REFERENCES `sylius_customer` (`id`);
ALTER TABLE
    `sylius_taxon_image`
    ADD CONSTRAINT `sylius_taxon_image_owner_id_foreign` FOREIGN KEY (`owner_id`) REFERENCES `sylius_taxon` (`id`);
ALTER TABLE
    `sylius_channel_price_history_config`
    ADD CONSTRAINT `sylius_channel_price_history_config_id_foreign` FOREIGN KEY (`id`) REFERENCES `sylius_channel_price_history_config_excluded_taxons` (`channel_id`);
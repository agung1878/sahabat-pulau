CREATE TABLE donation
(
    id                   VARCHAR(36)                               NOT NULL,
    created_at           TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    updated_at           TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) NOT NULL,
    name                 VARCHAR(36)                               NOT NULL,
    phone_number         VARCHAR(14)                               NOT NULL,
    email                VARCHAR(100)                              NOT NULL,
    bank_name            VARCHAR(100)                              NOT NULL,
    bank_account_number  VARCHAR(30)                               NOT NULL,
    bank_account_name    VARCHAR(100)                              NOT NULL,
    message              TEXT                                      NOT NULL,
    receipt_img_url      VARCHAR(255)                              NOT NULL,
    receipt_img_filename VARCHAR(255)                              NOT NULL,
    PRIMARY KEY (id)
);

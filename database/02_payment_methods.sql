
CREATE SEQUENCE payment_methods_ids START 1 INCREMENT 50;

CREATE TABLE CreditCard (
    account_id          INT NOT NULL,
    last4digits         VARCHAR(4) NOT NULL,
    credit_card_token   VARCHAR(50) NOT NULL,
    credit_card_id      BIGINT PRIMARY KEY DEFAULT nextval('payment_methods_ids'),

    FOREIGN KEY (account_id)
    REFERENCES Account(account_id)
);

CREATE TABLE Iban (
    account_id          INT NOT NULL,
    iban_string         VARCHAR(34) NOT NULL,
    iban_id             BIGINT PRIMARY KEY DEFAULT nextval('payment_methods_ids'),

    FOREIGN KEY (account_id)
    REFERENCES Account(account_id)
);

CREATE TABLE CreditCard (
    account_id          INT NOT NULL,
    card_number         VARCHAR(16) NOT NULL,
    expiration_date     DATE NOT NULL,
    name                VARCHAR(50) NOT NULL,
    address             VARCHAR(100) NOT NULL,
    city                VARCHAR(50) NOT NULL,
    country             VARCHAR(50) NOT NULL,
    zip                 VARCHAR(10) NOT NULL,
    credit_card_id      SERIAL PRIMARY KEY,

    FOREIGN KEY (account_id)
    REFERENCES Account(account_id)
);

CREATE TABLE Iban (
    account_id          INT NOT NULL,
    iban_id             SERIAL PRIMARY KEY,
    iban_string         VARCHAR(34) NOT NULL,
    
    FOREIGN KEY (account_id)
    REFERENCES Account(account_id)
);
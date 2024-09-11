
CREATE TABLE CreditCard (
    account_id          INT NOT NULL,
    last4digits         VARCHAR(4) NOT NULL,
    credit_card_id      SERIAL PRIMARY KEY,
    credit_card_token   VARCHAR(50) NOT NULL,

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
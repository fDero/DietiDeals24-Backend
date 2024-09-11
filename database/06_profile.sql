
CREATE TABLE PersonalLink (
    link        TEXT NOT NULL,
    description TEXT NOT NULL,
    account_id  INT NOT NULL,
    link_id     SERIAL PRIMARY KEY,

    UNIQUE (account_id, link),
    
    FOREIGN KEY (account_id) 
    REFERENCES Account(account_id)
);

CREATE TABLE ContactInformation (
    email       TEXT NOT NULL,
    phone       VARCHAR(20) NOT NULL,
    account_id  INT NOT NULL,
    contact_id  SERIAL PRIMARY KEY,
    
    UNIQUE (account_id, email, phone),

    FOREIGN KEY (account_id) 
    REFERENCES Account(account_id)
);
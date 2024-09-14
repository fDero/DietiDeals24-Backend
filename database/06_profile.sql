
CREATE TABLE PersonalLink (
    link        TEXT NOT NULL,
    description TEXT NOT NULL,
    account_id  INT NOT NULL,
    link_id     SERIAL PRIMARY KEY,

    UNIQUE (account_id, link),
    
    FOREIGN KEY (account_id) 
    REFERENCES Account(account_id)
);
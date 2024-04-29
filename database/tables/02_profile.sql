
CREATE TABLE PersonalLink (
    link        TEXT NOT NULL,
    description TEXT NOT NULL,
    account_id  INT NOT NULL,
    
    PRIMARY KEY (account_id, link),
    
    FOREIGN KEY (account_id) 
    REFERENCES Account(id)
);

CREATE TABLE ContactInformation (
    account_id  INT NOT NULL,
    email       TEXT NOT NULL,
    phone       VARCHAR(20) NOT NULL,

    PRIMARY KEY (account_id, email, phone),
    
    FOREIGN KEY (account_id) 
    REFERENCES Account(id)
);
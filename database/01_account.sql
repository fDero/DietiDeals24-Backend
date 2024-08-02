
CREATE TABLE Account ( 
    name                 VARCHAR(30) NOT NULL,
	surname              VARCHAR(40) NOT NULL,
	birthday             DATE        NOT NULL,
	country              VARCHAR(5)  NOT NULL,
    city                 VARCHAR(50) NOT NULL,
	email                VARCHAR(50) NOT NULL,
    username             VARCHAR(30) NOT NULL,
	
    account_creation     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    profile_picture_url  VARCHAR(100),
    bio                  TEXT,

    UNIQUE(username),
    UNIQUE(email),
    
    account_id           SERIAL PRIMARY KEY
);

CREATE TABLE Password (
    password_last_change TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,    
    password_hash        VARCHAR(64) NOT NULL,
    password_salt        VARCHAR(10) NOT NULL,
    account_id           INT NOT NULL,

    password_id          SERIAL PRIMARY KEY,

    FOREIGN KEY (account_id) 
    REFERENCES Account(account_id) ON DELETE CASCADE
);
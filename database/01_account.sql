
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

    account_provider     VARCHAR(40) NOT NULL DEFAULT 'DIETIDEALS24',

    account_id           SERIAL PRIMARY KEY,

    UNIQUE(username),
    UNIQUE(email)
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

CREATE TABLE OAuthAccountBinding (
    
    internal_account_id       INT NOT NULL,
    oauth_account_id          TEXT NOT NULL,
    oauth_provider            TEXT NOT NULL,
    oauth_binding_pk          SERIAL PRIMARY KEY,

    FOREIGN KEY (internal_account_id)
    REFERENCES Account(account_id) ON DELETE CASCADE
);
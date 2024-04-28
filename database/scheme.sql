CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE TABLE Account ( 
    name     VARCHAR(30) NOT NULL,
	surname  VARCHAR(40) NOT NULL,
	birthday Date        NOT NULL,
	country  VARCHAR(5)  NOT NULL,
	iban     VARCHAR(34) NOT NULL,
	email    VARCHAR(50) NOT NULL,
	phone    VARCHAR(10) NOT NULL,
	id       SERIAL      NOT NULL
);

INSERT INTO Account (name, surname, birthday, country, iban, email, phone)
VALUES
('John', 'Smith', '1980-05-12', 'US', 'US123456789', 'john.smith@example.com', '1234567890'),
('Emma', 'Johnson', '1992-08-25', 'GB', 'GB987654321', 'emma.johnson@example.com', '9876543210'),
('William', 'Brown', '1975-11-04', 'US', 'US567890123', 'william.brown@example.com', '5678901234'),
('Olivia', 'Williams', '1988-09-30', 'CA', 'CA345678901', 'olivia.williams@example.com', '3456789012'),
('James', 'Jones', '1995-03-22', 'AU', 'AU678901234', 'james.jones@example.com', '6789012345'),
-- Add more sample data here...
-- Include additional randomly generated names and details to reach a total of 50 accounts.
('Sophia', 'Davis', '1982-07-14', 'DE', 'DE456789012', 'sophia.davis@example.com', '4567890123');



CREATE TABLE Resource (
    resource_url             TEXT NOT NULL,
    resource_key             TEXT NOT NULL,
    upload_timestamp         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmation_timestamp   TIMESTAMP DEFAULT NULL,
    resource_id              SERIAL PRIMARY KEY
);

CREATE TABLE Bid (
    auction_id            INT NOT NULL,
    bidder_id             INT NOT NULL,
    bid_amount            NUMERIC NOT NULL,
    bid_date              TIMESTAMP NOT NULL,
    bid_iban_id           INT,
    bid_id                SERIAL PRIMARY KEY,
    bid_refound_token     VARCHAR(50),

    FOREIGN KEY (auction_id) 
    REFERENCES Auction(auction_id),

    FOREIGN KEY (bidder_id) 
    REFERENCES Account(account_id)
);

CREATE TABLE Auction (
    price               NUMERIC NOT NULL,
    creator_id          INT NOT NULL,
    country             VARCHAR(5)  NOT NULL,
    city                VARCHAR(50) NOT NULL,
    item_condition      TEXT,
    item_category       TEXT,
    macro_category      TEXT,
    start_time          TIMESTAMP NOT NULL,
    end_time            TIMESTAMP NOT NULL,
    item_name           TEXT NOT NULL,
    description         TEXT NOT NULL,
    pictures_urls       TEXT[],
    auction_type        TEXT NOT NULL,
    auction_id          SERIAL PRIMARY KEY,
    
    FOREIGN KEY (creator_id)
    REFERENCES Account(account_id)    
);


CREATE VIEW ReverseAuction AS (
    SELECT 
        price AS max_price, creator_id, country, city, item_condition 
        item_category, macro_category, start_time, end_time, item_name,
        description, pictures_urls, auction_id
    FROM 
        Auction WHERE auction_type = 'reverse'
);


CREATE VIEW SilentAuction AS (
    SELECT 
        price AS minimum_bid, creator_id, country, city, item_condition 
        item_category, macro_category, start_time, end_time, item_name,
        description, pictures_urls, auction_id
    FROM 
        Auction WHERE auction_type = 'silent'
);


CREATE TABLE SilentAuctionBid (
    silent_auction_id INT NOT NULL,
    bidder_id         INT NOT NULL,
    bid               NUMERIC NOT NULL,
    bid_date          TIMESTAMP NOT NULL,

    PRIMARY KEY (silent_auction_id, bidder_id),
    
    FOREIGN KEY (silent_auction_id) 
    REFERENCES Auction(auction_id),

    FOREIGN KEY (bidder_id) 
    REFERENCES Account(account_id)
);

CREATE TABLE ReverseAuctionBid (
    reverse_auction_id INT NOT NULL,
    bidder_id         INT NOT NULL,
    bid               NUMERIC NOT NULL,
    bid_date          TIMESTAMP NOT NULL,

    PRIMARY KEY (reverse_auction_id, bidder_id),
    
    FOREIGN KEY (reverse_auction_id) 
    REFERENCES Auction(auction_id),

    FOREIGN KEY (bidder_id) 
    REFERENCES Account(account_id)
);
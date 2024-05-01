
CREATE TABLE ReverseAuction (
    max_price           NUMERIC NOT NULL,
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
    creation_time       TIMESTAMP NOT NULL,
    reverse_auction_id  INT PRIMARY KEY,
    
    FOREIGN KEY (creator_id)
    REFERENCES Account(account_id)
);

CREATE TABLE SilentAuction (
    minimum_bid        NUMERIC NOT NULL,
    creator_id         INT NOT NULL,
    country            VARCHAR(5)  NOT NULL,
    city               VARCHAR(50) NOT NULL,
    condition          TEXT,
    category           TEXT,
    macro_category     TEXT,
    start_date         TIMESTAMP NOT NULL,
    end_date           TIMESTAMP NOT NULL,
    title              TEXT NOT NULL,
    description        TEXT NOT NULL,
    pictures_urls      TEXT[],
    creation_time      TIMESTAMP NOT NULL,
    silent_auction_id  INT PRIMARY KEY,

    FOREIGN KEY (creator_id)
    REFERENCES Account(account_id)
);

CREATE TABLE SilentAuctionBid (
    silent_auction_id INT NOT NULL,
    bidder_id         INT NOT NULL,
    bid               NUMERIC NOT NULL,
    bid_date          TIMESTAMP NOT NULL,

    PRIMARY KEY (silent_auction_id, bidder_id),
    
    FOREIGN KEY (silent_auction_id) 
    REFERENCES SilentAuction(silent_auction_id),

    FOREIGN KEY (bidder_id) 
    REFERENCES Account(account_id)
);

CREATE TABLE ReverseAuctionBid (
    silent_auction_id INT NOT NULL,
    bidder_id         INT NOT NULL,
    bid               NUMERIC NOT NULL,
    bid_date          TIMESTAMP NOT NULL,

    PRIMARY KEY (silent_auction_id, bidder_id),
    
    FOREIGN KEY (silent_auction_id) 
    REFERENCES SilentAuction(silent_auction_id),

    FOREIGN KEY (bidder_id) 
    REFERENCES Account(account_id)
);
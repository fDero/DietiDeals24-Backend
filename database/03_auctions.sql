
CREATE TABLE AuctionStatus (
    status_descriptor VARCHAR(20) PRIMARY KEY
);

CREATE TABLE Category (
    macro_category VARCHAR(50) NOT NULL,
    item_category  VARCHAR(50) NOT NULL,
    category_id    SERIAL PRIMARY KEY
);

CREATE TABLE AuctionType (
    auction_type VARCHAR(50) NOT NULL    
);

CREATE TABLE Auction (
    maximum_bid         NUMERIC,
    minimum_bid         NUMERIC,
    lowest_bid_so_far   NUMERIC,
    highest_bid_so_far  NUMERIC,
    current_bidder_id   INT,
    number_of_bids      INT NOT NULL DEFAULT 0,
    creator_id          INT NOT NULL,
    country             VARCHAR(5)  NOT NULL,
    city                VARCHAR(50) NOT NULL,
    item_condition      TEXT,
    item_category       TEXT NOT NULL,
    macro_category      TEXT NOT NULL,
    start_time          TIMESTAMP NOT NULL,
    end_time            TIMESTAMP NOT NULL,
    item_name           TEXT NOT NULL,
    description         TEXT,
    pictures_urls       TEXT[],
    auction_type        TEXT NOT NULL,
    currency            VARCHAR(3) NOT NULL,
    status              TEXT NOT NULL DEFAULT 'active',
    auction_id          SERIAL PRIMARY KEY,

    
    FOREIGN KEY (status)
    REFERENCES AuctionStatus(status_descriptor),

    FOREIGN KEY (current_bidder_id)
    REFERENCES Account(account_id),

    FOREIGN KEY (creator_id)
    REFERENCES Account(account_id)
);

CREATE FUNCTION get_trending_categories()
RETURNS SETOF Category AS $$
BEGIN
    RETURN QUERY
    SELECT c.*
    FROM Category c LEFT JOIN Auction a ON c.item_category = a.item_category
    WHERE c.item_category != c.macro_category
    AND a.status = 'active'
    GROUP BY c.category_id
    ORDER BY COUNT(c.category_id) DESC;
END;
$$ LANGUAGE plpgsql;
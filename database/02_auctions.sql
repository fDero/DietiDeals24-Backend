
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
    description         TEXT NOT NULL,
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


CREATE TABLE Bid (
    auction_id  INT NOT NULL,
    bidder_id   INT NOT NULL,
    bid_amount  NUMERIC NOT NULL,
    bid_date    TIMESTAMP NOT NULL,
    bid_id      SERIAL PRIMARY KEY,

    FOREIGN KEY (auction_id) 
    REFERENCES Auction(auction_id),

    FOREIGN KEY (bidder_id) 
    REFERENCES Account(account_id)
);

CREATE VIEW Activity AS (
    SELECT DISTINCT ON (a.auction_id)
        b.bid_id,
        b.bidder_id,
        b.bid_amount,
        b.bid_date,
        a.maximum_bid,
        a.minimum_bid,
        a.lowest_bid_so_far,
        a.highest_bid_so_far,
        a.current_bidder_id,
        a.number_of_bids,
        a.creator_id,
        a.country,
        a.city,
        a.item_condition,
        a.item_category,
        a.macro_category,
        a.start_time,
        a.end_time,
        a.item_name,
        a.description,
        a.pictures_urls,
        a.auction_type,
        a.currency,
        a.status,
        a.auction_id
    FROM 
        Bid b RIGHT OUTER JOIN Auction a 
            ON b.auction_id = a.auction_id
    ORDER BY 
        a.auction_id, 
        b.bid_id DESC
);

CREATE FUNCTION get_user_activities(
    user_id INT, include_past_deals BOOLEAN, include_current_deals BOOLEAN,
    include_auctions BOOLEAN, include_bids BOOLEAN
)
RETURNS SETOF Activity AS $$
BEGIN
    RETURN QUERY
    SELECT a.* 
    FROM Activity a
    WHERE
        (   
            (include_current_deals AND (a.status = 'active' OR a.status = 'pending')) OR
            (include_past_deals AND (a.status = 'closed'))
        ) 
        AND 
        (
            (include_auctions AND a.creator_id = user_id )OR 
            (include_bids AND a.current_bidder_id = user_id) OR
            (include_bids AND a.bidder_id = user_id AND status = 'active' AND NOT EXISTS (
                SELECT b.bid_id 
                FROM Bid b
                WHERE 
                    b.auction_id = a.auction_id AND 
                    b.bidder_id = user_id AND 
                    b.bid_amount > a.bid_amount
                )
            )
        );
END;
$$
LANGUAGE plpgsql;
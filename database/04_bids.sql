
CREATE TABLE Bid (
    auction_id            INT NOT NULL,
    bidder_id             INT NOT NULL,
    bid_amount            NUMERIC NOT NULL,
    bid_date              TIMESTAMP NOT NULL,
    bid_id                SERIAL PRIMARY KEY,
    payment_informations  VARCHAR(50),

    FOREIGN KEY (auction_id) 
    REFERENCES Auction(auction_id),

    FOREIGN KEY (bidder_id) 
    REFERENCES Account(account_id)
);

CREATE FUNCTION get_user_bids(
    bidderId INT,
    auctionStatus VARCHAR(20)
)
RETURNS INT AS $$
DECLARE
    total_bids INT;
BEGIN
    SELECT COUNT(*)
    INTO total_bids
    FROM Bid b
    JOIN Auction a ON a.auction_id = b.auction_id
    WHERE 
        b.bidder_id = bidderId AND
        a.status = auctionStatus;
    RETURN total_bids;
END;
$$ LANGUAGE plpgsql;
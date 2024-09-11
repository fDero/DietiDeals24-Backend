
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
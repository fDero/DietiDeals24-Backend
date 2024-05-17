
INSERT INTO AuctionType (auction_type) VALUES 
    ('silent'),
    ('reverse');

INSERT INTO Category (macro_category, item_category) VALUES 
    ('product', 'Art and antiques'),
    ('product', 'Bikes'),
    ('product', 'Boating and boats'),
    ('product', 'Books'),
    ('product', 'Cars'),
    ('product', 'Cellphones'),
    ('product', 'Clothing'),
    ('product', 'Coins and banknotes'),
    ('product', 'Collectionism'),
    ('product', 'Comics'),
    ('product', 'Desktop PCs and Laptops'),
    ('product', 'Garden and outdoor furniture'),
    ('product', 'Home furniture'),
    ('product', 'Household appliances'),
    ('product', 'Informatics'),
    ('product', 'Jewelry'),
    ('product', 'Movies and DVDs'),
    ('product', 'Musical instruments'),
    ('product', 'Music, CD and vinils'),
    ('product', 'Tablets'),
    ('product', 'Telephony'),
    ('product', 'Tickets and events'),
    ('product', 'Toys'),
    ('product', 'TV'),
    ('product', 'Videogames and consoles'),
    ('product', 'Watches'),
    ('service', 'Audio editing'),
    ('service', 'Business'),
    ('service', 'Cleaning'),
    ('service', 'Data analytics'),
    ('service', 'Data processing'),
    ('service', 'Graphic design'),
    ('service', 'Marketing advice'),
    ('service', 'Photography'),
    ('service', 'Programming and tech'),
    ('service', 'School Tutoring'),
    ('service', 'Socials'),
    ('service', 'Video and animation'),
    ('service', 'Voice over'),
    ('service', 'Writing and translation');



INSERT INTO Account 
    (name, surname, birthday, country, city, email, username, password_hash, password_salt, password_last_change, account_creation,
    last_login, profile_picture_url, bio, unread_notifications_counter, online_auctions_counter, past_deals_counter)

VALUES 
    ('John',   'Doe',      '1990-01-01', 'UK', 'New York', 'john.doe@example.com',        'johndoe', 'hash123', 'salt123', '2022-01-01', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO', 0, 0, 0),
    ('Mike',   'Gordon',   '1990-01-01', 'UK', 'New York', 'mike.gordon@example.com',     'johndoe', 'hash123', 'salt123', '2022-01-01', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO', 0, 0, 0),
    ('Samuel', 'Stafford', '1990-01-01', 'UK', 'New York', 'samuel.stafford@example.com', 'johndoe', 'hash123', 'salt123', '2022-01-01', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO', 0, 0, 0),
    ('Mario',  'Rossi',    '1990-01-01', 'IT', 'New York', 'mario.rossi@example.com',     'johndoe', 'hash123', 'salt123', '2022-01-01', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO', 0, 0, 0);




INSERT INTO Auction
    (maximum_bid, minimum_bid, lowest_bid_so_far, creator_id, country, city, item_condition, item_category, macro_category, start_time, end_time, item_name, description, pictures_urls, auction_type, currency)
VALUES
    (NULL, 100, NULL, 1, 'UK', 'London', 'New', 'Electronics', 'product', '2025-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR'),
    (NULL, 100, NULL, 1, 'UK', 'London', 'New', 'Electronics', 'product', '2025-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR'),
    (NULL, 100, NULL, 1, 'UK', 'London', 'New', 'Electronics', 'product', '2025-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR');

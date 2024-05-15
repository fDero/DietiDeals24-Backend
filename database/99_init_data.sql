
INSERT INTO AuctionType (auction_type) VALUES 
    ('Silent'),
    ('Reverse');

INSERT INTO Category (macro_category, item_category) VALUES 
    ('Product', 'Art and antiques'),
    ('Product', 'Bikes'),
    ('Product', 'Boating and boats'),
    ('Product', 'Books'),
    ('Product', 'Cars'),
    ('Product', 'Cellphones'),
    ('Product', 'Clothing'),
    ('Product', 'Coins and banknotes'),
    ('Product', 'Collectionism'),
    ('Product', 'Comics'),
    ('Product', 'Desktop PCs and Laptops'),
    ('Product', 'Garden and outdoor furniture'),
    ('Product', 'Home furniture'),
    ('Product', 'Household appliances'),
    ('Product', 'Informatics'),
    ('Product', 'Jewelry'),
    ('Product', 'Movies and DVDs'),
    ('Product', 'Musical instruments'),
    ('Product', 'Music, CD and vinils'),
    ('Product', 'Tablets'),
    ('Product', 'Telephony'),
    ('Product', 'Tickets and events'),
    ('Product', 'Toys'),
    ('Product', 'TV'),
    ('Product', 'Videogames and consoles'),
    ('Product', 'Watches'),
    ('Serice',  'Audio editing'),
    ('Serice',  'Business'),
    ('Serice',  'Cleaning'),
    ('Serice',  'Data analytics'),
    ('Serice',  'Data processing'),
    ('Serice',  'Graphic design'),
    ('Serice',  'Marketing advice'),
    ('Serice',  'Photography'),
    ('Serice',  'Programming and tech'),
    ('Serice',  'School Tutoring'),
    ('Serice',  'Socials'),
    ('Serice',  'Video and animation'),
    ('Serice',  'Voice over'),
    ('Serice',  'Writing and translation');



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
    (NULL, 100, NULL, 1, 'UK', 'London', 'New', 'Electronics', 'Product', '2025-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'Silent', 'EUR'),
    (NULL, 100, NULL, 1, 'UK', 'London', 'New', 'Electronics', 'Product', '2025-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'Silent', 'EUR'),
    (NULL, 100, NULL, 1, 'UK', 'London', 'New', 'Electronics', 'Product', '2025-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'Silent', 'EUR');

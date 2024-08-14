
INSERT INTO AuctionStatus (status_descriptor) VALUES 
    ('active'),
    ('pending'),
    ('aborted'),
    ('rejected'),
    ('closed');

INSERT INTO AuctionType (auction_type) VALUES 
    ('silent'),
    ('reverse');

INSERT INTO NotificationType (notification_type) VALUES 
    ('auction-over'),
    ('auction-expired'),
    ('auction-aborted'),
    ('new-bid'),
    ('winning-bid'),
    ('out-bid'),
    ('bid-rejected');

INSERT INTO Category (macro_category, item_category) VALUES 
    ('products', 'Art and antiques'),
    ('products', 'Bikes'),
    ('products', 'Boating and boats'),
    ('products', 'Books'),
    ('products', 'Cars'),
    ('products', 'Cellphones'),
    ('products', 'Clothing'),
    ('products', 'Coins and banknotes'),
    ('products', 'Collectionism'),
    ('products', 'Comics'),
    ('products', 'Desktop PCs and Laptops'),
    ('products', 'Garden and outdoor furniture'),
    ('products', 'Home furniture'),
    ('products', 'Household appliances'),
    ('products', 'Informatics'),
    ('products', 'Jewelry'),
    ('products', 'Movies and DVDs'),
    ('products', 'Musical instruments'),
    ('products', 'Music, CD and vinils'),
    ('products', 'Tablets'),
    ('products', 'Telephony'),
    ('products', 'Tickets and events'),
    ('products', 'Toys'),
    ('products', 'TV'),
    ('products', 'Videogames and consoles'),
    ('products', 'Watches'),
    ('services', 'Audio editing'),
    ('services', 'Business'),
    ('services', 'Cleaning'),
    ('services', 'Data analytics'),
    ('services', 'Data processing'),
    ('services', 'Graphic design'),
    ('services', 'Marketing advice'),
    ('services', 'Photography'),
    ('services', 'Programming and tech'),
    ('services', 'School Tutoring'),
    ('services', 'Socials'),
    ('services', 'Video and animation'),
    ('services', 'Voice over'),
    ('services', 'Writing and translation');

INSERT INTO Account 
    (name, surname, birthday, country, city, email, username, account_creation, last_login, profile_picture_url, bio)
VALUES 
    ('John',     'Doe',        '1990-01-01', 'UK', 'New York', 'john.doe@example.com',        'johndoe', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO'),
    ('Mike',     'Gordon',     '1990-01-01', 'UK', 'New York', 'mike.gordon@example.com',     'mikeg',   '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO'),
    ('Samuel',   'Stafford',   '1990-01-01', 'UK', 'New York', 'samuel.stafford@example.com', 'samuels', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO'),
    ('Mario',    'Rossi',      '1990-01-01', 'IT', 'New York', 'mario.rossi@example.com',     'marior',  '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO'),
    ('Sarah',    'Parker',     '1990-01-01', 'UK', 'New York', 'sarah.parker@example.com',    'sarahp',  '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO'),
    ('Vincent',  'Gordon',     '1990-01-01', 'UK', 'New York', 'vincent.gordon@example.com',  'vincentg','2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO'),
    ('Robert',   'Nerris',     '1990-01-01', 'UK', 'New York', 'robert.nerris@example.com',   'robertn', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO'),
    ('Ruben',    'Randolf',    '1990-01-01', 'UK', 'New York', 'ruber.randolf@example.com',   'ruberr',  '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO'),
    ('Leonardo', 'Nuccio',     '1990-01-01', 'IT', 'New York', 'sik.kia@example.com',         'sikkia',  '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO');
    
INSERT INTO Password 
    (account_id, password_hash, password_salt)
VALUES 
    (1, '6fdbf8a0615321a0664e6678a6114c596c608918982f7d2f2663b0dbd0aa459a', 'lRbmWPn36v'),
    (2, '6fdbf8a0615321a0664e6678a6114c596c608918982f7d2f2663b0dbd0aa459a', 'lRbmWPn36v'),
    (3, '6fdbf8a0615321a0664e6678a6114c596c608918982f7d2f2663b0dbd0aa459a', 'lRbmWPn36v'),
    (4, '6fdbf8a0615321a0664e6678a6114c596c608918982f7d2f2663b0dbd0aa459a', 'lRbmWPn36v'),
    (5, '6fdbf8a0615321a0664e6678a6114c596c608918982f7d2f2663b0dbd0aa459a', 'lRbmWPn36v'),
    (6, '6fdbf8a0615321a0664e6678a6114c596c608918982f7d2f2663b0dbd0aa459a', 'lRbmWPn36v'),
    (7, '6fdbf8a0615321a0664e6678a6114c596c608918982f7d2f2663b0dbd0aa459a', 'lRbmWPn36v'),
    (8, '6fdbf8a0615321a0664e6678a6114c596c608918982f7d2f2663b0dbd0aa459a', 'lRbmWPn36v'),
    (9, '6fdbf8a0615321a0664e6678a6114c596c608918982f7d2f2663b0dbd0aa459a', 'lRbmWPn36v');

INSERT INTO Auction
    (maximum_bid, minimum_bid, number_of_bids, lowest_bid_so_far, creator_id, country, city, item_condition, item_category, macro_category, start_time, end_time, item_name, description, pictures_urls, auction_type, currency, status, winner_id)
VALUES
    (NULL, 100, 2, NULL, id_by_username('johndoe'),  'UK', 'London', 'new', 'Electronics', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 1, NULL, id_by_username('mikeg'),    'UK', 'Brighton', 'used', 'Cars', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'Mini Countrymen', '163 Horse Power, 50000km', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 3, NULL, id_by_username('marior'),   'IT', 'Milano', 'as-new', 'Electronics', 'products', '2023-01-01 00:00:00', '2024-03-10 00:00:00', 'Ryzen 7 7700X', 'Still in the original box', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'closed', NULL),
    (NULL, 100, 3, NULL, id_by_username('marior'),   'IT', 'Milano', 'broken', 'Videogames and consoles', 'products', '2023-01-01 00:00:00', '2023-10-10 00:00:00', 'Old PS4', 'The controller fails to connect to it', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'rejected', NULL),
    (100, NULL, 9, 60,   id_by_username('marior'),   'IT', 'Napoli', NULL, 'School Tutoring', 'service', '2023-01-01 00:00:00', '2023-10-10 00:00:00', 'Math tutoring for my son', 'high-school math, calculus and polynomial algebra', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'reverse', 'EUR', 'closed', NULL),
    (NULL, 100, 2, NULL, id_by_username('sarahp'),   'UK', 'Manchester', 'used', 'Clothing', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'Designer Jacket', 'High-end designer jacket', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 1, NULL, id_by_username('vincentg'), 'UK', 'Liverpool', 'new', 'Home furniture', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'Modern Sofa', 'Brand new modern sofa', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 3, NULL, id_by_username('robertn'),  'UK', 'Birmingham', 'used', 'Musical instruments', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'Electric Guitar', 'Used electric guitar in good condition', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 3, NULL, id_by_username('ruberr'),   'UK', 'Leeds', 'new', 'Toys', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'LEGO Set', 'Brand new LEGO set', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 3, NULL, id_by_username('sikkia'),   'IT', 'Napoli', 'used', 'Books', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'Harry Potter Full Saga', 'Used books in good condition', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 3, NULL, id_by_username('sarahp'),   'UK', 'Bristol', 'as-new', 'Bikes', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'Mountain Bike', 'Almost new mountain bike', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'aborted', NULL),
    (NULL, 100, 3, NULL, id_by_username('vincentg'), 'UK', 'Edinburgh', 'used', 'Coins and banknotes', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'Rare Coin', 'Rare coin from the 18th century', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 3, NULL, id_by_username('robertn'),  'UK', 'Glasgow', 'new', 'Music, CD and vinils', 'products', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'Vinyl Record', 'Brand new vinyl record', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 3, NULL, id_by_username('ruberr'),   'UK', 'Cardiff', 'used', 'Telephony', 'products', '2023-01-01 00:00:00', '2023-10-10 00:00:00', 'Samsung Galaxy S21', 'Used Samsung Galaxy S21 in good condition', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (NULL, 100, 3, NULL, id_by_username('sikkia'),   'IT', 'Napoli', 'new', 'Movies and DVDs', 'products', '2023-01-01 00:00:00', '2023-10-10 00:00:00', 'Blu-ray Movie', 'Brand new Blu-ray movie', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent', 'EUR', 'active', NULL),
    (100, NULL, 9, 40,   id_by_username('marior'),   'IT', 'Napoli', NULL, 'School Tutoring', 'service', '2023-01-01 00:00:00', '2025-10-10 00:00:00', 'Math tutoring for my son', 'high-school math, calculus and polynomial algebra', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'reverse', 'EUR', 'active', NULL);

INSERT INTO NotificationData
    (auction_id, notification_type, account_id, visualized, eliminated)
VALUES
    (3, 'auction-over',     id_by_username('johndoe'),  FALSE, FALSE),
    (4, 'auction-expired',  id_by_username('johndoe'),  FALSE, FALSE),
    (5, 'auction-over',     id_by_username('sikkia'),   FALSE, FALSE),
    (4, 'out-bid',          id_by_username('robertn'),  TRUE,  FALSE),
    (4, 'winning-bid',      id_by_username('sikkia'),   FALSE, FALSE),
    (11, 'out-bid',         id_by_username('vincentg'), TRUE,  FALSE),
    (11, 'auction-aborted', id_by_username('vincentg'), TRUE,  FALSE),
    (15, 'bid-rejected',    id_by_username('vincentg'), TRUE,  FALSE),
    (14, 'bid-rejected',    id_by_username('marior'),   FALSE, FALSE),
    (14, 'bid-rejected',    id_by_username('marior'),   FALSE, FALSE),
    (14, 'bid-rejected',    id_by_username('marior'),   FALSE, FALSE),
    (14, 'bid-rejected',    id_by_username('marior'),   FALSE, FALSE),
    (14, 'bid-rejected',    id_by_username('marior'),   FALSE, FALSE),
    (14, 'bid-rejected',    id_by_username('marior'),   FALSE, FALSE);
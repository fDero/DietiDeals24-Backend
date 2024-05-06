
INSERT INTO Account 
    (name, surname, birthday, country, city, email, username, password_hash, password_salt, password_last_change, account_creation,
    last_login, profile_picture_url, bio, unread_notifications_counter, online_auctions_counter, past_deals_counter)

VALUES 
    ('John',   'Doe',      '1990-01-01', 'UK', 'New York', 'john.doe@example.com',        'johndoe', 'hash123', 'salt123', '2022-01-01', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO', 0, 0, 0),
    ('Mike',   'Gordon',   '1990-01-01', 'UK', 'New York', 'mike.gordon@example.com',     'johndoe', 'hash123', 'salt123', '2022-01-01', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO', 0, 0, 0),
    ('Samuel', 'Stafford', '1990-01-01', 'UK', 'New York', 'samuel.stafford@example.com', 'johndoe', 'hash123', 'salt123', '2022-01-01', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO', 0, 0, 0),
    ('Mario',  'Rossi',    '1990-01-01', 'IT', 'New York', 'mario.rossi@example.com',     'johndoe', 'hash123', 'salt123', '2022-01-01', '2022-01-01', '2022-01-01', 'https://example.com/profile.jpg', 'My Cool BIO', 0, 0, 0);




INSERT INTO Auction
    (price, creator_id, country, city, item_condition, item_category, macro_category, start_time, end_time, item_name, description, pictures_urls, auction_type)
VALUES
    (100, 1, 'UK', 'London', 'New', 'Electronics', 'Product', '2025-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent'),
    (100, 1, 'UK', 'London', 'New', 'Electronics', 'Product', '2025-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent'),
    (100, 1, 'UK', 'London', 'New', 'Electronics', 'Product', '2025-01-01 00:00:00', '2025-10-10 00:00:00', 'iPhone 13', 'Brand new iPhone 13', '{https://example.com/pic1.jpg, https://example.com/pic2.jpg}', 'silent');

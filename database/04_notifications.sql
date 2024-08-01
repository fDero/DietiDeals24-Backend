
CREATE TABLE NotificationType (
    notification_type VARCHAR(20) PRIMARY KEY
);

CREATE TABLE NotificationData (
    
    auction_id          INT,
    notification_type   VARCHAR(20) NOT NULL,
    account_id          INT NOT NULL,
    notification_id     SERIAL PRIMARY KEY,
    visualized          BOOLEAN NOT NULL DEFAULT FALSE,
    eliminated          BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (account_id) 
    REFERENCES Account(account_id),

    FOREIGN KEY (notification_type)
    REFERENCES NotificationType(notification_type),

    FOREIGN KEY (auction_id)
    REFERENCES Auction(auction_id)
);

CREATE VIEW Notification AS (
    SELECT 
        NotificationData.notification_type,
        NotificationData.account_id,
        NotificationData.notification_id,
        NotificationData.visualized,
        NotificationData.eliminated,
        Auction.*
    FROM NotificationData, Auction
    WHERE NotificationData.auction_id = Auction.auction_id
);
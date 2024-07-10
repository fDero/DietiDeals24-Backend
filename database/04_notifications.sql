
CREATE TABLE NotificationData (
    
    auction_id          INT,
    notification_type   VARCHAR(20) NOT NULL,
    account_id          INT NOT NULL,
    notification_id     SERIAL PRIMARY KEY,
    visualized          BOOLEAN NOT NULL DEFAULT FALSE,
    eliminated          BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (account_id) 
    REFERENCES Account(account_id),

    FOREIGN KEY (auction_id)
    REFERENCES Auction(auction_id)
);

CREATE VIEW Notification AS (
    SELECT Account.email, NotificationData.*
    FROM   Account, NotificationData
    WHERE  Account.account_id = NotificationData.account_id
);
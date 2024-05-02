
CREATE TABLE NotificationData (
    
    silent_auction_id   INT,
    reverse_auction_id  INT,
    notification_type   VARCHAR(20) NOT NULL,
    account_id          INT NOT NULL,
    notification_id     SERIAL PRIMARY KEY,
    visualized          BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (account_id) 
    REFERENCES Account(account_id),

    FOREIGN KEY (silent_auction_id)
    REFERENCES SilentAuction(silent_auction_id),

    FOREIGN KEY (reverse_auction_id)
    REFERENCES ReverseAuction(reverse_auction_id)
);

CREATE VIEW Notification AS (
    SELECT Account.email, NotificationData.*
    FROM   Account, NotificationData
    WHERE  Account.account_id = NotificationData.account_id
);
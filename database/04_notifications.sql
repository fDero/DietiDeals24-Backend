
CREATE TABLE NotificationType (
    notification_type VARCHAR(20) PRIMARY KEY
);

CREATE TABLE Notification (
    
    auction_id          INT,
    notification_type   VARCHAR(20) NOT NULL,
    account_username    VARCHAR(30) NOT NULL,
    notification_id     SERIAL PRIMARY KEY,
    visualized          BOOLEAN NOT NULL DEFAULT FALSE,
    eliminated          BOOLEAN NOT NULL DEFAULT FALSE,


    FOREIGN KEY (account_username) 
    REFERENCES Account(username),

    FOREIGN KEY (notification_type)
    REFERENCES NotificationType(notification_type),

    FOREIGN KEY (auction_id)
    REFERENCES Auction(auction_id)
);
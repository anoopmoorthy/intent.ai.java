CREATE TABLE exchange_metrics (
    id				INT AUTO_INCREMENT PRIMARY KEY,
    iab				VARCHAR(32) ,
    min_bid_price	DOUBLE ,
    max_bid_price	DOUBLE ,
    avg_bid_price	DOUBLE ,    
    device_type		INT ,
    revenue			INT ,
    response_rate	INT 
);

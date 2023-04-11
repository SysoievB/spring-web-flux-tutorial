CREATE TABLE IF NOT EXISTS customers
(
    id   INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    age  INT,
    job  VARCHAR(255),
    PRIMARY KEY (id)
);
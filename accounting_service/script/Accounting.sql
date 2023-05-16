CREATE SCHEMA IF NOT EXISTS accounting;
USE accounting;

DROP TABLE users;

CREATE TABLE IF NOT EXISTS users( id INT NOT NULL AUTO_INCREMENT,
                                         email text NOT NULL,
                                         account DECIMAL NOT NULL,
                                         PRIMARY KEY (id));

INSERT INTO users (id, email, account) VALUES (1, 'test@email.com', 10000);

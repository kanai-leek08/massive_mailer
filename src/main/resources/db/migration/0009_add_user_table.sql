create table if NOT EXISTS users(
    id          INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    email       VARCHAR(50),
    password    VARCHAR(100)
)
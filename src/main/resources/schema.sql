DROP TABLE IF EXISTS ADDRESS_BOOK;
CREATE TABLE ADDRESS_BOOK (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    FIRST_NAME  VARCHAR(50) NOT NULL,
    LAST_NAME   VARCHAR(50) NOT NULL,
    PHONE       VARCHAR(20),
    BIRTHDAY    DATE
)
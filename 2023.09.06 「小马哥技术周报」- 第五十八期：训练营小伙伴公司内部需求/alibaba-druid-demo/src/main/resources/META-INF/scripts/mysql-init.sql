-- 数据库创建
DROP DATABASE IF EXISTS test_db;
CREATE DATABASE IF NOT EXISTS test_db;
-- 切换数据库
USE test_db;

-- 数据表准备
CREATE TABLE users (
    id INT(10) AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    amt_sold INT(10) DEFAULT 0,
    amt_bought INT(10) DEFAULT 0
);

INSERT INTO users(id,name) VALUES (1,"seller");
INSERT INTO users(id,name) VALUES (2,"buyer");

CREATE TABLE transactions (
    xid INT(10) AUTO_INCREMENT PRIMARY KEY,
    seller_id INT(10),
    buyer_id INT(10),
    amount INT(10) NOT NULL
);

CREATE TABLE tx_messages (
	  id INT(10) AUTO_INCREMENT PRIMARY KEY,
  	xid INT(10) NOT NULL,
    user_id INT(10) NOT NULL,
    amount INT(10) NOT NULL
);
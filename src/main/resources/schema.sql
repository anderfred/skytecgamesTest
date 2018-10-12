DROP DATABASE IF EXISTS obrunv7qq3jwq10d;
CREATE DATABASE obrunv7qq3jwq10d;
USE obrunv7qq3jwq10d;
CREATE TABLE player(
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL ,
  health int NOT NULL ,
  damage int NOT NULL ,
  rating int NOT NULL,
  ready boolean NOT NULL ,
  PRIMARY KEY (id)
)DEFAULT CHARSET=utf8;
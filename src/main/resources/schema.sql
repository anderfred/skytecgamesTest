DROP DATABASE IF EXISTS oa4wm3rd1n7vrr3l;
CREATE DATABASE oa4wm3rd1n7vrr3l;
USE oa4wm3rd1n7vrr3l;
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
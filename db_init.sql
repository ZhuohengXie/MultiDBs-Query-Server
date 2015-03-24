CREATE DATABASE  IF NOT EXISTS `infsci2711_tutorial`;
USE `infsci2711_tutorial`;

DROP TABLE IF EXISTS `Person`;

CREATE TABLE `Person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `test1`;
DROP TABLE IF EXISTS `test2`;

CREATE TABLE `test1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `test2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

INSERT INTO `test1`
VALUES (1,2);
INSERT INTO `test1`
VALUES (3,4);
INSERT INTO `test1`
VALUES (5,6);
INSERT INTO `test1`
VALUES (7,8);

INSERT INTO `test2`
VALUES (2,123);
INSERT INTO `test2`
VALUES (3,456);
INSERT INTO `test2`
VALUES (5,789);
INSERT INTO `test2`
VALUES (8,147);


CREATE USER `group2`@`localhost` IDENTIFIED BY `infsci2711`;
grant all privileges on *.* to `dataverse`@`localhost`;

CREATE USER `multidbsuser`@`%` IDENTIFIED BY `infsci2711`;
grant all privileges on *.* to `dataverse`@`%`;

FLUSH PRIVILEGES;
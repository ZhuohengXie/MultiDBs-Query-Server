CREATE DATABASE  IF NOT EXISTS `prestomatch` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `prestomatch`;
DROP TABLE IF EXISTS `matcher`;

CREATE TABLE `matcher` (
  `did` int(11) NOT NULL,
  `catalog` varchar(45) DEFAULT NULL,
  `dbname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`did`)
) ENGINE=InnoDB DEFAULT CHARSET=utf-8;

CREATE USER 'multidbsuser'@'%' IDENTIFIED BY 'infsci2711';
grant all privileges on *.* to 'multidbsuser'@'%';

FLUSH PRIVILEGES;


CREATE TABLE IF NOT EXISTS `czechcompanies` (
  `url` varchar(100) character set utf8 default NULL,
  `ico` bigint(10) unsigned NOT NULL,
  `name` varchar(250) character set utf8 default NULL,
  `kind` varchar(200) character set utf8 default NULL,
  `registration` date default NULL,
  `employees` varchar(50) character set utf8 default NULL,
  `activity` varchar(200) character set utf8 default NULL,
  `typeOfCompany` varchar(100) character set utf8 default NULL,
  `region` varchar(10) character set utf8 default NULL,
  PRIMARY KEY  (`ico`),
  KEY `kind` (`kind`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_czech_ci COMMENT='List of Czech companies as downloaded from CZSO';

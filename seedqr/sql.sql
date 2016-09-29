/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.14-log : Database - zzcx
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`zzcx` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `zzcx`;

/*Table structure for table `code_mapping` */

DROP TABLE IF EXISTS `code_mapping`;

CREATE TABLE `code_mapping` (
  `source_code` varchar(20) NOT NULL,
  `target_codes` varchar(2000) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`source_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `code_mapping` */

/*Table structure for table `qr_code` */

DROP TABLE IF EXISTS `qr_code`;

CREATE TABLE `qr_code` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `unit_code` bigint(19) NOT NULL DEFAULT '0',
  `company_code` varchar(4) DEFAULT NULL,
  `ext_unit_code` varchar(20) DEFAULT NULL,
  `seed_name` varchar(50) NOT NULL,
  `company_name` varchar(50) NOT NULL,
  `tracking_url` varchar(255) NOT NULL,
  `seed_id` int(11) NOT NULL DEFAULT '0',
  `status` int(4) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `bind_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qr_code` */

/*Table structure for table `seed_config` */

DROP TABLE IF EXISTS `seed_config`;

CREATE TABLE `seed_config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `seed_id` int(11) NOT NULL,
  `para_name` varchar(50) NOT NULL,
  `para_value` varchar(1000) NOT NULL,
  `type` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `seed_config` */

/*Table structure for table `seed_logistics` */

DROP TABLE IF EXISTS `seed_logistics`;

CREATE TABLE `seed_logistics` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `qr_code_id` int(11) NOT NULL,
  `ticket_no` varchar(30) DEFAULT NULL,
  `message` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `logistics_provider` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `seed_logistics` */

/*Table structure for table `seed_sales_info` */

DROP TABLE IF EXISTS `seed_sales_info`;

CREATE TABLE `seed_sales_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `qr_code_id` int(11) NOT NULL,
  `wholesaler_id` int(11) NOT NULL,
  `message` varchar(255) NOT NULL,
  `type` int(4) NOT NULL,
  `sale_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `seed_sales_info` */

/*Table structure for table `sequence` */

DROP TABLE IF EXISTS `sequence`;

CREATE TABLE `sequence` (
  `name` varchar(50) NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sequence` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `handphone` varchar(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `company_code` varchar(4) DEFAULT NULL COMMENT '公司编码，从1000开始到9000',
  `company_name` varchar(50) DEFAULT NULL COMMENT '生成经营者或进口商名称',
  `last_login_time` datetime DEFAULT NULL,
  `type` int(4) DEFAULT NULL COMMENT '1,生产厂家，2，批发商',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '谁的下属。谁创建的',
  `status` int(4) DEFAULT NULL COMMENT '1,激活状态，0，未激活',
  `region_id` int(8) DEFAULT '0' COMMENT '区域id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_index` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

/*Table structure for table `user_billing_setting` */

DROP TABLE IF EXISTS `user_billing_setting`;

CREATE TABLE `user_billing_setting` (
  `user_id` int(11) NOT NULL,
  `level` varchar(30) NOT NULL,
  `code_count` int(10) NOT NULL DEFAULT '0',
  `paid_amount` int(10) DEFAULT NULL,
  `total_count` int(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_billing_setting` */

/*Table structure for table `user_seed` */

DROP TABLE IF EXISTS `user_seed`;

CREATE TABLE `user_seed` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `seed_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_seed` */

/* Function  structure for function  `currval` */

/*!50003 DROP FUNCTION IF EXISTS `currval` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`zzcx`@`%` FUNCTION `currval`(seq_name VARCHAR(50)) RETURNS int(11)
    DETERMINISTIC
BEGIN
         DECLARE VALUE INTEGER;
         SET VALUE = 0;
         SELECT current_value INTO VALUE
                   FROM sequence
                   WHERE NAME = seq_name;
				 IF (VALUE = 0) THEN 
				 INSERT INTO sequence VALUES (seq_name,1,1);
				 RETURN 1;
					END IF; 
         RETURN VALUE;
END */$$
DELIMITER ;

/* Function  structure for function  `nextval` */

/*!50003 DROP FUNCTION IF EXISTS `nextval` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`zzcx`@`%` FUNCTION `nextval`(seq_name VARCHAR(50)) RETURNS int(11)
    DETERMINISTIC
BEGIN
         UPDATE sequence
                   SET current_value = current_value + increment
                   WHERE NAME = seq_name;
         RETURN currval(seq_name);
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        5.7.31-log - MySQL Community Server (GPL)
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- imarket 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `imarket` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `imarket`;

-- 테이블 imarket.buyer 구조 내보내기
CREATE TABLE IF NOT EXISTS `buyer` (
  `buyer_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `member_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`buyer_id`),
  KEY `FKw70g737m31d79dtqydhwwjqh` (`member_id`),
  CONSTRAINT `FKw70g737m31d79dtqydhwwjqh` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 imarket.member 구조 내보내기
CREATE TABLE IF NOT EXISTS `member` (
  `member_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `gender` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `UK_mbmcqelty0fbrvxp1q58dn57t` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 imarket.member_roles 구조 내보내기
CREATE TABLE IF NOT EXISTS `member_roles` (
  `member_member_id` bigint(20) NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  KEY `FKruptm2dtwl95mfks4bnhv828k` (`member_member_id`),
  CONSTRAINT `FKruptm2dtwl95mfks4bnhv828k` FOREIGN KEY (`member_member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 imarket.payment 구조 내보내기
CREATE TABLE IF NOT EXISTS `payment` (
  `payment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `card_nick_name` varchar(255) NOT NULL,
  `card_number` varchar(255) NOT NULL,
  `buyer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `FK4kqbxd01k30c1m0lu6akeooyl` (`buyer_id`),
  CONSTRAINT `FK4kqbxd01k30c1m0lu6akeooyl` FOREIGN KEY (`buyer_id`) REFERENCES `buyer` (`buyer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 imarket.product 구조 내보내기
CREATE TABLE IF NOT EXISTS `product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `can_purchase` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` bigint(20) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_category_id` bigint(20) DEFAULT NULL,
  `seller_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `FKcwclrqu392y86y0pmyrsi649r` (`product_category_id`),
  KEY `FKesd6fy52tk7esoo2gcls4lfe3` (`seller_id`),
  CONSTRAINT `FKcwclrqu392y86y0pmyrsi649r` FOREIGN KEY (`product_category_id`) REFERENCES `product_category` (`product_category_id`),
  CONSTRAINT `FKesd6fy52tk7esoo2gcls4lfe3` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`seller_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 imarket.product_category 구조 내보내기
CREATE TABLE IF NOT EXISTS `product_category` (
  `product_category_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  PRIMARY KEY (`product_category_id`),
  UNIQUE KEY `UK_2us25uva2olofkk3yh8dhlq87` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 imarket.product_order 구조 내보내기
CREATE TABLE IF NOT EXISTS `product_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `amount` int(11) NOT NULL,
  `order_number` varchar(255) NOT NULL,
  `order_status` varchar(255) NOT NULL,
  `paid_at` datetime NOT NULL,
  `buyer_id` bigint(20) DEFAULT NULL,
  `payment_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `seller_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `UK_8vtdwtwmgbffpvhj1gibuntqq` (`order_number`),
  KEY `FKmx8e980ryj9gsgyeho1wyj7x1` (`buyer_id`),
  KEY `FKmni913akx13af0efthfqi81tg` (`payment_id`),
  KEY `FKh73acsd9s5wp6l0e55td6jr1m` (`product_id`),
  KEY `FKqdqvgqfyb8qlghmvda8k2d10f` (`seller_id`),
  CONSTRAINT `FKh73acsd9s5wp6l0e55td6jr1m` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `FKmni913akx13af0efthfqi81tg` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`),
  CONSTRAINT `FKmx8e980ryj9gsgyeho1wyj7x1` FOREIGN KEY (`buyer_id`) REFERENCES `buyer` (`buyer_id`),
  CONSTRAINT `FKqdqvgqfyb8qlghmvda8k2d10f` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 imarket.seller 구조 내보내기
CREATE TABLE IF NOT EXISTS `seller` (
  `seller_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `member_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`seller_id`),
  KEY `FK8xy0y20dnm987syb4xvpkglab` (`member_id`),
  CONSTRAINT `FK8xy0y20dnm987syb4xvpkglab` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

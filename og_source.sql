/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50552
Source Host           : localhost:3306
Source Database       : wisdom_farm

Target Server Type    : MYSQL
Target Server Version : 50552
File Encoding         : 65001

Date: 2019-10-24 14:47:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `og_source`
-- ----------------------------
DROP TABLE IF EXISTS `og_source`;
CREATE TABLE `og_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `house_id` int(11) NOT NULL,
  `source_code` varchar(255) NOT NULL,
  `hash` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of og_source
-- ----------------------------
INSERT INTO `og_source` VALUES ('1', '53', 'wreg4eg34fgsd54v3veg2r4fg3', '233r22rrg3g');

/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50552
Source Host           : localhost:3306
Source Database       : wisdom_farm

Target Server Type    : MYSQL
Target Server Version : 50552
File Encoding         : 65001

Date: 2019-10-31 15:00:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `pm_warning_phone`
-- ----------------------------
DROP TABLE IF EXISTS `pm_warning_phone`;
CREATE TABLE `pm_warning_phone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `farm_id` int(11) DEFAULT NULL,
  `introduce` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_farmID` (`farm_id`) USING BTREE,
  KEY `index_phone` (`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pm_warning_phone
-- ----------------------------
INSERT INTO `pm_warning_phone` VALUES ('10', '13433322222', 'non', '91', 'ggi');
INSERT INTO `pm_warning_phone` VALUES ('46', '13433322222', '', '90', 'nimk');
INSERT INTO `pm_warning_phone` VALUES ('51', '13677776666', '', '75', 'iuih');
INSERT INTO `pm_warning_phone` VALUES ('52', '13433322222', '', '75', 'iuih');

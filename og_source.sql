/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50552
Source Host           : localhost:3306
Source Database       : wisdom_farm

Target Server Type    : MYSQL
Target Server Version : 50552
File Encoding         : 65001

Date: 2019-11-04 10:51:22
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
  `createTime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of og_source
-- ----------------------------
INSERT INTO `og_source` VALUES ('1', '53', 'wreg4eg34fgsd54v3veg2r4fg3', '233r22rrg3g', '2019-11-04 09:06:38');
INSERT INTO `og_source` VALUES ('2', '53', 'vyufv75fd45drdu0', 'bu879gt7r7fdcr', '2019-11-04 09:47:21');
INSERT INTO `og_source` VALUES ('3', '53', 'trrt', 'j755', '2019-11-04 10:31:51');
INSERT INTO `og_source` VALUES ('4', '53', 'nr5gf', '4334gbf', '2019-11-04 10:32:04');
INSERT INTO `og_source` VALUES ('5', '53', 'rgbr', '3rt3tdfy56', '2019-11-04 10:32:21');
INSERT INTO `og_source` VALUES ('6', '53', 'bgynr', '34byt5', '2019-11-04 10:32:36');

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `mq_failed_message`
-- ----------------------------
DROP TABLE IF EXISTS `mq_failed_message`;
CREATE TABLE `mq_failed_message` (
  `id` char(36) NOT NULL,
  `body` longtext,
  `properties` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

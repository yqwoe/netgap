SET FOREIGN_KEY_CHECKS=0;

CREATE DATABASE IF NOT EXISTS `netgap`;

USE `netgap`;
-- ----------------------------
-- Table structure for sync_job_config
-- ----------------------------
DROP TABLE IF EXISTS `sync_job_config`;
CREATE TABLE `sync_job_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `source_db_name` varchar(50) NOT NULL COMMENT '源数据库名称',
  `source_db_host` varchar(50) NOT NULL COMMENT '源数据库地址',
  `source_db_port` int(11) NOT NULL COMMENT '源数据库端口',
  `source_db_username` varchar(30) NOT NULL COMMENT '源数据库用户名',
  `source_db_password` varchar(50) NOT NULL COMMENT '源数据库用户密码',
  `source_table_name` varchar(50) NOT NULL COMMENT '源表名称',
  `target_db_name` varchar(50) NOT NULL COMMENT '目标数据库名称',
  `target_db_host` varchar(50) NOT NULL COMMENT '目标数据库地址',
  `target_db_port` int(11) NOT NULL COMMENT '目标数据库端口',
  `target_db_username` varchar(30) NOT NULL COMMENT '目标数据库用户名',
  `target_db_password` varchar(50) NOT NULL COMMENT '目标数据库用户密码',
  `target_table_name` varchar(50) NOT NULL COMMENT '目标表名称',
  `enable` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否启用同步',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

模拟网闸说明
=
一般来说，安全级别 公安网 》视频专网 》政务网 》互联网
如果数据跨网络访问则会受到重重限制， 而这种跨网络数据沟通的唯一方式是通过网闸。该程序主要实现了Mysql数据经过网闸近实时同步的功能，主要有以下特性：
- 暂时只支持mysql数据同步
- 系统启动时自动根据sync_job_config表中的源数据库和源数据表来创建目标数据库和目标数据表
- 数据近实时同步。每隔10ms同步一次
- 源数据库、表和目标数据库、表名称允许不一致


架构说明
===
项目采用SpringBoot2.0+HikariCP（高性能数据库连接池）+ThreadPoolTaskScheduler（线程池任务调度）等技术。
只有一张功能数据表，如下：
```mysql
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
```
核心业务逻辑：
1. 应用启动时，获取所有启用的数据同步任务
2. 初始化数据同步任务的DataSource、JdbcTemplate
3. 初始化目标数据库、数据表
4. 使用ThreadPoolTaskScheduler启动数据同步任务



Quick Start
===
快速开始步骤：

1. 执行~/db/init.sql进行数据库初始化
2. resources目录applicaiton-dev.properties进行服务端口号和jdbc连接配置
3. 添加一个数据同步任务，手动在sync_job_config表添加一条记录即可。（`需要服务重启才会生效`）
4. 启动 App.java 或使用 start.sh

`请注意，源数据表中一定要有id唯一主键字段，且是趋势递增（建议默认自增）。因为该程序只会同步比当前已同步的最大id要大的源表数据`

下面是新增数据同步表记录参考：

```mysql
INSERT INTO `netgap`.`sync_job_config` (
	`id`,
	`source_db_name`,
	`source_db_host`,
	`source_db_port`,
	`source_db_username`,
	`source_db_password`,
	`source_table_name`,
	`target_db_name`,
	`target_db_host`,
	`target_db_port`,
	`target_db_username`,
	`target_db_password`,
	`target_table_name`,
	`enable`
)
VALUES
	(
		'1',
		'user',
		'192.168.10.100',
		'3306',
		'root',
		'123456',
		't_user',
		'user',
		'127.0.0.1',
		'3306',
		'root',
		'123456',
		't_user',
		'1'
	);


```
CREATE TABLE `stock_exchanger` (
	`code` varchar(50)  NOT NULL COMMENT '证券交易所代码',
	`name` varchar(50)  NOT NULL COMMENT '证券所名称',
	`country` varchar(50)  NOT NULL COMMENT '所在国家',
	PRIMARY KEY (`code`)
) ;

CREATE TABLE `stock_info` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`code` varchar(20)  NOT NULL COMMENT '股票代码',
	`name` varchar(50)  NOT NULL COMMENT '股票名称',
	`exchanger` varchar(20)  NOT NULL COMMENT '上市证券市场',
	`subExchanger` varchar(20)  NOT NULL COMMENT '子板',
	`biz` varchar(2000)  NOT NULL COMMENT '经营范围',
	`openDay` varchar(20)  NOT NULL COMMENT '注册日期',
	`marketDay` varchar(20)  NOT NULL COMMENT '上市日期',
	`infoInit` tinyint(4) NOT NULL DEFAULT 0 COMMENT '基本信息初始化',
	`priceComplete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '股价初始化',
	`financeComplete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '财报初始化',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updatetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`code`) USING BTREE
) ;

CREATE TABLE `stock_price` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`code` varchar(20)  NOT NULL COMMENT '股票代码',
	`open` decimal(11,4)  NOT NULL COMMENT '开盘价',
	`close` decimal(11,4)  NOT NULL COMMENT '收盘价',
	`high` decimal(11,4)  NOT NULL COMMENT '最高价',
	`low` decimal(11,4)  NOT NULL COMMENT '最低价',
	`volume` decimal(11,4)  NOT NULL COMMENT '成交量',
	`amount` decimal(11,4)  NOT NULL COMMENT '成交额',
	`updown` decimal(11,4)  NOT NULL COMMENT '涨跌幅',
	`change` decimal(11,4)  NOT NULL COMMENT '涨跌额',
	`capital` decimal(20,4)  NOT NULL COMMENT '市值',
	`date` varchar(20)  NOT NULL COMMENT '股市日期',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code_date`(`code`,`date`) USING BTREE
) ;

CREATE TABLE `stock_history_price` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`code` varchar(20)  NOT NULL COMMENT '股票代码',
	`open` decimal(11,4)  NOT NULL COMMENT '开盘价',
	`close` decimal(11,4)  NOT NULL COMMENT '收盘价',
	`high` decimal(11,4)  NOT NULL COMMENT '最高价',
	`low` decimal(11,4)  NOT NULL COMMENT '最低价',
	`volume` decimal(11,4)  NOT NULL COMMENT '成交量',
	`turnover` decimal(11,4)  NOT NULL COMMENT '成交额',
	`percent` decimal(11,4)  NOT NULL COMMENT '涨跌幅',
	`updown` decimal(11,4)  NOT NULL COMMENT '涨跌额',
	`amplitude` decimal(11,4)  NOT NULL COMMENT '振幅',
	`date` varchar(20)  NOT NULL COMMENT '股价日期',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code_date`(`code`,`date`) USING BTREE
) ;

CREATE TABLE `stock_finance` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`code` varchar(50)  NOT NULL COMMENT '股票编码',
	`income` varchar(50)  NOT NULL COMMENT '当季收入',
	`y2yIncome` varchar(50)  NOT NULL COMMENT '同比',
	`m2mIncome` varchar(50)  NOT NULL COMMENT '环比',
	`profit` varchar(50)  NOT NULL COMMENT '当季利润',
	`y2yProfit` varchar(50)  NOT NULL COMMENT '同比',
	`m2mProfit` varchar(50)  NOT NULL COMMENT '环比',
	`pe` varchar(50)  NOT NULL COMMENT '市盈率',
	`date` varchar(20)  NOT NULL COMMENT '财报日期',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updatetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code_date`(`code`,`date`) USING BTREE
) ;
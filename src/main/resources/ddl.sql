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
	`open` decimal(20,4)  NOT NULL COMMENT '开盘价',
	`close` decimal(20,4)  NOT NULL COMMENT '收盘价',
	`high` decimal(20,4)  NOT NULL COMMENT '最高价',
	`low` decimal(20,4)  NOT NULL COMMENT '最低价',
	`volume` decimal(20,4)  NOT NULL COMMENT '成交量',
	`amount` decimal(20,4)  NOT NULL COMMENT '成交额',
	`percent` decimal(20,4)  NOT NULL COMMENT '涨跌幅',
	`updown` decimal(20,4)  NOT NULL COMMENT '涨跌额',
	`amplitude` decimal(20,4)  NOT NULL COMMENT '振幅',
	`total` decimal(20,4)  NOT NULL COMMENT '总股本',
	`num` decimal(20,4)  NOT NULL COMMENT '流通股本',
	`capital` decimal(20,4)  NOT NULL COMMENT '市值',
	`date` varchar(25)  NOT NULL COMMENT '股市日期',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`code`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
) ;

CREATE TABLE `stock_history_price` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`code` varchar(20)  NOT NULL COMMENT '股票代码',
	`open` decimal(20,4)  NOT NULL COMMENT '开盘价',
	`close` decimal(20,4)  NOT NULL COMMENT '收盘价',
	`high` decimal(20,4)  NOT NULL COMMENT '最高价',
	`low` decimal(20,4)  NOT NULL COMMENT '最低价',
	`volume` decimal(20,4)  NOT NULL COMMENT '成交量',
	`amount` decimal(20,4)  NOT NULL COMMENT '成交额',
	`percent` decimal(20,4)  NOT NULL COMMENT '涨跌幅',
	`updown` decimal(20,4)  NOT NULL COMMENT '涨跌额',
	`amplitude` decimal(20,4)  NOT NULL COMMENT '振幅',
	`total` decimal(20,4)  NOT NULL COMMENT '总股本',
	`capital` decimal(20,4)  NOT NULL COMMENT '市值',
	`date` varchar(25)  NOT NULL COMMENT '股价日期',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`code`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
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
	`date` varchar(25)  NOT NULL COMMENT '财报日期',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updatetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`code`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
) ;

CREATE TABLE `fund_info` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`fundCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '基金编码',
	`fundName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '基金名称',
	`historyInit` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否拉取历史数据',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	Unique KEY `ix_code`(`fundCode`) USING BTREE
) ;

CREATE TABLE `fund_price` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`fundCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '基金编码',
	`fundName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '基金名称',
	`unit` decimal(20,2)  NOT NULL COMMENT '当日单位净值',
	`amount` decimal(20,4)  NOT NULL COMMENT '当日基金总额',
	`ratio` decimal(20,4)  NOT NULL COMMENT '涨跌幅度',
	`date` datetime NOT NULL COMMENT '统计日期',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`fundCode`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
) ;

CREATE TABLE `fund_stock` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`fundCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '基金编码',
	`stockCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '股票编码',
	`stockNums` decimal(20,2)  NOT NULL COMMENT '股票份额',
	`stockAmount` decimal(20,4)  NOT NULL COMMENT '股票金额',
	`stockRatio` decimal(20,4)  NOT NULL COMMENT '股票占比',
	`date` datetime NOT NULL COMMENT '拉取日期',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_stock_code`(`stockCode`) USING BTREE,
	KEY `ix_fund_code`(`fundCode`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
) ;
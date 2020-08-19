CREATE TABLE `stock_exchanger` (
    `code` varchar(50) COMMENT '证券交易所代码',
    `name` varchar(50) COMMENT '证券所名称',
    `country` varchar(50) COMMENT '所在国家',
    PRIMARY KEY (`code`)
) ;

CREATE TABLE `stock_info` (
	`id` bigint(20) AUTO_INCREMENT,
	`code` varchar(20) COMMENT '股票代码',
	`name` varchar(50) COMMENT '股票名称',
	`exchanger` varchar(20) COMMENT '上市证券市场',
	`sub_exchanger` varchar(20) COMMENT '子板',
	`biz` varchar(2000) COMMENT '经营范围',
	`open_day` datetime COMMENT '注册日期',
	`market_day` datetime COMMENT '上市日期',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP,
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	UNIQUE KEY `ix_code`(`code`) USING BTREE
) ;

CREATE TABLE `stock_price` (
	`id` bigint(20) AUTO_INCREMENT,
	`code` varchar(20) COMMENT '股票代码',
	`last_close` decimal(20,4) COMMENT '昨日收盘',
	`open` decimal(20,4) COMMENT '开盘价',
	`close` decimal(20,4) COMMENT '收盘价',
	`high` decimal(20,4) COMMENT '最高价',
	`low` decimal(20,4) COMMENT '最低价',
	`volume` decimal(20,4) COMMENT '成交量',
	`amount` decimal(20,4) COMMENT '成交额',
	`percent` decimal(20,4) COMMENT '涨跌幅',
	`updown` decimal(20,4) COMMENT '涨跌额',
	`amplitude` decimal(20,4) COMMENT '振幅',
	`total` decimal(20,4) COMMENT '总股本',
	`num` decimal(20,4) COMMENT '流通股本',
	`capital` decimal(20,4) COMMENT '市值',
	`date` varchar(25) COMMENT '股市日期',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP,
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`code`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE,
    UNIQUE KEY `uix_code_date`(`code`,`date`) USING BTREE
) ;

CREATE TABLE `stock_price_history` (
	`id` bigint(20) AUTO_INCREMENT,
	`code` varchar(20) COMMENT '股票代码',
	`last_close` decimal(20,4) COMMENT '昨日收盘',
	`open` decimal(20,4) COMMENT '开盘价',
	`close` decimal(20,4) COMMENT '收盘价',
	`high` decimal(20,4) COMMENT '最高价',
	`low` decimal(20,4) COMMENT '最低价',
	`volume` decimal(20,4) COMMENT '成交量',
	`amount` decimal(20,4) COMMENT '成交额',
	`percent` decimal(20,4) COMMENT '涨跌幅',
	`updown` decimal(20,4) COMMENT '涨跌额',
	`amplitude` decimal(20,4) COMMENT '振幅',
	`total` decimal(20,4) COMMENT '总股本',
	`capital` decimal(20,4) COMMENT '市值',
	`pe` decimal(20,4) COMMENT '市盈率',
	`date` varchar(25) COMMENT '股价日期',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP,
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`code`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE,
    UNIQUE KEY `uix_code_date`(`code`,`date`) USING BTREE
) ;

CREATE TABLE `stock_finance` (
	`id` bigint(20) AUTO_INCREMENT,
	`code` varchar(50) COMMENT '股票编码',
	`income` varchar(50) COMMENT '收入',
	`profit` varchar(50) COMMENT '利润',
	`cur_income` varchar(50) COMMENT '当季收入',
	`cur_profit` varchar(50) COMMENT '当季利润',
	`y2y_income` varchar(50) COMMENT '同比',
	`m2m_income` varchar(50) COMMENT '环比',
	`y2y_profit` varchar(50) COMMENT '同比',
	`m2m_profit` varchar(50) COMMENT '环比',
	`date` varchar(25) COMMENT '财报日期',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP,
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`code`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE,
    UNIQUE KEY `uix_code_date`(`code`,`date`) USING BTREE
) ;

CREATE TABLE `fund_info` (
	`id` bigint(20) AUTO_INCREMENT,
	`fund_code` varchar(20) COMMENT '基金编码',
	`fund_name` varchar(50) COMMENT '基金名称',
	`market_date` varchar(50) COMMENT '成立日期',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP,
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	UNIQUE KEY `ix_code`(`fundCode`) USING BTREE
) ;

CREATE TABLE `fund_price` (
	`id` bigint(20) AUTO_INCREMENT,
	`fundCode` varchar(20) COMMENT '基金编码',
	`unit` decimal(20,2) COMMENT '当日单位净值',
	`amount` decimal(20,4) COMMENT '当日基金总额',
	`ratio` decimal(20,4) COMMENT '涨跌幅度',
	`m1ret` decimal(20,4) COMMENT '单月回报',
	`m3ret` decimal(20,4) COMMENT '季度回报',
	`m6ret` decimal(20,4) COMMENT '半年回报',
	`m12ret` decimal(20,4) COMMENT '年度回报',
	`date` datetime COMMENT '统计日期',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP,
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`fundCode`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
) ;

CREATE TABLE `fund_stock` (
	`id` bigint(20) AUTO_INCREMENT,
	`fundCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '基金编码',
	`stockCode` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '股票编码',
	`stockNums` decimal(20,2) COMMENT '股票份额',
	`stockAmount` decimal(20,4) COMMENT '股票金额',
	`stockRatio` decimal(20,4) COMMENT '股票占比',
	`date` datetime COMMENT '拉取日期',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP,
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_stock_code`(`stockCode`) USING BTREE,
	KEY `ix_fund_code`(`fundCode`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
) ;
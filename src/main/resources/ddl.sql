
CREATE TABLE `ipo_stock_page` (
  `code` varchar(20) NOT NULL COMMENT '股票代码',
  `name` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `market_day` varchar(20) DEFAULT NULL COMMENT '上市日期',
  `market_year` varchar(20) DEFAULT NULL COMMENT '上市年份',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`),
  KEY `ix_market_day` (`market_day`),
  KEY `ix_market_year` (`market_year`)
) ;

CREATE TABLE `stock_info_page` (
  `code` varchar(20) NOT NULL COMMENT '股票代码',
  `name` varchar(50) DEFAULT NULL COMMENT '公司简名称',
  `biz` varchar(4000) DEFAULT NULL COMMENT '经营业务',
  `open_day` varchar(20) DEFAULT NULL COMMENT '成立日期',
  `market_day` varchar(20) DEFAULT NULL COMMENT '上市日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`),
  KEY `ix_market_day` (`market_day`)
) ;

CREATE TABLE `stock_num_page` (
  `code` varchar(20) NOT NULL COMMENT '股票代码',
  `name` varchar(500) DEFAULT NULL COMMENT '公司全名称',
  `biz` varchar(4000) DEFAULT NULL COMMENT '经营业务',
  `market_day` varchar(20) DEFAULT NULL COMMENT '上市日期',
  `total` varchar(50) DEFAULT NULL COMMENT '总股份',
  `cycle` varchar(50) DEFAULT NULL COMMENT '流通股份',
  `date` varchar(20) NOT NULL COMMENT '爬取日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`,`date`),
  KEY `ix_market_day` (`market_day`)
) ;

CREATE TABLE `stock_finance_page` (
  `code` varchar(20) NOT NULL,
  `income` varchar(50) DEFAULT NULL COMMENT '收入',
  `profit` varchar(50) DEFAULT NULL COMMENT '利润',
  `report_day` varchar(20) NOT NULL COMMENT '财报日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`,`report_day`),
  KEY `ix_report_day` (`report_day`)
) ;

CREATE TABLE `stock_price_page` (
  `code` varchar(20) NOT NULL COMMENT '股票代码',
  `last_close` varchar(50) DEFAULT NULL COMMENT '昨日收盘价',
  `open` varchar(50) DEFAULT NULL COMMENT '开盘价',
  `cur` varchar(50) DEFAULT NULL COMMENT '当前价',
  `high` varchar(50) DEFAULT NULL COMMENT '最高价',
  `low` varchar(50) DEFAULT NULL COMMENT '最低价',
  `volume` varchar(50) DEFAULT NULL COMMENT '交易量',
  `amount` varchar(50) DEFAULT NULL COMMENT '交易额',
  `percent` varchar(50) DEFAULT NULL COMMENT '涨跌幅',
  `change` varchar(50) DEFAULT NULL COMMENT '涨跌额',
  `date` varchar(20) NOT NULL COMMENT '报告日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`,`date`),
  KEY `ix_date` (`date`)
) ;

CREATE TABLE `stock_price_history_page` (
  `code` varchar(20) NOT NULL COMMENT '股票代码',
  `open` varchar(50) DEFAULT NULL COMMENT '开盘价',
  `high` varchar(50) DEFAULT NULL COMMENT '最高价',
  `low` varchar(50) DEFAULT NULL COMMENT '最低价',
  `close` varchar(50) DEFAULT NULL COMMENT '收盘价',
  `percent` varchar(50) DEFAULT NULL COMMENT '涨跌幅',
  `change` varchar(50) DEFAULT NULL COMMENT '涨跌额',
  `amplitude` varchar(50) DEFAULT NULL COMMENT '振幅',
  `volume` varchar(50) DEFAULT NULL COMMENT '交易量',
  `amount` varchar(29) DEFAULT NULL COMMENT '交易额',
  `exchange` varchar(50) DEFAULT NULL COMMENT '换手率',
  `date` varchar(20) NOT NULL COMMENT '报告日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`,`date`),
  KEY `ix_date` (`date`)
) ;








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
	`openDay` datetime  NOT NULL COMMENT '注册日期',
	`marketDay` datetime  NOT NULL COMMENT '上市日期',
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
	`lastClose` decimal(20,4)  NOT NULL COMMENT '昨日收盘',
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
	`lastClose` decimal(20,4)  NOT NULL COMMENT '昨日收盘',
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
	`pe` varchar(50) COMMENT '市盈率',
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
	`income` varchar(50)  NOT NULL COMMENT '收入',
	`profit` varchar(50)  NOT NULL COMMENT '利润',
	`curIncome` varchar(50) COMMENT '当季收入',
	`curProfit` varchar(50) COMMENT '当季利润',
	`y2yIncome` varchar(50) COMMENT '同比',
	`m2mIncome` varchar(50) COMMENT '环比',
	`y2yProfit` varchar(50) COMMENT '同比',
	`m2mProfit` varchar(50) COMMENT '环比',
	`date` varchar(25)  NOT NULL COMMENT '财报日期',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updatetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`code`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
) ;

CREATE TABLE `fund_info` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`fundCode` varchar(20) NOT NULL COMMENT '基金编码',
	`fundName` varchar(50) NOT NULL COMMENT '基金名称',
	`marketDate` varchar(50) NOT NULL COMMENT '成立日期',
	`historyInit` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否拉取历史数据',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	Unique KEY `ix_code`(`fundCode`) USING BTREE
) ;

CREATE TABLE `fund_price` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`fundCode` varchar(20) NOT NULL COMMENT '基金编码',
	`unit` decimal(20,2)  NOT NULL COMMENT '当日单位净值',
	`amount` decimal(20,4)  NOT NULL COMMENT '当日基金总额',
	`ratio` decimal(20,4)  NOT NULL COMMENT '涨跌幅度',
	`m1ret` decimal(20,4) COMMENT '单月回报',
	`m3ret` decimal(20,4) COMMENT '季度回报',
	`m6ret` decimal(20,4) COMMENT '半年回报',
	`m12ret` decimal(20,4) COMMENT '年度回报',
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
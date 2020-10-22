
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

CREATE TABLE `stock_fund_page` (
  `code` varchar(20) NOT NULL,
  `fund_code` varchar(20) NOT NULL,
  `fund_name` varchar(100) DEFAULT NULL COMMENT '基金名称',
  `stock_nums` varchar(50) DEFAULT NULL COMMENT '持仓数目',
  `stock_amount` varchar(50) DEFAULT NULL COMMENT '持仓金额',
  `report_day` varchar(20) NOT NULL COMMENT '报告日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`,`fund_code`,`report_day`),
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

CREATE TABLE `fund_price_page` (
	`fund_code` varchar(20) NOT NULL COMMENT '基金编码',
	`fund_name` varchar(50) COMMENT '基金名称',
	`market_date` varchar(50) COMMENT '成立日期',
	`unit` varchar(50) COMMENT '当日单位净值',
	`amount` varchar(50) COMMENT '当日基金总额',
	`ratio` varchar(50) COMMENT '涨跌幅度',
	`m1ret` varchar(50) COMMENT '单月回报',
	`m3ret` varchar(50) COMMENT '季度回报',
	`m6ret` varchar(50) COMMENT '半年回报',
	`m12ret` varchar(50) COMMENT '年度回报',
	`date` datetime NOT NULL COMMENT '统计日期',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`fund_code`,`date`),
	KEY `ix_code`(`fund_code`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE,
	KEY `ix_market_date`(`market_date`) USING BTREE
) ;

CREATE TABLE `fund_price_history_page` (
	`fund_code` varchar(20) NOT NULL COMMENT '基金编码',
	`unit` varchar(50)  DEFAULT NULL COMMENT '当日单位净值',
	`history_unit` varchar(50)  DEFAULT NULL COMMENT '当日单位净值',
	`amount` varchar(50)  DEFAULT NULL COMMENT '当日基金总额',
	`ratio` varchar(50)  DEFAULT NULL COMMENT '涨跌幅度',
	`date` datetime NOT NULL COMMENT '统计日期',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`fund_code`,`date`),
	KEY `ix_code`(`fundCode`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
) ;

CREATE TABLE `fund_stock_page` (
	`fund_code` varchar(20) NOT NULL COMMENT '基金编码',
	`stock_code` varchar(20) NOT NULL COMMENT '股票编码',
	`stock_nums` varchar(50) COMMENT '股票份额',
	`stock_amount` varchar(50) COMMENT '股票金额',
	`stock_ratio` varchar(50) COMMENT '股票占比',
	`date` datetime NOT NULL COMMENT '拉取日期',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`fund_code`,`stock_code`,`date`),
	KEY `ix_stock_code`(`stock_code`) USING BTREE,
	KEY `ix_fund_code`(`fund_code`) USING BTREE,
	KEY `ix_date`(`date`) USING BTREE
) ;

CREATE TABLE `stock_day_price` (
  `code` varchar(20) NOT NULL COMMENT '股票代码',
  `last_close` decimal(40,10) DEFAULT NULL COMMENT '昨日收盘价',
  `open` decimal(50,10) DEFAULT NULL COMMENT '开盘价',
  `high` decimal(50,10) DEFAULT NULL COMMENT '最高价',
  `low` decimal(50,10) DEFAULT NULL COMMENT '最低价',
  `close` decimal(50,10) DEFAULT NULL COMMENT '收盘价',
  `percent` decimal(50,10) DEFAULT NULL COMMENT '涨跌幅',
  `change` decimal(50,10) DEFAULT NULL COMMENT '涨跌额',
  `amplitude` decimal(50,10) DEFAULT NULL COMMENT '振幅',
  `volume` decimal(50,10) DEFAULT NULL COMMENT '交易量',
  `amount` decimal(29,10) DEFAULT NULL COMMENT '交易额',
  `exchange` decimal(50,10) DEFAULT NULL COMMENT '换手率',
  `total` decimal(50,10) DEFAULT NULL COMMENT '股本',
  `capital` decimal(29,10) DEFAULT NULL COMMENT '市值',
  `pe` decimal(50,10) DEFAULT NULL COMMENT '市盈率',
  `date` varchar(20) NOT NULL COMMENT '报告日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`,`date`),
  KEY `ix_date` (`date`)
) ;

CREATE TABLE `stock_info` (
  `code` varchar(20) NOT NULL COMMENT '股票代码',
  `name` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `biz` varchar(4000) DEFAULT NULL COMMENT '营业范围',
  `open_day` datetime DEFAULT NULL COMMENT '成立日期',
  `market_day` datetime DEFAULT NULL COMMENT '上市日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`),
  KEY `ix_market_day` (`market_day`)
) ;

CREATE TABLE `stock_quartly_report` (
  `code` varchar(20) NOT NULL,
  `income` decimal(50,10) DEFAULT NULL COMMENT '当期收入',
  `profit` decimal(50,10) DEFAULT NULL COMMENT '当期利润',
  `total_income` decimal(50,10) DEFAULT NULL COMMENT '收入',
  `total_profit` decimal(50,10) DEFAULT NULL COMMENT '利润',
  `fund_ratio` decimal(50,10) DEFAULT NULL COMMENT '基金持股占比',
  `y2y_income` decimal(50,10) DEFAULT NULL COMMENT '收入同比',
  `y2y_profit` decimal(50,10) DEFAULT NULL COMMENT '利润同比',
  `m2m_income` decimal(50,10) DEFAULT NULL COMMENT '收入环比',
  `m2m_profit` decimal(50,10) DEFAULT NULL COMMENT '利润环比',
  `report_day` datetime NOT NULL COMMENT '财报日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`,`report_day`),
  KEY `ix_report_day` (`report_day`)
) ;

CREATE TABLE `market_info` (
  `market` varchar(20) NOT NULL COMMENT '市场',
  `timezone` varchar(20) NOT NULL COMMENT '时区',
  `break_date` datetime DEFAULT NULL COMMENT '休息日'
) ;

CREATE TABLE `market_break` (
  `market` varchar(20) NOT NULL COMMENT '市场',
  `timezone` varchar(20) NOT NULL COMMENT '时区',
  `break_date` datetime DEFAULT NULL COMMENT '休息日'
) ;
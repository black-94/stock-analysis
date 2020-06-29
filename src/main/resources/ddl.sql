CREATE TABLE `stock_info` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '股票代码',
	`name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '股票名称',
	`exchanger` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上市证券市场',
	`subExchanger` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '子板',
	`biz` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '经营范围',
	`openDay` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '注册日期',
	`marketDay` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上市日期',
	`infoInit` tinyint(4) NOT NULL DEFAULT 0 COMMENT '基本信息初始化',
	`priceComplete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '股价初始化',
	`financeComplete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '财报初始化',
	`createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`updatetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	KEY `ix_code`(`code`) USING BTREE
);

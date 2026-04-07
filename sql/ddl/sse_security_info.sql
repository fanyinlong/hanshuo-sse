drop table if exists sse_security_info;
-- smdata.sse_security_info definition

CREATE TABLE `sse_security_info` (
  `pk_id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品编号',
  `security_id` char(6) DEFAULT NULL COMMENT '证券代码，前6位有效',
  `isin_code` char(12) DEFAULT NULL COMMENT 'ISIN代码（预留）',
  `update_time` char(32) DEFAULT NULL,
  `security_cname` char(8) DEFAULT NULL COMMENT '中文证券名称（短）',
  `security_name` char(10) DEFAULT NULL COMMENT '英文证券名称（预留）',
  `ori_security_id` char(6) DEFAULT NULL COMMENT '基础证券代码',
  `security_market` char(4) DEFAULT NULL COMMENT '市场种类',
  `security_type` char(6) DEFAULT NULL COMMENT '证券类别',
  `security_sub_type` char(3) DEFAULT NULL COMMENT '证券子类别',
  `currency_type` char(3) DEFAULT NULL COMMENT '货币种类',
  `par_value` double DEFAULT NULL COMMENT '债券面值',
  `unissued_securities` bigint DEFAULT NULL COMMENT '可流通证券未上市数量',
  `last_trade_date` char(15) DEFAULT NULL COMMENT '最后交易日期',
  `listing_date` char(15) DEFAULT NULL COMMENT '上市日期',
  `security_set` int DEFAULT NULL COMMENT '产品集set编号',
  `buy_unit` bigint DEFAULT NULL COMMENT '买数量单位',
  `sell_unit` bigint DEFAULT NULL COMMENT '卖数量单位',
  `limit_order_bottom` bigint DEFAULT NULL COMMENT '限价申报数量下限',
  `limit_order_top` bigint DEFAULT NULL COMMENT '限价申报数量上限',
  `prior_closing_price` double DEFAULT NULL COMMENT '前收盘价格',
  `price_tick` double DEFAULT NULL COMMENT '价格档位',
  `price_limit_type` char(1) DEFAULT NULL COMMENT '涨跌幅限制类型',
  `price_upside_limit` double DEFAULT NULL COMMENT '涨幅上限价格',
  `price_downside_limit` double DEFAULT NULL COMMENT '涨幅下限价格',
  `usage_status` char(1) DEFAULT NULL COMMENT '使用状态:0=未使用,1=已使用',
  PRIMARY KEY (`pk_id`),
  KEY `sse_security_info_security_id_IDX` (`security_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产品信息表';
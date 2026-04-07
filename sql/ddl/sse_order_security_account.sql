DROP table IF EXISTS `sse_order_security_account`;
CREATE TABLE `sse_order_security_account` (
                                              `pk_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                              `rule_id` bigint NOT NULL COMMENT '规则ID',
                                              `security_id` char(6) COMMENT '产品代码',
                                              `account_id` char(10) COMMENT '投资者账户',
                                              `field_1` varchar(20) DEFAULT NULL COMMENT '预留扩展字段1',
                                              `field_2` varchar(20) DEFAULT NULL COMMENT '预留扩展字段2',
                                              PRIMARY KEY (`pk_id`),
                                              KEY `sse_order_security_account_security_id_IDX` (`security_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='加持仓统计表';
DROP table IF EXISTS `sse_order_stats`;
CREATE TABLE `sse_order_stats` (
                                   `pk_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `rule_id` bigint NOT NULL COMMENT '规则ID',
                                   `total_orders` varchar(500) DEFAULT NULL COMMENT '总订单数',
                                   `login_pbu_orders` varchar(500) DEFAULT NULL COMMENT '各登录PBU订单数',
                                   `biz_pbu_orders` varchar(2000) DEFAULT NULL COMMENT '各业务PBU订单数',
                                   `ash_ksh_orders` varchar(50) DEFAULT NULL COMMENT '主板科创板订单数',
                                   `sets_orders` varchar(200) DEFAULT NULL COMMENT '各SET订单数',
                                   `matching_type_orders` varchar(200) DEFAULT NULL COMMENT '各撮合方式订单数',
                                   `matching_side_orders` varchar(300) DEFAULT NULL COMMENT '各撮合比订单数，如一撮十',
                                   `account_type_orders` varchar(100) DEFAULT NULL COMMENT '各账户类型订单数',
                                   `field_1` varchar(100) DEFAULT NULL COMMENT '预留扩展字段1',
                                   `field_2` varchar(100) DEFAULT NULL COMMENT '预留扩展字段2',
                                   `field_3` varchar(100) DEFAULT NULL COMMENT '预留扩展字段3',
                                   `field_4` varchar(100) DEFAULT NULL COMMENT '预留扩展字段4',
                                   `field_5` varchar(100) DEFAULT NULL COMMENT '预留扩展字段5',
                                   `field_6` varchar(100) DEFAULT NULL COMMENT '预留扩展字段6',
                                   PRIMARY KEY (`pk_id`),
                                   UNIQUE KEY `rule_id` (`rule_id`),
                                   KEY `sse_order_stats_rule_id_IDX` (`rule_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单统计表';
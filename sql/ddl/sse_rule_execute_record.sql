DROP table IF EXISTS `sse_rule_execute_record`;
CREATE TABLE `sse_rule_execute_record` (
                                           `pk_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                           `rule_id` bigint NOT NULL COMMENT '规则ID',
                                           `order_number` bigint DEFAULT NULL COMMENT '订单数量',
                                           `order_type` char(1) DEFAULT NULL COMMENT '数据类型:全量:0,增量:1',
                                           `execute_status` char(1) DEFAULT NULL COMMENT '执行状态:0:成功,2:执行中,9:失败',
                                           `execute_user` char(30) DEFAULT NULL COMMENT '执行人',
                                           `execute_time` datetime COMMENT '执行时间',
                                           `execute_rule_version` char(50) DEFAULT NULL COMMENT '执行版本',
                                           PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则执行记录表';
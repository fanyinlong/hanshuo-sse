-- smdata.sse_data_rule_init definition
DROP table IF EXISTS `sse_data_rule_init`;
CREATE TABLE `sse_data_rule_init` (
  `pk_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `login_pbu` varchar(500) DEFAULT NULL COMMENT '登录pbu',
  `biz_pbu` varchar(3000) DEFAULT NULL COMMENT '业务pbu',
  `pbu_account_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'pbu和账户集合',
  `security_ash_list` varchar(2000) DEFAULT NULL COMMENT '主板股票',
  `security_ksh_list` varchar(1000) DEFAULT NULL COMMENT '科创板股票',
  `set_ash_security` varchar(2000) DEFAULT NULL COMMENT '主板SET股票分配',
  `set_ksh_security` varchar(1000) DEFAULT NULL COMMENT '科创板SET股票分配',
  `match_order_list` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '撮合占比分类列表',
  `field_2` varchar(100) DEFAULT NULL COMMENT '预留扩展字段2',
  `field_3` varchar(100) DEFAULT NULL COMMENT '预留扩展字段3',
  `status` char(1) DEFAULT '0' COMMENT '状态',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`pk_id`),
  KEY `sse_data_rule_init_rule_id_IDX` (`rule_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则初始化表';
drop table if exists sse_pbu_info;
CREATE TABLE `sse_pbu_info` (
  `pk_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `pbu_id` char(8) DEFAULT NULL COMMENT 'PBU编号',
  `mkt_id` char(1) DEFAULT NULL COMMENT '所属市场',
  `pbu_type` char(2) DEFAULT NULL COMMENT '控制类型',
  `status` char(1) DEFAULT NULL COMMENT 'PBU状态',
  `buyon_marginInd` char(1) DEFAULT NULL COMMENT '保证金买入',
  `sellon_borrowInd` char(1) DEFAULT NULL COMMENT '融券卖出',
  `login_pwd` varchar(64) DEFAULT NULL COMMENT '密码密文',
  `institute_id` varchar(12) DEFAULT NULL COMMENT '机构代码',
  `partition_id` int DEFAULT NULL COMMENT '参考分区编号',
  `flowctrl_value` int DEFAULT NULL COMMENT '流速权',
  `head_pbuid` varchar(8) DEFAULT NULL COMMENT '联通圈主PBU',
  `clear_member` varchar(8) DEFAULT NULL COMMENT '结算会员',
  `memb_cert` varchar(5) DEFAULT NULL COMMENT '机构身份代码',
  PRIMARY KEY (`pk_id`),
  KEY `sse_pbu_info_pbu_id_IDX` (`pbu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='PBU信息表';
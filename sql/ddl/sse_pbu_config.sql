drop table if exists sse_pbu_config;
CREATE TABLE sse_pbu_config (
                                `pk_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                `pbu_id` char(8) COMMENT 'PBU编号',
                                `is_used` char(1) COMMENT '使用状态:0=未使用,1=已使用',
                                `is_loginPbu` char(1) COMMENT '是否登陆pbu:0=不是,1=是',
                                `is_bizPbu` char(1) COMMENT '是否业务pbu:0=不是,1=是',
                                `belongTo_loginPbu` char(8) COMMENT '业务pbu所属登陆pbu，非业务pbu该项为空',
                                `rule_Id` bigint default null COMMENT '对应规则Id',
                                `rule_Name` bigint default null COMMENT '对应规则名称',
                                `account_number` bigint default Null COMMENT '包含账户数量',
                                `account_numberUsed` bigint default Null COMMENT '规则使用账户数量',
                                PRIMARY KEY (`pk_id`),
                                KEY `sse_pbu_config_pbu_id_IDX` (`pbu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='PBU配置表';

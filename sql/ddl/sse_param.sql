DROP table IF EXISTS `sse_param`;
CREATE TABLE `sse_param` (
                             `param_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数英文名称(唯一)',
                             `param_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数中文名称',
                             `param_category` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数分类:BASIC,RATIO,DIST,RULE,EXEC',
                             `data_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据类型:BIGINT,DECIMAL,VARCHAR',
                             `default_value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '默认值',
                             `value_unit` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '值单位',
                             `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '参数描述',
                             `constraint_rule` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '约束规则',
                             `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态:0正常,1停用',
                             `del_flag` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '删除标志:0存在,1删除',
                             `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
                             PRIMARY KEY (`param_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参数定义表';
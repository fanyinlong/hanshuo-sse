DROP table IF EXISTS `sse_model`;

CREATE TABLE `sse_model` (
                             `model_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据模型编号:S1,S2...S12',
                             `model_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据模型名称',
                             `core_dimension` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '唯一系统关注维度',
                             `main_risk_target` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '主要风险对象',
                             `description` text COLLATE utf8mb4_unicode_ci COMMENT '数据模型详细描述',
                             `purpose` text COLLATE utf8mb4_unicode_ci COMMENT '设置目的',
                             `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态:0正常,1停用',
                             `del_flag` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '删除标志:0存在,1删除',
                             `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
                             `business_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '业务类型:竞价,期权,债券',
                             PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据模型表';
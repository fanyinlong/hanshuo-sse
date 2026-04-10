
DROP table IF EXISTS `sse_model_param`;
CREATE TABLE `sse_model_param` (
                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
                                   `model_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据模型ID',
                                   `param_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数英文名称',
                                   `is_required` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '是否必填:1是,0否',
                                   `recommend_min` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '建议最小值',
                                   `recommend_max` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '建议最大值',
                                   `recommend_value` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '建议值',
                                   `display_order` int DEFAULT '999' COMMENT '显示顺序',
                                   `dependency_rule` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '依赖规则',
                                   `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态:0正常,1停用',
                                   `del_flag` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '删除标志:0存在,1删除',
                                   `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
                                   `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
                                   `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模型参数关联表';
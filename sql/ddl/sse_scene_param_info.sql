DROP table IF EXISTS `sse_scene_param_info`;

CREATE TABLE `sse_scene_param_info` (
                                        `param_info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '场景参数ID',
                                        `scene_id` bigint(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '场景ID',
                                        `param_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数ID',
                                        `param_value` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数值',
                                        `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
                                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
                                        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`param_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='场景参数信息表';
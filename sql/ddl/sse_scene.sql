DROP table IF EXISTS `sse_scene`;
CREATE TABLE `sse_scene` (
                             `scene_id` bigint NOT NULL AUTO_INCREMENT COMMENT '场景ID',
                             `scene_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '场景名称',
                             `remark` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '场景说明',
                             `model_id` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模型id',
                             `del_flag` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '删除标志:0存在,1删除',
                             `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`scene_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试场景表';
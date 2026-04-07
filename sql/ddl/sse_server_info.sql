CREATE TABLE `sse_server_info` (
                                   `server_id` int NOT NULL AUTO_INCREMENT COMMENT '主机编号',
                                   `server_name` char(20) DEFAULT NULL COMMENT '主机名',
                                   `is_avaliable` char(1) DEFAULT NULL COMMENT '是否有效:N=无效，Y=有效',
                                   `platform_id` char(10) DEFAULT NULL COMMENT '测试系统',
                                   `env_id` char(10) DEFAULT NULL COMMENT '测试环境号',
                                   `ip_address` char(20) DEFAULT NULL COMMENT 'IP地址',
                                   `login_name` varchar(500) DEFAULT NULL COMMENT '登陆名(加密保存）',
                                   `auth_code` varchar(500) DEFAULT NULL COMMENT '密码(加密保存）',
                                   `encryption_key` char(100) DEFAULT NULL COMMENT '加密密钥',
                                   PRIMARY KEY (`server_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='主机信息表';
INSERT INTO smdata.sys_dict_data (dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark) VALUES
	 (1,'成功','0','sse_rule_execute_status',NULL,'default','N','0','admin','2025-08-05 01:02:01','',NULL,NULL),
	 (9,'失败','9','sse_rule_execute_status',NULL,'default','N','0','admin','2025-08-05 01:02:26','admin','2025-08-05 01:03:00',NULL),
	 (3,'执行中','2','sse_rule_execute_status',NULL,'default','N','0','admin','2025-08-05 01:02:48','',NULL,NULL),
	 (0,'全量','0','sse_execute_data_type',NULL,'default','N','0','admin','2025-08-05 01:05:12','',NULL,NULL),
	 (1,'增量','1','sse_execute_data_type',NULL,'default','N','0','admin','2025-08-05 01:05:18','',NULL,NULL);

INSERT INTO smdata.sys_dict_type (dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark) VALUES
	 ('规则执行状态','sse_rule_execute_status','0','admin','2025-08-05 01:01:29','',NULL,NULL),
	 ('规则执行数据类型','sse_execute_data_type','0','admin','2025-08-05 01:04:53','',NULL,NULL);

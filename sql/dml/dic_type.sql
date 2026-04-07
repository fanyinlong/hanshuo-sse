INSERT INTO smdata.sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES(100, '产品交易类型', 'sse_trade_type', '0', 'admin', '2025-07-20 11:24:09', '', NULL, NULL);
INSERT INTO smdata.sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES(101, '产品业务分类', 'sse_business_type', '0', 'admin', '2025-07-20 11:25:57', '', NULL, NULL);
INSERT INTO smdata.sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES(102, '证券子类别', 'sse_security_sub_category', '0', 'admin', '2025-07-20 11:28:40', '', NULL, NULL);
INSERT INTO smdata.sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES(103, '产品所属SET', 'sse_product_set', '0', 'admin', '2025-07-20 11:30:22', '', NULL, NULL);
INSERT INTO smdata.sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES(104, '账户类型', 'sse_account_type', '0', 'admin', '2025-07-20 11:35:38', '', NULL, NULL);
INSERT INTO smdata.sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES(105, '撮合方式', 'sse_match_method', '0', 'admin', '2025-07-20 11:36:01', '', NULL, NULL);
INSERT INTO smdata.sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES(106, '价格档位', 'sse_price_level', '0', 'admin', '2025-07-20 11:42:58', '', NULL, NULL);
INSERT INTO smdata.sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES(108, '删除标识', 'sys_del_flag', '0', 'admin', '2025-07-20 16:01:42', '', NULL, NULL);
INSERT INTO smdata.sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES(109, '产品市场类型', 'sse_security_market', '0', 'admin', '2025-07-26 10:53:43', '', NULL, NULL);

-- PBU字典使用
INSERT INTO smdata.sys_dict_type (dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark) VALUES
	 ('所属市场','sse_pub_mktid','0','admin','2025-07-27 12:19:56','',NULL,NULL),
	 ('控制类型','sse_pbu_type','0','admin','2025-07-27 12:21:22','',NULL,NULL),
	 ('PBU状态','sse_pbu_status','0','admin','2025-07-27 12:22:34','',NULL,NULL),
	 ('PBU权限','sse_pbu_permission','0','admin','2025-07-27 12:24:46','admin','2025-07-27 12:26:54',NULL);


INSERT INTO smdata.sys_dict_type
(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES('订单类型', 'order_type', '0', 'admin', '2025-08-02 13:53:50', '', NULL, NULL);
INSERT INTO smdata.sys_dict_type
(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES('订单有效时间类型', 'time_in_force', '0', 'admin', '2025-08-02 13:57:27', '', NULL, NULL);

INSERT INTO smdata.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, '市转撤', '1', 'order_type', NULL, 'default', 'N', '0', 'admin', '2025-08-02 13:54:25', '', NULL, NULL);
INSERT INTO smdata.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(2, '限价', '2', 'order_type', NULL, 'default', 'N', '0', 'admin', '2025-08-02 13:54:36', '', NULL, NULL);
INSERT INTO smdata.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(3, '市转限', '3', 'order_type', NULL, 'default', 'N', '0', 'admin', '2025-08-02 13:54:47', '', NULL, NULL);
INSERT INTO smdata.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(4, '本方最优', '4', 'order_type', NULL, 'default', 'N', '0', 'admin', '2025-08-02 13:55:03', '', NULL, NULL);
INSERT INTO smdata.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(5, '对手方最优', '5', 'order_type', NULL, 'default', 'N', '0', 'admin', '2025-08-02 13:55:14', '', NULL, NULL);
INSERT INTO smdata.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(0, '当日有效', '0', 'time_in_force', NULL, 'default', 'N', '0', 'admin', '2025-08-02 13:57:47', '', NULL, NULL);

INSERT INTO smdata.sys_dict_type
(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES('买卖方向', 'buy_sell', '0', 'admin', '2025-08-06 22:23:24', '', NULL, NULL);
INSERT INTO smdata.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(1, '买', '1', 'buy_sell', NULL, 'default', 'N', '0', 'admin', '2025-08-06 22:23:40', '', NULL, NULL);
INSERT INTO smdata.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES(2, '卖', '2', 'buy_sell', NULL, 'default', 'N', '0', 'admin', '2025-08-06 22:23:47', '', NULL, NULL);
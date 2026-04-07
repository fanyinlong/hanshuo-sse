INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2045, '订单信息', 0, 2, 'orderList', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'list', 'admin', '2025-08-02 10:36:54', '', NULL, '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单汇总', '2045', '1', 'orderInfo', 'sse/orderInfo/index', 1, 0, 'C', '0', '0', 'sse:orderInfo:list', '#', 'admin', sysdate(), '', null, '订单汇总菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单汇总查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'sse:orderInfo:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单汇总新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'sse:orderInfo:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单汇总修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'sse:orderInfo:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单汇总删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'sse:orderInfo:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单汇总导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'sse:orderInfo:export',       '#', 'admin', sysdate(), '', null, '');
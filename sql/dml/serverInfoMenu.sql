-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('主机信息', '2007', '1', 'serverInfo', 'sse/serverInfo/index', 1, 0, 'C', '0', '0', 'sse:serverInfo:list', '#', 'admin', sysdate(), '', null, '主机信息菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('主机信息查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'sse:serverInfo:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('主机信息新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'sse:serverInfo:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('主机信息修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'sse:serverInfo:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('主机信息删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'sse:serverInfo:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('主机信息导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'sse:serverInfo:export',       '#', 'admin', sysdate(), '', null, '');
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PBU配置', '2000', '1', 'pbuConfig', 'sse/pbuConfig/index', 1, 0, 'C', '0', '0', 'sse:pbuConfig:list', '#', 'admin', sysdate(), '', null, 'PBU配置菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PBU配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PBU配置新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PBU配置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PBU配置删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PBU配置导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:export',       '#', 'admin', sysdate(), '', null, '');
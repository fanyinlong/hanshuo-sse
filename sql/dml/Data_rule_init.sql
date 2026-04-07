-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则初始化', '2000', '1', 'ruleInit', 'sse/ruleInit/index', 1, 0, 'C', '0', '0', 'sse:ruleInit:list', '#', 'admin', sysdate(), '', null, '规则初始化菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则初始化查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleInit:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则初始化新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleInit:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则初始化修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleInit:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则初始化删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleInit:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则初始化导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleInit:export',       '#', 'admin', sysdate(), '', null, '');
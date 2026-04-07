INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2000, '性能造数', 0, 1, 'ssedata', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'excel', 'admin', '2025-07-19 18:45:58', '', NULL, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则配置', '2000', '1', 'ruleinfo', 'sse/ruleinfo/index', 1, 0, 'C', '0', '0', 'sse:ruleinfo:list', '#', 'admin', sysdate(), '', null, '规则配置菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则配置新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则配置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则配置删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('规则配置导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:export',       '#', 'admin', sysdate(), '', null, '');

INSERT INTO smdata.sys_menu (menu_name,parent_id,order_num,`path`,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,update_by,update_time,remark) VALUES
	 ('新增规则',2000,0,'ruleadd','sse/ruleinfo/add',NULL,'',1,0,'C','0','0','sse:ruleinfo:add','documentation','admin','2025-07-21 20:01:14','admin','2025-07-26 09:59:51','');

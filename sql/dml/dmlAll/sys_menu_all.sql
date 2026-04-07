INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1, '系统管理', 0, 0, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2025-08-15 00:28:46', 'admin', '2025-08-15 08:55:09', '系统管理目录');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2, '系统监控', 0, 98, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2025-08-15 00:28:46', 'admin', '2025-08-15 01:00:26', '系统监控目录');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(3, '系统工具', 0, 99, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2025-08-15 00:28:46', 'admin', '2025-08-15 01:00:19', '系统工具目录');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2025-08-15 00:28:47', '', NULL, '用户管理菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2025-08-15 00:28:47', '', NULL, '角色管理菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2025-08-15 00:28:47', '', NULL, '菜单管理菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2025-08-15 00:28:47', '', NULL, '部门管理菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2025-08-15 00:28:47', '', NULL, '岗位管理菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2025-08-15 00:28:47', '', NULL, '字典管理菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2025-08-15 00:28:47', '', NULL, '参数设置菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2025-08-15 00:28:47', '', NULL, '通知公告菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2025-08-15 00:28:47', '', NULL, '日志管理菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2025-08-15 00:28:47', '', NULL, '在线用户菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2025-08-15 00:28:47', '', NULL, '定时任务菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2025-08-15 00:28:47', '', NULL, '数据监控菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2025-08-15 00:28:47', '', NULL, '服务监控菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2025-08-15 00:28:47', '', NULL, '缓存监控菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2025-08-15 00:28:47', '', NULL, '缓存列表菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2025-08-15 00:28:47', '', NULL, '表单构建菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2025-08-15 00:28:47', '', NULL, '代码生成菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2025-08-15 00:28:47', '', NULL, '系统接口菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2025-08-15 00:28:47', '', NULL, '操作日志菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2025-08-15 00:28:47', '', NULL, '登录日志菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2025-08-15 00:28:47', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2000, '业务规则', 0, 2, 'ssedata', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'excel', 'admin', '2025-07-19 18:45:58', 'admin', '2025-08-05 19:12:10', '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2001, '规则配置', 2000, 1, 'ruleinfo', 'sse/ruleinfo/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:ruleinfo:list', 'example', 'admin', '2025-07-24 22:49:30', 'admin', '2025-08-07 12:15:50', '规则配置菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2002, '规则配置查询', 2001, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:query', '#', 'admin', '2025-07-24 22:49:30', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2003, '规则配置新增', 2001, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:add', '#', 'admin', '2025-07-24 22:49:30', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2004, '规则配置修改', 2001, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:edit', '#', 'admin', '2025-07-24 22:49:30', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2005, '规则配置删除', 2001, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:remove', '#', 'admin', '2025-07-24 22:49:30', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2006, '规则配置导出', 2001, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleinfo:export', '#', 'admin', '2025-07-24 22:49:30', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2007, '基础数据', 0, 1, 'baseInfo', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'table', 'admin', '2025-07-25 21:40:15', 'admin', '2025-07-25 21:40:33', '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2008, '产品信息', 2007, 2, 'securityInfo', 'sse/securityInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:securityInfo:list', 'druid', 'admin', '2025-07-25 23:43:24', 'admin', '2025-08-15 08:17:54', '产品信息菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2009, '产品信息查询', 2008, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityInfo:query', '#', 'admin', '2025-07-25 23:43:24', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2010, '产品信息新增', 2008, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityInfo:add', '#', 'admin', '2025-07-25 23:43:24', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2011, '产品信息修改', 2008, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityInfo:edit', '#', 'admin', '2025-07-25 23:43:24', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2012, '产品信息删除', 2008, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityInfo:remove', '#', 'admin', '2025-07-25 23:43:24', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2013, '产品信息导出', 2008, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityInfo:export', '#', 'admin', '2025-07-25 23:43:24', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2020, 'PBU信息', 2007, 1, 'pbuInfo', 'sse/pbuInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:pbuInfo:list', 'checkbox', 'admin', '2025-07-27 23:50:04', 'admin', '2025-08-07 12:13:43', 'PBU信息菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2021, 'PBU信息查询', 2020, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuInfo:query', '#', 'admin', '2025-07-27 23:50:04', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2022, 'PBU信息新增', 2020, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuInfo:add', '#', 'admin', '2025-07-27 23:50:04', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2023, 'PBU信息修改', 2020, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuInfo:edit', '#', 'admin', '2025-07-27 23:50:04', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2024, 'PBU信息删除', 2020, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuInfo:remove', '#', 'admin', '2025-07-27 23:50:04', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2025, 'PBU信息导出', 2020, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuInfo:export', '#', 'admin', '2025-07-27 23:50:04', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2026, '账户信息', 2007, 3, 'accountInfo', 'sse/accountInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:accountInfo:list', 'user', 'admin', '2025-07-28 12:17:22', 'admin', '2025-08-15 08:18:02', '账户信息菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2027, '账户信息查询', 2026, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:accountInfo:query', '#', 'admin', '2025-07-28 12:17:23', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2028, '账户信息新增', 2026, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:accountInfo:add', '#', 'admin', '2025-07-28 12:17:23', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2029, '账户信息修改', 2026, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:accountInfo:edit', '#', 'admin', '2025-07-28 12:17:23', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2030, '账户信息删除', 2026, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:accountInfo:remove', '#', 'admin', '2025-07-28 12:17:23', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2031, '账户信息导出', 2026, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:accountInfo:export', '#', 'admin', '2025-07-28 12:17:23', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2032, '新增规则', 2000, 0, 'ruleadd', 'sse/ruleinfo/add', NULL, '', 1, 0, 'C', '0', '0', 'sse:ruleinfo:add', 'documentation', 'admin', '2025-07-21 20:01:14', 'admin', '2025-08-07 09:43:51', '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2033, 'PBU配置', 2007, 9, 'pbuConfig', 'sse/pbuConfig/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:pbuConfig:list', 'component', 'admin', '2025-07-28 23:30:53', 'admin', '2025-08-07 12:15:20', 'PBU配置菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2034, 'PBU配置查询', 2033, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:query', '#', 'admin', '2025-07-28 23:30:53', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2035, 'PBU配置新增', 2033, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:add', '#', 'admin', '2025-07-28 23:30:53', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2036, 'PBU配置修改', 2033, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:edit', '#', 'admin', '2025-07-28 23:30:53', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2037, 'PBU配置删除', 2033, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:remove', '#', 'admin', '2025-07-28 23:30:53', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2038, 'PBU配置导出', 2033, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:pbuConfig:export', '#', 'admin', '2025-07-28 23:30:53', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2039, '主机信息', 2007, 1, 'serverInfo', 'sse/serverInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:serverInfo:list', 'monitor', 'admin', '2025-07-29 23:26:17', 'admin', '2025-08-07 12:14:49', '主机信息菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2040, '主机信息查询', 2039, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:serverInfo:query', '#', 'admin', '2025-07-29 23:26:18', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2041, '主机信息新增', 2039, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:serverInfo:add', '#', 'admin', '2025-07-29 23:26:18', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2042, '主机信息修改', 2039, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:serverInfo:edit', '#', 'admin', '2025-07-29 23:26:18', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2043, '主机信息删除', 2039, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:serverInfo:remove', '#', 'admin', '2025-07-29 23:26:18', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2044, '主机信息导出', 2039, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:serverInfo:export', '#', 'admin', '2025-07-29 23:26:18', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2045, '订单信息', 0, 4, 'orderList', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'list', 'admin', '2025-08-02 10:36:54', 'admin', '2025-08-05 19:12:31', '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2046, '订单汇总', 2045, 1, 'orderInfo', 'sse/orderInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:orderInfo:list', 'date-range', 'admin', '2025-08-02 14:12:09', 'admin', '2025-08-07 12:18:10', '订单汇总菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2047, '订单汇总查询', 2046, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderInfo:query', '#', 'admin', '2025-08-02 14:12:09', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2048, '订单汇总新增', 2046, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderInfo:add', '#', 'admin', '2025-08-02 14:12:09', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2049, '订单汇总修改', 2046, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderInfo:edit', '#', 'admin', '2025-08-02 14:12:09', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2050, '订单汇总删除', 2046, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderInfo:remove', '#', 'admin', '2025-08-02 14:12:09', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2051, '订单汇总导出', 2046, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderInfo:export', '#', 'admin', '2025-08-02 14:12:09', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2052, '规则初始化', 2000, 2, 'ruleInit', 'sse/ruleInit/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:ruleInit:list', 'form', 'admin', '2025-08-05 19:01:37', 'admin', '2025-08-15 08:17:31', '规则初始化菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2053, '规则初始化查询', 2052, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleInit:query', '#', 'admin', '2025-08-05 19:01:37', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2054, '规则初始化新增', 2052, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleInit:add', '#', 'admin', '2025-08-05 19:01:37', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2055, '规则初始化修改', 2052, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleInit:edit', '#', 'admin', '2025-08-05 19:01:37', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2056, '规则初始化删除', 2052, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleInit:remove', '#', 'admin', '2025-08-05 19:01:37', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2057, '规则初始化导出', 2052, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleInit:export', '#', 'admin', '2025-08-05 19:01:37', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2058, '规则执行', 0, 3, 'orderexe', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'button', 'admin', '2025-08-05 19:11:09', 'admin', '2025-08-05 19:12:44', '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2059, '规则执行记录', 2058, 1, 'ruleExecute', 'sse/ruleExecute/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:ruleExecute:list', 'log', 'admin', '2025-08-05 19:18:33', 'admin', '2025-08-07 12:17:15', '规则执行记录菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2060, '规则执行记录查询', 2059, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleExecute:query', '#', 'admin', '2025-08-05 19:18:33', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2061, '规则执行记录新增', 2059, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleExecute:add', '#', 'admin', '2025-08-05 19:18:33', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2062, '规则执行记录修改', 2059, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleExecute:edit', '#', 'admin', '2025-08-05 19:18:33', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2063, '规则执行记录删除', 2059, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleExecute:remove', '#', 'admin', '2025-08-05 19:18:33', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2064, '规则执行记录导出', 2059, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:ruleExecute:export', '#', 'admin', '2025-08-05 19:18:33', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2071, '加持仓统计', 2045, 3, 'securityAndAccount', 'sse/securityAndAccount/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:securityAndAccount:list', 'nested', 'admin', '2025-08-15 01:29:20', 'admin', '2025-08-15 08:19:35', '加持仓统计菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2072, '加持仓统计查询', 2071, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityAndAccount:query', '#', 'admin', '2025-08-15 01:29:20', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2073, '加持仓统计新增', 2071, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityAndAccount:add', '#', 'admin', '2025-08-15 01:29:20', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2074, '加持仓统计修改', 2071, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityAndAccount:edit', '#', 'admin', '2025-08-15 01:29:20', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2075, '加持仓统计删除', 2071, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityAndAccount:remove', '#', 'admin', '2025-08-15 01:29:20', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2076, '加持仓统计导出', 2071, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:securityAndAccount:export', '#', 'admin', '2025-08-15 01:29:20', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2077, '订单统计', 2045, 2, 'orderStats', 'sse/orderStats/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:orderStats:list', 'dashboard', 'admin', '2025-08-15 01:32:15', 'admin', '2025-08-15 08:19:08', '订单统计菜单');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2078, '订单统计查询', 2077, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderStats:query', '#', 'admin', '2025-08-15 01:32:15', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2079, '订单统计新增', 2077, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderStats:add', '#', 'admin', '2025-08-15 01:32:15', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2080, '订单统计修改', 2077, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderStats:edit', '#', 'admin', '2025-08-15 01:32:15', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2081, '订单统计删除', 2077, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderStats:remove', '#', 'admin', '2025-08-15 01:32:15', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2082, '订单统计导出', 2077, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'sse:orderStats:export', '#', 'admin', '2025-08-15 01:32:15', '', NULL, '');
INSERT INTO smdata.sys_menu
(menu_id, menu_name, parent_id, order_num, `path`, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES(2083, '订单图表', 2045, 5, 'orderCharts', 'sse/orderCharts/index', NULL, '', 1, 0, 'C', '0', '0', 'sse:orderCharts:list', 'example', 'admin', '2025-08-15 08:49:52', 'admin', '2025-08-15 09:08:20', '');
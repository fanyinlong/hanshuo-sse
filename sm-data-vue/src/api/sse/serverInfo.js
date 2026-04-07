import request from '@/utils/request'

// 查询主机信息列表
export function listServerInfo(query) {
  return request({
    url: '/sse/serverInfo/list',
    method: 'get',
    params: query
  })
}

// 查询主机规则信息列表
export function listServerRule(query) {
  return request({
    url: '/sse/serverRule/list',
    method: 'get',
    params: query
  })
}

// 查询主机信息详细
export function getServerInfo(serverId) {
  return request({
    url: '/sse/serverInfo/' + serverId,
    method: 'get'
  })
}

// 查询主机规则详细
export function getServerRule(id) {
  return request({
    url: '/sse/serverRule/' + id,
    method: 'get'
  })
}


// 新增主机信息
export function addServerInfo(data) {
  return request({
    url: '/sse/serverInfo',
    method: 'post',
    data: data
  })
}

// 新增主机规则信息
export function addServerRule(data) {
  return request({
    url: '/sse/serverRule/addServerRule',
    method: 'post',
    data: data
  })
}

// 修改主机信息
export function updateServerInfo(data) {
  return request({
    url: '/sse/serverInfo',
    method: 'put',
    data: data
  })
}

// 修改主机规则
export function updateServerRule(data) {
  return request({
    url: '/sse/serverRule',
    method: 'put',
    data: data
  })
}

// 导出到sqlServer
export function exportServerRule(ruleId) {
  return request({
    url: '/sse/serverRule/export/' + ruleId,
    method: 'get'
  })
}



// 删除主机信息
export function delServerInfo(serverId) {
  return request({
    url: '/sse/serverInfo/' + serverId,
    method: 'delete'
  })
}

// 删除主机规则信息
export function delServerRule(id) {
  return request({
    url: '/sse/serverRule/' + id,
    method: 'delete'
  })
}

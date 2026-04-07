import request from '@/utils/request'

// 查询加持仓统计列表
export function listSecurityAndAccount(query) {
  return request({
    url: '/sse/securityAndAccount/list',
    method: 'get',
    params: query
  })
}

// 查询加持仓统计详细
export function getSecurityAndAccount(pkId) {
  return request({
    url: '/sse/securityAndAccount/' + pkId,
    method: 'get'
  })
}

// 新增加持仓统计
export function addSecurityAndAccount(data) {
  return request({
    url: '/sse/securityAndAccount',
    method: 'post',
    data: data
  })
}

// 修改加持仓统计
export function updateSecurityAndAccount(data) {
  return request({
    url: '/sse/securityAndAccount',
    method: 'put',
    data: data
  })
}

// 删除加持仓统计
export function delSecurityAndAccount(pkId) {
  return request({
    url: '/sse/securityAndAccount/' + pkId,
    method: 'delete'
  })
}

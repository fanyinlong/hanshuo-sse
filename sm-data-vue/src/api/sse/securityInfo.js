import request from '@/utils/request'

// 查询产品信息列表
export function listSecurityInfo(query) {
  return request({
    url: '/sse/securityInfo/list',
    method: 'get',
    params: query
  })
}

// 查询产品信息详细
export function getSecurityInfo(pkId) {
  return request({
    url: '/sse/securityInfo/' + pkId,
    method: 'get'
  })
}

// 新增产品信息
export function addSecurityInfo(data) {
  return request({
    url: '/sse/securityInfo',
    method: 'post',
    data: data
  })
}

// 修改产品信息
export function updateSecurityInfo(data) {
  return request({
    url: '/sse/securityInfo',
    method: 'put',
    data: data
  })
}

// 删除产品信息
export function delSecurityInfo(pkId) {
  return request({
    url: '/sse/securityInfo/' + pkId,
    method: 'delete'
  })
}

// 初始化产品信息
export function init() {
  return request({
    url: '/sse/securityInfo/init',
    method: 'post'
  })
}




import request from '@/utils/request'

// 查询PBU信息列表
export function listPbuInfo(query) {
  return request({
    url: '/sse/pbuInfo/list',
    method: 'get',
    params: query
  })
}

// 查询PBU信息详细
export function getPbuInfo(pbuId) {
  return request({
    url: '/sse/pbuInfo/' + pbuId,
    method: 'get'
  })
}

// 新增PBU信息
export function addPbuInfo(data) {
  return request({
    url: '/sse/pbuInfo',
    method: 'post',
    data: data
  })
}

// 修改PBU信息
export function updatePbuInfo(data) {
  return request({
    url: '/sse/pbuInfo',
    method: 'put',
    data: data
  })
}

// 删除PBU信息
export function delPbuInfo(pbuId) {
  return request({
    url: '/sse/pbuInfo/' + pbuId,
    method: 'delete'
  })
}

// 初始化PBU信息
export function init() {
  return request({
    url: '/sse/pbuInfo/init',
    method: 'post'
  })
}

import request from '@/utils/request'

// 查询PBU配置列表
export function listPbuConfig(query) {
  return request({
    url: '/sse/pbuConfig/list',
    method: 'get',
    params: query
  })
}

// 查询PBU配置详细
export function getPbuConfig(pkId) {
  return request({
    url: '/sse/pbuConfig/' + pkId,
    method: 'get'
  })
}

// 新增PBU配置
export function addPbuConfig(data) {
  return request({
    url: '/sse/pbuConfig',
    method: 'post',
    data: data
  })
}

// 修改PBU配置
export function updatePbuConfig(data) {
  return request({
    url: '/sse/pbuConfig',
    method: 'put',
    data: data
  })
}

// 删除PBU配置
export function delPbuConfig(pkId) {
  return request({
    url: '/sse/pbuConfig/' + pkId,
    method: 'delete'
  })
}


// 初始化PBU信息
export function init() {
  return request({
    url: '/sse/pbuConfig/init',
    method: 'post'
  })
}
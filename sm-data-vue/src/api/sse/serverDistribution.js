import request from '@/utils/request'

// 查询服务器分发记录列表
export function listServerDistribution(query) {
  return request({
    url: '/sse/serverDistribution/list',
    method: 'get',
    params: query
  })
}

// 查询服务器分发记录详细
export function getServerDistribution(pkId) {
  return request({
    url: '/sse/serverDistribution/' + pkId,
    method: 'get'
  })
}

// 新增服务器分发记录
export function addServerDistribution(data) {
  return request({
    url: '/sse/serverDistribution',
    method: 'post',
    data: data
  })
}

// 修改服务器分发记录
export function updateServerDistribution(data) {
  return request({
    url: '/sse/serverDistribution',
    method: 'put',
    data: data
  })
}

// 删除服务器分发记录
export function delServerDistribution(pkId) {
  return request({
    url: '/sse/serverDistribution/' + pkId,
    method: 'delete'
  })
}

// 获取分发进度
export function getServerDistributionProgress(pkId) {
  return request({
    url: '/sse/serverDistribution/progress/' + pkId,
    method: 'get'
  })
}

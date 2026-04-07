import request from '@/utils/request'

// 查询订单汇总列表
export function listOrderInfo(query) {
  return request({
    url: '/sse/orderInfo/list',
    method: 'get',
    params: query
  })
}

// 查询订单汇总详细
export function getOrderInfo(pkId) {
  return request({
    url: '/sse/orderInfo/' + pkId,
    method: 'get'
  })
}

// 新增订单汇总
export function addOrderInfo(data) {
  return request({
    url: '/sse/orderInfo',
    method: 'post',
    data: data
  })
}

// 修改订单汇总
export function updateOrderInfo(data) {
  return request({
    url: '/sse/orderInfo',
    method: 'put',
    data: data
  })
}

// 删除订单汇总
export function delOrderInfo(pkId) {
  return request({
    url: '/sse/orderInfo/' + pkId,
    method: 'delete'
  })
}

// 导出sql文件
export function exportSqlFile(ruleId) {
  return request({
    url: '/sse/orderInfo/exportSqlFile/' + ruleId,
    method: 'get'
  })
}

//获取订单进度
export function getOrderProgress(pkId) {
  return request({
    url: '/sse/ruleExecute/progress/'+pkId,
    method: 'get'
  })
}

import request from '@/utils/request'

// 查询规则执行记录列表
export function listRuleExecute(query) {
  return request({
    url: '/sse/ruleExecute/list',
    method: 'get',
    params: query
  })
}

// 查询规则执行记录详细
export function getRuleExecute(pkId) {
  return request({
    url: '/sse/ruleExecute/' + pkId,
    method: 'get'
  })
}

// 新增规则执行记录
export function addRuleExecute(data) {
  return request({
    url: '/sse/ruleExecute',
    method: 'post',
    data: data
  })
}

// 修改规则执行记录
export function updateRuleExecute(data) {
  return request({
    url: '/sse/ruleExecute',
    method: 'put',
    data: data
  })
}

// 删除规则执行记录
export function delRuleExecute(pkId) {
  return request({
    url: '/sse/ruleExecute/' + pkId,
    method: 'delete'
  })
}

// 获取订单总数
export function getTotalOrderCountByRuleId(ruleId) {
  return request({
    url: '/sse/ruleExecute/totalOrderCount/' + ruleId,
    method: 'get'
  })
}

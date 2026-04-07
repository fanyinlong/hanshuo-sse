import request from '@/utils/request'

// 查询规则初始化列表
export function listRuleInit(query) {
  return request({
    url: '/sse/ruleInit/list',
    method: 'get',
    params: query
  })
}

// 查询规则初始化详细
export function getRuleInit(pkId) {
  return request({
    url: '/sse/ruleInit/' + pkId,
    method: 'get'
  })
}

// 新增规则初始化
export function addRuleInit(data) {
  return request({
    url: '/sse/ruleInit',
    method: 'post',
    data: data
  })
}

// 修改规则初始化
export function updateRuleInit(data) {
  return request({
    url: '/sse/ruleInit',
    method: 'put',
    data: data
  })
}

// 删除规则初始化
export function delRuleInit(pkId) {
  return request({
    url: '/sse/ruleInit/' + pkId,
    method: 'delete'
  })
}

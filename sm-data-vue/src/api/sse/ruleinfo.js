import request from '@/utils/request'

// 查询规则配置列表
export function listRuleinfo(query) {
  return request({
    url: '/sse/ruleinfo/list',
    method: 'get',
    params: query
  })
}

// 查询规则配置详细
export function getRuleinfo(pkId) {
  return request({
    url: '/sse/ruleinfo/' + pkId,
    method: 'get'
  })
}

// 新增规则配置
export function addRuleinfo(data) {
  return request({
    url: '/sse/ruleinfo',
    method: 'post',
    data: data
  })
}

// 修改规则配置
export function updateRuleinfo(data) {
  return request({
    url: '/sse/ruleinfo',
    method: 'put',
    data: data
  })
}

// 删除规则配置
export function delRuleinfo(pkId) {
  return request({
    url: '/sse/ruleinfo/' + pkId,
    method: 'delete'
  })
}


// 规则配置初始化
export function initRuleData(pkId) {
  return request({
    url: '/sse/ruleinfo/ruleInit/' + pkId,
    method: 'post'
  })
}
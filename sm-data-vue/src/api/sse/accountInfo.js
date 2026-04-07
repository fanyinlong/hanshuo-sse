import request from '@/utils/request'

// 查询账户信息列表
export function listAccountInfo(query) {
  return request({
    url: '/sse/accountInfo/list',
    method: 'get',
    params: query
  })
}

// 查询账户信息详细
export function getAccountInfo(pkId) {
  return request({
    url: '/sse/accountInfo/' + pkId,
    method: 'get'
  })
}

// 新增账户信息
export function addAccountInfo(data) {
  return request({
    url: '/sse/accountInfo',
    method: 'post',
    data: data
    
  })
}

// 修改账户信息
export function updateAccountInfo(data) {
  return request({
    url: '/sse/accountInfo',
    method: 'put',
    data: data
  })
}

// 删除账户信息
export function delAccountInfo(pkId) {
  return request({
    url: '/sse/accountInfo/' + pkId,
    method: 'delete'
  })
}

// 初始化账户信息
export function init() {
  return request({
    url: '/sse/accountInfo/init',
    method: 'post'
  })
}

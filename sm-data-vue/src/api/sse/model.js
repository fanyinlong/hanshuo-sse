import request from '@/utils/request'

// 查询模型参数列表
export function listModelParams(query) {
  return request({
    url: '/sse/model/addShow',
    method: 'get'
  })
}

// 查询模型信息列表
export function listDodelInfo(query) {
  return request({
    url: '/sse/model/list',
    method: 'get',
    params: query
  })
}

// 查询模型信息详细
export function getModelInfo(modelId) {
  return request({
    url: '/sse/model/updateShow/'+ modelId,
    method: 'get',
  })
}

// 新增模型信息
export function addModelInfo(data) {
  return request({
    url: '/sse/model',
    method: 'post',
    data: data
    
  })
}

// 修改模型信息
// export function updatemodelInfo(modelId) {
//   return request({
//     url: '/dev-api/sse/model/updateShow'+ modelId,
//     method: 'get',
//   })
// }

// 删除模型信息
// export function delAccountInfo(pkId) {
//   return request({
//     url: '/sse/accountInfo/' + pkId,
//     method: 'delete'
//   })
// }

// 禁用/启用模型信息
export function initModelInfo() {
  return request({
    url: '/sse/model/setStatus/{modelId}/{status}',
    method: 'get'
  })
}

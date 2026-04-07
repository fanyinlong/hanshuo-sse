import request from '@/utils/request'

// 查询订单统计列表
export function listOrderStats(query) {
  return request({
    url: '/sse/orderStats/list',
    method: 'get',
    params: query
  })
}

// 查询订单统计详细
export function getOrderStats(pkId) {
  return request({
    url: '/sse/orderStats/' + pkId,
    method: 'get'
  })
}

// 新增订单统计
export function addOrderStats(data) {
  return request({
    url: '/sse/orderStats',
    method: 'post',
    data: data
  })
}

// 刷新订单统计
export function refreshOrderStats(pkId) {
  return request({
    url: '/sse/orderStats/refresh/' + pkId,
    method: 'post'
  })
}

// 修改订单统计
export function updateOrderStats(data) {
  return request({
    url: '/sse/orderStats',
    method: 'put',
    data: data
  })
}

// 删除订单统计
export function delOrderStats(pkId) {
  return request({
    url: '/sse/orderStats/' + pkId,
    method: 'delete'
  })
}

// 查询订单分类详情
export function listOrderStatsDetail(query) {
  return request({
    url: '/sse/orderStats/listDetail',
    method: 'post',
    params: query
  })
}

export function convertToChartData(jsonString) {
  try {
    // 解析JSON字符串为对象
    const jsonObj = JSON.parse(jsonString);

    // 转换为饼图所需的数据结构
    const data = Object.entries(jsonObj).map(([key, value]) => ({
      name: key,
      value: value
    }));

    return { data: data };
  } catch (error) {
    console.error('JSON转换失败:', error);
    return { data: [] }; // 转换失败返回空数据
  }
}

<template>
  <div class="stats-dashboard">
    <!-- 顶部筛选区域 -->
    <div class="filter-bar">
      <!-- 左侧：ruleId搜索选择器 -->
      <div class="left-filter">
        <div class="search-wrapper" ref="searchWrapper">
          <el-input
            v-model="inputValue"
            placeholder="请输入ruleId搜索"
            clearable
            @input="handleInput"
            @clear="handleClear"
            @focus="handleFocus"
            @blur="handleBlur"
            ref="searchInput"
            :readonly="isSelecting"
          >
            <template slot="prefix">
              <span class="search-icon">🔍</span>
            </template>
          </el-input>
          
          <!-- 搜索结果下拉框 -->
          <el-popover
            v-model="dropdownVisible"
            :width="250"
            trigger="manual"
            placement="bottom-start"
            :offset="[-1, 5]"
            popper-class="search-popover"
          >
            <div class="result-container" @click.stop>
              <template v-if="loadingRuleId">
                <div class="loading-state">
                  <span class="loading-spinner">●●●</span>
                  <span class="loading-text">搜索中...</span>
                </div>
              </template>
              
              <template v-else-if="searchResult.length > 0">
                <el-scrollbar max-height="300px" class="result-scroll">
                  <div 
                    class="result-item"
                    v-for="item in searchResult"
                    :key="item.ruleId"
                    @click="handleSelectRuleId(item)"
                    @mouseenter="hoverIndex = item.ruleId"
                    :class="{ 'result-item-hover': hoverIndex === item.ruleId }"
                  >
                    <div class="rule-id">ruleId: {{ item.ruleId }}</div>
                    <div class="other-info" v-if="item.name">名称: {{ item.name }}</div>
                  </div>
                </el-scrollbar>
              </template>
              
              <template v-else>
                <div class="empty-state">
                  <span class="empty-text">未找到匹配的ruleId</span>
                </div>
              </template>
            </div>
          </el-popover>
        </div>
      </div>
      <!-- 中间空白区域（自动填充剩余空间） -->
      <div class="middle-space"></div>
      <!-- 右侧：图表类型单选下拉框 -->
      <div class="right-filter">
        <el-select
          v-model="selectedChartType"
          placeholder="请选择统计图表类型"
          @change="handleChartTypeChange"
          clearable
          style="width: 250px"
        >
          <el-option
            v-for="dict in dict.type.order_stats_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </div>
    </div>
    
    <!-- 图表展示区域 - 占满剩余屏幕空间 -->
    <div class="chart-container" v-if="selectedRuleId && selectedChartType">
      <div class="chart-title">
        {{ getChartTypeName(selectedChartType) }} 统计 (ruleId: {{ selectedRuleId }})
      </div>
      <div class="chart-wrapper">
        <div 
          :id="'main-chart'"
          class="chart-dom"
        ></div>
        <div class="chart-loading" v-if="chartLoading">
          <span class="loading-spinner">●●●</span>
          <span class="loading-text">数据加载中...</span>
        </div>
        <div class="chart-empty" v-if="!chartLoading && !chartData">
          暂无数据可展示
        </div>
      </div>
    </div>
    
    <!-- 空状态提示 -->
    <div class="dashboard-empty" v-else>
      <div class="empty-content">
        <i class="el-icon-info"></i>
        <p>请先选择ruleId和统计图表类型</p>
      </div>
    </div>
  </div>
</template>

<script>
import { listOrderStats,listOrderStatsDetail,convertToChartData } from '@/api/sse/orderStats';
// 正确的引入方式（替换原有的 import 语句）
import * as echarts from 'echarts/lib/echarts.js';
import 'echarts/lib/chart/pie'; 
// 按需引入饼图组件（减少打包体积，可选）
import 'echarts/lib/chart/pie';
// 引入提示框、图例等组件（按需添加）
import 'echarts/lib/component/tooltip';
import 'echarts/lib/component/legend';
console.log('ECharts 引入结果:', echarts); // 应显示 Object 而非 undefined


export default {
  name: 'SingleChartDashboard',
  dicts: ['order_stats_type'],

  data() {
    return {
      // 左侧搜索相关
      inputValue: '',
      dropdownVisible: false,
      searchResult: [],
      loadingRuleId: false,
      hoverIndex: null,
      searchTimeout: null,
      isSelecting: false,
      selectedRuleId: null,
      
      // 右侧图表选择相关（改为单选）
      selectedChartType: '', // 单选值
      
      // 图表数据相关
      chartData: null,      // 单个图表数据
      chartLoading: false,  // 单个图表加载状态
      chartInstance: null  // 单个echarts实例
    };
  },
  methods: {
    // 左侧ruleId搜索处理
    handleInput(val) {
      if (this.searchTimeout) clearTimeout(this.searchTimeout);
      
      if (!val) {
        this.searchResult = [];
        this.dropdownVisible = false;
        return;
      }
      
      this.searchTimeout = setTimeout(() => {
        this.fetchRuleIdResult(val);
      }, 300);
    },
    
    handleClear() {
      this.searchResult = [];
      this.dropdownVisible = false;
      this.selectedRuleId = null;
      this.clearChart();
    },
    
    handleFocus() {
      if (this.inputValue) this.dropdownVisible = true;
    },
    
    handleBlur() {
      if (!this.isSelecting) {
        this.searchTimeout = setTimeout(() => {
          this.dropdownVisible = false;
        }, 200);
      }
    },
    
    async fetchRuleIdResult(ruleId) {
      try {
        this.loadingRuleId = true;
        const response = await listOrderStats({ ruleId });
        this.searchResult = response.rows || [];
        this.dropdownVisible = true;
      } catch (error) {
        console.error('搜索ruleId失败:', error);
        this.searchResult = [];
      } finally {
        this.loadingRuleId = false;
      }
    },
    
    handleSelectRuleId(item) {
      this.isSelecting = true;
      this.inputValue = item.ruleId.toString();
      this.selectedRuleId = item.ruleId;
      this.dropdownVisible = false;
      this.$refs.searchInput.focus();
      this.isSelecting = false;
      
      // 选中ruleId后，如果已有图表选择则加载数据
      if (this.selectedChartType) {
        this.fetchChartData();
      }
    },
    
    // 右侧图表类型选择处理（单选）
    handleChartTypeChange() {
      if (this.selectedRuleId && this.selectedChartType) {
        this.fetchChartData();
      } else {
        this.clearChart(); // 清除图表
      }
    },
    
    // 获取图表数据
    async fetchChartData() {
      // 清空之前的图表
      this.clearChart();
      
      // 标记加载状态
      this.chartLoading = true;
      
      try {
        // 调用接口，传入ruleId和选中的图表类型
        const params = new URLSearchParams();
        params.append('ruleId', this.selectedRuleId);
        params.append('chartType', this.selectedChartType);
        const response = await listOrderStatsDetail(params);
            // 2. 处理响应数据（关键转换逻辑）
            const rawData = response; // 手动解析字符串为对象
            console.log('原始响应数据:', rawData);        
            // 3. 判空处理
            if (!rawData || Object.keys(rawData).length === 0) {
          this.$message.warning('暂无数据可展示');
          this.chartData = null;
          return;
        }
                // 3. 转换为饼图所需格式：[{name: '12020', value: 9827}, ...]
            const chartData = Object.entries(rawData).map(([key, value]) => ({
              name: key,       // 键作为名称
              value: Number(value)     // 值作为数值
            }));
            console.log('处理后的图表数据:', chartData);
                        // 5. 保存数据并生成图表
            this.chartData = chartData;
            this.generatePieChart(chartData);
      } catch (error) {
        console.error('获取图表数据失败:', error);
        this.chartData = null;
      } finally {
        this.chartLoading = false;
      }
    },
    
    // 生成饼图（占满屏幕）
    generatePieChart(data) {
       // 1. 再次确认 ECharts 可用
  if (typeof echarts === 'undefined' || typeof echarts.init !== 'function') {
    console.error('ECharts 初始化函数不存在！');
    this.$message.error('图表依赖异常');
    return;
  }

  // 2. 确保数据有效
  if (!data || !Array.isArray(data) || data.length === 0) {
    console.error('图表数据无效:', data);
    this.$message.warning('图表数据为空');
    return;
  }

      const chartDom = document.getElementById('main-chart');
      if (!chartDom) return;
      
      // 销毁已有实例
      if (this.chartInstance) {
        this.chartInstance.dispose();
      }
      
      // 创建新实例
      this.chartInstance = echarts.init(chartDom);

      
      // 饼图配置（优化展示效果）
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)',
          textStyle: { fontSize: 14 },
          padding: 12
        },
        legend: {
          orient: 'horizontal',
          bottom: 20,
          textStyle: { fontSize: 14 },
          itemWidth: 14,
          itemHeight: 14,
          data: data.map(item => item.name)
        },
        series: [
          {
            name: this.getChartTypeName(this.selectedChartType),
            type: 'pie',
            radius: ['35%', '70%'], // 适中大小，保证清晰
            center: ['50%', '45%'], // 垂直居中
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 8,
              borderColor: '#fff',
              borderWidth: 2,
              shadowBlur: 10,
              shadowColor: 'rgba(0, 0, 0, 0.1)'
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 20,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data // 格式: [{ name: 'xxx', value: 123 }, ...]
          }
        ]
      };
      
      this.chartInstance.setOption(option);
      
      // 监听窗口大小变化，重绘图表
      const resizeHandler = () => {
        this.chartInstance.resize();
      };
      
      window.addEventListener('resize', resizeHandler);
      
      // 组件销毁时移除监听
      this.$once('hook:beforeDestroy', () => {
        window.removeEventListener('resize', resizeHandler);
      });
    },
    
    // 清空图表
    clearChart() {
      if (this.chartInstance) {
        this.chartInstance.dispose();
        this.chartInstance = null;
      }
      this.chartData = null;
      this.chartLoading = false;
    },
    
    // 根据图表类型编码获取名称
    getChartTypeName(code) {
      const dict = this.dict.type.order_stats_type.find(item => item.code === code);
      return dict ? dict.name : code;
    },
    
    // 点击外部关闭下拉框
    handleClickOutside(e) {
      if (this.dropdownVisible && this.$refs.searchWrapper) {
        const popover = document.querySelector('.search-popover');
        if (!this.$refs.searchWrapper.contains(e.target) && 
            !(popover && popover.contains(e.target))) {
          this.dropdownVisible = false;
        }
      }
    }
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside);
    
    // 初始化图表容器高度（占满剩余屏幕）
    const setChartHeight = () => {
      const windowHeight = window.innerHeight;
      const filterBarHeight = document.querySelector('.filter-bar').offsetHeight;
      const chartDom = document.querySelector('.chart-dom');
      if (chartDom) {
        // 减去顶部筛选区高度和边距
        chartDom.style.height = `${windowHeight - filterBarHeight - 100}px`;
        // 如果已有图表实例，触发重绘
        if (this.chartInstance) {
          this.chartInstance.resize();
        }
      }
    };
    
    // 初始设置
    setChartHeight();
    // 窗口大小变化时重新计算
    window.addEventListener('resize', setChartHeight);
    // 组件销毁时移除监听
    this.$once('hook:beforeDestroy', () => {
      window.removeEventListener('resize', setChartHeight);
    });
  },
  beforeDestroy() {
    document.removeEventListener('click', this.handleClickOutside);
    if (this.searchTimeout) clearTimeout(this.searchTimeout);
    this.clearChart();
  }
};
</script>

<style scoped>
.stats-dashboard {
  padding: 20px;
  box-sizing: border-box;
  height: 100vh; /* 占满整个视口高度 */
  overflow: hidden;
}

/* 筛选区域样式 */
.filter-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  gap: 20px;
  height: 40px;
}

.left-filter {
  width: 250px;
}

.search-wrapper {
  position: relative;
  width: 100%;
}
/* 中间空白区域自动填充剩余空间 */
.middle-space {
  flex: 1;
}

/* 右侧选择器固定宽度250px */
.right-filter {
  width: 250px;
}
.search-icon {
  color: #909399;
  font-size: 16px;
}

::v-deep .search-popover {
  z-index: 1000 !important;
}

.result-container {
  padding: 10px 0;
}

.loading-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
  color: #606266;
}

.loading-spinner {
  animation: spin 1.5s infinite;
  font-size: 16px;
}

@keyframes spin {
  0% { opacity: 0.3; }
  50% { opacity: 1; }
  100% { opacity: 0.3; }
}

.loading-text {
  margin-left: 8px;
  font-size: 14px;
}

.result-item {
  padding: 10px 12px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 14px;
  user-select: none;
}

.result-item-hover {
  background-color: #f5f7fa;
}

/* 图表区域样式 - 占满剩余空间 */
.chart-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: calc(100vh - 100px); /* 减去顶部高度和边距 */
  background-color: #fff;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.chart-title {
  padding: 15px 20px;
  font-size: 18px;
  color: #303133;
  border-bottom: 1px solid #f0f0f0;
  font-weight: 500;
}

.chart-wrapper {
  flex: 1;
  position: relative;
  overflow: hidden;
}

.chart-dom {
  width: 100%;
  height: 100%;
}

.chart-loading {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #606266;
  background-color: rgba(255, 255, 255, 0.8);
  flex-direction: column;
  gap: 10px;
}

.chart-loading .loading-spinner {
  font-size: 24px;
}

.chart-empty {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  background-color: #f9f9f9;
  font-size: 16px;
}

/* 空状态样式 */
.dashboard-empty {
  height: calc(100vh - 100px);
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9f9f9;
  border-radius: 6px;
}

.empty-content {
  text-align: center;
  color: #909399;
}

.empty-content .el-icon-info {
  font-size: 36px;
  margin-bottom: 15px;
}

.empty-content p {
  font-size: 16px;
}
</style>

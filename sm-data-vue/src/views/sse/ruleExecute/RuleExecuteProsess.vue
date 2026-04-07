<template>
    <div class="app-container">
        <el-card shadow="hover">
            <div class="progress-container">
                <div class="progress-info">
                    <span>订单总数量: {{ totalCount }}</span>
                    <span>已生成数量: {{ generatedCount }}</span>
                </div>
                <el-progress :percentage="progressPercentage" :color="customColors" :stroke-width="16"
                    :text-inside="true" />
                <div class="time-info">
                    预计剩余时间: {{ estimatedTime }}
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
import { getOrderProgress } from '@/api/sse/orderInfo'

export default {
    name: 'OrderProgress',
    data() {
        return {
            totalCount: 0, // 示例总数
            generatedCount: 0,
            progressPercentage: 0,
            customColors: [
                { color: '#f56c6c', percentage: 20 },
                { color: '#e6a23c', percentage: 40 },
                { color: '#5cb87a', percentage: 60 },
                { color: '#1989fa', percentage: 80 },
                { color: '#6f7ad3', percentage: 100 }
            ],
            timer: null,
            refreshInterval: 3000, // 3秒刷新一次
            pkId: null,
        }
    },
    computed: {
        estimatedTime() {
            if (this.generatedCount >= this.totalCount) return '已完成'
            const remaining = this.totalCount - this.generatedCount
            // 按照每秒8000个订单数量计算
            const seconds = Math.floor(remaining / 8000)
            if (seconds < 60) {
                return `${seconds}秒`
            } else if (seconds < 3600) {
                return `${Math.floor(seconds / 60)}分${seconds % 60}秒`
            } else {
                const hours = Math.floor(seconds / 3600)
                const minutes = Math.floor((seconds % 3600) / 60)
                return `${hours}小时${minutes}分`
            }
        }
    },
    created() {
        console.log(this.$route.params.id)
        this.pkId = this.$route.params.id
        this.fetchProgress()
        this.startAutoRefresh()
    },
    beforeDestroy() {
        this.stopAutoRefresh()
        if (this.timer) {
            clearInterval(this.timer)
            this.timer = null
        }
    },
    methods: {
        async fetchProgress() {
            try {
                const response = await getOrderProgress(this.pkId)
                this.generatedCount = response.data.generatedCount || 0
                this.totalCount = response.data.totalCount || this.totalCount
                this.progressPercentage = Math.floor((this.generatedCount / this.totalCount) * 100)
            } catch (error) {
                console.error('获取订单进度失败:', error)
            }
        },
        startAutoRefresh() {
            this.timer = setInterval(() => {
                this.fetchProgress()
            }, this.refreshInterval)
        },
        stopAutoRefresh() {
            if (this.timer) {
                clearInterval(this.timer)
                this.timer = null
            }
        }
    }
}
</script>

<style scoped>
.progress-container {
    padding: 20px;
}

.progress-info {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    font-size: 16px;
    color: #606266;
}

.time-info {
    margin-top: 10px;
    text-align: right;
    font-size: 14px;
    color: #909399;
}

.el-progress {
    margin: 20px 0;
}
</style>

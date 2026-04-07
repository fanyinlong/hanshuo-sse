<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="规则ID" prop="ruleId">
        <el-input
          v-model="queryParams.ruleId"
          placeholder="请输入规则ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分发状态" prop="distributionStatus">
        <el-select v-model="queryParams.distributionStatus" placeholder="请选择分发状态" clearable>
          <el-option label="执行中" value="2"/>
          <el-option label="已完成" value="0"/>
          <el-option label="已失败" value="1"/>
        </el-select>
      </el-form-item>
      <el-form-item label="分发人" prop="distributionUser">
        <el-input
          v-model="queryParams.distributionUser"
          placeholder="请输入分发人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分发时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['sse:serverDistribution:remove']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="distributionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="主键 ID" align="center" prop="pkId"/>
      <el-table-column label="规则 ID" align="center" prop="ruleId"/>
      <el-table-column label="分发状态" align="center" prop="distributionStatus">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.distributionStatus === '0'" type="success">已完成</el-tag>
          <el-tag v-else-if="scope.row.distributionStatus === '1'" type="danger">已失败</el-tag>
          <el-tag v-else-if="scope.row.distributionStatus === '2'" type="warning">执行中</el-tag>
          <span v-else>{{ scope.row.distributionStatus }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分发总条数" align="center" prop="totalCount"/>
      <el-table-column label="分发人" align="center" prop="distributionUser"/>
      <el-table-column label="分发时间" align="center" prop="distributionTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.distributionTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分发版本" align="center" prop="distributionVersion"/>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewProgress(scope.row)"
            v-hasPermi="['sse:serverDistribution:query']"
          >查看进度
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sse:serverDistribution:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看分发进度对话框 -->
    <el-dialog title="分发进度详情" :visible.sync="progressDialogVisible" width="600px" append-to-body>
      <div v-if="progressInfo">
        <el-progress
          :percentage="calculateProgress()"
          :status="getProgressStatus()"
          :text-inside="true"
          :stroke-width="20"
        ></el-progress>
        <div style="margin-top: 20px;">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="规则 ID">{{ progressInfo.ruleId }}</el-descriptions-item>
            <el-descriptions-item label="分发状态">
              <el-tag v-if="progressInfo.distributionStatus === '0'" type="success">已完成</el-tag>
              <el-tag v-else-if="progressInfo.distributionStatus === '1'" type="danger">已失败</el-tag>
              <el-tag v-else-if="progressInfo.distributionStatus === '2'" type="warning">执行中</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="分发人">{{ progressInfo.distributionUser }}</el-descriptions-item>
            <el-descriptions-item label="分发时间">{{ parseTime(progressInfo.distributionTime) }}</el-descriptions-item>
            <el-descriptions-item label="分发版本">{{ progressInfo.distributionVersion }}</el-descriptions-item>
            <el-descriptions-item label="分发总条数" v-if="progressInfo.totalCount !== undefined">
              {{ progressInfo.totalCount }}
            </el-descriptions-item>
            <el-descriptions-item label="错误信息" v-if="progressInfo.errorMessage" :span="2">
              <el-alert
                :title="progressInfo.errorMessage"
                type="error"
                :closable="false"
                show-icon
              />
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="progressDialogVisible = false">关 闭</el-button>
        <el-button v-if="progressInfo && progressInfo.distributionStatus === '2'" type="primary"
                   @click="refreshProgress">刷 新
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listServerDistribution,
  getServerDistribution,
  delServerDistribution,
  getServerDistributionProgress
} from "@/api/sse/serverDistribution"

export default {
  name: "DistributionRecord",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 分发记录表格数据
      distributionList: [],
      // 时间范围
      dateRange: [],
      // 进度对话框是否显示
      progressDialogVisible: false,
      // 进度信息
      progressInfo: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleId: null,
        distributionStatus: null,
        distributionUser: null
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询分发记录列表 */
    getList() {
      this.loading = true
      listServerDistribution(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.distributionList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.pkId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const pkIds = row.pkId || this.ids
      this.$modal.confirm('是否确认删除分发记录编号为"' + pkIds + '"的数据项？').then(function () {
        return delServerDistribution(pkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 查看进度详情 */
    handleViewProgress(row) {
      getServerDistribution(row.pkId).then(response => {
        this.progressInfo = response.data
        this.progressDialogVisible = true

        // 如果是执行中状态，开始轮询进度
        if (this.progressInfo.distributionStatus === '2') {
          this.startProgressPolling()
        }
      })
    },
    /** 开始进度轮询 */
    startProgressPolling() {
      this.progressTimer = setInterval(() => {
        if (this.progressInfo && this.progressInfo.distributionStatus === '2') {
          this.refreshProgress()
        } else {
          this.stopProgressPolling()
        }
      }, 2000)
    },
    /** 停止进度轮询 */
    stopProgressPolling() {
      if (this.progressTimer) {
        clearInterval(this.progressTimer)
        this.progressTimer = null
      }
    },
    /** 刷新进度 */
    refreshProgress() {
      if (!this.progressInfo) return

      getServerDistributionProgress(this.progressInfo.pkId).then(response => {
        if (response.code === 200) {
          const progressData = response.data
          this.progressInfo.distributionStatus = progressData.status === 'completed' ? '0' :
            progressData.status === 'failed' ? '1' : '2'

          if (progressData.status === 'completed' || progressData.status === 'failed') {
            this.stopProgressPolling()
            // 重新加载列表以更新状态
            this.getList()
          }

          this.progressInfo.generatedCount = progressData.generatedCount
        }
      }).catch(error => {
        console.error("获取进度失败:", error)
      })
    },
    /** 计算进度百分比 */
    calculateProgress() {
      if (!this.progressInfo) return 0
      if (this.progressInfo.distributionStatus === '0') {
        return 100
      } else if (this.progressInfo.distributionStatus === '1') {
        return 0
      } else {
        // 执行中，可以根据实际处理的数据量计算百分比
        return this.progressInfo.generatedCount ? Math.min(99, this.progressInfo.generatedCount / 1000 * 100) : 0
      }
    },
    /** 获取进度状态 */
    getProgressStatus() {
      if (!this.progressInfo) return null
      if (this.progressInfo.distributionStatus === '0') {
        return 'success'
      } else if (this.progressInfo.distributionStatus === '1') {
        return 'exception'
      } else {
        return null // 执行中
      }
    }
  },
  beforeDestroy() {
    // 组件销毁前清除定时器
    this.stopProgressPolling()
  }
}
</script>

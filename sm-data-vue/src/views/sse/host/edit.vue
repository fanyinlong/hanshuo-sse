<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="规则id" prop="ruleId">
        <el-input
          v-model="queryParams.ruleId"
          placeholder="请输入规则id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行机id" prop="serverId">
        <el-input
          v-model="queryParams.serverId"
          placeholder="请输入执行机id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行机名称" prop="serverName">
        <el-input
          v-model="queryParams.serverName"
          placeholder="请输入执行机名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['sse:host:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['sse:host:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['sse:host:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExportDialog"
          v-hasPermi="['sse:host:export']"
        >分发</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serverInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="规则id" align="center" prop="ruleId" />
      <el-table-column label="执行机id" align="center" prop="serverId" />
      <el-table-column label="执行机名称" align="center" prop="serverName" />
      <el-table-column label="每个执行机配置PBU个数" align="center" prop="pbuCount" />
      <el-table-column label="规则版本" align="center" prop="ruleVersion" />
      <el-table-column label="预留" align="center" prop="reverse" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:host:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sse:host:remove']"
          >删除</el-button>
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

    <!-- 添加或修改主机信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item v-if="title === '修改主机规则信息'" label="id" prop="serverId">
          <el-input v-model="form.id" :disabled="title === '修改主机规则信息'"   />
        </el-form-item>
        <el-form-item label="执行机" prop="serverId">
          <el-select
            v-model="form.serverId"
            placeholder="请选择执行机"
            :disabled="title === '修改主机规则信息'"
            @change="handleServerChange"  style="width: 100%"
          >
            <el-option
              v-for="item in hostOptions"
              :key="item.serverId"
              :label="item.serverName + '(' + item.serverId + ')'"
              :value="item.serverId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="規則" prop="ruleId">
          <el-select
            v-model="form.ruleId"
            placeholder="请选择规则"   style="width: 100%"
          >
            <el-option
              v-for="item in ruleOptions"
              :key="item.pkId"
              :label="item.ruleName + '(' + item.pkId + ')'"
              :value="item.pkId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="PBU個數" prop="pbuCount">
          <el-input v-model="form.pbuCount" placeholder="请输入pbu个數"  />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分发规则选择对话框 -->
    <el-dialog title="选择分发规则" :visible.sync="exportDialogVisible" width="400px" append-to-body>
      <el-form ref="exportForm" :model="exportForm" :rules="exportRules" label-width="80px">
        <el-form-item label="規則ID" prop="ruleId">
          <el-select
            v-model="exportForm.ruleId"
            placeholder="请选择要分发的规则"
            style="width: 100%"
          >
            <el-option
              v-for="item in ruleOptions"
              :key="item.pkId"
              :label="item.ruleName + '(' + item.pkId + ')'"
              :value="item.pkId"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exportDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleExportConfirm">确 定分发</el-button>
      </div>
    </el-dialog>

    <!-- 分发进度对话框 -->
    <el-dialog title="分发进度" :visible.sync="progressDialogVisible" width="500px" append-to-body>
      <div v-if="progressInfo">
        <el-progress
          :percentage="calculateProgress()"
          :status="getProgressStatus()"
          :text-inside="true"
          :stroke-width="20"
        ></el-progress>
        <div style="margin-top: 20px;">
          <p>状态: {{ progressInfo.message }}</p>
          <p v-if="progressInfo.generatedCount !== undefined">

            已处理数据量: {{ progressInfo.generatedCount }}
          </p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="progressDialogVisible = false">关 闭</el-button>
        <el-button v-if="progressInfo && progressInfo.status === 'running'" type="primary" @click="refreshProgress">刷 新</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listServerInfo,
  listServerRule,
  addServerRule,
  delServerRule,
  getServerRule,
  updateServerRule,
  exportServerRule
} from "@/api/sse/serverInfo"
import { listRuleinfo } from "@/api/sse/ruleinfo"
import {addServerDistribution, getServerDistributionProgress} from "@/api/sse/serverDistribution"
import { getTotalOrderCountByRuleId } from "@/api/sse/ruleExecute"

export default {
  name: "ServerInfo",
  dicts: ['sse_server_status'],
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
      // 主机信息表格数据
      serverInfoList: [],
      // 主机选项数据
      hostOptions: [],
      // 规则选项数据
      ruleOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 分发对话框是否显示
      exportDialogVisible: false,
      // 进度对话框是否显示
      progressDialogVisible: false,
      // 进度信息
      progressInfo: null,
      // 当前分发记录ID
      currentDistributionId: null,
      // 订单总数
      totalOrderCount: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serverId: null,
        serverName: null,
        ruleId: null,
        pbuCount: null
      },
      // 表单参数
      form: {
        instanceName: null
      },
      // 分发表单参数
      exportForm: {
        ruleId: null
      },
      // 表单校验
      rules: {
        serverId: [
          { required: true, message: "执行机Id不能为空", trigger: "change" }
        ],
        ruleId: [
          { required: true, message: "规则id不能为空", trigger: "change" }
        ],
        pbuCount: [
          { required: true, message: "pbu個數不能为空", trigger: "blur" }
        ]
      },
      // 分发表单校验
      exportRules: {
        ruleId: [
          { required: true, message: "请选择要分发的规则", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getHostOptions()
    this.getRuleOptions()
  },
  methods: {
    /** 查询主机信息列表 */
    getList() {
      this.loading = true
      listServerRule(this.queryParams).then(response => {
        this.serverInfoList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 获取主机选项列表 */
    getHostOptions() {
      listServerInfo({}).then(response => {
        this.hostOptions = response.rows || []
      })
    },
    /** 获取规则选项列表 */
    getRuleOptions() {
      listRuleinfo({}).then(response => {
        this.ruleOptions = response.rows || []
      })
    },
    /** 执行机选择变化事件 */
    handleServerChange(val) {
      const selectedHost = this.hostOptions.find(item => item.serverId === val)
      if (selectedHost) {
        this.form.serverName = selectedHost.serverName
      }
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        ruleId: null,
        serverId: null,
        serverName: null,
        loginName: null,
        authCode: null,
        encryptionKey: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加主机规则信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getServerRule(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改主机规则信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateServerRule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addServerRule(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id || this.ids
      this.$modal.confirm('是否确认删除编号为"' + id + '"的数据项？').then(function() {
        return delServerRule(id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 分发按钮点击事件 - 打开选择对话框 */
    handleExportDialog() {
      // 重置分发表单
      this.exportForm.ruleId = null
      this.exportDialogVisible = true
    },
    /** 确认分发 */
    handleExportConfirm() {
      this.$refs["exportForm"].validate(valid => {
        if (valid) {
          this.$modal.confirm('是否确认分发规则ID为"' + this.exportForm.ruleId + '"的数据项？').then(() => {
            // 立即关闭对话框并提示执行中
            this.exportDialogVisible = false
            this.$modal.msgSuccess("数据分发任务已启动，正在后台执行请稍后...")

            // 创建分发记录
            const distributionRecord = {
              ruleId: this.exportForm.ruleId,
              distributionVersion: "1.0"
            }

            // 计算订单总数
            this.calculateTotalOrderCount(this.exportForm.ruleId)

            addServerDistribution(distributionRecord).then(response => {
              if (response.code === 200) {
                this.currentDistributionId = response.data.pkId
                // 显示进度对话框
                this.progressDialogVisible = true
                this.progressInfo = null
                this.refreshProgress()
              }
            })
          })
        }
      })
    },
    /** 刷新进度 */
    refreshProgress() {
      if (!this.currentDistributionId) return

      // 调用后端接口获取进度
      getServerDistributionProgress(this.currentDistributionId).then(response => {
        if (response.code === 200) {
          this.progressInfo = response.data

          // 如果还在运行中，定时刷新
          if (this.progressInfo.status === 'running') {
            setTimeout(() => {
              this.refreshProgress()
            }, 2000) // 每2秒刷新一次
          }
        }
      }).catch(error => {
        console.error("获取进度失败:", error)
      })
    },
    /** 计算订单总数 */
    async calculateTotalOrderCount(ruleId) {
      try {
        const response = await getTotalOrderCountByRuleId(ruleId)
        if (response.code === 200) {
          this.totalOrderCount = response.data || 0
        }
      } catch (error) {
        console.error("获取订单总数失败:", error)
        this.totalOrderCount = 0
      }
    },
    /** 计算进度百分比 */
    calculateProgress() {
      if (!this.progressInfo) return 0
      // 这里可以根据实际情况计算进度百分比
      // 暂时返回固定值或根据状态返回
      if (this.progressInfo.status === 'completed') {
        return 100
      } else if (this.progressInfo.status === 'failed') {
        return 0
      } else {
        // 运行中，可以根据实际处理的数据量计算百分比
        if (this.totalOrderCount > 0) {
          return Math.min(99, Math.floor((this.progressInfo.generatedCount / this.totalOrderCount) * 100))
        }
        return this.progressInfo.generatedCount ? Math.min(99, this.progressInfo.generatedCount / 1000) : 0
      }
    },
    /** 获取进度状态 */
    getProgressStatus() {
      if (!this.progressInfo) return null
      if (this.progressInfo.status === 'completed') {
        return 'success'
      } else if (this.progressInfo.status === 'failed') {
        return 'exception'
      } else {
        return null // 运行中
      }
    }
  }
}
</script>

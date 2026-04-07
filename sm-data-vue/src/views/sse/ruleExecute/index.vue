<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="规则ID" prop="ruleId">
        <el-input v-model="queryParams.ruleId" placeholder="请输入规则ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="订单数量" prop="orderNumber">
        <el-input v-model="queryParams.orderNumber" placeholder="请输入订单数量" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="数据类型" prop="orderType">
        <el-select v-model="queryParams.orderType" placeholder="请选择数据类型" clearable>
          <el-option v-for="dict in dict.type.sse_execute_data_type" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="执行状态" prop="executeStatus">
        <el-select v-model="queryParams.executeStatus" placeholder="请选择执行状态" clearable>
          <el-option v-for="dict in dict.type.sse_rule_execute_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="执行人" prop="executeUser">
        <el-input v-model="queryParams.executeUser" placeholder="请输入执行人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="执行时间" prop="executeTime">
        <el-date-picker clearable v-model="queryParams.executeTime" type="date" value-format="yyyy-MM-dd"
          placeholder="请选择执行时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="执行版本" prop="executeRuleVersion">
        <el-input v-model="queryParams.executeRuleVersion" placeholder="请输入执行版本" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['sse:ruleExecute:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['sse:ruleExecute:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ruleExecuteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="pkId" />
      <el-table-column label="规则ID" align="center" prop="ruleId" />
      <el-table-column label="订单数量" align="center" prop="orderNumber" />
      <el-table-column label="数据类型" align="center" prop="orderType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_execute_data_type" :value="scope.row.orderType" />
        </template>
      </el-table-column>
      <el-table-column label="执行状态" align="center" prop="executeStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_rule_execute_status" :value="scope.row.executeStatus" />
        </template>
      </el-table-column>
      <el-table-column label="执行人" align="center" prop="executeUser" />
      <el-table-column label="执行时间" align="center" prop="executeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.executeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="执行版本" align="center" prop="executeRuleVersion" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:ruleExecute:list']">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['sse:ruleExecute:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改规则执行记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="规则ID" prop="ruleId">
          <el-input v-model="form.ruleId" placeholder="请输入规则ID" />
        </el-form-item>
        <el-form-item label="订单数量" prop="orderNumber">
          <el-input v-model="form.orderNumber" placeholder="请输入订单数量" />
        </el-form-item>
        <el-form-item label="全量:0,增量:1" prop="orderType">
          <el-radio-group v-model="form.orderType">
            <el-radio v-for="dict in dict.type.sse_execute_data_type" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="执行状态" prop="executeStatus">
          <el-select v-model="form.executeStatus" placeholder="请选择执行状态">
            <el-option v-for="dict in dict.type.sse_rule_execute_status" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="执行人" prop="executeUser">
          <el-input v-model="form.executeUser" placeholder="请输入执行人" />
        </el-form-item>
        <el-form-item label="执行时间" prop="executeTime">
          <el-date-picker clearable v-model="form.executeTime" type="date" value-format="yyyy-MM-dd"
            placeholder="请选择执行时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="执行版本" prop="executeRuleVersion">
          <el-input v-model="form.executeRuleVersion" placeholder="请输入执行版本" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRuleExecute, getRuleExecute, delRuleExecute, addRuleExecute, updateRuleExecute } from "@/api/sse/ruleExecute"

export default {
  name: "RuleExecute",
  dicts: ['sse_execute_data_type', 'sse_rule_execute_status'],
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
      // 规则执行记录表格数据
      ruleExecuteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleId: null,
        orderNumber: null,
        orderType: null,
        executeStatus: null,
        executeUser: null,
        executeTime: null,
        executeRuleVersion: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        ruleId: [
          { required: true, message: "规则ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询规则执行记录列表 */
    getList() {
      this.loading = true
      listRuleExecute(this.queryParams).then(response => {
        this.ruleExecuteList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        pkId: null,
        ruleId: null,
        orderNumber: null,
        orderType: null,
        executeStatus: null,
        executeUser: null,
        executeTime: null,
        executeRuleVersion: null
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
      this.ids = selection.map(item => item.pkId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加规则执行记录"
    },
    /** 修改按钮操作 */
handleUpdate(row) {
      this.$router.push({
        name: 'RuleExecuteProgress',
        params: { id: row.pkId }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.pkId != null) {
            updateRuleExecute(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRuleExecute(this.form).then(response => {
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
      const pkIds = row.pkId || this.ids
      this.$modal.confirm('是否确认删除规则执行记录编号为"' + pkIds + '"的数据项？').then(function () {
        return delRuleExecute(pkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => { })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('sse/ruleExecute/export', {
        ...this.queryParams
      }, `ruleExecute_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

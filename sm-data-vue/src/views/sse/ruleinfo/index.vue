<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="规则名称" prop="ruleName">
        <el-input v-model="queryParams.ruleName" placeholder="请输入规则名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="英文名称" prop="ruleEnName">
        <el-input v-model="queryParams.ruleEnName" placeholder="请输入规则英文名称" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建者" prop="createBy">
        <el-input v-model="queryParams.createBy" placeholder="请输入创建者" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['sse:ruleinfo:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['sse:ruleinfo:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['sse:ruleinfo:remove']">删除</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ruleinfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="规则ID" align="center" prop="pkId" />
      <el-table-column label="规则名称" align="center" prop="ruleName">
        <template slot-scope="scope">
          <span class="clickable-text" @click="handleDetail(scope.row)">{{ scope.row.ruleName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="机构PBU总数" align="center" prop="loginPbu" />
      <el-table-column label="业务PBU总数" align="center" prop="pbuCount" />
      <el-table-column label="成交比" align="center" prop="tradeRatio" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新者" align="center" prop="updateBy" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:ruleinfo:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-document-copy" @click="handleCopy(scope.row)"
            v-hasPermi="['sse:ruleinfo:edit']">复制</el-button>
          <el-button size="mini" type="text" icon="el-icon-refresh-right" @click="handleInit(scope.row)"
            v-hasPermi="['sse:ruleinfo:edit']">初始化</el-button>
          <el-button size="mini" type="text"  :loading="checkingStatus[scope.row.pkId]" :disabled="!initedStatus[scope.row.pkId]" icon="el-icon-caret-right" @click="handleExecute(scope.row)"
            v-hasPermi="['sse:ruleinfo:edit']">执行规则</el-button>
              <!-- 无权限提示（可选） -->
    <!-- <el-tooltip
      v-if="!isChecking && !isInited"
      content="规则未初始化，无法执行"
      placement="top"
    >
      <div class="permission-tip">
        <i class="el-icon-info-circle"></i>
      </div>
    </el-tooltip> -->
            <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sse:ruleinfo:remove']"
          >删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>


    <!-- 规则执行记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="executeForm" :model="executeForm" :rules="rules" label-width="80px">
        <el-form-item label="规则ID" prop="ruleId">
          <el-input v-model="executeForm.ruleId" placeholder="请输入规则ID" />
        </el-form-item>
        <el-form-item label="订单数量" prop="orderNumber">
          <el-input v-model="executeForm.orderNumber" placeholder="请输入订单数量" />
        </el-form-item>
        <el-form-item label="数据类型" prop="orderType">
          <el-radio-group v-model="executeForm.orderType">
            <el-radio v-for="dict in dict.type.sse_execute_data_type" :key="dict.value" :label="dict.value">{{
              dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="executeFormSubmit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

  </div>
</template>

<script>
import { listRuleinfo, delRuleinfo, updateRuleinfo, initRuleData } from "@/api/sse/ruleinfo"
import {  addRuleExecute } from "@/api/sse/ruleExecute"
import { listRuleInit } from "@/api/sse/ruleInit"

export default {
  name: "Ruleinfo",
  dicts: ['sse_security_sub_category', 'sse_match_method', 'sse_product_set', 'sys_del_flag', 'sse_account_type', 'sse_price_level', 'sys_normal_disable', 'sse_business_type', 'sse_trade_type', 'sse_execute_data_type'],
  data() {
    return {
      //初始化数据检查
      checkingStatus: {},
      initedStatus: {},
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
      // 规则配置表格数据
      ruleinfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleName: null,
        ruleEnName: null,
        status: null,
        createBy: null,
      },
      // 表单参数
      form: {},

      executeForm: {
        ruleId: null,
        orderNumber: null,
        orderType: null
      },
      // 表单校验
      rules: {
        ruleName: [
          { required: true, message: "规则名称不能为空", trigger: "blur" }
        ],
      },

    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询规则配置列表 */
    getList() {
      this.loading = true
      listRuleinfo(this.queryParams).then(response => {
        this.ruleinfoList = response.rows || [];
        this.total = response.total || 0;
        this.loading = false;
        // 列表加载完成后，查询每个规则的初始化状态
        this.loadInitStatus();
      }).catch(() => {
        this.loading = false;
      });
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
        ruleName: null,
        ruleEnName: null,
        orderTotal: null,
        tradeType: null,
        businessType: null,
        securitySubCategory: null,
        productSet: null,
        matchMethod: null,
        accountType: null,
        priceLevel: null,
        orderQuantity: null,
        tradeRatio: null,
        pbuCount: null,
        bidHost: null,
        loginPbu: null,
        businessPbu: null,
        performanceId: null,
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        ruleId: null,
        orderNumber: null,
        orderType: null,
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
      this.$router.push('ruleadd')
    },

    /** 展示操作 */
    handleDetail(row) {
      this.reset()
      const pkId = row.pkId || this.ids
      this.$router.push('/sse/ruleinfo/edit/' + pkId)
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pkId = row.pkId || this.ids
      this.$router.push('/sse/ruleinfo/edit/' + pkId)
    },

    /** 复制按钮操作 */
    handleCopy(row) {
      this.reset()
      const pkId = row.pkId || this.ids
      // Navigate to edit page with a query parameter indicating it's a copy operation
      this.$router.push({
        path: '/sse/ruleinfo/edit/' + pkId,
        query: {
          isCopy: true
        }
      })
    },

    /** 初始化按钮操作 */
    handleInit(row) {
      this.reset()
      const pkId = row.pkId || this.ids
      this.$modal.confirm('确定要初始化数据吗？此操作将会重新计算基础数据。').then(function () {
        return initRuleData(pkId);
      }).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess("初始化成功！")
        }
        this.getList();
      });

    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.pkId != null) {
            updateRuleinfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },


    /** 加载所有规则的初始化状态（关键优化） */
    async loadInitStatus() {
      // 1. 初始化状态对象（确保响应式）
      this.ruleinfoList.forEach(rule => {
        this.$set(this.checkingStatus, rule.pkId, true); // 标记为加载中
        this.$set(this.initedStatus, rule.pkId, false);  // 默认未初始化
      });

      try {
        // 2. 逐个查询每个规则的初始化状态
        for (const rule of this.ruleinfoList) {
          const response = await listRuleInit({ ruleId: rule.pkId });
          // 根据接口返回判断是否初始化（假设data是数组，有数据则认为已初始化）
          const isInited = response.code === 200 && Array.isArray(response.rows) && response.rows.length > 0;

          this.$set(this.initedStatus, rule.pkId, isInited);
        }
      } catch (error) {
        console.error('查询初始化状态失败', error);
        this.$message.error('部分规则初始化状态获取失败');
      } finally {
        // 3. 结束所有加载状态
        this.ruleinfoList.forEach(rule => {
          this.$set(this.checkingStatus, rule.pkId, false);
        });
      }
    },
    handleExecute(row) {
      this.resetForm("executeForm")
      this.executeForm.ruleId = row.pkId
      this.open = true
      this.title = "修改规则执行记录"
    },

    /** 执行规则表单提交 */
    executeFormSubmit() {
      this.$refs["executeForm"].validate(valid => {
        if (valid) {
          addRuleExecute(this.executeForm).then(response => {
            this.$modal.msgSuccess("正在执行中，请稍后！")
            this.open = false
            this.$router.push('/orderexe/ruleExecute')
              
          })
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const pkIds = row.pkId || this.ids
      this.$modal.confirm('是否确认删除规则配置编号为"' + pkIds + '"的数据项？').then(function () {
        return delRuleinfo(pkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => { })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('sse/ruleinfo/export', {
        ...this.queryParams
      }, `ruleinfo_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style>
/* 可点击文本样式 */
.clickable-text {
  color: #409eff;
  /* Element UI 主题色 */
  cursor: pointer;
  text-decoration: underline;
  transition: color 0.2s;
}

.clickable-text:hover {
  color: #66b1ff;
}
</style>

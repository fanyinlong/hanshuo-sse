<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="PBU编号" prop="pbuId">
        <el-input
          v-model="queryParams.pbuId"
          placeholder="请输入PBU编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="使用状态" prop="isUsed">
        <el-select v-model="queryParams.isUsed" placeholder="请选择使用状态" clearable>
          <el-option
            v-for="dict in dict.type.sse_usage_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
            <el-form-item label="是否登陆pbu" prop="isLoginpbu">
        <el-select v-model="queryParams.isLoginpbu" placeholder="请选择是否登陆pbu" clearable>
          <el-option
            v-for="dict in dict.type.boolean_flag"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否业务pbu" prop="isBizpbu">
        <el-select v-model="queryParams.isBizpbu" placeholder="请选择是否业务pbu" clearable>
          <el-option
            v-for="dict in dict.type.boolean_flag"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属登陆pbu" prop="belongtoLoginpbu">
        <el-input
          v-model="queryParams.belongtoLoginpbu"
          placeholder="请输入业务pbu所属登陆pbu，非业务pbu该项为空"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="对应规则Id" prop="ruleId">
        <el-input
          v-model="queryParams.ruleId"
          placeholder="请输入对应规则Id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="对应规则名称" prop="ruleName">
        <el-input
          v-model="queryParams.ruleName"
          placeholder="请输入对应规则名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="包含账户数量" prop="accountNumber">
        <el-input
          v-model="queryParams.accountNumber"
          placeholder="请输入包含账户数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="规则使用账户数量" prop="accountNumberused">
        <el-input
          v-model="queryParams.accountNumberused"
          placeholder="请输入规则使用账户数量"
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
          v-hasPermi="['sse:pbuConfig:add']"
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
          v-hasPermi="['sse:pbuConfig:edit']"
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
          v-hasPermi="['sse:pbuConfig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-import" size="mini" @click="handleImport"
          v-hasPermi="['sse:pbuInfo:add']">初始化</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pbuConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="PBU编号" align="center" prop="pbuId" />
      <el-table-column label="使用状态" align="center" prop="isUsed">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_usage_status" :value="scope.row.isUsed"/>
        </template>
      </el-table-column>
      <el-table-column label="是否登陆pbu" align="center" prop="isLoginpbu">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.boolean_flag" :value="scope.row.isLoginpbu"/>
        </template>
      </el-table-column>
      <el-table-column label="是否业务pbu" align="center" prop="isBizpbu">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.boolean_flag" :value="scope.row.isBizpbu"/>
        </template>
      </el-table-column>
      <el-table-column label="业务pbu所属登陆pbu" align="center" prop="belongtoLoginpbu" />
      <el-table-column label="对应规则Id" align="center" prop="ruleId" />
      <el-table-column label="对应规则名称" align="center" prop="ruleName" />
      <el-table-column label="包含账户数量" align="center" prop="accountNumber" />
      <el-table-column label="规则使用账户数量" align="center" prop="accountNumberused" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:pbuConfig:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sse:pbuConfig:remove']"
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

    <!-- 添加或修改PBU配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="pbu编号" prop="pbuId">
        <el-input v-model="form.pbuId" placeholder="请输入pbu编号" />
        </el-form-item>
        <el-form-item label="使用状态" prop="isUsed">
          <el-radio-group v-model="form.isUsed">
            <el-radio
              v-for="dict in dict.type.sse_usage_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否登陆pbu" prop="isLoginpbu">
          <el-radio-group v-model="form.isLoginpbu">
            <el-radio
              v-for="dict in dict.type.boolean_flag"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否业务pbu" prop="isBizpbu">
          <el-radio-group v-model="form.isBizpbu">
            <el-radio
              v-for="dict in dict.type.boolean_flag"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="业务pbu所属登陆pbu" prop="belongtoLoginpbu">
          <el-input v-model="form.belongtoLoginpbu" placeholder="请输入业务pbu所属登陆pbu，非业务pbu该项为空" />
        </el-form-item>
        <el-form-item label="对应规则Id" prop="ruleId">
          <el-input v-model="form.ruleId" placeholder="请输入对应规则Id" />
        </el-form-item>
        <el-form-item label="对应规则名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入对应规则名称" />
        </el-form-item>
        <el-form-item label="包含账户数量" prop="accountNumber">
          <el-input v-model="form.accountNumber" placeholder="请输入包含账户数量" />
        </el-form-item>
        <el-form-item label="规则使用账户数量" prop="accountNumberused">
          <el-input v-model="form.accountNumberused" placeholder="请输入规则使用账户数量" />
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
import { listPbuConfig, getPbuConfig, delPbuConfig, addPbuConfig, updatePbuConfig, init } from "@/api/sse/pbuConfig"

export default {
  name: "PbuConfig",
  dicts: ['boolean_flag', 'sse_usage_status'],
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
      // PBU配置表格数据
      pbuConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        pbuId: null,
        isUsed: null,
        isLoginpbu: null,
        isBizpbu: null,
        belongtoLoginpbu: null,
        ruleId: null,
        ruleName: null,
        accountNumber: null,
        accountNumberused: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询PBU配置列表 */
    getList() {
      this.loading = true
      listPbuConfig(this.queryParams).then(response => {
        this.pbuConfigList = response.rows
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
        pbuId: null,
        isUsed: null,
        isLoginpbu: null,
        isBizpbu: null,
        belongtoLoginpbu: null,
        ruleId: null,
        ruleName: null,
        accountNumber: null,
        accountNumberused: null
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加PBU配置"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pkId = row.pkId || this.ids
      getPbuConfig(pkId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改PBU配置"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.pkId != null) {
            updatePbuConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPbuConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除PBU配置编号为"' + pkIds + '"的数据项？').then(function() {
        return delPbuConfig(pkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 初始化操作 */
    handleImport() {
      this.$modal.confirm('确定要初始化数据吗？此操作将会清除现有数据并导入默认数据。').then(function () {
        return init();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("初始化成功");
      }).catch(() => {
        this.$modal.msgInfo("已取消初始化");
      });
    }
  }
}
</script>

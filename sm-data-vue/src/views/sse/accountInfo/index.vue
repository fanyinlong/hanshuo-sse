<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="投资者账户" prop="accountId">
        <el-input
          v-model="queryParams.accountId"
          placeholder="请输入投资者账户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="PBU编号" prop="pbuId">
        <el-input
          v-model="queryParams.pbuId"
          placeholder="请输入PBU编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账户类型" prop="accountType">
        <el-select v-model="queryParams.accountType" placeholder="请选择账户类型" clearable>
          <el-option
            v-for="dict in dict.type.sse_account_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="指定交易类型" prop="tradeType">
        <el-input
          v-model="queryParams.tradeType"
          placeholder="请输入指定交易类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账户能否撤销标识" prop="cancelFlag">
        <el-input
          v-model="queryParams.cancelFlag"
          placeholder="请输入账户能否撤销标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账户状态标识" prop="statusFlag">
        <el-input
          v-model="queryParams.statusFlag"
          placeholder="请输入账户状态标识"
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
          v-hasPermi="['sse:accountInfo:add']"
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
          v-hasPermi="['sse:accountInfo:edit']"
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
          v-hasPermi="['sse:accountInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-import" size="mini" @click="handleImport"
          v-hasPermi="['sse:pbuInfo:add']">初始化</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="accountInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="投资者账户" align="center" prop="accountId" />
      <el-table-column label="子账户" align="center" prop="accountSubId" />
      <el-table-column label="PBU编号" align="center" prop="pbuId" />
      <el-table-column label="账户类型" align="center" prop="accountType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_account_type" :value="scope.row.accountType"/>
        </template>
      </el-table-column>
      <el-table-column label="指定交易类型" align="center" prop="tradeType" />
      <el-table-column label="账户能否撤销标识" align="center" prop="cancelFlag" />
      <el-table-column label="账户状态标识" align="center" prop="statusFlag" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:accountInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sse:accountInfo:remove']"
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

    <!-- 添加或修改账户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账户号" prop="accountId">
          <el-input v-model="form.accountId" placeholder="请输入账户号" />
        </el-form-item>
        <el-form-item label="子账户" prop="accountSubId">
          <el-input v-model="form.accountSubId" placeholder="请输入子账户" />
        </el-form-item>
        <el-form-item label="PBU编号" prop="pbuId">
          <el-input v-model="form.pbuId" placeholder="请输入PBU编号" />
        </el-form-item>
        <el-form-item label="账户类型" prop="accountType">
          <el-select v-model="form.accountType" placeholder="请选择账户类型">
            <el-option
              v-for="dict in dict.type.sse_account_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="指定交易类型" prop="tradeType">
          <el-input v-model="form.tradeType" placeholder="请输入指定交易类型" />
        </el-form-item>
        <el-form-item label="账户能否撤销标识" prop="cancelFlag">
          <el-input v-model="form.cancelFlag" placeholder="请输入账户能否撤销标识" />
        </el-form-item>
        <el-form-item label="账户状态标识" prop="statusFlag">
          <el-input v-model="form.statusFlag" placeholder="请输入账户状态标识" />
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
import { listAccountInfo, getAccountInfo, delAccountInfo, addAccountInfo, updateAccountInfo, init } from "@/api/sse/accountInfo"

export default {
  name: "AccountInfo",
  dicts: ['sse_account_type'],
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
      // 账户信息表格数据
      accountInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountId: null,
        pbuId: null,
        accountType: null,
        tradeType: null,
        cancelFlag: null,
        statusFlag: null
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
    /** 查询账户信息列表 */
    getList() {
      this.loading = true
      listAccountInfo(this.queryParams).then(response => {
        this.accountInfoList = response.rows
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
        accountId: null,
        accountSubId: null,
        pbuId: null,
        accountType: null,
        tradeType: null,
        cancelFlag: null,
        statusFlag: null
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
      this.title = "添加账户信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pkId = row.pkId || this.ids
      getAccountInfo(pkId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改账户信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.pkId != null) {
            updateAccountInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addAccountInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除账户信息编号为"' + pkIds + '"的数据项？').then(function() {
        return delAccountInfo(pkIds)
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

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="PBU编号" prop="pbuId">
        <el-input v-model="queryParams.pbuId" placeholder="请输入PBU编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所属市场" prop="mktId">
        <el-select v-model="queryParams.mktId" placeholder="请选择所属市场" clearable>
          <el-option v-for="dict in dict.type.sse_pub_mktid" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="控制类型" prop="pbuType">
        <el-select v-model="queryParams.pbuType" placeholder="请选择控制类型" clearable>
          <el-option v-for="dict in dict.type.sse_pbu_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="PBU状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择PBU状态" clearable>
          <el-option v-for="dict in dict.type.sse_pbu_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="机构代码" prop="instituteId">
        <el-input v-model="queryParams.instituteId" placeholder="请输入机构代码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>

      <el-form-item label="机构身份代码" prop="membCert">
        <el-input v-model="queryParams.membCert" placeholder="请输入机构身份代码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['sse:pbuInfo:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['sse:pbuInfo:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['sse:pbuInfo:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-import" size="mini" @click="handleImport"
          v-hasPermi="['sse:pbuInfo:add']">初始化</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pbuInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="PBU编号" align="center" prop="pbuId" />
      <el-table-column label="所属市场" align="center" prop="mktId">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_pub_mktid" :value="scope.row.mktId" />
        </template>
      </el-table-column>
      <el-table-column label="控制类型" align="center" prop="pbuType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_pbu_type" :value="scope.row.pbuType" />
        </template>
      </el-table-column>
      <el-table-column label="PBU状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_pbu_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="保证金买入" align="center" prop="buyonMarginind">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_pbu_permission" :value="scope.row.buyonMarginind" />
        </template>
      </el-table-column>
      <el-table-column label="融券卖出" align="center" prop="sellonBorrowind">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_pbu_permission" :value="scope.row.sellonBorrowind" />
        </template>
      </el-table-column>
      <el-table-column label="机构代码" align="center" prop="instituteId" />
      <el-table-column label="参考分区编号" align="center" prop="partitionId" />
      <el-table-column label="流速权" align="center" prop="flowctrlValue" />
      <el-table-column label="联通圈主PBU" align="center" prop="headPbuid" />
      <el-table-column label="结算会员" align="center" prop="clearMember" />
      <el-table-column label="机构身份代码" align="center" prop="membCert" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:pbuInfo:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['sse:pbuInfo:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改PBU信息对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" size="small" label-width="100px">
        <el-form-item label="pbu编号" prop="pbuId">
        <el-input v-model="form.pbuId" placeholder="请输入pbu编号" />
        </el-form-item>
        <el-form-item label="所属市场" prop="mktId">
          <el-radio-group v-model="form.mktId">
            <el-radio v-for="dict in dict.type.sse_pub_mktid" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="控制类型" prop="pbuType">
          <el-radio-group v-model="form.pbuType">
            <el-radio v-for="dict in dict.type.sse_pbu_type" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="PBU状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.sse_pbu_status" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="保证金买入" prop="buyonMarginind">
          <el-radio-group v-model="form.buyonMarginind">
            <el-radio v-for="dict in dict.type.sse_pbu_permission" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="融券卖出" prop="sellonBorrowind">
          <el-radio-group v-model="form.sellonBorrowind">
            <el-radio v-for="dict in dict.type.sse_pbu_permission" :key="dict.value"
              :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="密码密文" prop="loginPwd">
          <el-input v-model="form.loginPwd" placeholder="请输入密码密文" />
        </el-form-item>
        <el-form-item label="机构代码" prop="instituteId">
          <el-input v-model="form.instituteId" placeholder="请输入机构代码" />
        </el-form-item>
        <el-form-item label="参考分区编号" prop="partitionId">
          <el-input v-model="form.partitionId" placeholder="请输入参考分区编号" />
        </el-form-item>
        <el-form-item label="流速权" prop="flowctrlValue">
          <el-input v-model="form.flowctrlValue" placeholder="请输入流速权" />
        </el-form-item>
        <el-form-item label="联通圈主PBU" prop="headPbuid">
          <el-input v-model="form.headPbuid" placeholder="请输入联通圈主PBU" />
        </el-form-item>
        <el-form-item label="结算会员" prop="clearMember">
          <el-input v-model="form.clearMember" placeholder="请输入结算会员" />
        </el-form-item>
        <el-form-item label="机构身份代码" prop="membCert">
          <el-input v-model="form.membCert" placeholder="请输入机构身份代码" />
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
import { listPbuInfo, getPbuInfo, delPbuInfo, addPbuInfo, updatePbuInfo, init } from "@/api/sse/pbuInfo"

export default {
  name: "PbuInfo",
  dicts: ['sse_pbu_type', 'sse_pub_mktid', 'sse_pbu_permission', 'sse_pbu_status'],
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
      // PBU信息表格数据
      pbuInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        pbuId: null,
        mktId: null,
        pbuType: null,
        status: null,
        buyonMarginind: null,
        sellonBorrowind: null,
        loginPwd: null,
        instituteId: null,
        partitionId: null,
        flowctrlValue: null,
        headPbuid: null,
        clearMember: null,
        membCert: null
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
    /** 查询PBU信息列表 */
    getList() {
      this.loading = true
      listPbuInfo(this.queryParams).then(response => {
        this.pbuInfoList = response.rows
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
        pbuId: null,
        mktId: null,
        pbuType: null,
        status: null,
        buyonMarginind: null,
        sellonBorrowind: null,
        loginPwd: null,
        instituteId: null,
        partitionId: null,
        flowctrlValue: null,
        headPbuid: null,
        clearMember: null,
        membCert: null
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
      this.ids = selection.map(item => item.pbuId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加PBU信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pbuId = row.pbuId || this.ids
      getPbuInfo(pbuId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改PBU信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("this.form",this.form.pkId)
          if (this.form.pkId != null) {
            updatePbuInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPbuInfo(this.form).then(response => {
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
      const pbuIds = row.pbuId || this.ids
      this.$modal.confirm('是否确认删除PBU信息编号为"' + pbuIds + '"的数据项？').then(function () {
        return delPbuInfo(pbuIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => { })
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

<style scoped>
el-form-item__label {
  width: 70px;
}
</style>
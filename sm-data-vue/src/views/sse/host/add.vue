<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="主机编号" prop="serverId">
        <el-input
          v-model="queryParams.serverId"
          placeholder="请输入主机编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主机名" prop="serverName">
        <el-input
          v-model="queryParams.serverName"
          placeholder="请输入主机名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否有效" prop="isAvaliable">
        <el-select v-model="queryParams.isAvaliable" placeholder="请选择是否有效" clearable>
          <el-option
            v-for="dict in dict.type.sse_server_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="测试系统" prop="platformId">
        <el-input
          v-model="queryParams.platformId"
          placeholder="请输入测试系统"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="测试环境号" prop="envId">
        <el-input
          v-model="queryParams.envId"
          placeholder="请输入测试环境号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="IP地址" prop="ipAddress">
        <el-input
          v-model="queryParams.ipAddress"
          placeholder="请输入IP地址"
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


      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="serverInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主机编号" align="center" prop="serverId" />
      <el-table-column label="主机名" align="center" prop="serverName" />
      <el-table-column label="是否有效:" align="center" prop="isAvaliable">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_server_status" :value="scope.row.isAvaliable"/>
        </template>
      </el-table-column>
      <el-table-column label="测试系统" align="center" prop="platformId" />
      <el-table-column label="测试环境号" align="center" prop="envId" />
      <el-table-column label="IP地址" align="center" prop="ipAddress" />
      <el-table-column label="登陆用户名" align="center" prop="loginName" />
      <el-table-column label="密码" align="center" prop="authCode" />
      <el-table-column label="端口" align="center" prop="port" />
      <el-table-column label="数据库实例名称" align="center" prop="instanceName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">

        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-add"
            @click="handleAdd(scope.row)"
            v-hasPermi="['sse:serverInfo:edit']"
          >新增</el-button>

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
        <el-form-item label="主机编号" prop="serverId">
          <el-input v-model="form.serverId" placeholder="请输入主机编号" :disabled="title === '修改主机信息'" />
        </el-form-item>
        <el-form-item label="主机名" prop="serverName">
          <el-input v-model="form.serverName" placeholder="请输入主机名" :disabled="title === '修改主机信息'" />
        </el-form-item>
        <el-form-item label="規則" prop="ruleId">
          <el-select
            v-model="form.ruleId"
            placeholder="请选择规则"
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
          <el-input v-model="form.pbuCount" placeholder="請輸入pbu個數"  />
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
import { listServerInfo, getServerInfo, addServerRule } from "@/api/sse/serverInfo"
import { listRuleinfo } from "@/api/sse/ruleinfo"
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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serverId: null,
        serverName: null,
        isAvaliable: null,
        platformId: null,
        envId: null,
        ipAddress: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        serverId: [
          { required: true, message: "主机Id不能为空", trigger: "blur" }
        ],
        serverName: [
          { required: true, message: "主机名不能为空", trigger: "blur" }
        ],
        ruleId: [
          { required: true, message: "ruleid不能为空", trigger: "blur" }
        ],
        pbuCount: [
          { required: true, message: "pbu個數不能为空", trigger: "blur" }
        ]

      }
    }
  },
  created() {
    this.getList()
    this.getRuleOptions()
  },
  methods: {
    /** 查询主机信息列表 */
    getList() {
      this.loading = true
      listServerInfo(this.queryParams).then(response => {
        this.serverInfoList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 获取规则选项列表 */
    getRuleOptions() {
      listRuleinfo({}).then(response => {
        this.ruleOptions = response.rows || []
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
        serverId: null,
        serverName: null,
        isAvaliable: null,
        platformId: null,
        envId: null,
        ipAddress: null,
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
      this.ids = selection.map(item => item.serverId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    /** 修改按钮操作 */
    handleAdd(row) {
      this.reset()
      const serverId = row.serverId || this.ids
      getServerInfo(serverId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改主机信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addServerRule(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
        }
      })
    }

  }
}
</script>

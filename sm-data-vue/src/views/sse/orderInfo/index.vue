<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :rules="searchRules" size="small" :inline="true" v-show="showSearch"
      label-width="120px">


      <el-form-item label="规则ID" prop="ruleId">
        <el-input v-model="queryParams.ruleId" placeholder="请输入规则ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="登陆PBU对应表" prop="orderTable">
        <el-input v-model="queryParams.orderTable" placeholder="请输入登陆PBU对应表" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>

      <el-form-item label="业务PBU编号" prop="bizPbu">
        <el-input v-model="queryParams.bizPbu" placeholder="请输入业务PBU编号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>

      <el-form-item label="买卖方向" prop="side">
        <el-select v-model="queryParams.side" placeholder="请选择买卖方向" clearable>
          <el-option v-for="dict in dict.type.buy_sell" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="申报数量" prop="orderQty">
        <el-input v-model="queryParams.orderQty" placeholder="请输入申报数量" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>

      <el-form-item label="订单类型" prop="ordType">
        <el-select v-model="queryParams.ordType" placeholder="请选择订单类型" clearable>
          <el-option v-for="dict in dict.type.order_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单所属set" prop="orderSet">
        <el-select v-model="queryParams.orderSet" placeholder="请选择订单所属set" clearable>
          <el-option v-for="dict in dict.type.sse_product_set" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单撮合方式" prop="orderMatching">
        <el-select v-model="queryParams.orderMatching" placeholder="请选择订单撮合方式" clearable>
          <el-option v-for="dict in dict.type.sse_match_method" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="下发主机id" prop="orderServerId">
        <el-input v-model="queryParams.orderServerId" placeholder="请输入订单下发主机id" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['sse:orderInfo:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['sse:orderInfo:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['sse:orderInfo:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['sse:orderInfo:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleSqlExport"
          v-hasPermi="['sse:orderInfo:export']" :loading="exportLoading">导出sql文件</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="false" :data="orderInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会员内部订单编号" align="center" prop="clOrdId" />
      <el-table-column label="业务PBU编号" align="center" prop="bizPbu" />
      <el-table-column label="证券代码" align="center" prop="securityId" />
      <el-table-column label="证券账户" align="center" prop="account" />
      <el-table-column label="买卖方向" align="center" prop="side">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.buy_sell" :value="scope.row.side" />
        </template>
      </el-table-column> <el-table-column label="申报价格" align="center" prop="price" />
      <el-table-column label="申报数量" align="center" prop="orderQty" />
      <el-table-column label="订单类型" align="center" prop="ordType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_type" :value="scope.row.ordType" />
        </template>
      </el-table-column>
      <el-table-column label="订单所属set" align="center" prop="orderSet">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_product_set" :value="scope.row.orderSet" />
        </template>
      </el-table-column>
      <el-table-column label="订单撮合方式" align="center" prop="orderMatching">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_match_method" :value="scope.row.orderMatching" />
        </template>
      </el-table-column>
      <el-table-column label="原始会员内部订单编号(待撤原订单的ClOrdID)" align="center" prop="origClOrdId" />
      <el-table-column label="订单下发表" align="center" prop="orderTable" />
      <el-table-column label="订单下发主机id" align="center" prop="orderServerId" />
      <el-table-column label="规则ID" align="center" prop="ruleId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:orderInfo:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['sse:orderInfo:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改订单汇总对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="消息头" prop="msgHead">
          <el-input v-model="form.msgHead" placeholder="请输入消息头" />
        </el-form-item>
        <el-form-item label="业务PBU编号，前5位有效" prop="bizPbu">
          <el-input v-model="form.bizPbu" placeholder="请输入业务PBU编号，前5位有效" />
        </el-form-item>
        <el-form-item label="会员内部订单编号" prop="clOrdId">
          <el-input v-model="form.clOrdId" placeholder="请输入会员内部订单编号" />
        </el-form-item>
        <el-form-item label="证券代码，前6位有效" prop="securityId">
          <el-input v-model="form.securityId" placeholder="请输入证券代码，前6位有效" />
        </el-form-item>
        <el-form-item label="证券账户，前10位有效" prop="account">
          <el-input v-model="form.account" placeholder="请输入证券账户，前10位有效" />
        </el-form-item>
        <el-form-item label="买卖方向:1=买,2=卖" prop="side">
          <el-input v-model="form.side" placeholder="请输入买卖方向:1=买,2=卖" />
        </el-form-item>
        <el-form-item label="申报价格" prop="price">
          <el-input v-model="form.price" placeholder="请输入申报价格" />
        </el-form-item>
        <el-form-item label="申报数量" prop="orderQty">
          <el-input v-model="form.orderQty" placeholder="请输入申报数量" />
        </el-form-item>
        <el-form-item label="订单类型" prop="ordType">
          <el-select v-model="form.ordType" placeholder="请选择订单类型:1=市转撤,2=限价,3=市转限,4=本方最优,5=对手方最优">
            <el-option v-for="dict in dict.type.order_type" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="订单有效时间类型" prop="timeInForce">
          <el-select v-model="form.timeInForce" placeholder="请选择订单有效时间类型:0=当日有效">
            <el-option v-for="dict in dict.type.time_in_force" :key="dict.value" :label="dict.label"
              :value="dict.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="申报时间" prop="transactTime">
          <el-date-picker clearable v-model="form.transactTime" type="date" value-format="yyyy-MM-dd"
            placeholder="请选择申报时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="信用标签" prop="creditTag">
          <el-input v-model="form.creditTag" placeholder="请输入信用标签" />
        </el-form-item>
        <el-form-item label="结算会员代码" prop="clearingFirm">
          <el-input v-model="form.clearingFirm" placeholder="请输入结算会员代码" />
        </el-form-item>
        <el-form-item label="营业部代码" prop="branchId">
          <el-input v-model="form.branchId" placeholder="请输入营业部代码" />
        </el-form-item>
        <el-form-item label="原始会员内部订单编号(待撤原订单的ClOrdID)" prop="origClOrdId">
          <el-input v-model="form.origClOrdId" placeholder="请输入原始会员内部订单编号(待撤原订单的ClOrdID)" />
        </el-form-item>
        <el-form-item label="用户私有信息" prop="userInfo">
          <el-input v-model="form.userInfo" placeholder="请输入用户私有信息" />
        </el-form-item>
        <el-form-item label="各业务扩展字段" prop="extendFields">
          <el-input v-model="form.extendFields" placeholder="请输入各业务扩展字段" />
        </el-form-item>
        <el-form-item label="订单所属set" prop="orderSet">
          <el-input v-model="form.orderSet" placeholder="请输入订单所属set" />
        </el-form-item>
        <el-form-item label="订单撮合方式" prop="orderMatching">
          <el-input v-model="form.orderMatching" placeholder="请输入订单撮合方式" />
        </el-form-item>
        <el-form-item label="订单下发表" prop="orderTable">
          <el-input v-model="form.orderTable" placeholder="请输入订单下发表" />
        </el-form-item>
        <el-form-item label="订单下发主机id" prop="orderServerId">
          <el-input v-model="form.orderServerId" placeholder="请输入订单下发主机id" />
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
import { listOrderInfo, getOrderInfo, delOrderInfo, addOrderInfo, updateOrderInfo, exportSqlFile } from "@/api/sse/orderInfo"

export default {
  name: "OrderInfo",
  dicts: ['time_in_force', 'sse_match_method', 'order_type', 'buy_sell', 'sse_product_set'],
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
      // 订单汇总表格数据
      orderInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bizPbu: null,
        clOrdId: null,
        securityId: null,
        account: null,
        side: null,
        price: null,
        orderQty: null,
        ordType: null,
        timeInForce: null,
        origClOrdId: null,
        orderSet: null,
        orderMatching: null,
        orderTable: null,
        orderServerId: null,
        ruleId: null
      },
      searchRules: {
        ruleId: [
          { required: true, message: 'ruleId不能为空', trigger: 'blur' }
        ]
      },
      // 表单参数
      form: {},
      // 导出加载状态
      exportLoading: false,
      // 表单校验
      rules: {
        bizId: [
          { required: true, message: "业务编号不能为空", trigger: "blur" }
        ],
        securityId: [
          { required: true, message: "证券代码，前6位有效不能为空", trigger: "blur" }
        ],
        account: [
          { required: true, message: "证券账户，前10位有效不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "申报价格不能为空", trigger: "blur" }
        ],
        orderQty: [
          { required: true, message: "申报数量不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    // this.getList()
  },
  methods: {
    /** 查询订单汇总列表 */
    getList() {
      this.loading = true
      listOrderInfo(this.queryParams).then(response => {
        this.orderInfoList = response.rows
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
        msgHead: null,
        bizId: null,
        bizPbu: null,
        clOrdId: null,
        securityId: null,
        account: null,
        ownerType: null,
        side: null,
        price: null,
        orderQty: null,
        ordType: null,
        timeInForce: null,
        transactTime: null,
        creditTag: null,
        clearingFirm: null,
        branchId: null,
        origClOrdId: null,
        userInfo: null,
        extendFields: null,
        orderSet: null,
        orderMatching: null,
        orderTable: null,
        orderServerId: null,
        ruleId: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.$refs.queryForm.validate((valid) => {
        if (valid) {
          // 验证通过，执行搜索
          this.queryParams.pageNum = 1
          this.getList()
        } else {
          // 验证失败，提示用户
          this.$message.warning('请填写ruleId');
          return false;
        }
      });
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
      this.title = "添加订单汇总"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pkId = row.pkId || this.ids
      getOrderInfo(pkId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改订单汇总"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.pkId != null) {
            updateOrderInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addOrderInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除订单汇总编号为"' + pkIds + '"的数据项？').then(function () {
        return delOrderInfo(pkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => { })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('sse/orderInfo/export', {
        ...this.queryParams
      }, `orderInfo_${new Date().getTime()}.xlsx`)
    },

    /** 导出sql文件操作 */
    handleSqlExport() {
      if (!this.queryParams.ruleId) {
        this.$message.warning('请选择规则ID');
        return;
      }
     this.exportLoading = true;
      exportSqlFile(this.queryParams.ruleId).then(response => {
        // console.log('导出SQL文件响应:', response.data);
        const base64Data = response.data;
       if (!base64Data) {
          this.$message.error('未获取到导出数据');
          return;
        }

        // 2. Base64转ArrayBuffer
        const binaryString = window.atob(base64Data);
        const len = binaryString.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
          bytes[i] = binaryString.charCodeAt(i);
        }
        // 创建Blob对象
        const blob = new Blob([bytes], {
          type: 'application/zip'
        });
        // 创建下载链接
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `order_table_sql_${this.queryParams.ruleId}_${new Date().getTime()}.zip`;
        document.body.appendChild(link);
        link.click();

        // 清理资源
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);

        this.$message.success('SQL文件导出成功');
      }).catch(error => {
        console.error('导出SQL失败:', error);
        this.$message.error('导出SQL失败，请重试');
      })
        .finally(() => {
          this.exportLoading = false;
        });
    },
  }
}
</script>

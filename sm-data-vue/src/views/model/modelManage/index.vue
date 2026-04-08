<template>
  <div class="app-container model-manage">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="100px"
    >
      <el-form-item label="模型名称" prop="modelName">
        <el-input
          v-model="queryParams.modelName"
          placeholder="请输入模型名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间范围" prop="timeRang">
        <el-date-picker
          v-model="queryParams.timeRang"
          type="daterange"
          range-separator="至"
          start-placeholder="创建时间起"
          end-placeholder="创建时间止"
        >
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >查询</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
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
          v-hasPermi="['sse:serverInfo:add']"
          >新增</el-button
        >
      </el-col>

      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="modelInfoList"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column type="index" width="50" label="序号" />
      <el-table-column label="模型名称" align="center" prop="modelName" />
      <el-table-column label="模型说明" align="center" prop="description" />
      <el-table-column label="参数数量" align="center" prop="modelParams" />
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="200"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['sse:serverInfo:edit']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:serverInfo:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sse:serverInfo:remove']"
            >删除</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-setting"
            @click="handleDisabled(scope.row)"
            v-hasPermi="['sse:serverInfo:disabled']"
            >{{ scope.row.status ? "禁用" : "启用" }}</el-button
          >
          <!-- <el-tooltip :content="禁用" placement="top">
            <el-switch`
              v-model="value"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleDisabled(scope.row)"
              v-hasPermi="['sse:serverInfo:disabled']">
            </el-switch>`
          </el-tooltip> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改模型信息对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="800px"
      append-to-body
      class="model-info"
    >
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-form-item label="模型名称" prop="modelName">
            <el-input v-model="form.modelName" placeholder="请输入模型名称" :disabled="isEdit"/>
          </el-form-item>
          <el-form-item label="模型说明" prop="description">
            <el-input
              v-model="form.description"
              placeholder="请输入模型说明"
              type="textarea"
              :rows="2"
              :disabled="isEdit"
            />
          </el-form-item>
          <el-divider
            content-position="left"
            @click="toggleCollapse('basicInfoCollapsed')"
            >基础信息
            <i
              class="el-icon-arrow-right"
              :class="{ 'rotate-icon': collapsed.basicInfoCollapsed }"
            ></i>
          </el-divider>
          <div v-if="!collapsed.basicInfoCollapsed">
            <el-form-item label="" >
              <el-checkbox :disabled="isEdit" :indeterminate="isIndeterminateBasic" v-model="checkAllBasic" @change="handleCheckAllChangeBasic">全选</el-checkbox>
              <div style="margin: 15px 0;"></div>
              <el-checkbox-group v-model="form.checkedBasic" @change="handleCheckedBasicChange">
                <el-checkbox :disabled="isEdit" v-for="(city,index) in basicInfoList" :label="city" :key="city.paramName">{{city.paramName}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>

          <el-divider content-position="left" @click="toggleCollapse('productInfoCollapsed')">产品信息
            <i class="el-icon-arrow-right" :class="{ 'rotate-icon': collapsed.productInfoCollapsed }"></i>
          </el-divider>
          <div v-if="!collapsed.productInfoCollapsed">
            <el-form-item label="" >
              <el-checkbox :disabled="isEdit" :indeterminate="isIndeterminateProduct" v-model="checkAllProduct" @change="handleCheckAllChangeProduct">全选</el-checkbox>
              <div style="margin: 15px 0;"></div>
              <el-checkbox-group v-model="form.checkedProduct" @change="handleCheckedProductChange">
                <el-checkbox :disabled="isEdit" v-for="(city,index) in productInfoList" :label="city" :key="index">{{city.paramName}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>

          <el-divider content-position="left" @click="toggleCollapse('tradeInfoCollapsed')">交易信息
            <i class="el-icon-arrow-right" :class="{ 'rotate-icon': collapsed.tradeInfoCollapsed }"></i>
          </el-divider>
          <div v-if="!collapsed.tradeInfoCollapsed">
            <el-form-item label="" >
              <el-checkbox :disabled="isEdit" :indeterminate="isIndeterminateTrade" v-model="checkAllTrade" @change="handleCheckAllChangeTrade">全选</el-checkbox>
              <div style="margin: 15px 0;"></div>
              <el-checkbox-group v-model="form.checkedTrade" @change="handleCheckedTradeChange">
                <el-checkbox :disabled="isEdit" v-for="(city,index) in tradeInfoList" :label="city" :key="index">{{city.paramName}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 模型详情信息对话框 -->
    <el-dialog
      title="模型详情信息"
      :visible.sync="modelDetailDialog"
      width="550px"
      append-to-body
    >
      <el-table v-loading="loading" :data="modelDetailList">
        <el-table-column label="模型名称" align="center" prop="modelName" />
        <el-table-column label="模型说明" align="center" prop="description" />
        <el-table-column label="状态" align="center" prop="status">
          <template slot-scope="scope">
            {{ scope.row.status ? "禁用" : "启用" }}
          </template>
        </el-table-column>
        <el-table-column label="创建人" align="center" prop="createBy" />
        <el-table-column label="创建时间" align="center" prop="createTime" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <!-- <el-button type="primary" @click="submitForm">确 定</el-button> -->
        <el-button @click="modelDetailDialog = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listModelParams,
  getModelInfo,
  addModelInfo,
  initModelInfo,
  listDodelInfo,
} from "@/api/sse/model";

export default {
  name: "ModelInfo",
  dicts: ["sse_server_status"],
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
      // 模型信息表格数据
      modelInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        timeRang: null,
        modelName: null,
      },
      modelParamsOptions: [
        {
          value: "1",
          label: "参数1",
        },
        {
          value: "2",
          label: "参数2",
        },
      ],
      // 表单参数
      form: {
        instanceName: null,
      },
      // 表单校验
      rules: {
        modelName: [
          { required: true, message: "模型名称不能为空", trigger: "blur" },
        ],
      },
      modelDetailDialog: false,
      modelDetailList: [],
      collapsed: {
        basicInfoCollapsed: false,
        productInfoCollapsed: false,
        tradeInfoCollapsed: false,
      },
      basicInfoList: [],
      checkAllBasic: false,
      isIndeterminateBasic: true,

      productInfoList: [],
      checkAllProduct: false,
      isIndeterminateProduct: true, 

      tradeInfoList: [],
      checkAllTrade: false,
      isIndeterminateTrade: true,
      isEdit: false
    };
  },
  created() {
    this.getList();
    this.getModelParams()
  },
  methods: {
    getModelParams(){
      listModelParams().then(response => {
        console.log("response",response);
        response.data.forEach(ele => {
          if(ele.type == 'BASIC'){
            this.basicInfoList = ele.paramList
          } else if(ele.type == 'PRODUCT'){
            this.productInfoList = ele.paramList
          } else if(ele.type == 'TRADE'){
            this.tradeInfoList = ele.paramList
          }
        });
      })
    },
    /** 查询模型信息列表 */
    getList() {
      this.loading = false;
      
      listDodelInfo(this.queryParams).then(response => {
        this.modelInfoList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.collapsed = {
        basicInfoCollapsed: false,
        productInfoCollapsed: false,
        tradeInfoCollapsed: false,
      },
      this.form = {
        modelName: null,
        description: null,
        checkedBasic: [],
        checkedProduct: [],
        checkedTrade: []
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      // 未查询到数据
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.serverId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.isEdit = false
      this.open = true;
      this.title = "添加模型信息";
    },
    /** 查看详情按钮操作 */
    handleDetail(row) {
      this.open = true;
      this.title = "查看模型信息";
      this.isEdit = true
    },
    /** 修改按钮操作 */
    async handleUpdate(row) {
      this.reset();
      this.isEdit = false
      await getModelInfo(row.modelId).then(response => {
        this.form.description = response.data.description
        this.form.modelName = response.data.modelName
        this.form.modelId = response.data.modelId
        
        response.data.modelAddDoList.forEach(ele => {
          if(ele.type == 'BASIC'){
            this.form.checkedBasic = this.basicInfoList.filter(basicItem => 
              ele.paramList.some(backendItem => backendItem.paramValue === basicItem.paramValue)
            );
          } else if(ele.type == 'PRODUCT'){
            // this.form.checkedProduct = ele.paramList || []
            this.form.checkedProduct = this.productInfoList.filter(basicItem => 
              ele.paramList.some(backendItem => backendItem.paramValue === basicItem.paramValue)
            );
          } else if(ele.type == 'TRADE'){
            // this.form.checkedTrade = ele.paramList || []
            this.form.checkedTrade = this.tradeInfoList.filter(basicItem => 
              ele.paramList.some(backendItem => backendItem.paramValue === basicItem.paramValue)
            );
          }
          
        
  
  // 更新全选状态
  // this.checkAllBasic = this.form.checkedBasic.length === this.basicInfoList.length;
  // this.isIndeterminateBasic = this.form.checkedBasic.length > 0 && 
  //                             this.form.checkedBasic.length < this.basicInfoList.length;
          console.log('this.form',this.form);
          
        });
      })
      this.open = true
      this.title = "修改模型信息"
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.modelId != null) {
            
            addModelInfo(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            let modelAddDoList =  [
              {"type": "Basic",
                "paramList":this.form.checkedBasic
              },
              {"type": "PRODUCT",
                "paramList":this.form.checkedProduct
              },
              {"type": "TRADE",
                "paramList":this.form.checkedTrade
              },
            ]
            this.form.modelAddDoList = modelAddDoList
            addModelInfo(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const serverIds = row.modelName ;
      this.$modal
        .confirm('是否确认删除模型信息名称为"' + serverIds + '"的数据项？')
        .then(function () {
          return delServerInfo(serverIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /**警用按钮操作 */
    handleDisabled(row) {
      const serverIds = row.modelName ;
      let status = row.status ? "禁用" : "启用";
      this.$modal
        .confirm(
          "是否确认" + status + "模型信息名称为" + serverIds + '"的数据项？'
        )
        .then(function () {
          return delServerInfo(serverIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("禁用成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "sse/serverInfo/export",
        {
          ...this.queryParams,
        },
        `serverInfo_${new Date().getTime()}.xlsx`
      );
    },
    /** 信息折功能 */
    toggleCollapse(key) {
      this.collapsed[key] = !this.collapsed[key];
    },
    /**基础信息全选功能 */
    handleCheckAllChangeBasic(val){
      if(val){
        this.form.checkedBasic =  [...this.basicInfoList]
      } else {
        this.form.checkedBasic = []
      }
      // this.form.checkedBasic = val ? [...this.basicInfoList] : [];
      this.isIndeterminateBasic = false;
    },
    handleCheckedBasicChange(value) {
      console.log(111,value);
      
      let checkedCount = value.length;
      // const totalCount = this.basicInfoList.length;
      this.checkAllBasic = checkedCount === this.basicInfoList.length;
      this.isIndeterminateBasic = checkedCount > 0 && checkedCount < this.basicInfoList.length;
    },
    // 全选/全不选
    handleCheckAllChange(val) {
        if (val) {
            // 全选：复制原数组的所有对象引用
            this.selectedItems = [...this.options];
        } else {
            // 全不选：清空数组
            this.selectedItems = [];
        }
        this.isIndeterminate = false;
    },
    
    // 复选框组变化时的处理
    handleCheckedChange(value) {
        const checkedCount = value.length;
        const totalCount = this.options.length;
        
        // 更新全选状态
        this.checkAll = checkedCount === totalCount;
        // 更新半选状态
        this.isIndeterminate = checkedCount > 0 && checkedCount < totalCount;
        
        console.log(`选中数量：${checkedCount}/${totalCount}`);
    },
    /**产品信息全选功能 */
    handleCheckAllChangeProduct(val){
      this.form.checkedProduct = val ? this.productInfoList : [];
      this.isIndeterminateProduct = false;
    },
    handleCheckedProductChange(value) {
      let checkedCount = value.length;
      this.checkAllProduct = checkedCount === this.productInfoList.length;
      this.isIndeterminateProduct = checkedCount > 0 && checkedCount < this.productInfoList.length;
    },
    /**交易信息全选功能 */
    handleCheckAllChangeTrade(val){
      this.form.checkedTrade = val ? this.tradeInfoList : [];
      this.isIndeterminateTrade = false;
    },
    handleCheckedTradeChange(value) {
      let checkedCount = value.length;
      this.checkAllTrade = checkedCount === this.tradeInfoList.length;
      this.isIndeterminateTrade = checkedCount > 0 && checkedCount < this.tradeInfoList.length;
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep.model-info {
  .el-dialog__body {
    height: 600px;
    overflow-y: auto;
  }
}
.rotate-icon {
  transform: rotate(90deg);
}
</style>
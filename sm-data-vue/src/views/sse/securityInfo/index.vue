<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="证券代码" prop="securityId">
        <el-input
          v-model="queryParams.securityId"
          placeholder="请输入证券代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="证券名称" prop="securityCname">
        <el-input
          v-model="queryParams.securityCname"
          placeholder="请输入证券名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="市场种类" prop="securityMarket">
        <el-select v-model="queryParams.securityMarket" placeholder="请选择市场种类" clearable>
          <el-option
            v-for="dict in dict.type.sse_security_market"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="证券类别" prop="securityType">
        <el-select v-model="queryParams.securityType" placeholder="请选择证券类别" clearable>
          <el-option
            v-for="dict in dict.type.sse_business_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="证券子类别" prop="securitySubType">
        <el-select v-model="queryParams.securitySubType" placeholder="请选择证券子类别" clearable>
          <el-option
            v-for="dict in dict.type.sse_security_sub_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="set编号" prop="securitySet">
        <el-select v-model="queryParams.securitySet" placeholder="请选择产品集set编号" clearable>
          <el-option
            v-for="dict in dict.type.sse_product_set"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="涨跌幅限制" prop="priceLimitType">
        <el-select v-model="queryParams.priceLimitType" placeholder="请选择涨跌幅限制类型" clearable>
          <el-option
            v-for="dict in dict.type.sse_price_range_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="使用状态" prop="usageStatus">
        <el-select v-model="queryParams.usageStatus" placeholder="请选择使用状态" clearable>
          <el-option
            v-for="dict in dict.type.sse_usage_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
            <el-button
            type="info"
            plain
            icon="el-icon-upload2"
            size="mini"
            @click="handleImport"
            v-hasPermi="['system:user:import']"
            >导入</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['sse:securityInfo:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['sse:securityInfo:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['sse:securityInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-import"
          size="mini"
          @click="handleImport"
          v-hasPermi="['sse:pbuInfo:add']"
        >初始化
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload"
          size="mini"
          @click="handleFileImport"
          v-hasPermi="['sse:securityInfo:add']"
        >文件导入
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="securityInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="证券代码" align="center" prop="securityId"/>
      <el-table-column label="证券名称" align="center" prop="securityCname"/>
      <el-table-column label="市场种类" align="center" prop="securityMarket">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_security_market" :value="scope.row.securityMarket"/>
        </template>
      </el-table-column>
      <el-table-column label="证券类别" align="center" prop="securityType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_business_type" :value="scope.row.securityType"/>
        </template>
      </el-table-column>
      <el-table-column label="证券子类别" align="center" prop="securitySubType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_security_sub_category" :value="scope.row.securitySubType"/>
        </template>
      </el-table-column>
      <el-table-column label="产品集set编号" align="center" prop="securitySet">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_product_set" :value="scope.row.securitySet"/>
        </template>
      </el-table-column>
      <el-table-column label="前收盘价格" align="center" prop="priorClosingPrice"/>
      <el-table-column label="涨跌幅限制类型" align="center" prop="priceLimitType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_price_range_type" :value="scope.row.priceLimitType"/>
        </template>
      </el-table-column>
      <el-table-column label="使用状态" align="center" prop="usageStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sse_usage_status" :value="scope.row.usageStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:securityInfo:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sse:securityInfo:remove']"
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

    <!-- 添加或修改产品信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="证券代码" prop="securityId">
          <el-input v-model="form.securityId" placeholder="请输入证券代码"/>
        </el-form-item>
        <el-form-item label="证券名称" prop="securityCname">
          <el-input v-model="form.securityCname" placeholder="请输入证券名称"/>
        </el-form-item>
        <el-form-item label="英文证券名称" prop="securityName">
          <el-input v-model="form.securityName" placeholder="请输入英文证券名称"/>
        </el-form-item>
        <el-form-item label="基础证券代码" prop="oriSecurityId">
          <el-input v-model="form.oriSecurityId" placeholder="请输入基础证券代码"/>
        </el-form-item>
        <el-form-item label="市场种类" prop="securityMarket">
          <el-select v-model="form.securityMarket" placeholder="请选择市场种类">
            <el-option
              v-for="dict in dict.type.sse_security_market"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="证券类别" prop="securityType">
          <el-select v-model="form.securityType" placeholder="请选择证券类别">
            <el-option
              v-for="dict in dict.type.sse_business_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="证券子类别" prop="securitySubType">
          <el-select v-model="form.securitySubType" placeholder="请选择证券子类别">
            <el-option
              v-for="dict in dict.type.sse_security_sub_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="货币种类" prop="currencyType">
          <el-input v-model="form.currencyType" placeholder="请输入货币种类"/>
        </el-form-item>
        <el-form-item label="债券面值" prop="parValue">
          <el-input v-model="form.parValue" placeholder="请输入债券面值"/>
        </el-form-item>
        <el-form-item label="可流通证券未上市数量" prop="unissuedSecurities">
          <el-input v-model="form.unissuedSecurities" placeholder="请输入可流通证券未上市数量"/>
        </el-form-item>
        <el-form-item label="最后交易日期" prop="lastTradeDate">
          <el-input v-model="form.lastTradeDate" placeholder="请输入最后交易日期"/>
        </el-form-item>
        <el-form-item label="上市日期" prop="listingDate">
          <el-input v-model="form.listingDate" placeholder="请输入上市日期"/>
        </el-form-item>
        <el-form-item label="产品集set编号" prop="securitySet">
          <el-select v-model="form.securitySet" placeholder="请选择产品集set编号">
            <el-option
              v-for="dict in dict.type.sse_product_set"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="买数量单位" prop="buyUnit">
          <el-input v-model="form.buyUnit" placeholder="请输入买数量单位"/>
        </el-form-item>
        <el-form-item label="卖数量单位" prop="sellUnit">
          <el-input v-model="form.sellUnit" placeholder="请输入卖数量单位"/>
        </el-form-item>
        <el-form-item label="限价申报数量下限" prop="limitOrderBottom">
          <el-input v-model="form.limitOrderBottom" placeholder="请输入限价申报数量下限"/>
        </el-form-item>
        <el-form-item label="限价申报数量上限" prop="limitOrderTop">
          <el-input v-model="form.limitOrderTop" placeholder="请输入限价申报数量上限"/>
        </el-form-item>
        <el-form-item label="前收盘价格" prop="priorClosingPrice">
          <el-input v-model="form.priorClosingPrice" placeholder="请输入前收盘价格"/>
        </el-form-item>
        <el-form-item label="价格档位" prop="priceTick">
          <el-input v-model="form.priceTick" placeholder="请输入价格档位"/>
        </el-form-item>
        <el-form-item label="涨跌幅限制类型" prop="priceLimitType">
          <el-select v-model="form.priceLimitType" placeholder="请选择涨跌幅限制类型">
            <el-option
              v-for="dict in dict.type.sse_price_range_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="涨幅上限价格" prop="priceUpsideLimit">
          <el-input v-model="form.priceUpsideLimit" placeholder="请输入涨幅上限价格"/>
        </el-form-item>
        <el-form-item label="涨幅下限价格" prop="priceDownsideLimit">
          <el-input v-model="form.priceDownsideLimit" placeholder="请输入涨幅下限价格"/>
        </el-form-item>
        <el-form-item label="使用状态" prop="usageStatus">
          <el-radio-group v-model="form.usageStatus">
            <el-radio
              v-for="dict in dict.type.sse_usage_status"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 用户导入对话框
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".txt" :headers="upload.headers" :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
          </div>
          <span>仅允许导入txt格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog> -->
    <!-- 文件导入对话框 -->
    <el-dialog :title="fileUpload.title" :visible.sync="fileUpload.open" width="500px" append-to-body>
      <el-upload
        ref="fileUpload"
        :limit="1"
        accept=".txt"
        :headers="fileUpload.headers"
        :action="fileUpload.url"
        :disabled="fileUpload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileImportSuccess"
        :on-error="handleFileImportError"
        :auto-upload="false"
        drag
        :on-change="handleFileChange"

        :file-list="fileUpload.fileList"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击选择文件</em>
        </div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅支持导入 .txt 格式文件（如 cpxx02011225.txt）</span>
          <br>
          <span style="color: #999; font-size: 12px;">文件编码要求：GBK</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileUpload"
        >确 定</el-button>
        <el-button @click="fileUpload.open = false" :disabled="fileUpload.isUploading">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 初始化对话框 -->
    <el-dialog :title="initDialog.title" :visible.sync="initDialog.open" width="600px" append-to-body>
      <el-form :model="initDialog.form" label-width="100px">
        <el-form-item label="文件路径">
          <el-input
            v-model="initDialog.form.filePath"
            placeholder="请输入文件完整路径"
            clearable
          />
          <div style="color: #999; font-size: 12px; margin-top: 5px;">
            Windows: D:\data\cpxx02011225.txt<br>
            Linux: /data/sse/cpxx02011225.txt
          </div>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitInitPath" :disabled="initDialog.isUploading">确 定</el-button>
        <el-button @click="initDialog.open = false" :disabled="initDialog.isUploading">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listSecurityInfo,
  getSecurityInfo,
  delSecurityInfo,
  addSecurityInfo,
  updateSecurityInfo,
  init
} from "@/api/sse/securityInfo"
import {getToken} from "@/utils/auth"
import request from '@/utils/request'

export default {
  name: "SecurityInfo",
  dicts: ['sse_price_range_type', 'sse_security_market', 'sse_security_sub_category', 'sse_product_set', 'sse_usage_status', 'sse_business_type'],
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
      // 产品信息表格数据
      securityInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/sse/securityInfo/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        securityId: null,
        securityCname: null,
        securityMarket: null,
        securityType: null,
        securitySubType: null,
        securitySet: null,
        priceLimitType: null,
        usageStatus: null
      },
      // 表单参数
      form: {},
      // 初始化对话框参数
      initDialog: {
        // 是否显示弹出层
        open: false,
        // 标题
        title: "初始化数据",
        // 是否禁用上传
        isUploading: false,
        // 表单数据
        form: {
          filePath: ''
        }
      },
      // 文件导入参数
      fileUpload: {
        // 是否显示弹出层
        open: false,
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/sse/securityInfo/importFile",
        // 已上传的文件列表
        fileList: []
      },
      // 表单校验
      rules: {
        securityId: [
          {required: true, message: "证券代码不能为空", trigger: "blur"}
        ],
        securityCname: [
          {required: true, message: "证券名称不能为空", trigger: "blur"}
        ],
        securityMarket: [
          {required: true, message: "市场种类不能为空", trigger: "change"}
        ],
        securityType: [
          {required: true, message: "证券类别不能为空", trigger: "change"}
        ],
        securitySubType: [
          {required: true, message: "证券子类别不能为空", trigger: "change"}
        ],
        securitySet: [
          {required: true, message: "产品集set编号不能为空", trigger: "change"}
        ],
        usageStatus: [
          {required: true, message: "使用状态不能为空", trigger: "change"}
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询产品信息列表 */
    getList() {
      this.loading = true
      listSecurityInfo(this.queryParams).then(response => {
        this.securityInfoList = response.rows
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
        securityId: null,
        isinCode: null,
        updateTime: null,
        securityCname: null,
        securityName: null,
        oriSecurityId: null,
        securityMarket: null,
        securityType: null,
        securitySubType: null,
        currencyType: null,
        parValue: null,
        unissuedSecurities: null,
        lastTradeDate: null,
        listingDate: null,
        securitySet: null,
        buyUnit: null,
        sellUnit: null,
        limitOrderBottom: null,
        limitOrderTop: null,
        priorClosingPrice: null,
        priceTick: null,
        priceLimitType: null,
        priceUpsideLimit: null,
        priceDownsideLimit: null,
        usageStatus: null
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
      this.title = "添加产品信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pkId = row.pkId || this.ids
      getSecurityInfo(pkId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改产品信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.pkId != null) {
            updateSecurityInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSecurityInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除产品信息编号为"' + pkIds + '"的数据项？').then(function () {
        return delSecurityInfo(pkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    // /** 导入按钮操作 */
    // handleImport() {
    //   this.upload.title = "用户导入"
    //   this.upload.open = true
    // },
    /** 初始化按钮操作 */
    handleImport() {
      this.initDialog.open = true;
      this.initDialog.form.filePath = '';
      this.initDialog.isUploading = false;
    },

    /** 提交初始化路径 */
    submitInitPath() {
      const filePath = this.initDialog.form.filePath;

      if (!filePath || !filePath.trim()) {
        this.$modal.msgWarning("请输入文件路径");
        return;
      }

      // 验证文件格式
      if (!filePath.toLowerCase().endsWith('.txt')) {
        this.$modal.msgWarning('仅支持导入 .txt 格式文件');
        return;
      }

      this.initDialog.isUploading = true;

      // 调用后端初始化接口

      ({
        url: "/sse/securityInfo/initData",
        method: 'post',
        data: {
          filePath: filePath.trim()
        },
        timeout: 600000
      }).then(response => {
        this.initDialog.isUploading = false;
        this.initDialog.open = false;

        if (response.code === 200) {
          this.$modal.msgSuccess(response.msg || "初始化成功");
          this.getList();
        } else {
          this.$modal.msgError(response.msg || "初始化失败");
        }
      }).catch(error => {
        this.initDialog.isUploading = false;
        this.$modal.msgError("初始化失败：" + error.message);
      });
    },

    /** 文件导入按钮操作 */
    handleFileImport() {
      this.fileUpload.open = true;
      this.fileUpload.fileList = [];
      this.fileUpload.isUploading = false;
    },

    /** 文件选择变化处理 */
    handleFileChange(file, fileList) {
      this.fileUpload.fileList = fileList;
    },

    /** 文件上传中处理 */
    handleFileUploadProgress(event, file, fileList) {
      this.fileUpload.isUploading = true;
    },

    /** 文件上传成功处理 */
    handleFileImportSuccess(response, file, fileList) {
      this.fileUpload.isUploading = false;
      this.fileUpload.open = false;
      this.fileUpload.fileList = [];

      if (response.code === 200) {
        this.$modal.msgSuccess(response.msg || "导入成功");
        this.getList();
      } else {
        this.$modal.msgError(response.msg || "导入失败");
      }
    },

    /** 文件上传失败处理 */
    handleFileImportError(error, file, fileList) {
      this.fileUpload.isUploading = false;
      this.$modal.msgError("上传失败：" + error.message);
    },

    /** 提交上传文件 */
    submitFileUpload() {
      if (this.fileUpload.fileList.length === 0) {
        this.$modal.msgWarning("请先选择文件");
        return;
      }
      this.$refs.fileUpload.submit();
    }

    // // 文件上传中处理
    // handleFileUploadProgress(event, file, fileList) {
    //   this.upload.isUploading = true
    // },
    // 文件上传成功处理
    // handleFileSuccess(response, file, fileList) {
    //   this.upload.open = false
    //   this.upload.isUploading = false
    //   this.$refs.upload.clearFiles()
    //   this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
    //   this.getList()
    // },
    // // 提交上传文件
    // submitFileForm() {
    //   this.$refs.upload.submit()
    // }
  }
}
</script>

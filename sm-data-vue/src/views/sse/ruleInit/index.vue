<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="规则ID" prop="ruleId">
        <el-input
          v-model="queryParams.ruleId"
          placeholder="请输入规则ID"
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
          v-hasPermi="['sse:ruleInit:add']"
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
          v-hasPermi="['sse:ruleInit:edit']"
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
          v-hasPermi="['sse:ruleInit:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['sse:ruleInit:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ruleInitList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="规则ID" align="center" prop="ruleId" />

 <el-table-column label="pbu和账户集合" align="center" prop="pbuAccountList">
    <template #default="scope">
      <el-tooltip
        placement="top"
        effect="light"
        :disabled="shouldDisableTooltip(scope.row.pbuAccountList)"
      >
          <div slot="content" class="json-tooltip">
            <pre v-html="formatJsonWithColor(scope.row.pbuAccountList)"></pre>
          </div>
        <div class="ellipsis-cell" >{{ formatJson(scope.row.pbuAccountList) }}</div>
      </el-tooltip>
    </template>
  </el-table-column>
  <!-- 主板股票列 -->
  <el-table-column label="主板股票" align="center" prop="securityAshList">
    <template slot-scope="scope">
      <el-tooltip
        placement="top"
        effect="light"
        :disabled="shouldDisableTooltip(scope.row.securityAshList)"
      >
        <div slot="content" class="json-tooltip">
          <pre v-html="formatJsonWithColor(scope.row.securityAshList)"></pre>
        </div>
        
        <div class="ellipsis-cell">{{ formatList(scope.row.securityAshList) }}</div>
      </el-tooltip>
    </template>
  </el-table-column>


<!-- 科创板股票 -->
   <el-table-column label="科创板股票" align="center" prop="securityKshList">
    <template slot-scope="scope">
      <el-tooltip
        placement="top"
        effect="light"
        :disabled="shouldDisableTooltip(scope.row.securityKshList)"
      >
        <div slot="content" class="json-tooltip">
          <pre v-html="formatJsonWithColor(scope.row.securityKshList)"></pre>
        </div>
        
        <div class="ellipsis-cell">{{ formatList(scope.row.securityKshList) }}</div>
      </el-tooltip>
    </template>
  </el-table-column>
    
    <!-- 主板SET股票分配 -->
    <el-table-column label="主板SET股票分配" align="center" prop="setAshSecurity">
    <template slot-scope="scope">
      <el-tooltip
        placement="top"
        effect="light"
        :disabled="shouldDisableTooltip(scope.row.setAshSecurity)"
      >
        <div slot="content" class="json-tooltip">
          <pre v-html="formatJsonWithColor(scope.row.setAshSecurity)"></pre>
        </div>
        
        <div class="ellipsis-cell">{{ formatList(scope.row.setAshSecurity) }}</div>
      </el-tooltip>
    </template>
  </el-table-column>
    
    <!-- 科创板SET股票分配 -->
         <el-table-column label="科创板SET股票分配" align="center" prop="setKshSecurity">
    <template slot-scope="scope">
      <el-tooltip
        placement="top"
        effect="light"
        :disabled="shouldDisableTooltip(scope.row.setKshSecurity)"
      >
        <div slot="content" class="json-tooltip">
          <pre v-html="formatJsonWithColor(scope.row.setKshSecurity)"></pre>
        </div>
        
        <div class="ellipsis-cell">{{ formatList(scope.row.setKshSecurity) }}</div>
      </el-tooltip>
    </template>
  </el-table-column>

    
    <!-- 撮合占比分类列表 -->
  <el-table-column label="撮合占比分类列表" align="center" prop="matchOrderList">
    <template slot-scope="scope">
      <el-tooltip
        placement="top"
        effect="light"
        :disabled="shouldDisableTooltip(scope.row.matchOrderList)"
      >
        <div slot="content" class="json-tooltip">
          <pre v-html="formatJsonWithColor(scope.row.matchOrderList)"></pre>
        </div>
        
        <div class="ellipsis-cell">{{ formatList(scope.row.matchOrderList) }}</div>
      </el-tooltip>
    </template>
  </el-table-column>

      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:ruleInit:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sse:ruleInit:remove']"
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

    <!-- 添加或修改规则初始化对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="规则ID" prop="ruleId">
          <el-input v-model="form.ruleId" placeholder="请输入规则ID" />
        </el-form-item>
        <el-form-item label="登录pbu" prop="loginPbu">
          <el-input v-model="form.loginPbu" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="业务pbu" prop="bizPbu">
          <el-input v-model="form.bizPbu" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="pbu和账户集合" prop="pbuAccountList">
          <el-input v-model="form.pbuAccountList" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="主板股票" prop="securityAshList">
          <el-input v-model="form.securityAshList" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="科创板股票" prop="securityKshList">
          <el-input v-model="form.securityKshList" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="主板SET股票分配" prop="setAshSecurity">
          <el-input v-model="form.setAshSecurity" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="科创板SET股票分配" prop="setKshSecurity">
          <el-input v-model="form.setKshSecurity" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="撮合占比分类列表" prop="matchOrderList">
          <el-input v-model="form.matchOrderList" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listRuleInit, getRuleInit, delRuleInit, addRuleInit, updateRuleInit } from "@/api/sse/ruleInit"

export default {
  name: "RuleInit",
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
      // 规则初始化表格数据
      ruleInitList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleId: null,
        pbuAccountList: null,
        securityAshList: null,
        securityKshList: null,
        setAshSecurity: null,
        setKshSecurity: null,
        matchOrderList: null,
        status: null,
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
  
// 判断是否禁用浮窗
    shouldDisableTooltip(data) {
      try {
        const parsedData = typeof data === 'string' ? JSON.parse(data) : data;
        const str = JSON.stringify(parsedData);
        return str.length <= 30;
      } catch (e) {
        return false;
      }
    },


    // Vue 2适配的JSON格式化方法
  formatJson(data) {
  // 处理空数据
  if (!data) return '无数据';
  
  try {
    // 统一解析数据（支持字符串和原始对象/数组）
    const parsedData = typeof data === 'string' 
      ? JSON.parse(data) 
      : data;
    
    // 递归处理函数：将数据转为带缩进的树形字符串
    const render = (item, indent = 0) => {
      const spaces = '  '.repeat(indent); // 缩进空格
      const nextIndent = indent + 1;      // 下一级缩进
      
      // 如果是数组
      if (Array.isArray(item)) {
        if (item.length === 0) return '[]';
        
        let result = '[\n';
        item.forEach((child, index) => {
          const isLast = index === item.length - 1;
          // 递归处理数组项
          result += `${spaces}  ${render(child, nextIndent)}${isLast ? '' : ','}\n`;
        });
        result += `${spaces}]`;
        return result;
      }
      
      // 如果是对象
      if (item !== null && typeof item === 'object') {
        const keys = Object.keys(item);
        if (keys.length === 0) return '{}';
        
        let result = '{\n';
        keys.forEach((key, index) => {
          const isLast = index === keys.length - 1;
          const value = item[key];
          // 递归处理对象值
          result += `${spaces}  "${key}": ${render(value, nextIndent)}${isLast ? '' : ','}\n`;
        });
        result += `${spaces}}`;
        return result;
      }
      
      // 处理基本类型（字符串/数字/布尔值/null）
      if (typeof item === 'string') {
        return `"${item}"`; // 字符串加引号
      }
      return String(item); // 其他类型直接转为字符串
    };
    
    // 从根节点开始渲染，初始缩进为0
    return render(parsedData);
  } catch (error) {
    return `格式错误: ${error.message}`;
  }
},
  // 带颜色的JSON格式化（基于原树形结构扩展）
    formatJsonWithColor(data) {
      if (!data) return '<span class="json-null">无数据</span>';
      
      try {
        // 统一解析数据
        const parsedData = typeof data === 'string' 
          ? JSON.parse(data) 
          : data;
        
        // 递归处理并添加样式类
        const render = (item, indent = 0) => {
          const spaces = '  '.repeat(indent);
          const nextIndent = indent + 1;
          
          // 数组处理
          if (Array.isArray(item)) {
            if (item.length === 0) {
              return '<span class="json-bracket">[]</span>';
            }
            
            let result = '<span class="json-bracket">[</span>\n';
            item.forEach((child, index) => {
              const isLast = index === item.length - 1;
              result += `${spaces}  ${render(child, nextIndent)}`;
              result += isLast ? '' : '<span class="json-comma">,</span>';
              result += '\n';
            });
            result += `${spaces}<span class="json-bracket">]</span>`;
            return result;
          }
          
          // 对象处理
          if (item !== null && typeof item === 'object') {
            const keys = Object.keys(item);
            if (keys.length === 0) {
              return '<span class="json-brace">{}</span>';
            }
            
            let result = '<span class="json-brace">{</span>\n';
            keys.forEach((key, index) => {
              const isLast = index === keys.length - 1;
              const value = item[key];
              // 键名添加样式
              result += `${spaces}  <span class="json-key">"${key}"</span><span class="json-colon">:</span> ${render(value, nextIndent)}`;
              result += isLast ? '' : '<span class="json-comma">,</span>';
              result += '\n';
            });
            result += `${spaces}<span class="json-brace">}</span>`;
            return result;
          }
          
          // 字符串类型
          if (typeof item === 'string') {
            return `<span class="json-string">"${item}"</span>`;
          }
          
          // 数字类型
          if (typeof item === 'number') {
            return `<span class="json-number">${item}</span>`;
          }
          
          // 布尔值
          if (typeof item === 'boolean') {
            return `<span class="json-boolean">${item}</span>`;
          }
          
          // null
          if (item === null) {
            return '<span class="json-null">null</span>';
          }
          
          // 其他类型
          return String(item);
        };
        
        return render(parsedData);
      } catch (error) {
        return `<span class="json-error">格式错误: ${error.message}</span>`;
      }
    },
    /** 查询规则初始化列表 */
    getList() {
      this.loading = true
      listRuleInit(this.queryParams).then(response => {
        this.ruleInitList = response.rows
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
        loginPbu: null,
        bizPbu: null,
        pbuAccountList: null,
        securityAshList: null,
        securityKshList: null,
        setAshSecurity: null,
        setKshSecurity: null,
        matchOrderList: null,
        field2: null,
        field3: null,
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.title = "添加规则初始化"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const pkId = row.pkId || this.ids
      getRuleInit(pkId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改规则初始化"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.pkId != null) {
            updateRuleInit(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRuleInit(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除规则初始化编号为"' + pkIds + '"的数据项？').then(function() {
        return delRuleInit(pkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('sse/ruleInit/export', {
        ...this.queryParams
      }, `ruleInit_${new Date().getTime()}.xlsx`)
    },
    // 计算字符串长度（处理null/undefined）
    getStringLength(str) {
      if (!str) return 0;
      return String(str).length;
    },
    // 格式化列表数据（将数组转为字符串）
    formatList(list) {
      if (!list || !Array.isArray(list)) {
        return list || '';
      }
      // 数组转为以逗号分隔的字符串
      return list.join(', ');
    }
  }
}
</script>


<style scoped>

/* 优化提示框样式 */
::deep .el-tooltip__popper {
  max-width: 900px;         /* 提示框最大宽度 */
  padding: 8px 12px;
  font-size: 14px;
  line-height: 1.5;         /* 提高多行可读性 */
}

/* JSON浮窗容器样式 */

.json-tooltip {
  background: #fff !important;
  padding: 15px;
  border-radius: 6px;
  box-shadow: 0 3px 12px rgba(0,0,0,0.1);
  max-width: 900px; /* 增大宽度（默认700px，可根据需要调整） */
  width: auto; /* 允许宽度自适应内容 */
  min-width: 300px; /* 可选：设置最小宽度 */
  max-height: 500px; /* 可同步增大高度 */
  overflow: auto;
}

.json-tooltip pre {
  margin: 0;
  font-family: Consolas, "Liberation Mono", Menlo, monospace;
  font-size: 13px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-wrap: break-word;
}

/* JSON元素颜色样式 */
/* 确保样式正确应用 */
/* 关键：使用/deep/穿透，确保样式能应用到v-html生成的元素 */
::v-deep .json-brace { color: #888 !important; }
::v-deep .json-bracket { color: #666 !important; }
::v-deep .json-colon { color: #bbb !important; }
::v-deep .json-comma { color: #bbb !important; }
::v-deep .json-key { color: #2c3e50 !important; font-weight: bold !important; }
::v-deep .json-string { color: #27ae60 !important; }
::v-deep .json-number { color: #e74c3c !important; }
::v-deep .json-boolean { color: #3498db !important; }
::v-deep .json-null { color: #999 !important; }
::v-deep .json-error { color: #e74c3c !important; }

/* 单元格省略样式 */
.ellipsis-cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 250px;
  color: #333;
    /* 禁用浏览器原生tooltip */
  pointer-events: auto !important;
}

</style>
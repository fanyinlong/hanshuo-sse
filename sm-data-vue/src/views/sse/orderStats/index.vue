<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="规则ID" prop="ruleId">
        <el-input
          v-model="queryParams.ruleId"
          placeholder="请输入规则ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
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
          v-hasPermi="['sse:orderStats:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['sse:orderStats:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['sse:orderStats:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['sse:orderStats:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="orderStatsList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="规则ID" align="center" prop="ruleId" />
      <el-table-column label="总订单数" align="center" prop="totalOrders" />
      <el-table-column
        label="各登录PBU订单数"
        align="center"
        prop="loginPbuOrders"
      >
        <template slot-scope="scope">
          <el-tooltip
            placement="top"
            effect="light"
            :disabled="shouldDisableTooltip(scope.row.loginPbuOrders)"
          >
            <div slot="content" class="json-tooltip">
              <pre v-html="formatJsonWithColor(scope.row.loginPbuOrders)"></pre>
            </div>

            <div class="ellipsis-cell">
              {{ formatList(scope.row.loginPbuOrders) }}
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="各业务PBU订单数"
        align="center"
        prop="bizPbuOrders"
      >
        <template slot-scope="scope">
          <el-tooltip
            placement="top"
            effect="light"
            :disabled="shouldDisableTooltip(scope.row.bizPbuOrders)"
          >
            <div slot="content" class="json-tooltip">
              <pre v-html="formatJsonWithColor(scope.row.bizPbuOrders)"></pre>
            </div>

            <div class="ellipsis-cell">
              {{ formatList(scope.row.bizPbuOrders) }}
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="主板科创板订单数"
        align="center"
        prop="ashKshOrders"
      >
        <template slot-scope="scope">
          <el-tooltip
            placement="top"
            effect="light"
            :disabled="shouldDisableTooltip(scope.row.ashKshOrders)"
          >
            <div slot="content" class="json-tooltip">
              <pre v-html="formatJsonWithColor(scope.row.ashKshOrders)"></pre>
            </div>

            <div class="ellipsis-cell">
              {{ formatList(scope.row.ashKshOrders) }}
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="各SET订单数" align="center" prop="setsOrders">
        <template slot-scope="scope">
          <el-tooltip
            placement="top"
            effect="light"
            :disabled="shouldDisableTooltip(scope.row.setsOrders)"
          >
            <div slot="content" class="json-tooltip">
              <pre v-html="formatJsonWithColor(scope.row.setsOrders)"></pre>
            </div>

            <div class="ellipsis-cell">
              {{ formatList(scope.row.setsOrders) }}
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="各撮合方式订单数"
        align="center"
        prop="matchingTypeOrders"
      >
        <template slot-scope="scope">
          <el-tooltip
            placement="top"
            effect="light"
            :disabled="shouldDisableTooltip(scope.row.matchingTypeOrders)"
          >
            <div slot="content" class="json-tooltip">
              <pre
                v-html="formatJsonWithColor(scope.row.matchingTypeOrders)"
              ></pre>
            </div>

            <div class="ellipsis-cell">
              {{ formatList(scope.row.matchingTypeOrders) }}
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="各撮合比订单数"
        align="center"
        prop="matchingSideOrders"
      />
      <el-table-column
        label="各账户类型订单数"
        align="center"
        prop="accountTypeOrders"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-refresh"
            @click="handleRefresh(scope.row)"
            v-hasPermi="['sse:orderStats:edit']"
            >刷新</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['sse:orderStats:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sse:orderStats:remove']"
            >删除</el-button
          >
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
    <!-- 添加或修改订单统计对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="规则ID" prop="ruleId">
          <el-input v-model="form.ruleId" placeholder="请输入规则ID" />
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
import {
  listOrderStats,
  getOrderStats,
  delOrderStats,
  addOrderStats,
  updateOrderStats,
  refreshOrderStats,
} from "@/api/sse/orderStats";

export default {
  name: "OrderStats",
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
      // 订单统计表格数据
      orderStatsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        ruleId: [
          { required: true, message: "规则ID不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 判断是否禁用浮窗
    shouldDisableTooltip(data) {
      try {
        const parsedData = typeof data === "string" ? JSON.parse(data) : data;
        const str = JSON.stringify(parsedData);
        return str.length <= 30;
      } catch (e) {
        return false;
      }
    },

    // Vue 2适配的JSON格式化方法
    formatJson(data) {
      // 处理空数据
      if (!data) return "无数据";

      try {
        // 统一解析数据（支持字符串和原始对象/数组）
        const parsedData = typeof data === "string" ? JSON.parse(data) : data;

        // 递归处理函数：将数据转为带缩进的树形字符串
        const render = (item, indent = 0) => {
          const spaces = "  ".repeat(indent); // 缩进空格
          const nextIndent = indent + 1; // 下一级缩进

          // 如果是数组
          if (Array.isArray(item)) {
            if (item.length === 0) return "[]";

            let result = "[\n";
            item.forEach((child, index) => {
              const isLast = index === item.length - 1;
              // 递归处理数组项
              result += `${spaces}  ${render(child, nextIndent)}${
                isLast ? "" : ","
              }\n`;
            });
            result += `${spaces}]`;
            return result;
          }

          // 如果是对象
          if (item !== null && typeof item === "object") {
            const keys = Object.keys(item);
            if (keys.length === 0) return "{}";

            let result = "{\n";
            keys.forEach((key, index) => {
              const isLast = index === keys.length - 1;
              const value = item[key];
              // 递归处理对象值
              result += `${spaces}  "${key}": ${render(value, nextIndent)}${
                isLast ? "" : ","
              }\n`;
            });
            result += `${spaces}}`;
            return result;
          }

          // 处理基本类型（字符串/数字/布尔值/null）
          if (typeof item === "string") {
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
        const parsedData = typeof data === "string" ? JSON.parse(data) : data;

        // 递归处理并添加样式类
        const render = (item, indent = 0) => {
          const spaces = "  ".repeat(indent);
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
              result += isLast ? "" : '<span class="json-comma">,</span>';
              result += "\n";
            });
            result += `${spaces}<span class="json-bracket">]</span>`;
            return result;
          }

          // 对象处理
          if (item !== null && typeof item === "object") {
            const keys = Object.keys(item);
            if (keys.length === 0) {
              return '<span class="json-brace">{}</span>';
            }

            let result = '<span class="json-brace">{</span>\n';
            keys.forEach((key, index) => {
              const isLast = index === keys.length - 1;
              const value = item[key];
              // 键名添加样式
              result += `${spaces}  <span class="json-key">"${key}"</span><span class="json-colon">:</span> ${render(
                value,
                nextIndent
              )}`;
              result += isLast ? "" : '<span class="json-comma">,</span>';
              result += "\n";
            });
            result += `${spaces}<span class="json-brace">}</span>`;
            return result;
          }

          // 字符串类型
          if (typeof item === "string") {
            return `<span class="json-string">"${item}"</span>`;
          }

          // 数字类型
          if (typeof item === "number") {
            return `<span class="json-number">${item}</span>`;
          }

          // 布尔值
          if (typeof item === "boolean") {
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
    /** 查询订单统计列表 */
    getList() {
      this.loading = true;
      listOrderStats(this.queryParams).then((response) => {
        this.orderStatsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        pkId: null,
        ruleId: null,
        totalOrders: null,
        loginPbuOrders: null,
        bizPbuOrders: null,
        ashKshOrders: null,
        setsOrders: null,
        matchingTypeOrders: null,
        matchingSideOrders: null,
        accountTypeOrders: null,
        field1: null,
        field2: null,
        field3: null,
        field4: null,
        field5: null,
        field6: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.pkId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订单统计";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const pkId = row.pkId || this.ids;
      getOrderStats(pkId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改订单统计";
      });
    },
    handleRefresh(row) {
      const pkId = row.pkId || this.ids;
      refreshOrderStats(pkId).then(() => {
        this.$modal.msgSuccess("刷新成功");
        this.getList();
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.pkId != null) {
            updateOrderStats(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOrderStats(this.form).then((response) => {
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
      const pkIds = row.pkId || this.ids;
      this.$modal
        .confirm('是否确认删除订单统计编号为"' + pkIds + '"的数据项？')
        .then(function () {
          return delOrderStats(pkIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "sse/orderStats/export",
        {
          ...this.queryParams,
        },
        `orderStats_${new Date().getTime()}.xlsx`
      );
    },
    getStringLength(str) {
      if (!str) return 0;
      return String(str).length;
    },
    // 格式化列表数据（将数组转为字符串）
    formatList(list) {
      if (!list || !Array.isArray(list)) {
        return list || "";
      }
      // 数组转为以逗号分隔的字符串
      return list.join(", ");
    },
  },
};
</script>

<style scoped>
/* 文本截断核心样式 */
.ellipsis-cell {
  white-space: nowrap; /* 不换行 */
  overflow: hidden; /* 超出隐藏 */
  text-overflow: ellipsis; /* 显示省略号 */
  max-width: 180px; /* 最大宽度，可根据需要调整 */
  cursor: default; /* 鼠标样式 */
}

/* 优化提示框样式 */
::deep .el-tooltip__popper {
  max-width: 400px; /* 提示框最大宽度 */
  padding: 8px 12px;
  font-size: 14px;
  line-height: 1.5; /* 提高多行可读性 */
}

/* JSON浮窗容器样式 */

.json-tooltip {
  background: #fff !important;
  padding: 15px;
  border-radius: 6px;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.1);
  max-width: 700px; /* 增大宽度（默认700px，可根据需要调整） */
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
::v-deep .json-brace {
  color: #888 !important;
}
::v-deep .json-bracket {
  color: #666 !important;
}
::v-deep .json-colon {
  color: #bbb !important;
}
::v-deep .json-comma {
  color: #bbb !important;
}
::v-deep .json-key {
  color: #2c3e50 !important;
  font-weight: bold !important;
}
::v-deep .json-string {
  color: #27ae60 !important;
}
::v-deep .json-number {
  color: #e74c3c !important;
}
::v-deep .json-boolean {
  color: #3498db !important;
}
::v-deep .json-null {
  color: #999 !important;
}
::v-deep .json-error {
  color: #e74c3c !important;
}

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
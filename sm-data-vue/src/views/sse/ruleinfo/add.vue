<template>
  <div class="app-container">
    <el-card shadow="hover" class="form-container">
      <div slot="header" class="clearfix">
        <span>规则配置信息</span>
      </div>

      <el-form ref="form" :model="form" :rules="rules" label-width="160px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="form.ruleName" placeholder="请输入规则名称" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则英文名称" prop="ruleEnName">
              <el-input v-model="form.ruleEnName" placeholder="请输入规则英文名称" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left" @click="toggleCollapse('basicInfoCollapsed')">基础信息
          <i class="el-icon-arrow-right" :class="{ 'rotate-icon': collapsed.basicInfoCollapsed }"></i>
        </el-divider>
        <div v-if="!collapsed.basicInfoCollapsed">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="订单总数" prop="orderCount">
                <el-input v-model="form.orderCount" placeholder="请输入订单总数"
                          style="width: 100%"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="成交比(%)" prop="tradeRatio">
                <el-input v-model="form.tradeRatio" placeholder="请输入成交比"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="业务PBU总数" prop="pbuCount">
                <el-input-number v-model="form.pbuCount" :min="1" :precision="0" placeholder="请输入业务PBU总数"
                                 style="width: 100%"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="登录PBU总数" prop="loginPbu">
                <el-input-number v-model="form.loginPbu" :min="1" :precision="0" placeholder="请输入登陆PBU总数"
                                 style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="报盘主机" prop="bidHost">
              <el-input v-model="form.bidHost" placeholder="请输入报盘主机" />
            </el-form-item>
          </el-col>
        </el-row> -->
          <!--          <el-row :gutter="20">-->
          <!--            <el-col :span="24">-->
          <!--              <el-form-item label="报盘主机" prop="bidHost">-->
          <!--                <el-select v-model="selectedIds" multiple placeholder="请选择报盘主机" clearable filterable-->
          <!--                  :loading="serverListLoading">-->
          <!--                  <el-option v-for="server in serverInfoList" :key="server.serverId"-->
          <!--                    :label="`${server.serverId} (ID: ${server.serverId}, IP: ${server.ipAddress})`"-->
          <!--                    :value="server.serverId" />-->
          <!--                </el-select>-->
          <!--              </el-form-item>-->
          <!--            </el-col>-->
          <!--          </el-row>-->
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="订阅PBU总量" prop="subscribePbuCount">
                <el-input-number v-model="form.subscribePbuCount" :min="1" :precision="0" style="width: 100%"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="全市场被订阅PBU数量" prop="totalSubscribedPbuCount">
                <el-input-number v-model="form.totalSubscribedPbuCount" :min="1" :precision="0" :default-value="2000"
                                 style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="订阅执行报告占比(%)" prop="subscribeReportRatio">
                <el-input v-model="form.subscribeReportRatio"/>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <el-divider content-position="left" @click="toggleCollapse('productInfoCollapsed')">产品信息
          <i class="el-icon-arrow-right" :class="{ 'rotate-icon': collapsed.productInfoCollapsed }"></i>
        </el-divider>
        <div v-if="!collapsed.productInfoCollapsed">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="产品交易类型" prop="tradeTypeList">
                <el-checkbox-group v-model="form.tradeTypeList"
                                   @change="(values) => handleSelectChange('tradeTypeList', values)">
                  <el-checkbox v-for="dict in dict.type.sse_trade_type" :key="dict.value" :label="dict.value">{{
                      dict.label
                    }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="form.tradeTypeList && form.tradeTypeList.length > 0">
            <el-col :span="24">
              <el-card shadow="hover" style="margin-bottom: 20px;">
                <div slot="header" class="clearfix">
                  <span>交易类型比例配置</span>
                </div>
                <el-table :data="tradeTypeRatios" border style="width: 100%;">
                  <el-table-column prop="label" label="交易类型" width="180">
                    <template slot-scope="scope">
                      {{ getDictLabel('sse_trade_type', scope.row.value) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="quantity" label="产品配置数量">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.quantity" :min="0" :precision="0" @change="validateRatios"
                                       controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column prop="ratio" label="配置比例(%)">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.ratio" :min="0" :max="100" :precision="2"
                                       @change="validateRatios" controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                </el-table>
                <div style="margin-top: 15px; text-align: left;">
                  <span :style="{ color: ratioTotal === 100 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
                    比例总和: {{ ratioTotal }}%
                    <span v-if="ratioTotal !== 100" style="margin-left: 10px;">(比例总和必须等于100%)</span>
                  </span>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="产品业务分类" prop="businessTypeList">
                <el-select v-model="form.businessTypeList" multiple placeholder="请选择产品业务类型" style="width:100%"
                           @change="(values) => handleSelectChange('businessTypeList', values)">
                  <el-option v-for="dict in dict.type.sse_business_type" :key="dict.value" :label="dict.label"
                             :value="dict.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="form.businessTypeList && form.businessTypeList.length > 0">
            <el-col :span="24">
              <el-card shadow="hover" style="margin-bottom: 20px;">
                <div slot="header" class="clearfix">
                  <span>产品业务分类比例配置</span>
                </div>
                <el-table :data="businessTypeRatios" border style="width: 100%;">
                  <el-table-column prop="label" label="业务类型" width="180">
                    <template slot-scope="scope">
                      {{ getDictLabel('sse_business_type', scope.row.value) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="quantity" label="产品配置数量">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.quantity" :min="0" :precision="0" @change="validateRatios"
                                       controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column prop="ratio" label="配置比例(%)">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.ratio" :min="0" :max="100" :precision="2"
                                       @change="validateRatios" controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                </el-table>
                <div style="margin-top: 15px; text-align: left;">
                  <span :style="{ color: businessTypeRatioTotal === 100 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
                    比例总和: {{ businessTypeRatioTotal }}%
                    <span v-if="businessTypeRatioTotal !== 100" style="margin-left: 10px;">(比例总和必须等于100%)</span>
                  </span>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="证券子类别" prop="securitySubCategoryList">
                <el-select v-model="form.securitySubCategoryList" multiple placeholder="请选择证券子类别"
                           style="width:100%"
                           @change="(values) => handleSelectChange('securitySubCategoryList', values)">
                  <el-option v-for="dict in dict.type.sse_security_sub_category" :key="dict.value" :label="dict.label"
                             :value="dict.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="form.securitySubCategoryList && form.securitySubCategoryList.length > 0">
            <el-col :span="24">
              <el-card shadow="hover" style="margin-bottom: 20px;">
                <div slot="header" class="clearfix">
                  <span>证券子类别比例配置</span>
                </div>
                <el-table :data="securitySubCategoryRatios" border style="width: 100%;">
                  <el-table-column prop="label" label="证券子类别" width="180">
                    <template slot-scope="scope">
                      {{ getDictLabel('sse_security_sub_category', scope.row.value) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="quantity" label="产品配置数量">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.quantity" :min="0" :precision="0" @change="validateRatios"
                                       controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column prop="ratio" label="配置比例(%)">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.ratio" :min="0" :max="100" :precision="2"
                                       @change="validateRatios" controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                </el-table>
                <div style="margin-top: 15px; text-align: left;">
                  <span
                    :style="{ color: securitySubCategoryRatioTotal === 100 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
                    比例总和: {{ securitySubCategoryRatioTotal }}%
                    <span v-if="securitySubCategoryRatioTotal !== 100"
                          style="margin-left: 10px;">(比例总和必须等于100%)</span>
                  </span>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="产品所属SET" prop="productSetList">
                <el-select v-model="form.productSetList" multiple placeholder="请选择产品所属SET" style="width:100%"
                           @change="(values) => handleSelectChange('productSetList', values)">
                  <el-option v-for="dict in dict.type.sse_product_set" :key="dict.value" :label="dict.label"
                             :value="dict.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="form.productSetList && form.productSetList.length > 0">
            <el-col :span="24">
              <el-card shadow="hover" style="margin-bottom: 20px;">
                <div slot="header" class="clearfix">
                  <span>产品所属SET比例配置</span>
                </div>
                <el-table :data="productSetRatios" border style="width: 100%;">
                  <el-table-column prop="label" label="产品所属SET" width="180">
                    <template slot-scope="scope">
                      {{ getDictLabel('sse_product_set', scope.row.value) }}
                    </template>
                  </el-table-column>

                  <el-table-column prop="quantity" label="产品配置数量">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.quantity" :min="0" :precision="0" @change="validateRatios"
                                       controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>

                  <el-table-column prop="ratio" label="配置比例(%)">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.ratio" :min="0" :max="100" :precision="2"
                                       @change="validateRatios" controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                </el-table>
                <div style="margin-top: 15px; text-align: left;">
                  <span :style="{ color: productSetRatioTotal === 100 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
                    比例总和: {{ productSetRatioTotal }}%
                    <span v-if="productSetRatioTotal !== 100" style="margin-left: 10px;">(比例总和必须等于100%)</span>
                  </span>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <el-divider content-position="left" @click="toggleCollapse('tradeInfoCollapsed')">交易信息
          <i class="el-icon-arrow-right" :class="{ 'rotate-icon': collapsed.tradeInfoCollapsed }"></i>
        </el-divider>
        <div v-if="!collapsed.tradeInfoCollapsed">
          <el-row :gutter="20" v-if="!collapsed.tradeInfoCollapsed">
            <el-col :span="24">
              <el-form-item label="撮合方式" prop="matchMethodList">
                <el-select v-model="form.matchMethodList" multiple placeholder="请选择撮合方式" style="width:100%"
                           @change="(values) => handleSelectChange('matchMethodList', values)">
                  <el-option v-for="dict in dict.type.sse_match_method" :key="dict.value" :label="dict.label"
                             :value="dict.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="form.matchMethodList && form.matchMethodList.length > 0">
            <el-col :span="24">
              <el-card shadow="hover" style="margin-bottom: 20px;">
                <div slot="header" class="clearfix">
                  <span>撮合方式比例配置</span>
                </div>
                <el-table :data="matchMethodRatios" border style="width: 100%;">
                  <el-table-column prop="label" label="撮合方式" width="180">
                    <template slot-scope="scope">
                      {{ getDictLabel('sse_match_method', scope.row.value) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="ratio" label="配置比例(%)">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.ratio" :min="0" :max="100" :precision="2"
                                       @change="validateRatios" controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                </el-table>
                <div style="margin-top: 15px; text-align: left;">
                  <span :style="{ color: matchMethodRatioTotal === 100 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
                    比例总和: {{ matchMethodRatioTotal }}%
                    <span v-if="matchMethodRatioTotal !== 100" style="margin-left: 10px;">(比例总和必须等于100%)</span>
                  </span>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="1撮多" prop="oneToManyConfig">
                <el-card shadow="hover">
                  <div style="margin-bottom: 10px;">
                    <el-button size="small" type="primary" icon="el-icon-plus" @click="addOneToManyConfig">
                      新增配置
                    </el-button>
                  </div>
                  <el-table :data="form.oneToManyList" border style="width: 100%;">
                    <el-table-column type="index" label="序号" width="80"></el-table-column>
                    <el-table-column label="1撮多配置(1撮1不用配置)" width="220">
                      <template slot-scope="scope">
                        <div style="display: flex; align-items: center; gap: 10px;">
                          <span>1 撮</span>
                          <el-input-number v-model="scope.row.oneToManyCount" :min="1"

                                           :precision="0"
                                           placeholder="请输入1撮几" style="width: 150px;"/>
                        </div>
                      </template>
                    </el-table-column>
                    <el-table-column label="配置数量" width="220">
                      <template slot-scope="scope">
                        <el-input-number v-model="scope.row.quantity" :min="1" :precision="0"
                                         placeholder="请输入数量" style="width: 180px;"/>
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" width="80">
                      <template slot-scope="scope">
                        <el-button size="mini" type="danger" icon="el-icon-delete"
                                   @click="removeOneToManyConfig(scope.$index)">
                        </el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-card>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="账户类型" prop="accountTypeList">
                <el-select v-model="form.accountTypeList" multiple placeholder="请选择账户类型" style="width:100%"
                           @change="(values) => handleSelectChange('accountTypeList', values)">
                  <el-option v-for="dict in dict.type.sse_account_type" :key="dict.value" :label="dict.label"
                             :value="dict.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="form.accountTypeList && form.accountTypeList.length > 0">
            <el-col :span="24">
              <el-card shadow="hover" style="margin-bottom: 20px;">
                <div slot="header" class="clearfix">
                  <span>账户类型比例配置</span>
                </div>
                <el-table :data="accountTypeRatios" border style="width: 100%;">
                  <el-table-column prop="label" label="账户类型" width="180">
                    <template slot-scope="scope">
                      {{ getDictLabel('sse_account_type', scope.row.value) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="ratio" label="配置比例(%)">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.ratio" :min="0" :max="100" :precision="2"
                                       @change="validateRatios" controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                </el-table>
                <div v-for="(accountTypeItem, index) in accountTypeRatios" :key="accountTypeItem.value"
                     style="margin-top: 20px;">
                  <div style="background-color: #f5f7fa; padding: 10px; border-radius: 4px;">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                      <span style="font-weight: bold;">{{ getDictLabel('sse_account_type', accountTypeItem.value) }}
                        详细配置</span>
                      <el-button size="mini" type="primary" icon="el-icon-plus"
                                 @click="addAccountTypeDetail(accountTypeItem.value)">新增配置项
                      </el-button>
                    </div>

                    <el-table :data="getAccountTypeDetails(accountTypeItem.value)" border
                              style="width: 100%; margin-top: 10px;">
                      <el-table-column type="index" label="序号" width="60"></el-table-column>
                      <el-table-column label="业务PBU个数">
                        <template slot-scope="scope">
                          <el-input-number v-model="scope.row.pbuCount" :min="0" :precision="0" @change="validateRatios"
                                           controls-position="right">
                          </el-input-number>
                        </template>
                      </el-table-column>
                      <el-table-column label="每个PBU申报账户数"
                                       v-if="!form.totalAccountCount || form.totalAccountCount === 0">
                        <template slot-scope="scope">
                          <el-input-number v-model="scope.row.accountsPerPbu" :min="0" :precision="0"
                                           @change="validateRatios" controls-position="right">
                          </el-input-number>
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="80">
                        <template slot-scope="scope">
                          <el-button size="mini" type="danger" icon="el-icon-delete"
                                     @click="removeAccountTypeDetail(accountTypeItem.value, scope.$index)">
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
                <div style="margin-top: 15px; text-align: left;">
                  <span :style="{ color: accountTypeRatioTotal === 100 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
                    比例总和: {{ accountTypeRatioTotal }}%
                    <span v-if="accountTypeRatioTotal !== 100" style="margin-left: 10px;">(比例总和必须等于100%)</span>
                  </span>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="申报账户总量" prop="totalAccountCount" v-if="!hasAnyDetailConfig()">
                <el-input-number v-model="form.totalAccountCount" :min="0" :precision="0"
                                 placeholder="请输入申报账户总量" style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="价格档位" prop="priceLevelList">
                <el-select v-model="form.priceLevelList" multiple placeholder="请选择价格档位" style="width:100%"
                           @change="(values) => handleSelectChange('priceLevelList', values)">
                  <el-option v-for="dict in dict.type.sse_price_level" :key="dict.value" :label="dict.label"
                             :value="dict.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20" v-if="form.priceLevelList && form.priceLevelList.length > 0">
            <el-col :span="24">
              <el-card shadow="hover" style="margin-bottom: 20px;">
                <div slot="header" class="clearfix">
                  <span>价格档位比例配置</span>
                </div>
                <el-table :data="priceLevelRatios" border style="width: 100%;">
                  <el-table-column prop="label" label="价格档位" width="180">
                    <template slot-scope="scope">
                      {{ getDictLabel('sse_price_level', scope.row.value) }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="ratio" label="配置比例(%)">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.ratio" :min="0" :max="100" :precision="2"
                                       @change="validateRatios" controls-position="right"></el-input-number>
                    </template>
                  </el-table-column>
                </el-table>
                <div style="margin-top: 15px; text-align: left;">
                  <span :style="{ color: priceLevelRatioTotal === 100 ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">
                    比例总和: {{ priceLevelRatioTotal }}%
                    <span v-if="priceLevelRatioTotal !== 100" style="margin-left: 10px;">(比例总和必须等于100%)</span>
                  </span>
                </div>
              </el-card>
            </el-col>
          </el-row>


          <!-- <el-divider content-position="left">系统信息</el-divider>
        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="性能场景" prop="performanceId">
              <el-input v-model="form.performanceId" placeholder="请输入性能场景" />
            </el-form-item>
          </el-col>
        </el-row>
         -->
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="申报数量" prop="orderQuantity">
                <el-input-number v-model="form.orderQuantity" :min="1" :precision="0" placeholder="请输入申报数量"
                                 style="width: 100%"/>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                  <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{
                      dict.label
                    }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item style="text-align: center; margin-top: 30px;">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="goBack">取 消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {addRuleinfo, getRuleinfo, updateRuleinfo} from "@/api/sse/ruleinfo"
import {listServerInfo} from "@/api/sse/serverInfo"

export default {
  name: "RuleinfoAdd",
  dicts: ['sse_security_sub_category', 'sse_match_method', 'sse_product_set', 'sys_del_flag', 'sse_account_type', 'sse_price_level', 'sys_normal_disable', 'sse_business_type', 'sse_trade_type'],
  data() {
    return {
      isCopy: false,
      collapsed: {
        basicInfoCollapsed: false,
        productInfoCollapsed: false,
        tradeInfoCollapsed: false,
      },
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
      form: {
        pkId: null,
        ruleName: null,
        ruleEnName: null,
        tradeType: null,
        tradeTypeList: [],
        tradeTypeRatios: {},
        tradeRatios: null,
        businessType: null,
        businessTypeList: [],
        businessTypeRatios: {},
        securitySubCategoryList: [],
        securitySubCategoryRatios: {},
        matchMethodList: [],
        matchMethodRatios: {},
        priceLevelList: [],
        priceLevelRatios: {},
        accountTypeList: [],
        accountTypeRatios: {},
        accountTypeDetails: {},
        securitySubCategory: null,
        productSetList: [],
        productSetRatios: {},
        matchMethod: null,
        accountType: null,
        priceLevel: null,
        orderQuantity: null,
        tradeRatio: null,
        pbuCount: null,
        bidHost: '',
        loginPbu: null,
        businessPbu: null,
        performanceId: null,
        status: null,
        remark: null,
        subscribePbuCount: null,
        totalSubscribedPbuCount: 2000,
        subscribeReportRatio: 30,
        totalAccountCount: null,
        orderCount: null,
        oneToManyList: []
      },
      selectedIds: [],
      serverInfoList: [], // 服务器列表数据
      serverListLoading: false, // 加载状态
      // 表单校验
      rules: {
        ruleName: [
          {required: true, message: "规则名称不能为空", trigger: "blur"}
        ],
        orderCount: [
          {required: true, message: "订单总数不能为空", trigger: "blur"}
        ],
      }
    }

  },
  rules: {
    // bidHost: [
    //   {
    //     type: 'array',
    //     required: true,
    //     message: '请至少选择一个报盘主机',
    //     trigger: 'change'
    //   }
    // ]
  },
  created() {
    this.loadServerInfoList();
    console.log(this.$route.params.id)
    const pkId = this.$route.params.id
    // Convert string to boolean
    const isCopy = this.$route.query.isCopy === 'true'
    console.log('Is copy? ', isCopy)

    if (pkId !== undefined && pkId != 0) {
      this.resetForm()
      getRuleinfo(pkId).then(response => {
        this.form = response.data

        if (isCopy) {
          this.isCopy = true;
          this.form.pkId = null
          this.form.status = 0
          this.form.ruleEnName = null
          this.form.ruleEnName = null
        }

        if (response.data.bidHost) {
          // 按逗号拆分（与保存时的分隔符一致）
          this.selectedIds = response.data.bidHost.split(',');
        }

        if (response.data.tradeType) {
          const parsedTradeRatios = JSON.parse(response.data.tradeType)
          this.form.tradeTypeRatios = {}

          // Handle both old format (just ratios) and new format (ratio + quantity)
          for (const [key, value] of Object.entries(parsedTradeRatios)) {
            if (typeof value === 'object' && value !== null) {
              // New format with ratio and quantity
              this.form.tradeTypeRatios[key] = {
                ratio: value.ratio || 0,
                quantity: value.quantity || 0
              }
            } else {
              // Old format with just ratios
              this.form.tradeTypeRatios[key] = {
                ratio: value,
                quantity: 0
              }
            }
          }
          // 使用 $set 确保响应式
          this.$set(this.form, 'tradeTypeRatios', this.form.tradeTypeRatios);
          this.$set(this.form, 'tradeTypeList', Object.keys(this.form.tradeTypeRatios))
        }

        if (response.data.businessType) {
          const parsedBusinessTypeRatios = JSON.parse(response.data.businessType)
          this.form.businessTypeRatios = {}
          for (const [key, value] of Object.entries(parsedBusinessTypeRatios)) {
            if (typeof value === 'object' && value !== null) {
              // New format with ratio and quantity
              this.form.businessTypeRatios[key] = {
                ratio: value.ratio || 0,
                quantity: value.quantity || 0
              }
            } else {
              // Old format with just ratios
              this.form.businessTypeRatios[key] = {
                ratio: value,
                quantity: 0
              }
            }
          }
          // 使用 $set 确保响应式
          this.$set(this.form, 'businessTypeRatios', this.form.businessTypeRatios);
          this.$set(this.form, 'businessTypeList', Object.keys(this.form.businessTypeRatios))

        }

        if (response.data.securitySubCategory) {
          const parsedSecuritySubCategoryRatios = JSON.parse(response.data.securitySubCategory)
          this.form.securitySubCategoryRatios = {}
          for (const [key, value] of Object.entries(parsedSecuritySubCategoryRatios)) {
            if (typeof value === 'object' && value !== null) {
              // New format with ratio and quantity
              this.form.securitySubCategoryRatios[key] = {
                ratio: value.ratio || 0,
                quantity: value.quantity || 0
              }
            } else {
              // Old format with just ratios
              this.form.securitySubCategoryRatios[key] = {
                ratio: value,
                quantity: 0
              }
            }
          }
          this.$set(this.form, 'securitySubCategoryRatios', this.form.securitySubCategoryRatios);
          this.$set(this.form, 'securitySubCategoryList', Object.keys(this.form.securitySubCategoryRatios));
        }

        if (response.data.productSet) {
          const parsedProductSetRatios = JSON.parse(response.data.productSet)
          this.form.productSetRatios = {}
          for (const [key, value] of Object.entries(parsedProductSetRatios)) {
            if (typeof value === 'object' && value !== null) {
              // New format with ratio and quantity
              this.form.productSetRatios[key] = {
                ratio: value.ratio || 0,
                quantity: value.quantity || 0
              }
            } else {
              // Old format with just ratios
              this.form.productSetRatios[key] = {
                ratio: value,
                quantity: 0
              }
            }
          }
          this.$set(this.form, 'productSetRatios', this.form.productSetRatios);
          this.$set(this.form, 'productSetList', Object.keys(this.form.productSetRatios));
        }

        if (response.data.matchMethod) {
          this.form.matchMethodRatios = JSON.parse(response.data.matchMethod)
          this.$set(this.form, 'matchMethodRatios', this.form.matchMethodRatios);
          this.$set(this.form, 'matchMethodList', Object.keys(this.form.matchMethodRatios));

        }

        if (response.data.accountType) {
          const parsedAccountTypeRatios = JSON.parse(response.data.accountType)
          this.form.accountTypeRatios = {}
          this.form.accountTypeDetails = {}

          for (const [key, value] of Object.entries(parsedAccountTypeRatios)) {
            if (typeof value === 'object' && value !== null) {
              // New format with ratio and details
              this.form.accountTypeRatios[key] = value.ratio || 0
              // Handle details if they exist
              if (value.details && Array.isArray(value.details)) {
                this.form.accountTypeDetails[key] = value.details.map(detail => ({
                  pbuCount: detail.pbuCount || 0,
                  accountsPerPbu: detail.accountsPerPbu || 0
                }))
              } else {
                // Default empty array for details
                this.form.accountTypeDetails[key] = [{pbuCount: 0, accountsPerPbu: 0}]
              }
            } else {
              // Old format with just ratios
              this.form.accountTypeRatios[key] = value
              this.form.accountTypeDetails[key] = [{pbuCount: 0, accountsPerPbu: 0}]
            }
          }

          this.$set(this.form, 'accountTypeRatios', this.form.accountTypeRatios);
          this.$set(this.form, 'accountTypeDetails', this.form.accountTypeDetails);
          this.$set(this.form, 'accountTypeList', Object.keys(this.form.accountTypeRatios));
        }

        if (response.data.priceLevel) {
          this.form.priceLevelRatios = JSON.parse(response.data.priceLevel)
          this.$set(this.form, 'priceLevelRatios', this.form.priceLevelRatios);
          this.$set(this.form, 'priceLevelList', Object.keys(this.form.priceLevelRatios));
        }

        // 解析一撮多配置
        if (response.data.oneToManyList) {
          try {
            // 如果已经是数组，直接使用；如果是字符串，解析 JSON
            if (typeof response.data.oneToManyList === 'string') {
              this.form.oneToManyList = JSON.parse(response.data.oneToManyList);
            } else {
              this.form.oneToManyList = response.data.oneToManyList;
            }
          } catch (e) {
            console.error('解析一撮多配置失败:', e);
            this.form.oneToManyList = [];
          }
        }

        // 在数据加载完成后，使用 $nextTick 确保DOM更新完成后再强制刷新
        this.$nextTick(() => {
          // 重新设置所有列表字段以确保响应式
          const listFields = ['tradeTypeList', 'businessTypeList', 'securitySubCategoryList',
            'productSetList', 'matchMethodList', 'accountTypeList', 'priceLevelList'];

          listFields.forEach(field => {
            if (this.form[field]) {
              this.$set(this.form, field, [...this.form[field]]);
            }
          });

          // 重新设置所有ratio对象以确保响应式
          const ratioFields = ['tradeTypeRatios', 'businessTypeRatios', 'securitySubCategoryRatios',
            'productSetRatios', 'matchMethodRatios', 'accountTypeRatios', 'priceLevelRatios'];

          ratioFields.forEach(field => {
            if (this.form[field]) {
              this.$set(this.form, field, {...this.form[field]});
            }
          });

          // 特殊处理 accountTypeDetails
          if (this.form.accountTypeDetails) {
            this.$set(this.form, 'accountTypeDetails', {...this.form.accountTypeDetails});
          }

          this.$forceUpdate();
        });


      })
      // 如果是复制模式，强制触发一次验证以更新计算属性
      if (isCopy) {
        this.$nextTick(() => {
          // 强制更新计算属性
          this.$forceUpdate();
          // 触发验证更新
          this.validateRatios();
        });
      }
    } else {
      // 确保在新增模式下正确重置表单
      this.$nextTick(() => {
        this.resetForm();
      });
    }
  },


  computed: {
    tradeTypeRatios() {
      // 确保包含所有字典中的交易类型
      const allTradeTypes = this.dict.type.sse_trade_type || [];
      return allTradeTypes
        .filter(dict => this.form.tradeTypeList.includes(dict.value))
        .map(dict => ({
          value: dict.value,
          ratio: (this.form.tradeTypeRatios[dict.value]?.ratio || 0),
          quantity: (this.form.tradeTypeRatios[dict.value]?.quantity || 0)
        }));
    },
    businessTypeRatios() {
      return this.form.businessTypeList.map(value => ({
        value,
        ratio: this.form.businessTypeRatios[value] ? this.form.businessTypeRatios[value].ratio : 0,
        quantity: this.form.businessTypeRatios[value] ? this.form.businessTypeRatios[value].quantity : 0
      }))
    },
    ratioTotal() {
      return this.tradeTypeRatios.reduce((sum, item) => sum + (item.ratio || 0), 0)
    },
    businessTypeRatioTotal() {
      return this.businessTypeRatios.reduce((sum, item) => sum + (item.ratio || 0), 0)
    },
    securitySubCategoryRatios() {
      return this.form.securitySubCategoryList.map(value => ({
        value,
        ratio: (this.form.securitySubCategoryRatios[value] && this.form.securitySubCategoryRatios[value].ratio) || 0,
        quantity: (this.form.securitySubCategoryRatios[value] && this.form.securitySubCategoryRatios[value].quantity) || 0
      }))
    },
    securitySubCategoryRatioTotal() {
      return this.securitySubCategoryRatios.reduce((sum, item) => sum + (item.ratio || 0), 0)
    },
    matchMethodRatios() {
      return this.form.matchMethodList.map(value => ({
        value,
        ratio: this.form.matchMethodRatios[value] || 0
      }))
    },
    matchMethodRatioTotal() {
      return this.matchMethodRatios.reduce((sum, item) => sum + (item.ratio || 0), 0)
    },
    priceLevelRatios() {
      return this.form.priceLevelList.map(value => ({
        value,
        ratio: this.form.priceLevelRatios[value] || 0
      }))
    },
    priceLevelRatioTotal() {
      return this.priceLevelRatios.reduce((sum, item) => sum + (item.ratio || 0), 0)
    },
    accountTypeRatios() {
      return this.form.accountTypeList.map(value => ({
        value,
        ratio: this.form.accountTypeRatios[value] || 0
      }))
    },

    accountTypeRatioTotal() {
      return this.accountTypeRatios.reduce((sum, item) => sum + (item.ratio || 0), 0)
    },

    accountTypeDetails() {
      return this.form.accountTypeDetails || {}
    },

    productSetRatios() {
      return this.form.productSetList.map(value => ({
        value,
        ratio: (this.form.productSetRatios[value] && this.form.productSetRatios[value].ratio) || 0,
        quantity: (this.form.productSetRatios[value] && this.form.productSetRatios[value].quantity) || 0
      }))
    },
    productSetRatioTotal() {
      return this.productSetRatios.reduce((sum, item) => sum + (item.ratio || 0), 0)
    },
  },


  methods: {
    toggleCollapse(key) {
      this.collapsed[key] = !this.collapsed[key];
    },
    async loadServerInfoList() {
      this.serverListLoading = true;
      try {
        // 调用serverInfo.js中的listServerInfo方法，可传入查询参数（此处为空）
        listServerInfo(this.queryParams).then(response => {
          this.serverInfoList = response.rows
          this.total = response.total
          this.loading = false
        })

        //   // 假设接口返回格式为 { code: 200, data: [...] }
        //   if (response.code === 200) {
        //     this.serverInfoList = response.data;
        //   } else {
        //     this.$message.error(`获取服务器列表失败: ${response.msg || '未知错误'}`);
        //     this.serverInfoList = [];
        //   }
      } catch (error) {
        console.error('加载服务器列表出错:', error);
        this.$message.error('网络异常，无法获取服务器列表');
        this.serverInfoList = [];
      } finally {
        this.serverListLoading = false;
      }
    },

    getDictLabel(dictType, value) {
      const dict = this.dict.type[dictType] || []
      const item = dict.find(item => item.value === value)
      return item ? item.label : ''
    },

    // 添加一个刷新方法，用于复制模式下强制刷新数据
    refreshComputedProperties() {
      // 通过重新赋值触发计算属性更新
      this.form = {...this.form};
      this.$forceUpdate();
    },

    // 添加一个方法来处理选择变化，确保所有选项都可以被选择
    handleSelectChange(field, values) {
      console.log('handleSelectChange called:', field, values);
      // // 深度比较检查值是否真的发生变化
      // const oldValues = this.form[field] || [];
      // const hasChanged = JSON.stringify(oldValues) !== JSON.stringify(values);
      // console.log('Values have changed:', hasChanged);

      // if (!hasChanged) {
      //   console.log('No actual change in values, but event fired');
      //   return;
      // }

      // 更新表单字段
      this.$set(this.form, field, values);
      console.log('form field updated:', this.form[field]);
      // 根据字段名更新相应的ratios对象
      switch (field) {
        case 'tradeTypeList':
          console.log('处理tradeTypeList，处理前:', JSON.stringify(this.form.tradeTypeRatios));

          // 确保tradeTypeRatios中包含所有已选择的项目
          values.forEach(value => {
            if (!this.form.tradeTypeRatios[value]) {
              this.$set(this.form.tradeTypeRatios, value, {
                ratio: 0,
                quantity: 0
              });
            }
          });
          // 移除不在选择列表中的项目
          Object.keys(this.form.tradeTypeRatios).forEach(key => {
            if (!values.includes(key)) {
              this.$delete(this.form.tradeTypeRatios[key]);
            }
          });
          this.$set(this.form, 'tradeTypeRatios', {...this.form.tradeTypeRatios});
          console.log('处理tradeTypeList，处理后:', JSON.stringify(this.form.tradeTypeRatios));

          break;

        case 'businessTypeList':
          console.log('处理businessTypeList，处理前:', JSON.stringify(this.form.businessTypeRatios));
          console.log('当前 businessTypeRatios:', JSON.stringify(this.form.businessTypeRatios, null, 2));

          // 确保businessTypeRatios中包含所有已选择的项目
          values.forEach(value => {
            if (!this.form.businessTypeRatios[value]) {
              this.$set(this.form.businessTypeRatios, value, {
                ratio: 0,
                quantity: 0
              });
            }
          });
          Object.keys(this.form.businessTypeRatios).forEach(key => {
            if (!values.includes(key)) {
              this.$delete(this.form.businessTypeRatios[key]);
            }
          });
          console.log('处理businessTypeList，处理后:', JSON.stringify(this.form.businessTypeRatios));
          console.log('更新后的 businessTypeRatios:', JSON.stringify(this.form.businessTypeRatios, null, 2));
          this.$set(this.form, 'businessTypeRatios', {...this.form.businessTypeRatios});

          break;

        case 'securitySubCategoryList':
          console.log('处理securitySubCategoryList，处理前:', JSON.stringify(this.form.securitySubCategoryRatios));

          // 确保securitySubCategoryRatios中包含所有已选择的项目
          values.forEach(value => {
            if (!this.form.securitySubCategoryRatios[value]) {
              this.$set(this.form.securitySubCategoryRatios, value, {
                ratio: 0,
                quantity: 0
              });
            }
          });
          // 移除不在选择列表中的项目
          Object.keys(this.form.securitySubCategoryRatios).forEach(key => {
            if (!values.includes(key)) {
              this.$delete(this.form.securitySubCategoryRatios[key]);
            }
          });
          this.$set(this.form, 'securitySubCategoryRatios', {...this.form.securitySubCategoryRatios});
          console.log('处理securitySubCategoryList，处理后:', JSON.stringify(this.form.securitySubCategoryRatios));
          break;

        case 'productSetList':
          // 确保productSetRatios中包含所有已选择的项目
          values.forEach(value => {
            if (!this.form.productSetRatios[value]) {
              this.form.productSetRatios[value] = {
                ratio: 0,
                quantity: 0
              };
            }
          });
          // 移除不在选择列表中的项目
          Object.keys(this.form.productSetRatios).forEach(key => {
            if (!values.includes(key)) {
              this.$delete(this.form.productSetRatios[key]);
            }
          });
          this.$set(this.form, 'productSetRatios', {...this.form.productSetRatios});

          break;

        case 'matchMethodList':
          // 确保matchMethodRatios中包含所有已选择的项目
          values.forEach(value => {
            if (!this.form.matchMethodRatios[value]) {
              this.form.matchMethodRatios[value] = 0;
            }
          });
          // 移除不在选择列表中的项目
          Object.keys(this.form.matchMethodRatios).forEach(key => {
            if (!values.includes(key)) {
              this.$delete(this.form.matchMethodRatios[key]);
            }
          });

          this.$set(this.form, 'matchMethodRatios', {...this.form.matchMethodRatios});

          break;

        case 'accountTypeList':
          // 确保accountTypeRatios中包含所有已选择的项目
          values.forEach(value => {
            if (!this.form.accountTypeRatios[value]) {
              this.form.accountTypeRatios[value] = 0;
            }

            // 确保accountTypeDetails中包含所有已选择的项目
            if (!this.form.accountTypeDetails[value]) {
              this.form.accountTypeDetails[value] = [{pbuCount: 0, accountsPerPbu: 0}];
            }
          });
          // 移除不在选择列表中的项目，并清空其配置
          Object.keys(this.form.accountTypeRatios).forEach(key => {
            if (!values.includes(key)) {
              // 清空详细配置
              if (this.form.accountTypeDetails[key]) {
                this.form.accountTypeDetails[key] = [{pbuCount: 0, accountsPerPbu: 0}];
              }
              // 清空比例配置
              this.$delete(this.form.accountTypeRatios, key);
              // 删除详细配置
              this.$delete(this.form.accountTypeDetails, key);
            }
          });
          this.$set(this.form, 'accountTypeRatios', {...this.form.accountTypeRatios});
          this.$set(this.form, 'accountTypeDetails', {...this.form.accountTypeDetails});
          break;

        case 'priceLevelList':
          // 确保priceLevelRatios中包含所有已选择的项目
          values.forEach(value => {
            if (!this.form.priceLevelRatios[value]) {
              this.$set(this.form.priceLevelRatios[value] = 0);
            }
          });
          // 移除不在选择列表中的项目
          Object.keys(this.form.priceLevelRatios).forEach(key => {
            if (!values.includes(key)) {
              this.$delete(this.form.priceLevelRatios[key]);
            }
          });
          this.$set(this.form, 'priceLevelRatios', {...this.form.priceLevelRatios});

          break;
      }
      // 触发验证更新
      // 触发计算属性更新
      this.$nextTick(() => {
        this.validateRatios(); // 调用已有方法更新数据
        // 强制更新视图
        this.$forceUpdate();
      });
    },

    validateRatios() {
      // 更新交易类型比例数据
      const newTradeTypeRatios = this.tradeTypeRatios.reduce((obj, item) => {
        obj[item.value] = {
          ratio: item.ratio,
          quantity: item.quantity
        }
        return obj
      }, {})
      this.$set(this.form, 'tradeTypeRatios', newTradeTypeRatios);
      console.log('tradeTypeRatios updated:', JSON.stringify(this.form.tradeTypeRatios));

      // 更新业务类型比例数据
      const newBusinessTypeRatios = this.businessTypeRatios.reduce((obj, item) => {
        obj[item.value] = {
          ratio: item.ratio,
          quantity: item.quantity
        }
        return obj
      }, {})
      this.$set(this.form, 'businessTypeRatios', newBusinessTypeRatios);
      console.log('businessTypeRatios updated:', JSON.stringify(this.form.businessTypeRatios));

      // 更新证券子类别比例数据
      const newSecuritySubCategoryRatios = this.securitySubCategoryRatios.reduce((obj, item) => {
        obj[item.value] = {
          ratio: item.ratio,
          quantity: item.quantity
        }
        return obj
      }, {})
      this.$set(this.form, 'securitySubCategoryRatios', newSecuritySubCategoryRatios);
      console.log('securitySubCategoryRatios updated:', JSON.stringify(this.form.securitySubCategoryRatios));

      // 更新撮合方式比例数据
      const newMatchMethodRatios = this.matchMethodRatios.reduce((obj, item) => {
        obj[item.value] = item.ratio
        return obj
      }, {})
      this.$set(this.form, 'matchMethodRatios', newMatchMethodRatios);
      console.log('matchMethodRatios updated:', JSON.stringify(this.form.matchMethodRatios));

      // 更新价格档位比例数据
      const newPriceLevelRatios = this.priceLevelRatios.reduce((obj, item) => {
        obj[item.value] = item.ratio
        return obj
      }, {})
      this.$set(this.form, 'priceLevelRatios', newPriceLevelRatios);
      console.log('priceLevelRatios updated:', JSON.stringify(this.form.priceLevelRatios));

      // 更新账户类型比例数据
      const newAccountTypeRatios = this.accountTypeRatios.reduce((obj, item) => {
        obj[item.value] = item.ratio
        return obj
      }, {})
      this.$set(this.form, 'accountTypeRatios', newAccountTypeRatios);
      console.log('accountTypeRatios updated:', JSON.stringify(this.form.accountTypeRatios));

      // 更新SET集合比例数据
      const newProductSetRatios = this.productSetRatios.reduce((obj, item) => {
        obj[item.value] = {
          ratio: item.ratio,
          quantity: item.quantity
        }
        return obj
      }, {})
      this.$set(this.form, 'productSetRatios', newProductSetRatios);
      console.log('productSetRatios updated:', JSON.stringify(this.form.productSetRatios));
    },


    getAccountTypeDetails(accountTypeValue) {
      if (!this.form.accountTypeDetails) {
        this.form.accountTypeDetails = {}
      }

      if (!this.form.accountTypeDetails[accountTypeValue]) {
        this.form.accountTypeDetails[accountTypeValue] = [{pbuCount: 0, accountsPerPbu: 0}]
      }

      return this.form.accountTypeDetails[accountTypeValue]
    },

    addAccountTypeDetail(accountTypeValue) {
      if (!this.form.accountTypeDetails) {
        this.form.accountTypeDetails = {}
      }

      if (!this.form.accountTypeDetails[accountTypeValue]) {
        this.form.accountTypeDetails[accountTypeValue] = []
      }

      this.form.accountTypeDetails[accountTypeValue].push({
        pbuCount: 0,
        accountsPerPbu: 0
      })

      this.validateRatios()
    },

    removeAccountTypeDetail(accountTypeValue, index) {
      if (this.form.accountTypeDetails[accountTypeValue] &&
        this.form.accountTypeDetails[accountTypeValue].length > 1) {
        this.form.accountTypeDetails[accountTypeValue].splice(index, 1)
        this.validateRatios()
      } else {
        this.$message.warning('至少保留一个配置项')
      }
    },

    // 添加1撮多配置
    addOneToManyConfig() {
      if (!this.form.oneToManyList) {
        this.form.oneToManyList = []
      }
      this.form.oneToManyList.push({
        oneToManyCount: 1,
        quantity: 0
      })
    },

    // 删除1撮多配置
    removeOneToManyConfig(index) {
      this.form.oneToManyList.splice(index, 1)
    },

    prepareAccountTypeData() {
      const result = {}
      for (const [key, ratio] of Object.entries(this.form.accountTypeRatios)) {
        result[key] = {
          ratio: ratio,
          details: this.form.accountTypeDetails && this.form.accountTypeDetails[key]
            ? this.form.accountTypeDetails[key]
            : [{pbuCount: 0, accountsPerPbu: 0}]
        }
      }
      return result
    },


    /** 提交按钮 */
    submitForm() {
      this.form.bidHost = this.selectedIds.join(',');

      // 验证1撮多配置
      if (this.form.oneToManyList && this.form.oneToManyList.length > 0) {
        const oneToManyCounts = new Map();
        let hasRatio = false;
        let totalRatio = 0;

        for (const config of this.form.oneToManyList) {
          // 检查是否有1撮1配置
          if (config.oneToManyCount === 1) {
            this.$modal.msgError('1撮多配置中不用配置1撮1');
            return;
          }
          // 检查1撮几是否重复
          if (oneToManyCounts.has(config.oneToManyCount)) {
            this.$modal.msgError('1撮' + config.oneToManyCount + '重复了');
            return;
          }
          oneToManyCounts.set(config.oneToManyCount, true);
        }


        // 计算理论成交笔数
        // 从撮合方式比例配置中获取废单比率
        const mainBoardCancelRatio = (this.form.matchMethodRatios['03'] || 0) / 100;
        const starMarketCancelRatio = (this.form.matchMethodRatios['07'] || 0) / 100;
        const tradeRatio = (this.form.tradeRatio || 0) / 100;
        const orderCount = this.form.orderCount || 0;

        const theoreticalTradeCount = orderCount * (1 - mainBoardCancelRatio - starMarketCancelRatio) * tradeRatio;

        // 计算1撮多配置的总订单数和总成交笔数
        let totalOneToManyOrderCount = 0;
        let totalOneToManyTradeCount = 0;

        for (const config of this.form.oneToManyList) {
          const n = config.oneToManyCount;
          const quantity = config.quantity || 0;

          // 1撮 N: 订单数 = (N+1) * quantity, 成交笔数 = (2*N) * quantity
          const orderCountForThisConfig = (n + 1) * quantity;
          const tradeCountForThisConfig = (2 * n) * quantity;

          totalOneToManyOrderCount += orderCountForThisConfig;
          totalOneToManyTradeCount += tradeCountForThisConfig;
        }
        console.log('主板废单:', mainBoardCancelRatio);
        console.log('科创板废单:', starMarketCancelRatio);

        console.log('理论成交笔数:', theoreticalTradeCount);
        console.log('1撮多总成交笔数:', totalOneToManyTradeCount);
        console.log('1撮多总订单数:', totalOneToManyOrderCount);

        // 比较并处理
        if (totalOneToManyTradeCount > theoreticalTradeCount) {
          this.$modal.msgError('成交比不匹配：1撮多配置的总成交笔数 (' + totalOneToManyTradeCount.toFixed(2) + ') 大于理论成交笔数 (' + theoreticalTradeCount.toFixed(2) + ')');
          return;
        }
        // else if (totalOneToManyTradeCount < theoreticalTradeCount) {
        //   // 计算差额
        //   const tradeCountDiff = theoreticalTradeCount - totalOneToManyTradeCount;
        //
        //   // 需要补充的1撮1数量 (每笔1撮1产生 2 笔成交)
        //   const neededYiChuoYiQuantity = Math.ceil(tradeCountDiff / 2);
        //
        //   // 检查是否已有1撮1配置
        //   let yiChuoYiConfig = this.form.oneToManyList.find(config => config.oneToManyCount === 1);
        //
        //   if (yiChuoYiConfig) {
        //     // 已有1撮1，累加数量
        //     yiChuoYiConfig.quantity += neededYiChuoYiQuantity;
        //   } else {
        //     // 没有1撮1，添加新配置
        //     this.form.oneToManyList.push({
        //       oneToManyCount: 1,
        //       quantity: neededYiChuoYiQuantity
        //     });
        //   }
        //
        //   this.$modal.msgWarning('已自动补充 ' + neededYiChuoYiQuantity + ' 笔1撮11配置，补足成交笔数差额');
        //
        //   // 重新计算验证
        //   let finalTradeCount = 0;
        //   for (const config of this.form.oneToManyList) {
        //     const n = config.oneToManyCount;
        //     const quantity = config.quantity || 0;
        //     finalTradeCount += (2 * n) * quantity;
        //   }
        //
        //   console.log('补充后总成交笔数:', finalTradeCount);
        // }
      }


      // 验证交易类型比例总和
      if (this.form.tradeTypeList.length > 0 && this.ratioTotal !== 100) {

        this.$modal.msgError('交易类型配置比例总和必须等于100%')
        return
      }

      // 验证业务类型比例总和
      if (this.form.businessTypeList.length > 0 && this.businessTypeRatioTotal !== 100) {
        this.$modal.msgError('业务类型配置比例总和必须等于100%')
        return
      }

      // 验证证券子类别比例总和
      if (this.form.securitySubCategoryList.length > 0 && this.securitySubCategoryRatioTotal !== 100) {
        this.$modal.msgError('证券子类别配置比例总和必须等于100%')
        return
      }

      // 验证账户类型比例总和
      if (this.form.accountTypeList.length > 0 && this.accountTypeRatioTotal !== 100) {
        this.$modal.msgError('账户类型配置比例总和必须等于100%')
        return
      }

      // 验证账户类型比例总和
      if (this.form.productSetList.length > 0 && this.productSetRatioTotal !== 100) {
        this.$modal.msgError('SET集合配置比例总和必须等于100%')
        return
      }


      this.$refs["form"].validate(valid => {
        if (valid) {
          // 准备提交数据
          const submitData = {
            ...this.form,
            tradeType: JSON.stringify(this.form.tradeTypeRatios),
            businessType: JSON.stringify(this.form.businessTypeRatios),
            securitySubCategory: JSON.stringify(this.form.securitySubCategoryRatios),
            matchMethod: JSON.stringify(this.form.matchMethodRatios),
            priceLevel: JSON.stringify(this.form.priceLevelRatios),
            accountType: JSON.stringify(this.prepareAccountTypeData()),
            productSet: JSON.stringify(this.form.productSetRatios),
            oneToManyList: JSON.stringify(this.form.oneToManyList),


          }

          if (this.form.pkId == null) {
            addRuleinfo(submitData).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.resetForm()
              this.goBack()
            })
          } else {

            updateRuleinfo(submitData).then(response => {
              this.resetForm()
              this.$modal.msgSuccess("修改成功")
              this.goBack()
            })
          }
        }

      })
    },

    //返回列表
    goBack() {
      console.log('goBack')
      this.$router.push('/ssedata/ruleinfo')

    },

    hasAnyDetailConfig() {

      for (const accountType in this.form.accountTypeDetails) {
        const details = this.form.accountTypeDetails[accountType]
        if (details && details.length > 0) {
          // 计算该账户类型的总申报账户数：Σ(pbuCount * accountsPerPbu)
          const totalAccounts = details.reduce((sum, detail) => {
            return sum + ((detail.pbuCount || 0) * (detail.accountsPerPbu || 0))
          }, 0)


          // 如果总和不为 0，说明有有效配置
          if (totalAccounts !== 0) {
            return true
          }
        }
      }
      return false
    },


    // 重置表单
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      // 重置数组和对象类型的字段
      this.form.pkId = null
      this.form.tradeTypeList = []
      this.form.tradeTypeRatios = {}
      this.form.businessTypeList = []
      this.form.businessTypeRatios = {}
      this.form.securitySubCategoryList = []
      this.form.securitySubCategoryRatios = {}
      this.form.matchMethodList = []
      this.form.matchMethodRatios = {}
      this.form.priceLevelList = []
      this.form.priceLevelRatios = {}
      this.form.accountTypeList = []
      this.form.accountTypeRatios = {}
      this.form.accountTypeDetails = {}
      this.form.productSetList = []
      this.form.productSetRatios = {}
      this.form.subscribePbuCount = null
      this.form.totalSubscribedPbuCount = 2000
      this.form.subscribeReportRatio = 30
      this.form.totalAccountCount = null
      this.form.orderCount = null
      this.form.oneToManyList = []
    },
  },

}
</script>

<style scoped>
.form-container {
  margin: 20px;
  padding: 20px;
}

/* 箭头图标样式 */
.el-icon-arrow-right {
  color: #6b7280;
  transition: transform 0.3s ease;
}

.rotate-icon {
  transform: rotate(90deg);
}

</style>

package com.shenmei.data.sse.dto;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;


/**
 * 规则配置前端对象
 * 
 * @author teabot
 * @date 2025-07-26
 */
public class SseDataRuleVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long pkId;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String ruleName;

    /** 规则英文名称 */
    @Excel(name = "规则英文名称")
    private String ruleEnName;

    /** 订单总数 */
    @Excel(name = "订单总数")
    private Long orderTotal;

    /** 产品交易类型 */
    @Excel(name = "产品交易类型")
    private String tradeType;

    /** 产品业务类型 */
    @Excel(name = "产品业务类型")
    private String businessType;

    /** 证券子类别 */
    @Excel(name = "证券子类别")
    private String securitySubCategory;

    /** 产品所属SET */
    @Excel(name = "产品所属SET")
    private String productSet;

    /** 撮合方式 */
    @Excel(name = "撮合方式")
    private String matchMethod;

    /** 账户类型 */
    @Excel(name = "账户类型")
    private String accountType;

    /** 价格档位 */
    @Excel(name = "价格档位")
    private String priceLevel;

    /** 申报数量 */
    @Excel(name = "申报数量")
    private Long orderQuantity;

    /** 成交比 */
    @Excel(name = "成交比")
    private BigDecimal tradeRatio;

    /** 业务PBU总数 */
    @Excel(name = "业务PBU总数")
    private Long pbuCount;

    /** 报盘主机 */
    @Excel(name = "报盘主机")
    private String bidHost;

    /** 登录pbu */
    @Excel(name = "登录pbu")
    private String loginPbu;

    /** 业务pbu */
    @Excel(name = "业务pbu")
    private String businessPbu;

    /** 性能场景 */
    @Excel(name = "性能场景")
    private String performanceId;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标志 */
    @Excel(name = "删除标志")
    private String delFlag;

    public void setPkId(Long pkId) 
    {
        this.pkId = pkId;
    }

    public Long getPkId() 
    {
        return pkId;
    }

    public void setRuleName(String ruleName) 
    {
        this.ruleName = ruleName;
    }

    public String getRuleName() 
    {
        return ruleName;
    }

    public void setRuleEnName(String ruleEnName) 
    {
        this.ruleEnName = ruleEnName;
    }

    public String getRuleEnName() 
    {
        return ruleEnName;
    }

    public void setOrderTotal(Long orderTotal) 
    {
        this.orderTotal = orderTotal;
    }

    public Long getOrderTotal() 
    {
        return orderTotal;
    }

    public void setTradeType(String tradeType) 
    {
        this.tradeType = tradeType;
    }

    public String getTradeType() 
    {
        return tradeType;
    }

    public void setBusinessType(String businessType) 
    {
        this.businessType = businessType;
    }

    public String getBusinessType() 
    {
        return businessType;
    }

    public void setSecuritySubCategory(String securitySubCategory) 
    {
        this.securitySubCategory = securitySubCategory;
    }

    public String getSecuritySubCategory() 
    {
        return securitySubCategory;
    }

    public void setProductSet(String productSet) 
    {
        this.productSet = productSet;
    }

    public String getProductSet() 
    {
        return productSet;
    }

    public void setMatchMethod(String matchMethod) 
    {
        this.matchMethod = matchMethod;
    }

    public String getMatchMethod() 
    {
        return matchMethod;
    }

    public void setAccountType(String accountType) 
    {
        this.accountType = accountType;
    }

    public String getAccountType() 
    {
        return accountType;
    }

    public void setPriceLevel(String priceLevel) 
    {
        this.priceLevel = priceLevel;
    }

    public String getPriceLevel() 
    {
        return priceLevel;
    }

    public void setOrderQuantity(Long orderQuantity) 
    {
        this.orderQuantity = orderQuantity;
    }

    public Long getOrderQuantity() 
    {
        return orderQuantity;
    }

    public void setTradeRatio(BigDecimal tradeRatio) 
    {
        this.tradeRatio = tradeRatio;
    }

    public BigDecimal getTradeRatio() 
    {
        return tradeRatio;
    }

    public void setPbuCount(Long pbuCount) 
    {
        this.pbuCount = pbuCount;
    }

    public Long getPbuCount() 
    {
        return pbuCount;
    }

    public void setBidHost(String bidHost) 
    {
        this.bidHost = bidHost;
    }

    public String getBidHost() 
    {
        return bidHost;
    }

    public void setLoginPbu(String loginPbu) 
    {
        this.loginPbu = loginPbu;
    }

    public String getLoginPbu() 
    {
        return loginPbu;
    }

    public void setBusinessPbu(String businessPbu) 
    {
        this.businessPbu = businessPbu;
    }

    public String getBusinessPbu() 
    {
        return businessPbu;
    }

    public void setPerformanceId(String performanceId) 
    {
        this.performanceId = performanceId;
    }

    public String getPerformanceId() 
    {
        return performanceId;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("pkId", getPkId())
            .append("ruleName", getRuleName())
            .append("ruleEnName", getRuleEnName())
            .append("orderTotal", getOrderTotal())
            .append("tradeType", getTradeType())
            .append("businessType", getBusinessType())
            .append("securitySubCategory", getSecuritySubCategory())
            .append("productSet", getProductSet())
            .append("matchMethod", getMatchMethod())
            .append("accountType", getAccountType())
            .append("priceLevel", getPriceLevel())
            .append("orderQuantity", getOrderQuantity())
            .append("tradeRatio", getTradeRatio())
            .append("pbuCount", getPbuCount())
            .append("bidHost", getBidHost())
            .append("loginPbu", getLoginPbu())
            .append("businessPbu", getBusinessPbu())
            .append("performanceId", getPerformanceId())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

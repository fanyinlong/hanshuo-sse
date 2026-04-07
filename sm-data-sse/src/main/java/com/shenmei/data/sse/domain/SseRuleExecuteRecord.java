package com.shenmei.data.sse.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 规则执行记录对象 sse_rule_execute_record
 * 
 * @author ruoyi
 * @date 2025-08-05
 */
public class SseRuleExecuteRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long pkId;

    /** 规则ID */
    @Excel(name = "规则ID")
    private Long ruleId;

    /** 订单数量 */
    @Excel(name = "订单数量")
    private Long orderNumber;

    /** 全量:0,增量:1 */
    @Excel(name = "数据类型")
    private String orderType;

    /** 执行状态 */
    @Excel(name = "执行状态")
    private String executeStatus;

    /** 执行人 */
    @Excel(name = "执行人")
    private String executeUser;

    /** 执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date executeTime;

    /** 执行版本 */
    @Excel(name = "执行版本")
    private String executeRuleVersion;

    public void setPkId(Long pkId) 
    {
        this.pkId = pkId;
    }

    public Long getPkId() 
    {
        return pkId;
    }

    public void setRuleId(Long ruleId) 
    {
        this.ruleId = ruleId;
    }

    public Long getRuleId() 
    {
        return ruleId;
    }

    public void setOrderNumber(Long orderNumber) 
    {
        this.orderNumber = orderNumber;
    }

    public Long getOrderNumber() 
    {
        return orderNumber;
    }

    public void setOrderType(String orderType) 
    {
        this.orderType = orderType;
    }

    public String getOrderType() 
    {
        return orderType;
    }

    public void setExecuteStatus(String executeStatus) 
    {
        this.executeStatus = executeStatus;
    }

    public String getExecuteStatus() 
    {
        return executeStatus;
    }

    public void setExecuteUser(String executeUser) 
    {
        this.executeUser = executeUser;
    }

    public String getExecuteUser() 
    {
        return executeUser;
    }

    public void setExecuteTime(Date executeTime) 
    {
        this.executeTime = executeTime;
    }

    public Date getExecuteTime() 
    {
        return executeTime;
    }

    public void setExecuteRuleVersion(String executeRuleVersion) 
    {
        this.executeRuleVersion = executeRuleVersion;
    }

    public String getExecuteRuleVersion() 
    {
        return executeRuleVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("pkId", getPkId())
            .append("ruleId", getRuleId())
            .append("orderNumber", getOrderNumber())
            .append("orderType", getOrderType())
            .append("executeStatus", getExecuteStatus())
            .append("executeUser", getExecuteUser())
            .append("executeTime", getExecuteTime())
            .append("executeRuleVersion", getExecuteRuleVersion())
            .toString();
    }
}

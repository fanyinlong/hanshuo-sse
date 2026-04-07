package com.shenmei.data.sse.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 服务器分发记录对象 sse_server_distribution_record
 *
 * @author ruoyi
 * @date 2025-08-05
 */
public class SseServerDistributionRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long pkId;

    /** 规则ID */
    @Excel(name = "规则ID")
    private Long ruleId;

    /** 分发状态 */
    @Excel(name = "分发状态")
    private String distributionStatus;

    /** 分发人 */
    @Excel(name = "分发人")
    private String distributionUser;

    /** 分发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "分发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date distributionTime;

    /** 分发版本 */
    @Excel(name = "分发版本")
    private String distributionVersion;
    /** 分发总条数 */
    @Excel(name = "分发总条数")
    private Long totalCount;

    /** 失败错误信息 */
    @Excel(name = "失败错误信息")
    private String errorMessage;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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

    public void setDistributionStatus(String distributionStatus)
    {
        this.distributionStatus = distributionStatus;
    }

    public String getDistributionStatus()
    {
        return distributionStatus;
    }

    public void setDistributionUser(String distributionUser)
    {
        this.distributionUser = distributionUser;
    }

    public String getDistributionUser()
    {
        return distributionUser;
    }

    public void setDistributionTime(Date distributionTime)
    {
        this.distributionTime = distributionTime;
    }

    public Date getDistributionTime()
    {
        return distributionTime;
    }

    public void setDistributionVersion(String distributionVersion)
    {
        this.distributionVersion = distributionVersion;
    }

    public String getDistributionVersion()
    {
        return distributionVersion;
    }

    @Override
    public String toString() {
        return "SseServerDistributionRecord{" +
                "pkId=" + pkId +
                ", ruleId=" + ruleId +
                ", distributionStatus='" + distributionStatus + '\'' +
                ", distributionUser='" + distributionUser + '\'' +
                ", distributionTime=" + distributionTime +
                ", distributionVersion='" + distributionVersion + '\'' +
                ", totalCount=" + totalCount +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}


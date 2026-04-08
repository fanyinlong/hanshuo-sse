package com.shenmei.data.sse.domain;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 数据模型对象 sse_model
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public class SseModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 数据模型编号:S1,S2...S12 */
    private String modelId;

    /** 数据模型名称 */
    @Excel(name = "数据模型名称")
    private String modelName;

    /** 唯一系统关注维度 */
    @Excel(name = "唯一系统关注维度")
    private String coreDimension;

    /** 主要风险对象 */
    @Excel(name = "主要风险对象")
    private String mainRiskTarget;

    /** 数据模型详细描述 */
    @Excel(name = "数据模型详细描述")
    private String description;

    /** 设置目的 */
    @Excel(name = "设置目的")
    private String purpose;

    /** 状态:0正常,1停用 */
    @Excel(name = "状态:0正常,1停用")
    private String status;

    /** 删除标志:0存在,1删除 */
    private String delFlag;

    /** 业务类型:竞价,期权,债券 */
    @Excel(name = "业务类型:竞价,期权,债券")
    private String businessType;

    public void setModelId(String modelId) 
    {
        this.modelId = modelId;
    }

    public String getModelId() 
    {
        return modelId;
    }

    public void setModelName(String modelName) 
    {
        this.modelName = modelName;
    }

    public String getModelName() 
    {
        return modelName;
    }

    public void setCoreDimension(String coreDimension) 
    {
        this.coreDimension = coreDimension;
    }

    public String getCoreDimension() 
    {
        return coreDimension;
    }

    public void setMainRiskTarget(String mainRiskTarget) 
    {
        this.mainRiskTarget = mainRiskTarget;
    }

    public String getMainRiskTarget() 
    {
        return mainRiskTarget;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setPurpose(String purpose) 
    {
        this.purpose = purpose;
    }

    public String getPurpose() 
    {
        return purpose;
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

    public void setBusinessType(String businessType) 
    {
        this.businessType = businessType;
    }

    public String getBusinessType() 
    {
        return businessType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("modelId", getModelId())
            .append("modelName", getModelName())
            .append("coreDimension", getCoreDimension())
            .append("mainRiskTarget", getMainRiskTarget())
            .append("description", getDescription())
            .append("purpose", getPurpose())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("businessType", getBusinessType())
            .toString();
    }
}

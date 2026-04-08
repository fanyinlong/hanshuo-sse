package com.shenmei.data.sse.domain;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 模型参数关联对象 sse_model_param
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public class SseModelParam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联ID */
    private Long id;

    /** 数据模型ID */
    @Excel(name = "数据模型ID")
    private String modelId;

    /** 参数英文名称 */
    @Excel(name = "参数英文名称")
    private String paramId;

    /** 是否必填:1是,0否 */
    @Excel(name = "是否必填:1是,0否")
    private String isRequired;

    /** 建议最小值 */
    @Excel(name = "建议最小值")
    private String recommendMin;

    /** 建议最大值 */
    @Excel(name = "建议最大值")
    private String recommendMax;

    /** 建议值 */
    @Excel(name = "建议值")
    private String recommendValue;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long displayOrder;

    /** 依赖规则 */
    @Excel(name = "依赖规则")
    private String dependencyRule;

    /** 状态:0正常,1停用 */
    @Excel(name = "状态:0正常,1停用")
    private String status;

    /** 删除标志:0存在,1删除 */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setModelId(String modelId) 
    {
        this.modelId = modelId;
    }

    public String getModelId() 
    {
        return modelId;
    }

    public void setParamId(String paramId) 
    {
        this.paramId = paramId;
    }

    public String getParamId() 
    {
        return paramId;
    }

    public void setIsRequired(String isRequired) 
    {
        this.isRequired = isRequired;
    }

    public String getIsRequired() 
    {
        return isRequired;
    }

    public void setRecommendMin(String recommendMin) 
    {
        this.recommendMin = recommendMin;
    }

    public String getRecommendMin() 
    {
        return recommendMin;
    }

    public void setRecommendMax(String recommendMax) 
    {
        this.recommendMax = recommendMax;
    }

    public String getRecommendMax() 
    {
        return recommendMax;
    }

    public void setRecommendValue(String recommendValue) 
    {
        this.recommendValue = recommendValue;
    }

    public String getRecommendValue() 
    {
        return recommendValue;
    }

    public void setDisplayOrder(Long displayOrder) 
    {
        this.displayOrder = displayOrder;
    }

    public Long getDisplayOrder() 
    {
        return displayOrder;
    }

    public void setDependencyRule(String dependencyRule) 
    {
        this.dependencyRule = dependencyRule;
    }

    public String getDependencyRule() 
    {
        return dependencyRule;
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
            .append("id", getId())
            .append("modelId", getModelId())
            .append("paramId", getParamId())
            .append("isRequired", getIsRequired())
            .append("recommendMin", getRecommendMin())
            .append("recommendMax", getRecommendMax())
            .append("recommendValue", getRecommendValue())
            .append("displayOrder", getDisplayOrder())
            .append("dependencyRule", getDependencyRule())
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

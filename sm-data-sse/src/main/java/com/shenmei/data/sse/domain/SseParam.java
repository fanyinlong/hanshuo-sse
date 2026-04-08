package com.shenmei.data.sse.domain;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 参数定义对象 sse_param
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public class SseParam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 参数英文名称(唯一) */
    private String paramId;

    /** 参数中文名称 */
    @Excel(name = "参数中文名称")
    private String paramName;

    /** 参数分类:BASIC,RATIO,DIST,RULE,EXEC */
    @Excel(name = "参数分类:BASIC,RATIO,DIST,RULE,EXEC")
    private String paramCategory;

    /** 数据类型:BIGINT,DECIMAL,VARCHAR */
    @Excel(name = "数据类型:BIGINT,DECIMAL,VARCHAR")
    private String dataType;

    /** 默认值 */
    @Excel(name = "默认值")
    private String defaultValue;

    /** 值单位 */
    @Excel(name = "值单位")
    private String valueUnit;

    /** 参数描述 */
    @Excel(name = "参数描述")
    private String description;

    /** 约束规则 */
    @Excel(name = "约束规则")
    private String constraintRule;

    /** 状态:0正常,1停用 */
    @Excel(name = "状态:0正常,1停用")
    private String status;

    /** 删除标志:0存在,1删除 */
    private String delFlag;

    public void setParamId(String paramId) 
    {
        this.paramId = paramId;
    }

    public String getParamId() 
    {
        return paramId;
    }

    public void setParamName(String paramName) 
    {
        this.paramName = paramName;
    }

    public String getParamName() 
    {
        return paramName;
    }

    public void setParamCategory(String paramCategory) 
    {
        this.paramCategory = paramCategory;
    }

    public String getParamCategory() 
    {
        return paramCategory;
    }

    public void setDataType(String dataType) 
    {
        this.dataType = dataType;
    }

    public String getDataType() 
    {
        return dataType;
    }

    public void setDefaultValue(String defaultValue) 
    {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() 
    {
        return defaultValue;
    }

    public void setValueUnit(String valueUnit) 
    {
        this.valueUnit = valueUnit;
    }

    public String getValueUnit() 
    {
        return valueUnit;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setConstraintRule(String constraintRule) 
    {
        this.constraintRule = constraintRule;
    }

    public String getConstraintRule() 
    {
        return constraintRule;
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
            .append("paramId", getParamId())
            .append("paramName", getParamName())
            .append("paramCategory", getParamCategory())
            .append("dataType", getDataType())
            .append("defaultValue", getDefaultValue())
            .append("valueUnit", getValueUnit())
            .append("description", getDescription())
            .append("constraintRule", getConstraintRule())
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

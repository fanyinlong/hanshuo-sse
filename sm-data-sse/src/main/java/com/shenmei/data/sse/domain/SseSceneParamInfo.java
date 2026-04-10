package com.shenmei.data.sse.domain;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 场景参数信息对象 sse_scene_param_info
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
public class SseSceneParamInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 场景参数ID */
    private Long paramInfoId;

    /** 场景ID */
    @Excel(name = "场景ID")
    private Long sceneId;

    /** 参数ID */
    @Excel(name = "参数ID")
    private String paramId;

    /** 参数值 */
    @Excel(name = "参数值")
    private String paramValue;

    public void setParamInfoId(Long paramInfoId) 
    {
        this.paramInfoId = paramInfoId;
    }

    public Long getParamInfoId() 
    {
        return paramInfoId;
    }

    public void setSceneId(Long sceneId) 
    {
        this.sceneId = sceneId;
    }

    public Long getSceneId() 
    {
        return sceneId;
    }

    public void setParamId(String paramId) 
    {
        this.paramId = paramId;
    }

    public String getParamId() 
    {
        return paramId;
    }

    public void setParamValue(String paramValue) 
    {
        this.paramValue = paramValue;
    }

    public String getParamValue() 
    {
        return paramValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("paramInfoId", getParamInfoId())
            .append("sceneId", getSceneId())
            .append("paramId", getParamId())
            .append("paramValue", getParamValue())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}

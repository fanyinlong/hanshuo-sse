package com.shenmei.data.sse.domain;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 执行机分发配置对象 sse_server_config
 * 
 * @author ruoyi
 * @date 2026-02-12
 */
public class SseServerConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 规则id */
    @Excel(name = "规则id")
    private Long ruleId;

    /** 执行机id */
    @Excel(name = "执行机id")
    private Long serverId;

    /** 执行机名称 */
    @Excel(name = "执行机名称")
    private String serverName;

    /** 每个执行机配置PBU个数 */
    @Excel(name = "每个执行机配置PBU个数")
    private Long pbuCount;

    /** 规则版本 */
    @Excel(name = "规则版本")
    private String ruleVersion;

    /** 预留 */
    @Excel(name = "预留")
    private String reverse;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRuleId(Long ruleId) 
    {
        this.ruleId = ruleId;
    }

    public Long getRuleId() 
    {
        return ruleId;
    }

    public void setServerId(Long serverId) 
    {
        this.serverId = serverId;
    }

    public Long getServerId() 
    {
        return serverId;
    }

    public void setServerName(String serverName) 
    {
        this.serverName = serverName;
    }

    public String getServerName() 
    {
        return serverName;
    }

    public void setPbuCount(Long pbuCount) 
    {
        this.pbuCount = pbuCount;
    }

    public Long getPbuCount() 
    {
        return pbuCount;
    }

    public void setRuleVersion(String ruleVersion) 
    {
        this.ruleVersion = ruleVersion;
    }

    public String getRuleVersion() 
    {
        return ruleVersion;
    }

    public void setReverse(String reverse) 
    {
        this.reverse = reverse;
    }

    public String getReverse() 
    {
        return reverse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ruleId", getRuleId())
            .append("serverId", getServerId())
            .append("serverName", getServerName())
            .append("pbuCount", getPbuCount())
            .append("ruleVersion", getRuleVersion())
            .append("reverse", getReverse())
            .toString();
    }
}

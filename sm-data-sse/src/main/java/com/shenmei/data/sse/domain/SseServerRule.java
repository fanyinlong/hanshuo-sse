package com.shenmei.data.sse.domain;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;

public class SseServerRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID")
    private String id;

    /** 规则id */
    @Excel(name = "规则id")
    private Long ruleId;

    /** '执行机id' */
    @Excel(name = "'执行机id'")
    private String serverId;

    /** 执行机名称 */
    @Excel(name = "执行机名称")
    private String serverName;

    /** '每个执行机配置PBU个数' */
    @Excel(name = "每个执行机配置PBU个数")
    private int pbuCount;

    /** 规则版本 */
    @Excel(name = "规则版本")
    private String ruleVersion;

    /** 预留 */
    @Excel(name = "预留")
    private String reverse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getPbuCount() {
        return pbuCount;
    }

    public void setPbuCount(int pbuCount) {
        this.pbuCount = pbuCount;
    }

    public String getRuleVersion() {
        return ruleVersion;
    }

    public void setRuleVersion(String ruleVersion) {
        this.ruleVersion = ruleVersion;
    }

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }

    @Override
    public String toString() {
        return "SseServiceRule{" +
                "id='" + id + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", serverId='" + serverId + '\'' +
                ", serverName='" + serverName + '\'' +
                ", pbuCount='" + pbuCount + '\'' +
                ", ruleVersion='" + ruleVersion + '\'' +
                ", reverse='" + reverse + '\'' +
                '}';
    }
}


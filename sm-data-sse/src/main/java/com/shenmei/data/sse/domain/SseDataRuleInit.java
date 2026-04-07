package com.shenmei.data.sse.domain;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 规则初始化对象 sse_data_rule_init
 * 
 * @author ruoyi
 * @date 2025-08-02
 */
public class SseDataRuleInit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long pkId;

    /** 规则ID */
    @Excel(name = "规则ID")
    private Long ruleId;

    /** 登录pbu */
    @Excel(name = "登录pbu")
    private String loginPbu;

    /** 业务pbu */
    @Excel(name = "业务pbu")
    private String bizPbu;

    /** pbu和账户集合 */
    @Excel(name = "pbu和账户集合")
    private String pbuAccountList;

    /** 主板股票 */
    @Excel(name = "主板股票")
    private String securityAshList;

    /** 科创板股票 */
    @Excel(name = "科创板股票")
    private String securityKshList;

    /** 主板SET股票分配 */
    @Excel(name = "主板SET股票分配")
    private String setAshSecurity;

    /** 科创板SET股票分配 */
    @Excel(name = "科创板SET股票分配")
    private String setKshSecurity;

    /** 预留扩展字段1 */
    private String matchOrderList;

    /** 预留扩展字段2 */
    private String field2;

    /** 预留扩展字段3 */
    private String field3;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标志 */
    private String delFlag;

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

    public void setLoginPbu(String loginPbu) 
    {
        this.loginPbu = loginPbu;
    }

    public String getLoginPbu() 
    {
        return loginPbu;
    }

    public void setBizPbu(String bizPbu) 
    {
        this.bizPbu = bizPbu;
    }

    public String getBizPbu() 
    {
        return bizPbu;
    }

    public void setPbuAccountList(String pbuAccountList) 
    {
        this.pbuAccountList = pbuAccountList;
    }

    public String getPbuAccountList() 
    {
        return pbuAccountList;
    }

    public void setSecurityAshList(String securityAshList) 
    {
        this.securityAshList = securityAshList;
    }

    public String getSecurityAshList() 
    {
        return securityAshList;
    }

    public void setSecurityKshList(String securityKshList) 
    {
        this.securityKshList = securityKshList;
    }

    public String getSecurityKshList() 
    {
        return securityKshList;
    }

    public void setSetAshSecurity(String setAshSecurity) 
    {
        this.setAshSecurity = setAshSecurity;
    }

    public String getSetAshSecurity() 
    {
        return setAshSecurity;
    }

    public void setSetKshSecurity(String setKshSecurity) 
    {
        this.setKshSecurity = setKshSecurity;
    }

    public String getSetKshSecurity() 
    {
        return setKshSecurity;
    }

    public void setMatchOrderList(String matchOrderList)
    {
        this.matchOrderList = matchOrderList;
    }

    public String getMatchOrderList()
    {
        return matchOrderList;
    }

    public void setField2(String field2) 
    {
        this.field2 = field2;
    }

    public String getField2() 
    {
        return field2;
    }

    public void setField3(String field3) 
    {
        this.field3 = field3;
    }

    public String getField3() 
    {
        return field3;
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
            .append("ruleId", getRuleId())
            .append("loginPbu", getLoginPbu())
            .append("bizPbu", getBizPbu())
            .append("pbuAccountList", getPbuAccountList())
            .append("securityAshList", getSecurityAshList())
            .append("securityKshList", getSecurityKshList())
            .append("setAshSecurity", getSetAshSecurity())
            .append("setKshSecurity", getSetKshSecurity())
            .append("matchOrderList", getMatchOrderList())
            .append("field2", getField2())
            .append("field3", getField3())
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

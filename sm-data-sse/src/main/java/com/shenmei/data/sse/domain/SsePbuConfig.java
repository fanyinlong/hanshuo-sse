package com.shenmei.data.sse.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;

/**
 * PBU配置对象 sse_pbu_config
 *
 * @author Song
 * @date 2025-07-28
 */
public class SsePbuConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long pkId;

    /** PBU编号 */
    @Excel(name = "PBU编号")
    private String pbuId;

    /** 使用状态:0=未使用,1=已使用 */
    @Excel(name = "使用状态:0=未使用,1=已使用")
    private String isUsed;

    /** 是否登陆pbu:0=不是,1=是 */
    @Excel(name = "是否登陆pbu:0=不是,1=是")
    private String isLoginpbu;

    /** 是否业务pbu:0=不是,1=是 */
    @Excel(name = "是否业务pbu:0=不是,1=是")
    private String isBizpbu;

    /** 业务pbu所属登陆pbu，非业务pbu该项为空 */
    @Excel(name = "业务pbu所属登陆pbu，非业务pbu该项为空")
    private String belongtoLoginpbu;

    /** 对应规则Id */
    @Excel(name = "对应规则Id")
    private Long ruleId;

    /** 对应规则名称 */
    @Excel(name = "对应规则名称")
    private Long ruleName;

    /** 包含账户数量 */
    @Excel(name = "包含账户数量")
    private Long accountNumber;

    /** 规则使用账户数量 */
    @Excel(name = "规则使用账户数量")
    private Long accountNumberused;

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public void setPbuId(String pbuId)
    {
        this.pbuId = pbuId;
    }

    public String getPbuId()
    {
        return pbuId;
    }

    public void setIsUsed(String isUsed)
    {
        this.isUsed = isUsed;
    }

    public String getIsUsed()
    {
        return isUsed;
    }

    public void setIsLoginpbu(String isLoginpbu)
    {
        this.isLoginpbu = isLoginpbu;
    }

    public String getIsLoginpbu()
    {
        return isLoginpbu;
    }

    public void setIsBizpbu(String isBizpbu)
    {
        this.isBizpbu = isBizpbu;
    }

    public String getIsBizpbu()
    {
        return isBizpbu;
    }

    public void setBelongtoLoginpbu(String belongtoLoginpbu)
    {
        this.belongtoLoginpbu = belongtoLoginpbu;
    }

    public String getBelongtoLoginpbu()
    {
        return belongtoLoginpbu;
    }

    public void setRuleId(Long ruleId)
    {
        this.ruleId = ruleId;
    }

    public Long getRuleId()
    {
        return ruleId;
    }

    public void setRuleName(Long ruleName)
    {
        this.ruleName = ruleName;
    }

    public Long getRuleName()
    {
        return ruleName;
    }

    public void setAccountNumber(Long accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public Long getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumberused(Long accountNumberused)
    {
        this.accountNumberused = accountNumberused;
    }

    public Long getAccountNumberused()
    {
        return accountNumberused;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("pbuId", getPbuId())
                .append("isUsed", getIsUsed())
                .append("isLoginpbu", getIsLoginpbu())
                .append("isBizpbu", getIsBizpbu())
                .append("belongtoLoginpbu", getBelongtoLoginpbu())
                .append("ruleId", getRuleId())
                .append("ruleName", getRuleName())
                .append("accountNumber", getAccountNumber())
                .append("accountNumberused", getAccountNumberused())
                .toString();
    }
}

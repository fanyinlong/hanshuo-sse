package com.shenmei.data.sse.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;

/**
 * 加持仓统计对象 sse_order_security_account
 *
 * @author Song
 * @date 2025-08-13
 */
public class SseOrderSecurityAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long pkId;

    /** 规则ID */
    @Excel(name = "规则ID")
    private Long ruleId;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String securityId;

    /** 投资者账户 */
    @Excel(name = "投资者账户")
    private String accountId;

    /** 预留扩展字段1 */
    @Excel(name = "预留扩展字段1")
    private String field1;

    /** 预留扩展字段2 */
    @Excel(name = "预留扩展字段2")
    private String field2;

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

    public void setSecurityId(String securityId)
    {
        this.securityId = securityId;
    }

    public String getSecurityId()
    {
        return securityId;
    }

    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }

    public String getAccountId()
    {
        return accountId;
    }

    public void setField1(String field1)
    {
        this.field1 = field1;
    }

    public String getField1()
    {
        return field1;
    }

    public void setField2(String field2)
    {
        this.field2 = field2;
    }

    public String getField2()
    {
        return field2;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("pkId", getPkId())
                .append("ruleId", getRuleId())
                .append("securityId", getSecurityId())
                .append("accountId", getAccountId())
                .append("field1", getField1())
                .append("field2", getField2())
                .toString();
    }
}

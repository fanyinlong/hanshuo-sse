package com.shenmei.data.sse.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;

/**
 * 订单统计对象 sse_order_stats
 *
 * @author Song
 * @date 2025-08-11
 */
public class SseOrderStats extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long pkId;

    /** 规则ID */
    @Excel(name = "规则ID")
    private Long ruleId;

    /** 总订单数 */
    @Excel(name = "总订单数")
    private Long totalOrders;

    /** 各登录PBU订单数 */
    @Excel(name = "各登录PBU订单数")
    private String loginPbuOrders;

    /** 各业务PBU订单数 */
    @Excel(name = "各业务PBU订单数")
    private String bizPbuOrders;

    /** 主板科创板订单数 */
    @Excel(name = "主板科创板订单数")
    private String ashKshOrders;

    /** 各SET订单数 */
    @Excel(name = "各SET订单数")
    private String setsOrders;

    /** 各撮合方式订单数 */
    @Excel(name = "各撮合方式订单数")
    private String matchingTypeOrders;

    /** 各撮合比订单数，如一撮十 */
    @Excel(name = "各撮合比订单数，如一撮十")
    private String matchingSideOrders;

    /** 各账户类型订单数 */
    @Excel(name = "各账户类型订单数")
    private String accountTypeOrders;

    /** 预留扩展字段1 */
    @Excel(name = "预留扩展字段1")
    private String field1;

    /** 预留扩展字段2 */
    @Excel(name = "预留扩展字段2")
    private String field2;

    /** 预留扩展字段3 */
    @Excel(name = "预留扩展字段3")
    private String field3;

    /** 预留扩展字段4 */
    @Excel(name = "预留扩展字段4")
    private String field4;

    /** 预留扩展字段5 */
    @Excel(name = "预留扩展字段5")
    private String field5;

    /** 预留扩展字段6 */
    @Excel(name = "预留扩展字段6")
    private String field6;

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

    public void setTotalOrders(Long totalOrders)
    {
        this.totalOrders = totalOrders;
    }

    public Long getTotalOrders()
    {
        return totalOrders;
    }

    public void setLoginPbuOrders(String loginPbuOrders)
    {
        this.loginPbuOrders = loginPbuOrders;
    }

    public String getLoginPbuOrders()
    {
        return loginPbuOrders;
    }

    public void setBizPbuOrders(String bizPbuOrders)
    {
        this.bizPbuOrders = bizPbuOrders;
    }

    public String getBizPbuOrders()
    {
        return bizPbuOrders;
    }

    public void setAshKshOrders(String ashKshOrders)
    {
        this.ashKshOrders = ashKshOrders;
    }

    public String getAshKshOrders()
    {
        return ashKshOrders;
    }

    public void setSetsOrders(String setsOrders)
    {
        this.setsOrders = setsOrders;
    }

    public String getSetsOrders()
    {
        return setsOrders;
    }

    public void setMatchingTypeOrders(String matchingTypeOrders)
    {
        this.matchingTypeOrders = matchingTypeOrders;
    }

    public String getMatchingTypeOrders()
    {
        return matchingTypeOrders;
    }

    public void setMatchingSideOrders(String matchingSideOrders)
    {
        this.matchingSideOrders = matchingSideOrders;
    }

    public String getMatchingSideOrders()
    {
        return matchingSideOrders;
    }

    public void setAccountTypeOrders(String accountTypeOrders)
    {
        this.accountTypeOrders = accountTypeOrders;
    }

    public String getAccountTypeOrders()
    {
        return accountTypeOrders;
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

    public void setField3(String field3)
    {
        this.field3 = field3;
    }

    public String getField3()
    {
        return field3;
    }

    public void setField4(String field4)
    {
        this.field4 = field4;
    }

    public String getField4()
    {
        return field4;
    }

    public void setField5(String field5)
    {
        this.field5 = field5;
    }

    public String getField5()
    {
        return field5;
    }

    public void setField6(String field6)
    {
        this.field6 = field6;
    }

    public String getField6()
    {
        return field6;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("pkId", getPkId())
                .append("ruleId", getRuleId())
                .append("totalOrders", getTotalOrders())
                .append("loginPbuOrders", getLoginPbuOrders())
                .append("bizPbuOrders", getBizPbuOrders())
                .append("ashKshOrders", getAshKshOrders())
                .append("setsOrders", getSetsOrders())
                .append("matchingTypeOrders", getMatchingTypeOrders())
                .append("matchingSideOrders", getMatchingSideOrders())
                .append("accountTypeOrders", getAccountTypeOrders())
                .append("field1", getField1())
                .append("field2", getField2())
                .append("field3", getField3())
                .append("field4", getField4())
                .append("field5", getField5())
                .append("field6", getField6())
                .toString();
    }
}

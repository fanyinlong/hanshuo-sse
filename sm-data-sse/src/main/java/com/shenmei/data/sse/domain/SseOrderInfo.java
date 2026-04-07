package com.shenmei.data.sse.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;

/**
 * 订单汇总对象 sse_order_info
 *
 * @author song
 * @date 2025-08-02
 */
public class SseOrderInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单编号 */
    @Excel(name="ID")
    private Long pkId;

    /** 消息头 */
    @Excel(name="MsgType")
    private String msgHead;

    /** 业务编号 */
    @Excel(name="BizID")
    private Integer bizId;

    /** 业务PBU编号，前5位有效 */
    @Excel(name = "BizPbu")
    private String bizPbu;

    /** 会员内部订单编号 */
    @Excel(name = "ClOrdID")
    private String clOrdId;

    /** 证券代码，前6位有效 */
    @Excel(name = "SecurityID")
    private String securityId;

    /** 证券账户，前10位有效 */
    @Excel(name = "Account")
    private String account;

    /** 暂不启用 */
    @Excel(name="OwnerType")
    private String ownerType;

    /** 买卖方向:1=买,2=卖 */
    @Excel(name = "Side")
    private String side;

    /** 申报价格 */
    @Excel(name = "Price")
    private BigDecimal price;

    /** 申报数量 */
    @Excel(name = "OrderQty")
    private Long orderQty;

    /** 订单类型:1=市转撤,2=限价,3=市转限,4=本方最优,5=对手方最优 */
    @Excel(name = "OrdType")
    private String ordType;

    /** 订单有效时间类型:0=当日有效 */
    @Excel(name = "TimeInForce")
    private String timeInForce;

    /** 申报时间 */
    @Excel(name="TransactTime" , dateFormat="yyyyMMddHHmmss")
    private Date transactTime;

    /** 信用标签:XY=担保品买卖,RZ=融资交易,RQ=融券交易,PC=融券交易 */
    @Excel(name="CreditTag")
    private String creditTag;

    /** 结算会员代码，前5位有效 */
    @Excel(name="ClearingFirm")
    private String clearingFirm;

    /** 营业部代码，前5位有效 */
    @Excel(name="BranchID")
    private String branchId;

    /** 原始会员内部订单编号(待撤原订单的ClOrdID) */
    @Excel(name = "OrigClOrdID")
    private String origClOrdId;

    /** 用户私有信息，前12位有效 */
    @Excel(name="UserInfo")
    private String userInfo;

    /** 各业务扩展字段 */
    @Excel(name="ExtendFields")
    private String extendFields;

    /** 订单所属set */
    @Excel(name="OrderSet")
    private String orderSet;

    /** 订单撮合方式:详见字典定义 */
    @Excel(name="OrderMatching")
    private String orderMatching;

    /** 订单下发表 */
    @Excel(name="OrderTable")
    private String orderTable;

    /** 订单下发主机id */
    @Excel(name="OrderServerID")
    private Long orderServerId;

    /** 规则ID */
    @Excel(name = "RuleID")
    private Long ruleId;

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public void setPkId(Long pkId)
    {
        this.pkId = pkId;
    }

    public Long getPkId()
    {
        return pkId;
    }

    public void setMsgHead(String msgHead)
    {
        this.msgHead = msgHead;
    }

    public String getMsgHead()
    {
        return msgHead;
    }

    public void setBizId(Integer bizId)
    {
        this.bizId = bizId;
    }

    public Integer getBizId()
    {
        return bizId;
    }

    public void setBizPbu(String bizPbu)
    {
        this.bizPbu = bizPbu;
    }

    public String getBizPbu()
    {
        return bizPbu;
    }

    public void setClOrdId(String clOrdId)
    {
        this.clOrdId = clOrdId;
    }

    public String getClOrdId()
    {
        return clOrdId;
    }

    public void setSecurityId(String securityId)
    {
        this.securityId = securityId;
    }

    public String getSecurityId()
    {
        return securityId;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getAccount()
    {
        return account;
    }

    public void setOwnerType(String ownerType)
    {
        this.ownerType = ownerType;
    }

    public String getOwnerType()
    {
        return ownerType;
    }

    public void setSide(String side)
    {
        this.side = side;
    }

    public String getSide()
    {
        return side;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setOrderQty(Long orderQty)
    {
        this.orderQty = orderQty;
    }

    public Long getOrderQty()
    {
        return orderQty;
    }

    public void setOrdType(String ordType)
    {
        this.ordType = ordType;
    }

    public String getOrdType()
    {
        return ordType;
    }

    public void setTimeInForce(String timeInForce)
    {
        this.timeInForce = timeInForce;
    }

    public String getTimeInForce()
    {
        return timeInForce;
    }

    public void setTransactTime(Date transactTime)
    {
        this.transactTime = transactTime;
    }

    public Date getTransactTime()
    {
        return transactTime;
    }

    public void setCreditTag(String creditTag)
    {
        this.creditTag = creditTag;
    }

    public String getCreditTag()
    {
        return creditTag;
    }

    public void setClearingFirm(String clearingFirm)
    {
        this.clearingFirm = clearingFirm;
    }

    public String getClearingFirm()
    {
        return clearingFirm;
    }

    public void setBranchId(String branchId)
    {
        this.branchId = branchId;
    }

    public String getBranchId()
    {
        return branchId;
    }

    public void setOrigClOrdId(String origClOrdId)
    {
        this.origClOrdId = origClOrdId;
    }

    public String getOrigClOrdId()
    {
        return origClOrdId;
    }

    public void setUserInfo(String userInfo)
    {
        this.userInfo = userInfo;
    }

    public String getUserInfo()
    {
        return userInfo;
    }

    public void setExtendFields(String extendFields)
    {
        this.extendFields = extendFields;
    }

    public String getExtendFields()
    {
        return extendFields;
    }

    public void setOrderSet(String orderSet)
    {
        this.orderSet = orderSet;
    }

    public String getOrderSet()
    {
        return orderSet;
    }

    public void setOrderMatching(String orderMatching)
    {
        this.orderMatching = orderMatching;
    }

    public String getOrderMatching()
    {
        return orderMatching;
    }

    public void setOrderTable(String orderTable)
    {
        this.orderTable = orderTable;
    }

    public String getOrderTable()
    {
        return orderTable;
    }

    public void setOrderServerId(Long orderServerId)
    {
        this.orderServerId = orderServerId;
    }

    public Long getOrderServerId()
    {
        return orderServerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("pkId", getPkId())
                .append("msgHead", getMsgHead())
                .append("bizId", getBizId())
                .append("bizPbu", getBizPbu())
                .append("clOrdId", getClOrdId())
                .append("securityId", getSecurityId())
                .append("account", getAccount())
                .append("ownerType", getOwnerType())
                .append("side", getSide())
                .append("price", getPrice())
                .append("orderQty", getOrderQty())
                .append("ordType", getOrdType())
                .append("timeInForce", getTimeInForce())
                .append("transactTime", getTransactTime())
                .append("creditTag", getCreditTag())
                .append("clearingFirm", getClearingFirm())
                .append("branchId", getBranchId())
                .append("origClOrdId", getOrigClOrdId())
                .append("userInfo", getUserInfo())
                .append("extendFields", getExtendFields())
                .append("orderSet", getOrderSet())
                .append("orderMatching", getOrderMatching())
                .append("orderTable", getOrderTable())
                .append("orderServerId", getOrderServerId())
                .toString();
    }
}


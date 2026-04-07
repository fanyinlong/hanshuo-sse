package com.shenmei.data.sse.domain;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 产品信息对象 sse_security_info
 *
 * @author Song
 * @date 2025-07-26
 */
public class SseSecurityInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 产品编号 */
    private Long pkId;

    /** 证券代码 */
    @Excel(name = "证券代码")
    private String securityId;

    /** ISIN代码（预留） */
    private String isinCode;


    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    /** 更新时间 */
    private String recordTime;

    /** 证券名称 */
    @Excel(name = "证券名称")
    private String securityCname;

    /** 英文证券名称 */
    private String securityName;

    /** 基础证券代码 */
    private String oriSecurityId;

    /** 市场种类 */
    @Excel(name = "市场种类")
    private String securityMarket;

    /** 证券类别 */
    @Excel(name = "证券类别")
    private String securityType;

    /** 证券子类别 */
    @Excel(name = "证券子类别")
    private String securitySubType;

    /** 货币种类 */
    private String currencyType;

    /** 债券面值 */
    private Double parValue;

    /** 可流通证券未上市数量 */
    private Long unissuedSecurities;

    /** 最后交易日期 */
    private String lastTradeDate;

    /** 上市日期 */
    private String listingDate;

    /** 产品集set编号 */
    @Excel(name = "产品集set编号")
    private Long securitySet;

    /** 买数量单位 */
    private Long buyUnit;

    /** 卖数量单位 */
    private Long sellUnit;

    /** 限价申报数量下限 */
    private Long limitOrderBottom;

    /** 限价申报数量上限 */
    private Long limitOrderTop;

    /** 前收盘价格 */
    @Excel(name = "前收盘价格")
    private Double priorClosingPrice;

    /** 价格档位 */
    private Double priceTick;

    /** 涨跌幅限制类型 */
    @Excel(name = "涨跌幅限制类型")
    private String priceLimitType;

    /** 涨幅上限价格 */
    private Double priceUpsideLimit;

    /** 涨幅下限价格 */
    private Double priceDownsideLimit;

    /** 使用状态:0=未使用,1=已使用 */
    @Excel(name = "使用状态:0=未使用,1=已使用")
    private String usageStatus;

    public void setPkId(Long pkId)
    {
        this.pkId = pkId;
    }

    public Long getPkId()
    {
        return pkId;
    }

    public void setSecurityId(String securityId)
    {
        this.securityId = securityId;
    }

    public String getSecurityId()
    {
        return securityId;
    }

    public void setIsinCode(String isinCode)
    {
        this.isinCode = isinCode;
    }

    public String getIsinCode()
    {
        return isinCode;
    }

    public void setSecurityCname(String securityCname)
    {
        this.securityCname = securityCname;
    }

    public String getSecurityCname()
    {
        return securityCname;
    }

    public void setSecurityName(String securityName)
    {
        this.securityName = securityName;
    }

    public String getSecurityName()
    {
        return securityName;
    }

    public void setOriSecurityId(String oriSecurityId)
    {
        this.oriSecurityId = oriSecurityId;
    }

    public String getOriSecurityId()
    {
        return oriSecurityId;
    }

    public void setSecurityMarket(String securityMarket)
    {
        this.securityMarket = securityMarket;
    }

    public String getSecurityMarket()
    {
        return securityMarket;
    }

    public void setSecurityType(String securityType)
    {
        this.securityType = securityType;
    }

    public String getSecurityType()
    {
        return securityType;
    }

    public void setSecuritySubType(String securitySubType)
    {
        this.securitySubType = securitySubType;
    }

    public String getSecuritySubType()
    {
        return securitySubType;
    }

    public void setCurrencyType(String currencyType)
    {
        this.currencyType = currencyType;
    }

    public String getCurrencyType()
    {
        return currencyType;
    }

    public void setParValue(Double parValue)
    {
        this.parValue = parValue;
    }

    public Double getParValue()
    {
        return parValue;
    }

    public void setUnissuedSecurities(Long unissuedSecurities)
    {
        this.unissuedSecurities = unissuedSecurities;
    }

    public Long getUnissuedSecurities()
    {
        return unissuedSecurities;
    }

    public void setLastTradeDate(String lastTradeDate)
    {
        this.lastTradeDate = lastTradeDate;
    }

    public String getLastTradeDate()
    {
        return lastTradeDate;
    }

    public void setListingDate(String listingDate)
    {
        this.listingDate = listingDate;
    }

    public String getListingDate()
    {
        return listingDate;
    }

    public void setSecuritySet(Long securitySet)
    {
        this.securitySet = securitySet;
    }

    public Long getSecuritySet()
    {
        return securitySet;
    }

    public void setBuyUnit(Long buyUnit)
    {
        this.buyUnit = buyUnit;
    }

    public Long getBuyUnit()
    {
        return buyUnit;
    }

    public void setSellUnit(Long sellUnit)
    {
        this.sellUnit = sellUnit;
    }

    public Long getSellUnit()
    {
        return sellUnit;
    }

    public void setLimitOrderBottom(Long limitOrderBottom)
    {
        this.limitOrderBottom = limitOrderBottom;
    }

    public Long getLimitOrderBottom()
    {
        return limitOrderBottom;
    }

    public void setLimitOrderTop(Long limitOrderTop)
    {
        this.limitOrderTop = limitOrderTop;
    }

    public Long getLimitOrderTop()
    {
        return limitOrderTop;
    }

    public void setPriorClosingPrice(Double priorClosingPrice)
    {
        this.priorClosingPrice = priorClosingPrice;
    }

    public Double getPriorClosingPrice()
    {
        return priorClosingPrice;
    }

    public void setPriceTick(Double priceTick)
    {
        this.priceTick = priceTick;
    }

    public Double getPriceTick()
    {
        return priceTick;
    }

    public void setPriceLimitType(String priceLimitType)
    {
        this.priceLimitType = priceLimitType;
    }

    public String getPriceLimitType()
    {
        return priceLimitType;
    }

    public void setPriceUpsideLimit(Double priceUpsideLimit)
    {
        this.priceUpsideLimit = priceUpsideLimit;
    }

    public Double getPriceUpsideLimit()
    {
        return priceUpsideLimit;
    }

    public void setPriceDownsideLimit(Double priceDownsideLimit)
    {
        this.priceDownsideLimit = priceDownsideLimit;
    }

    public Double getPriceDownsideLimit()
    {
        return priceDownsideLimit;
    }

    public void setUsageStatus(String usageStatus)
    {
        this.usageStatus = usageStatus;
    }

    public String getUsageStatus()
    {
        return usageStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("pkId", getPkId())
                .append("securityId", getSecurityId())
                .append("isinCode", getIsinCode())
                .append("updateTime", getUpdateTime())
                .append("securityCname", getSecurityCname())
                .append("securityName", getSecurityName())
                .append("oriSecurityId", getOriSecurityId())
                .append("securityMarket", getSecurityMarket())
                .append("securityType", getSecurityType())
                .append("securitySubType", getSecuritySubType())
                .append("currencyType", getCurrencyType())
                .append("parValue", getParValue())
                .append("unissuedSecurities", getUnissuedSecurities())
                .append("lastTradeDate", getLastTradeDate())
                .append("listingDate", getListingDate())
                .append("securitySet", getSecuritySet())
                .append("buyUnit", getBuyUnit())
                .append("sellUnit", getSellUnit())
                .append("limitOrderBottom", getLimitOrderBottom())
                .append("limitOrderTop", getLimitOrderTop())
                .append("priorClosingPrice", getPriorClosingPrice())
                .append("priceTick", getPriceTick())
                .append("priceLimitType", getPriceLimitType())
                .append("priceUpsideLimit", getPriceUpsideLimit())
                .append("priceDownsideLimit", getPriceDownsideLimit())
                .append("usageStatus", getUsageStatus())
                .toString();
    }
}

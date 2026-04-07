package com.shenmei.data.sse.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;

/**
 * 账户信息对象 sse_account_info
 *
 * @author song
 * @date 2025-07-28
 */
public class SseAccountInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long pkId;

    /** 投资者账户 */
    @Excel(name = "投资者账户")
    private String accountId;

    /** 子账户 */
    @Excel(name = "子账户")
    private String accountSubId;

    /** PBU编号 */
    @Excel(name = "PBU编号")
    private String pbuId;

    /** 账户类型 */
    @Excel(name = "账户类型")
    private String accountType;

    /** 指定交易类型 */
    @Excel(name = "指定交易类型")
    private String tradeType;

    /** 账户能否撤销标识 */
    @Excel(name = "账户能否撤销标识")
    private String cancelFlag;

    /** 账户状态标识 */
    @Excel(name = "账户状态标识")
    private String statusFlag;

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }

    public String getAccountId()
    {
        return accountId;
    }

    public void setAccountSubId(String accountSubId)
    {
        this.accountSubId = accountSubId;
    }

    public String getAccountSubId()
    {
        return accountSubId;
    }

    public void setPbuId(String pbuId)
    {
        this.pbuId = pbuId;
    }

    public String getPbuId()
    {
        return pbuId;
    }

    public void setAccountType(String accountType)
    {
        this.accountType = accountType;
    }

    public String getAccountType()
    {
        return accountType;
    }

    public void setTradeType(String tradeType)
    {
        this.tradeType = tradeType;
    }

    public String getTradeType()
    {
        return tradeType;
    }

    public void setCancelFlag(String cancelFlag)
    {
        this.cancelFlag = cancelFlag;
    }

    public String getCancelFlag()
    {
        return cancelFlag;
    }

    public void setStatusFlag(String statusFlag)
    {
        this.statusFlag = statusFlag;
    }

    public String getStatusFlag()
    {
        return statusFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("accountId", getAccountId())
                .append("accountSubId", getAccountSubId())
                .append("pbuId", getPbuId())
                .append("accountType", getAccountType())
                .append("tradeType", getTradeType())
                .append("cancelFlag", getCancelFlag())
                .append("statusFlag", getStatusFlag())
                .toString();
    }
}


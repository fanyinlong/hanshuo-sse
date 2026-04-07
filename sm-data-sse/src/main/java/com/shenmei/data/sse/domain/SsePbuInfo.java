package com.shenmei.data.sse.domain;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * PBU信息对象 sse_pbu_info
 * 
 * @author teabot
 * @date 2025-07-27
 */
@Data
public class SsePbuInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long pkId;

    /** PBU编号 */
    @Excel(name = "PBU编号")
    private String pbuId;

    /** 所属市场 */
    @Excel(name = "所属市场")
    private String mktId;

    /** 控制类型 */
    @Excel(name = "控制类型")
    private String pbuType;

    /** PBU状态 */
    @Excel(name = "PBU状态")
    private String status;

    /** 保证金买入 */
    @Excel(name = "保证金买入")
    private String buyonMarginind;

    /** 融券卖出 */
    @Excel(name = "融券卖出")
    private String sellonBorrowind;

    /** 密码密文 */
    @Excel(name = "密码密文")
    private String loginPwd;

    /** 机构代码 */
    @Excel(name = "机构代码")
    private String instituteId;

    /** 参考分区编号 */
    @Excel(name = "参考分区编号")
    private Long partitionId;

    /** 流速权 */
    @Excel(name = "流速权")
    private Long flowctrlValue;

    /** 联通圈主PBU */
    @Excel(name = "联通圈主PBU")
    private String headPbuid;

    /** 结算会员 */
    @Excel(name = "结算会员")
    private String clearMember;

    /** 机构身份代码 */
    @Excel(name = "机构身份代码")
    private String membCert;

}

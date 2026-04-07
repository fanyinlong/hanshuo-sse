package com.shenmei.data.sse.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.core.domain.BaseEntity;

/**
 * 主机信息对象 sse_server_info
 *
 * @author Song
 * @date 2025-07-29
 */
public class SseServerInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主机编号 */
    @Excel(name = "主机编号")
    private String serverId;

    /** 主机名 */
    @Excel(name = "主机名")
    private String serverName;

    /** 是否有效:N=无效，Y=有效 */
    @Excel(name = "是否有效:N=无效，Y=有效")
    private String isAvaliable;

    /** 测试系统 */
    @Excel(name = "测试系统")
    private String platformId;

    /** 测试环境号 */
    @Excel(name = "测试环境号")
    private String envId;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddress;

    /** 登陆用户名 */
    @Excel(name = "登陆用户名")
    private String loginName;

    /** 密码 */
    @Excel(name = "密码")
    private String authCode;

    /** 加密密钥 */
    private String encryptionKey;

    /** 端口号 */
    @Excel(name = "端口号")
    private String port;

    /** 数据库实例名称 */
    @Excel(name = "数据库实例名称")
    private String instanceName;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setServerId(String serverId)
    {
        this.serverId = serverId;
    }

    public String getServerId()
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

    public void setIsAvaliable(String isAvaliable)
    {
        this.isAvaliable = isAvaliable;
    }

    public String getIsAvaliable()
    {
        return isAvaliable;
    }

    public void setPlatformId(String platformId)
    {
        this.platformId = platformId;
    }

    public String getPlatformId()
    {
        return platformId;
    }

    public void setEnvId(String envId)
    {
        this.envId = envId;
    }

    public String getEnvId()
    {
        return envId;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setAuthCode(String authCode)
    {
        this.authCode = authCode;
    }

    public String getAuthCode()
    {
        return authCode;
    }

    public void setEncryptionKey(String encryptionKey)
    {
        this.encryptionKey = encryptionKey;
    }

    public String getEncryptionKey()
    {
        return encryptionKey;
    }

    @Override
    public String toString() {
        return "SseServerInfo{" +
                "serverId='" + serverId + '\'' +
                ", serverName='" + serverName + '\'' +
                ", isAvaliable='" + isAvaliable + '\'' +
                ", platformId='" + platformId + '\'' +
                ", envId='" + envId + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", loginName='" + loginName + '\'' +
                ", authCode='" + authCode + '\'' +
                ", encryptionKey='" + encryptionKey + '\'' +
                ", port='" + port + '\'' +
                ", instanceName='" + instanceName + '\'' +
                '}';
    }
}

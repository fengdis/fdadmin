package com.fengdis.web.controller.system.cloud;

import java.io.Serializable;

/**
 * 邮件
 */
public class MailConfig implements Serializable
{
    //
    private static final long serialVersionUID = 1356257283938225230L;

    /** 发送方邮件 */
    private String            user;

    /** 邮箱密码 */
    private String            password;

    /** SMTP地址 */
    private String            host;

    /** SMTP端口 */
    private String            port;

    /** 发件用户名 */
    private String            name;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

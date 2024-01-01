package com.angelpro.exception;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description 登录出现的各种错误
 */
public class LoginException extends Exception{

    // 用户账号
    private String account;

    public LoginException(String msg) {
        super(msg);
    }

    public LoginException(String account, String msg) {
        super(msg);
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

package com.example.exception;

/**
 * @author gzx
 * @date 2023/11/5
 * @Description 注册相关异常，包括用户已存在等等
 */
public class RegisterException extends Exception{

    // 用户账号
    private String account;

    public RegisterException(String msg) {
        super(msg);
    }

    public RegisterException(String account, String msg) {
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

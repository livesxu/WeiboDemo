package com.example.weibodemo.WeiboLogin;

public class WeiboAccount {
    private volatile static WeiboAccount account = null;
    /**
     * access_token : 2.00ZJoCFCadEPxBdf6e733c1aU7mwCD
     * expires_in : 157679999
     * uid : 1906816793
     */

    private String access_token;
    private int expires_in;
    private String uid;

    private WeiboAccount(){}
    //创建一个单例,共享登录的用户信息
    public static WeiboAccount getInstance() {

        if (account == null) {

            synchronized (WeiboAccount.class) {

                if (account == null) {

                    account = new WeiboAccount();
                }
            }
        }
        return account;
    }


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}


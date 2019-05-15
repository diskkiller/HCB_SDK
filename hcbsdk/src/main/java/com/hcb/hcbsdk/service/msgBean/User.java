package com.hcb.hcbsdk.service.msgBean;

public class User {
    /**
     * uid : 5a72ce0b1fbb592f44a398d8
     * nickname : 吾皇万岁
     * mobile : 15201561935
     * avatar : https://wx.qlogo
     * .cn/mmopen/vi_32/Q0j4TwGTfTJ3sVy2qibiaBxKDocYQuDCc4rg5qTuOgGdw5fTsxHujRRAyyQoWYdKFibRSqnhKYRfa0EVcS2tpALTg/132
     * storeNo : 5a73de373f3c5e1582879968
     * superNo :
     * agentNo :
     * deviceNo :
     * bean : 0
     * point : 0
     * balance : 0
     * coupon : 0
     * gold : 0
     */

//        headPortrait,nickname,mobile,goldCoin
    private String nickname;
    private String mobile;
    private String headPortrait;
    private double goldCoin;
    private String token;
    private String idCard;//
    private String openId;//
    private boolean isSuccess;//

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", goldCoin=" + goldCoin +
                ", token='" + token + '\'' +
                ", idCard='" + idCard + '\'' +
                ", openId='" + openId + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }




    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getToken() {
        return token;

    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public double getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(double goldCoin) {
        this.goldCoin = goldCoin;
    }


}
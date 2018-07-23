package com.hcb.hcbsdk.service.msgBean;

/**
 * @author WangGuoWei
 * @time 2018/1/30 14:00
 * @des ${TODO}
 * <p>
 * ┽
 * ┽                            _ooOoo_
 * ┽                           o8888888o
 * ┽                           88" . "88
 * ┽                           (| -_- |)
 * ┽                           O\  =  /O
 * ┽                        ____/`---'\____
 * ┽                      .'  \\|     |//  `.
 * ┽                     /  \\|||  :  |||//  \
 * ┽                    /  _||||| -:- |||||-  \
 * ┽                    |   | \\\  -  /// |   |
 * ┽                    | \_|  ''\---/''  |   |
 * ┽                    \  .-\__  `-`  ___/-. /
 * ┽                  ___`. .'  /--.--\  `. . __
 * ┽               ."" '<  `.___\_<|>_/___.'  >'"".
 * ┽              | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * ┽              \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ┽         ======`-.____`-.___\_____/___.-`____.-'======
 * ┽                            `=---='
 * ┽         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * ┽                      佛祖保佑       永无BUG
 * ┽
 * ┽
 * ┽
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class LoginReslut {


    /**
     * status : 10000
     * data : {"uid":"5a72ce0b1fbb592f44a398d8","nickname":"吾皇万岁","mobile":"15201561935",
     * "avatar":"https://wx.qlogo
     * .cn/mmopen/vi_32
     * /Q0j4TwGTfTJ3sVy2qibiaBxKDocYQuDCc4rg5qTuOgGdw5fTsxHujRRAyyQoWYdKFibRSqnhKYRfa0EVcS2tpALTg
     * /132","storeNo":"5a73de373f3c5e1582879968","superNo":"","agentNo":"","deviceNo":"",
     * "bean":0,"point":0,"balance":0,"coupon":0,"gold":0}
     */

    private int status;
    private User data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public static class User {
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

        private String uid;
        private String nickname;
        private String mobile;
        private String avatar;
        private String storeNo;
        private String superNo;
        private String agentNo;
        private String deviceNo;
        private double bean;
        private double point;
        private double balance;
        private double coupon;
        private double gold;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getStoreNo() {
            return storeNo;
        }

        public void setStoreNo(String storeNo) {
            this.storeNo = storeNo;
        }

        public String getSuperNo() {
            return superNo;
        }

        public void setSuperNo(String superNo) {
            this.superNo = superNo;
        }

        public String getAgentNo() {
            return agentNo;
        }

        public void setAgentNo(String agentNo) {
            this.agentNo = agentNo;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public double getBean() {
            return bean;
        }

        public void setBean(double bean) {
            this.bean = bean;
        }

        public double getPoint() {
            return point;
        }

        public void setPoint(double point) {
            this.point = point;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getCoupon() {
            return coupon;
        }

        public void setCoupon(double coupon) {
            this.coupon = coupon;
        }

        public double getGold() {
            return gold;
        }

        public void setGold(double gold) {
            this.gold = gold;
        }

        @Override
        public String toString() {
            return "User{" +
                    "uid='" + uid + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", storeNo='" + storeNo + '\'' +
                    ", superNo='" + superNo + '\'' +
                    ", agentNo='" + agentNo + '\'' +
                    ", deviceNo='" + deviceNo + '\'' +
                    ", bean=" + bean +
                    ", point=" + point +
                    ", balance=" + balance +
                    ", coupon=" + coupon +
                    ", gold=" + gold +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginReslut{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}

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

//        headPortrait,nickname,mobile,goldCoin
        private String nickname;
        private String mobile;
        private String headPortrait;
        private double goldCoin;
        private String token;
        private String idCard;//


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

        @Override
        public String toString() {
            return "User{" +
                    "nickname='" + nickname + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", headPortrait='" + headPortrait + '\'' +
                    ", goldCoin=" + goldCoin +
                    ", token='" + token + '\'' +
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

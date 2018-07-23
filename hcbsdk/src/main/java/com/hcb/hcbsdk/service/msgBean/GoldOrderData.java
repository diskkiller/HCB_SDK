package com.hcb.hcbsdk.service.msgBean;

/**
 * @author WangGuoWei
 * @time 2018/2/4 17:31
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
public class GoldOrderData {


    /**
     * data : {"gameMustCoin":110,"orderId":123,"playCoin":false,"userLeftCoin":10,"beforeUserCoin":150,"afterUserCoin":150,"type":2,"url":"weixin://wxpay/bizpayurl?pr=26pChVr"}
     * message :
     * status : 1
     */

    private DataBean data;
    private String message;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * gameMustCoin : 110
         * orderId : 123
         * playCoin : false
         * userLeftCoin : 10
         * beforeUserCoin : 150
         * afterUserCoin : 150
         * type : 2
         * url : weixin://wxpay/bizpayurl?pr=26pChVr
         */

        private int gameMustCoin;
        private int orderId;
        private boolean playCoin;
        private int userLeftCoin;
        private int beforeUserCoin;
        private int afterUserCoin;
        private int type;
        private String url;

        public int getGameMustCoin() {
            return gameMustCoin;
        }

        public void setGameMustCoin(int gameMustCoin) {
            this.gameMustCoin = gameMustCoin;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public boolean isPlayCoin() {
            return playCoin;
        }

        public void setPlayCoin(boolean playCoin) {
            this.playCoin = playCoin;
        }

        public int getUserLeftCoin() {
            return userLeftCoin;
        }

        public void setUserLeftCoin(int userLeftCoin) {
            this.userLeftCoin = userLeftCoin;
        }

        public int getBeforeUserCoin() {
            return beforeUserCoin;
        }

        public void setBeforeUserCoin(int beforeUserCoin) {
            this.beforeUserCoin = beforeUserCoin;
        }

        public int getAfterUserCoin() {
            return afterUserCoin;
        }

        public void setAfterUserCoin(int afterUserCoin) {
            this.afterUserCoin = afterUserCoin;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

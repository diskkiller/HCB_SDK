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
public class OrderForm {

    /**
     * status : 10000
     * data : {"payId":"5a76d656c6ee40581f3fed6f","total":1000,"num":1000,"balance":0,"cash":0,
     * "bit":1000,"type":"gold","deviceNo":"0123456789ABCDE","appId":"5a7317d1f763e74421a104ab",
     * "appName":"财神来"}
     */

    private int status;
    private Order data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }

    public static class Order {
        /**
         * payId : 5a76d656c6ee40581f3fed6f
         * total : 1000
         * num : 1000
         * balance : 0.0  //已使用的奖金（需要支付2000金币，余额有1000，后台自动扣除奖券相当于1000金币的奖券1元，必须勾选）
         * cash : 0.0  //二维码显示的金额
         * bit : 1000
         * type : gold
         * deviceNo : 0123456789ABCDE
         * appId : 5a7317d1f763e74421a104ab
         * appName : 财神来
         */

        private String payId;
        private int total;
        private int num;
        private double balance;
        private double cash;
        private int bit;
        private String type;
        private String deviceNo;
        private String appId;
        private String appName;
        private boolean charge;

        public boolean getCharge() {
            return charge;
        }

        public void setCharge(boolean charge) {
            this.charge = charge;
        }


        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        private String qrcode;

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getCash() {
            return cash;
        }

        public void setCash(double cash) {
            this.cash = cash;
        }

        public int getBit() {
            return bit;
        }

        public void setBit(int bit) {
            this.bit = bit;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }
    }
}

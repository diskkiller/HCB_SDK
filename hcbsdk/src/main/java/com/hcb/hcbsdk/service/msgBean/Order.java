package com.hcb.hcbsdk.service.msgBean;

/**
 * Create by WangGuoWei on 2018/7/11
 */
public class Order {

    /**
     * data : {"orderId":123,"type":2,"url":"weixin://wxpay/bizpayurl?pr=26pChVr"}
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
         * orderId : 123
         * type : 2
         * url : weixin://wxpay/bizpayurl?pr=26pChVr
         */

        private int orderId;
        private int type;
        private String url;

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
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

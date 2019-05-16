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


    @Override
    public String toString() {
        return "LoginReslut{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}

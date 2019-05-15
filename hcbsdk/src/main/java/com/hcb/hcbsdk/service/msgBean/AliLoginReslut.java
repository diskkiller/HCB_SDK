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
public class AliLoginReslut {


    /**
     * {
     * 	"data": {
     * 		"idCard": "152301198604275050",
     * 		"nickname": "吾皇万岁",
     * 		"mobile": "15201561935",
     * 		"goldCoin": 214,
     * 		"headPortrait": "http:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/Q0j4TwGTfTLuic3icqTw4NNMM7ia3yo0DhCibOMQTfFOH9IcMaibTAKxXZIcLBiaf5pwib1pWmBKAbCWTSjKxOS3vrCkA\/132",
     * 		"token": "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ6aG9uZ3RpaHVhY2FpIiwiaXNzIjoiaHR0cDovL3p0aGMuY29tIiwiZXhwIjoxNTU4NDI5NjkzLCJpYXQiOjE1NTc4MjQ4OTMsInVzZXJJZCI6IjExNCJ9.9lEu0oVl8Dl1Q6aWv7OG-ZNFbotrho5sat5AQYZWkwA"
     *        },
     * 	"message": "",
     * 	"status": 1
     * }
     */


    /**
     * {
     * 	"status": 200,
     * 	"message": "请求成功",
     * 	"body": {
     * 		"nickName": "",
     * 		"openId": "2088512532637507",
     * 		"mobile": "",
     * 		"name": "",
     * 		"token": "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ6aG9uZ3RpaHVhY2FpIiwiaXNzIjoiaHR0cDovL3p0aGMuY29tIiwiZXhwIjoxNTczMzc2MzUwLCJpYXQiOjE1NTc4MjQzNTAsInVzZXJJZCI6IjE2OSJ9.cNp29Uevg4wwQ57BE-ZN20dobTNLc3Csv5wY4Vj_3zg",
     * 		"isSuccess": true
     *        },
     * 	"timestamp": 1557824356289,
     * 	"exception": ""
     * }
     */




    private int status;
    private User body;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getData() {
        return body;
    }

    public void setData(User data) {
        this.body = data;
    }



    @Override
    public String toString() {
        return "LoginReslut{" +
                "status=" + status +
                ", data=" + body +
                '}';
    }
}

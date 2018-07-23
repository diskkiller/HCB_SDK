package com.hcb.hcbsdk.service;

import com.hcb.hcbsdk.service.msgBean.LoginReslut;

/**
 * @author WangGuoWei
 * @time 2018/1/30 14:57
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
public class TuitaData {


    private static TuitaData INSTANCE;

    /**
     * 提供系统调用的构造函数，
     */
    private TuitaData() {
        INSTANCE = this;
    }

    /**
     * 获得实例
     *
     * @return
     */
    public static TuitaData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TuitaData();
        }
        return INSTANCE;
    }

    private LoginReslut.User user;
    public void setUser(LoginReslut.User user){
        this.user = user;
    }

    public LoginReslut.User getUser(){
        return this.user;
    }

}

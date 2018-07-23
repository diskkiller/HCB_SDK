package com.hcb.hcbsdk.logutils.save;


import com.hcb.hcbsdk.logutils.encryption.IEncryption;

/**
 * 保存日志与崩溃信息的接口
 * Created by czm.
 */
public interface ISave {

    void writeLog(String tag, String content);

    void writeCrash(Thread thread, Throwable ex, String tag, String content);

    void setEncodeType(IEncryption encodeType);

}

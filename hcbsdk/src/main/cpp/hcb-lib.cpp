#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_hcb_hcbsdk_util_C_getSocketURL(
        JNIEnv* env,
        jobject /* this */) {
    std::string socket = "http://v3ws.hcb66.com:80?deviceNo=";
    return env->NewStringUTF(socket.c_str());
}
extern "C"
jstring
Java_com_hcb_hcbsdk_util_C_getDebugSocketURL(
        JNIEnv* env,
        jobject /* this */) {
    std::string socket = "http://dev.hcb66.com:7301?deviceNo=";
    return env->NewStringUTF(socket.c_str());
}
extern "C"
jstring
Java_com_hcb_hcbsdk_util_C_getAPIURL(
        JNIEnv* env,
        jobject /* this */) {
    std::string socket = "http://v3api.hcb66.com";
    return env->NewStringUTF(socket.c_str());
}
extern "C"
jstring
Java_com_hcb_hcbsdk_util_C_getDebugapiURL(
        JNIEnv* env,
        jobject /* this */) {
    std::string socket = "http://dev.hcb66.com:7300";
    return env->NewStringUTF(socket.c_str());
}

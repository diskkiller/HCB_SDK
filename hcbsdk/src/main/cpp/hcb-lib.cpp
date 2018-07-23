#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_hcb_hcbsdk_util_C_getSocketURL(
        JNIEnv* env,
        jobject /* this */) {
    std::string socket = "http://m.zthuacai.com:9999?snNo=";
    return env->NewStringUTF(socket.c_str());
}
extern "C"
jstring
Java_com_hcb_hcbsdk_util_C_getDebugSocketURL(
        JNIEnv* env,
        jobject /* this */) {
    std::string socket = "http://mtest.zthuacai.com:9999?snNo=";
    return env->NewStringUTF(socket.c_str());
}
extern "C"
jstring
Java_com_hcb_hcbsdk_util_C_getAPIURL(
        JNIEnv* env,
        jobject /* this */) {
    std::string socket = "http://m.zthuacai.com";
    return env->NewStringUTF(socket.c_str());
}
extern "C"
jstring
Java_com_hcb_hcbsdk_util_C_getDebugapiURL(
        JNIEnv* env,
        jobject /* this */) {
    std::string socket = "http://mtest.zthuacai.com";
    return env->NewStringUTF(socket.c_str());
}

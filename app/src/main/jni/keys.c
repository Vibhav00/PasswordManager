#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_potentialServices_passwordmanager_security_EncryptionDecryption_getKey(JNIEnv *env, jobject instance) {

    // put your secret key here
    return (*env)-> NewStringUTF(env, "abcdefghijklmnop");
}
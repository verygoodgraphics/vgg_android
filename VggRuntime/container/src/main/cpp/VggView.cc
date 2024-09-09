#include <jni.h>
#include <memory>
#include <list>

#include "VggContainer.h"

#pragma clang diagnostic push
#pragma ide diagnostic ignored "UnusedParameter"
std::list<std::shared_ptr<jobject>> event_callbacks;

static std::string JstringToString(JNIEnv *env, jstring jstr) {
    if (jstr == nullptr) {
        return {};
    }
    const char *chars = env->GetStringUTFChars(jstr, nullptr);
    if (chars == nullptr) {
        env->ExceptionClear();
        return {};
    }
    std::string str(chars);
    env->ReleaseStringUTFChars(jstr, chars);
    return str;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeCreateVggView(JNIEnv *env,
                                                                             jobject clazz) {
    auto container = new vgg::VggContainer();
    return reinterpret_cast<jlong>(container);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeDestroyVggView(JNIEnv *env,
                                                                              jobject clazz,
                                                                              jlong native_ptr) {
    if (native_ptr != 0) {
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        container->destroy();
        delete container;
    }
    for (const auto &callback: event_callbacks) {
        env->DeleteGlobalRef(*callback);
    }
    event_callbacks.clear();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeUpdateVggView(JNIEnv *env,
                                                                             jobject clazz,
                                                                             jlong native_ptr,
                                                                             jobject surface,
                                                                             jint format,
                                                                             jint width,
                                                                             jint height) {
    if (native_ptr != 0) {
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        container->updateConfig(env, surface, format, width, height);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeDrawVggView(JNIEnv *env,
                                                                           jobject clazz,
                                                                           jlong native_ptr) {
    if (native_ptr != 0) {
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        container->drawSurface();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeSetModel(JNIEnv *env, jobject clazz,
                                                                        jlong native_ptr,
                                                                        jbyteArray model_content) {
    if (native_ptr != 0) {
        jsize len = env->GetArrayLength(model_content);
        auto buffer = std::make_shared<std::vector<char>>(len / sizeof(char));
        auto *dataBytes = reinterpret_cast<jbyte *>((*buffer).data());
        env->GetByteArrayRegion(model_content, 0, len, dataBytes);
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        container->setModel(buffer);
    }
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeOnButtonUpEvent(JNIEnv *env,
                                                                               jobject clazz,
                                                                               jlong native_ptr,
                                                                               jint button,
                                                                               jint windowX,
                                                                               jint windowY) {
    if (native_ptr != 0) {
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        auto event = UEvent();
        event.type = VGG_MOUSEBUTTONUP;
        event.button.button = button;
        event.button.windowX = windowX;
        event.button.windowY = windowY;
        return container->onEvent(event);
    } else return false;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeOnButtonDownEvent(JNIEnv *env,
                                                                                 jobject clazz,
                                                                                 jlong native_ptr,
                                                                                 jint button,
                                                                                 jint windowX,
                                                                                 jint windowY) {
    if (native_ptr != 0) {
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        auto event = UEvent();
        event.type = VGG_MOUSEBUTTONDOWN;
        event.button.button = button;
        event.button.windowX = windowX;
        event.button.windowY = windowY;
        return container->onEvent(event);
    }
    return false;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeOnMotionEvent(JNIEnv *env,
                                                                             jobject clazz,
                                                                             jlong native_ptr,
                                                                             jint windowX,
                                                                             jint windowY,
                                                                             jint xrel, jint yrel) {
    if (native_ptr != 0) {
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        auto event = UEvent();
        event.type = VGG_MOUSEMOTION;
        event.motion.windowX = windowX;
        event.motion.windowY = windowY;
        event.motion.xrel = xrel;
        event.motion.yrel = yrel;
        return container->onEvent(event);
    }
    return false;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeOnWindowSizeChangedEvent(JNIEnv *env,
                                                                                        jobject clazz,
                                                                                        jlong native_ptr,
                                                                                        jint width,
                                                                                        jint height) {
    if (native_ptr != 0) {
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        auto event = UEvent();
        event.window.type = VGG_WINDOWEVENT;
        event.window.event = VGG_WINDOWEVENT_SIZE_CHANGED;
        event.window.data1 = width;
        event.window.data2 = height;
        event.window.drawableWidth = width;
        event.window.drawableHeight = height;
        container->onEvent(event);
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeGetElementById(JNIEnv *env,
                                                                              jobject clazz,
                                                                              jlong native_ptr,
                                                                              jstring id) {
    if (native_ptr != 0) {
        auto id_str = JstringToString(env, id);
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        auto element = container->getElementById(id_str);
        return env->NewStringUTF(element.c_str());
    }
    return nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeUpdateElement(JNIEnv *env,
                                                                             jobject clazz,
                                                                             jlong native_ptr,
                                                                             jstring id,
                                                                             jstring element) {
    if (native_ptr != 0) {
        auto id_str = JstringToString(env, id);
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        auto element_str = JstringToString(env, element);
        container->updateElement(id_str, element_str);
    }
}

JavaVM *g_VM;

extern "C"
JNIEXPORT void JNICALL
Java_com_verygoodgraphics_android_container_view_VggView_nativeAddEventListener(JNIEnv *env,
                                                                                jobject clazz,
                                                                                jlong native_ptr,
                                                                                jobject vggEventListener) {
    if (native_ptr != 0) {
        auto container = reinterpret_cast<vgg::VggContainer *>(native_ptr);
        auto callback = std::make_shared<jobject>(env->NewGlobalRef(vggEventListener));
        event_callbacks.push_back(callback);
        env->GetJavaVM(&g_VM);
        container->addEventListener([callback](const std::string &type, const std::string &id,
                                               const std::string &path) {
            JNIEnv *env;
            int getEnvStat = g_VM->GetEnv((void **) &env, JNI_VERSION_1_6);
            auto mNeedDetach = JNI_FALSE;
            if (getEnvStat == JNI_EDETACHED) {
                if (g_VM->AttachCurrentThread(&env, nullptr) != 0) {
                    return;
                }
                mNeedDetach = JNI_TRUE;
            }

            auto javaClass = env->GetObjectClass(*callback);
            auto javaCallbackId = env->GetMethodID(javaClass, "onEvent",
                                                   "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
            auto jType = env->NewStringUTF(type.c_str());
            auto jId = env->NewStringUTF(id.c_str());
            auto jPath = env->NewStringUTF(path.c_str());
            env->CallVoidMethod(*callback, javaCallbackId, jType, jId, jPath);

            if (mNeedDetach) {
                g_VM->DetachCurrentThread();
            }
        });
    }
}
#pragma clang diagnostic pop
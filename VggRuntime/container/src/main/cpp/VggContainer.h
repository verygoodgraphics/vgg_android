#ifndef VGG_ANDROID_VGG_CONTAINER_H
#define VGG_ANDROID_VGG_CONTAINER_H

#include <cstdint>
#include <memory>
#include <string_view>
#include <string>
#include <optional>
#include <list>

#include <jni.h>
#include <android/native_window.h>

#include <VGG/AndroidContainer.hpp>

namespace vgg {

    class VggContainer {
    public:
        VggContainer();

        ~VggContainer();

        // no copy
        VggContainer(const VggContainer &) = delete;

        VggContainer &operator=(const VggContainer &) = delete;

        void updateConfig(JNIEnv *env, jobject surface, jint format, jint width, jint height);

        void drawSurface();

        void destroy();

        void setFitToViewportEnabled(bool enabled);

        std::string designDoc() const;

        std::string designDocValueAt(const std::string &path);

        std::string getElementById(const std::string &id);

        void updateElement(const std::string &id, const std::string &json);

        void addEventListener(const VGG::EventListener &eventListener);

        void *cppContainer();

        void setModel(std::shared_ptr<std::vector<char>> buffer);

        bool onEvent(UEvent event);

    private:

        void updateDelegate();

        void updateEventListener();

        class ASurfaceHolder {
        public:
            ASurfaceHolder() = default;

            explicit ASurfaceHolder(ANativeWindow *window) : raw(window) {}

            ASurfaceHolder(const ASurfaceHolder &) = delete;

            ASurfaceHolder &operator=(const ASurfaceHolder &) = delete;

            ~ASurfaceHolder() {
                if (raw != nullptr) {
                    ANativeWindow_release(raw);
                }
            }

            ANativeWindow *raw = nullptr;
        };

        int mPixelFormat = 0;
        int mHeight = 0;
        int mWidth = 0;
        bool mDestroyed = false;
        std::unique_ptr<ASurfaceHolder> mNativeWindow;
        std::shared_ptr<VGG::AndroidContainer> mCppContainer;
        std::shared_ptr<VGG::ISdk> mVggSdk;
        std::shared_ptr<std::vector<char>> mModelContent;
        std::list<VGG::EventListener> mEventListener;
    };

}

#endif //VGG_ANDROID_VGG_CONTAINER_H

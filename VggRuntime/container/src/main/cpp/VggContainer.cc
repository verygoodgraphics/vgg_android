#include "VggContainer.h"

#include <android/native_window_jni.h>

#include <android/log.h>

#include <utility>

#include "VggEventAdapter.h"

namespace vgg {

    void
    VggContainer::updateConfig(JNIEnv *env, jobject surface, jint format, jint width, jint height) {
        if (ANativeWindow *s = surface ? ANativeWindow_fromSurface(env, surface) : nullptr; s !=
                                                                                            nullptr) {
            mNativeWindow = std::make_unique<ASurfaceHolder>(s);
            mCppContainer->setView(mNativeWindow->raw);
            if (mModelContent != nullptr) {
                updateDelegate();
            }
        } else {
            mNativeWindow.reset(nullptr);
            mCppContainer->setView(nullptr);
        }
        mPixelFormat = static_cast<int>(format);
        mWidth = static_cast<int>(width);
        mHeight = static_cast<int>(height);
    }

    void VggContainer::drawSurface() {
        if (mNativeWindow == nullptr) {
            return;
        }
        if (mModelContent != nullptr) {
            mCppContainer->run();
        }
    }

    void VggContainer::destroy() {
        if (mDestroyed) {
            return;
        }
        mCppContainer->setView(nullptr);
        mNativeWindow.reset(nullptr);
    }

    VggContainer::VggContainer() {
        mCppContainer = std::make_shared<VGG::AndroidContainer>();
        mVggSdk = mCppContainer->sdk();
        vgg::android::adapter::VggEventAdapter::setup();
    }

    VggContainer::~VggContainer() {
        if (!mDestroyed) {
            destroy();
        }
    }

    void VggContainer::setFitToViewportEnabled(bool enabled) {
        mVggSdk->setFitToViewportEnabled(enabled);
    }

    std::string VggContainer::designDoc() const {
        return mVggSdk->designDocument();
    }

    std::string VggContainer::designDocValueAt(const std::string &path) {
        return mVggSdk->designDocumentValueAt(path);
    }

    std::string VggContainer::getElementById(const std::string &id) {
        return mVggSdk->getElement(id);
    }

    void VggContainer::updateElement(const std::string &id, const std::string &json) {
        mVggSdk->updateElement(id, json);
    }

    void *VggContainer::cppContainer() {
        return mCppContainer.get();
    }

    void VggContainer::updateDelegate() {
        if (mModelContent != nullptr && mNativeWindow != nullptr) {
            __android_log_print(ANDROID_LOG_INFO, "VggContainer", "this=%p, window=%p",
                                this,
                                mNativeWindow->raw);
            mCppContainer->load(*mModelContent);
            updateEventListener();
        }
    }

    void VggContainer::addEventListener(const VGG::EventListener &eventListener) {
        mEventListener.push_back(eventListener);
        updateEventListener();
    }

    void VggContainer::updateEventListener() {
        mCppContainer->setEventListener(
                [this](const std::string &type, const std::string &id, const std::string &path) {
                    __android_log_print(ANDROID_LOG_INFO, "VggContainer", "Event: %s, %s, %s",
                                        type.c_str(), id.c_str(),
                                        path.c_str());
                    for (const auto &eventListener: mEventListener) {
                        eventListener(type, id, path);
                    }
                }
        );
    }

    void VggContainer::setModel(std::shared_ptr<std::vector<char>> buffer) {
        mModelContent = std::move(buffer);
        updateDelegate();
    }

    bool VggContainer::onEvent(UEvent event) {
        return mCppContainer->onEvent(event);
    }

}

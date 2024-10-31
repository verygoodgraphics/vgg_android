/*
 * Copyright 2023-2024 VeryGoodGraphics LTD <bd@verygoodgraphics.com>
 *
 * Licensed under the VGG License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.verygoodgraphics.com/licenses/LICENSE-1.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "VggEventAdapter.h"

#include "VGG/Keycode.hpp"

#include <unordered_map>

namespace vgg::android::adapter {
    namespace {

        constexpr auto kReleased = 0;
        constexpr auto kPressed = 1;

        auto &getKeyMap() {
            static std::unordered_map<int, EVGGScancode> s_keyMap;
            return s_keyMap;
        }

        auto &getKeyState() {
            static uint8_t s_keyState[VGG_NUM_SCANCODES];
            return s_keyState;
        }

        int vggSendKeyboardKey(uint8_t state, EVGGScancode scancode) {
            if (scancode == VGG_SCANCODE_UNKNOWN || scancode >= VGG_NUM_SCANCODES) {
                return 0;
            }

            switch (state) {
                case kPressed:
                case kReleased:
                    break;
                default:
                    return 0;
            }

            getKeyState()[scancode] = state;

            return 0;
        }

    } // namespace

    EVGGKeymod VggEventAdapter::getModState() {
        return VGG_KMOD_NONE;
    }

    uint8_t *VggEventAdapter::getKeyboardState(int *nums) {
        if (nums) {
            *nums = VGG_NUM_SCANCODES;
        }

        return getKeyState();
    }

    void VggEventAdapter::setup() {
        auto eventApi = std::make_unique<VggEventAdapter>();
        EventManager::registerEventAPI(std::move(eventApi));
    }

}

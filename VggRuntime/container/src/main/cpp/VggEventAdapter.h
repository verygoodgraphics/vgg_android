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

#ifndef VggEventAdapter_h
#define VggEventAdapter_h

#include "VGG/Event.hpp"
#include "VGG/EventAPI.hpp"

namespace vgg::android::adapter {

    class VggEventAdapter : public EventAPI {
    public:
        static void setup();

        EVGGKeymod getModState() override;

        uint8_t *getKeyboardState(int *nums) override;
    };

}

#endif /* VggEventAdapter_h */

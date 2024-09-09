cmake_minimum_required(VERSION 3.21)
project(vgg_android)

enable_language(CXX C)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_C_STANDARD 11)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fPIC -Werror=return-type -Wno-invalid-offsetof")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fPIC -Werror=return-type")

set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,-z,max-page-size=16384 -Wl,--no-allow-shlib-undefined,--no-undefined -Wl,-z,defs,-z,now,-z,relro")

add_library(vgg_android SHARED
        VggView.cc
        VggContainer.cc
        VggRuntime.cc
        VggEventAdapter.cc
)

set_target_properties(vgg_android PROPERTIES
        CXX_EXTENSIONS OFF
        POSITION_INDEPENDENT_CODE ON
)

target_include_directories(vgg_android PUBLIC "../../../../external/${ANDROID_ABI}/include")
target_include_directories(vgg_android PUBLIC "../../../../external/${ANDROID_ABI}")
target_include_directories(vgg_android PUBLIC "../../../../external/json/include")

target_link_directories(vgg_android PRIVATE "../../../../external/${ANDROID_ABI}/lib")

target_link_libraries(
        vgg_android
        PRIVATE
        vgg_container
        zip
        node

        log
        android
        jnigraphics
        GLESv2
        EGL
        z
)

@file:JvmName("main")

package com.verygoodgraphics.android.container

import android.util.Log

fun initVggRuntime() {
    //System.loadLibrary("c++_shared");
    System.loadLibrary("node")
    System.loadLibrary("vgg_container")
    System.loadLibrary("vgg_android")
    Log.d("VGG", "VGG libraries loaded")
}
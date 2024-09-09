package com.verygoodgraphics.android.container.listener

interface VggEventListener {
    fun onEvent(type: String, id: String, path: String)
}
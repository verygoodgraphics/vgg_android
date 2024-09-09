package com.example.verygoodgraphics.android.demo.impl

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.example.verygoodgraphics.android.demo.BuildConfig
import com.example.verygoodgraphics.android.demo.R
import com.example.verygoodgraphics.android.demo.VggDemoDelegate
import com.example.verygoodgraphics.android.demo.VggDemos.DemoInfo
import com.example.verygoodgraphics.android.demo.activity.VggDemoViewActivity
import com.verygoodgraphics.android.container.listener.VggEventListener
import com.verygoodgraphics.android.container.view.VggView
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


private const val FILE_PATH = "counter_without_js.daruma"
private const val TAG = "vgg_counter"

class VggCounter : VggDemoDelegate {
    override fun setup(activity: VggDemoViewActivity, vggView: VggView, demoInfo: DemoInfo) {
        vggView.vggModelUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(BuildConfig.APPLICATION_ID)
            .path(R.raw.counter_without_js.toString())
            .build()
        vggView.addVggEventListener(object : VggEventListener {
            override fun onEvent(type: String, id: String, path: String) {
                Log.d(TAG, "onEvent: $type $id $path")
                when (path) {
                    "#counterButtonText", "#counterButton" -> {
                        when (type) {
                            "mousedown" -> {
                                val element = vggView.Elements()["#count"] ?: ""
                                Log.d(TAG, "element: $element")
                                var o = Json.parseToJsonElement(element).jsonObject
                                var count: Int =
                                    (o["content"]?.jsonPrimitive?.content ?: "0").toInt()
                                count += 1
                                o = JsonObject(o + ("content" to JsonPrimitive(count.toString())))
                                vggView.elements["content"] = o.toString()
                            }
                        }
                    }
                }
            }
        })
    }
}

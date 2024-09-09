package com.example.verygoodgraphics.android.demo.impl;

import android.content.ContentResolver;
import android.net.Uri;

import com.example.verygoodgraphics.android.demo.BuildConfig;
import com.example.verygoodgraphics.android.demo.R;
import com.example.verygoodgraphics.android.demo.VggDemoDelegate;
import com.example.verygoodgraphics.android.demo.VggDemos;
import com.example.verygoodgraphics.android.demo.activity.VggDemoViewActivity;
import com.verygoodgraphics.android.container.view.VggView;

public class VggJsCounter implements VggDemoDelegate {

    private static final String FILE_PATH = "counter_with_js.daruma";

    @Override
    public void setup(VggDemoViewActivity activity, VggView vggView, VggDemos.DemoInfo demoInfo) {
        vggView.setVggModelUri(new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(BuildConfig.APPLICATION_ID)
                .path(String.valueOf(R.raw.counter_with_js))
                .build());
    }

}

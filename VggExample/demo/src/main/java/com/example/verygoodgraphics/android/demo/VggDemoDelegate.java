package com.example.verygoodgraphics.android.demo;

import com.example.verygoodgraphics.android.demo.activity.VggDemoViewActivity;
import com.verygoodgraphics.android.container.view.VggView;

public interface VggDemoDelegate {

    void setup(VggDemoViewActivity activity, VggView vggView, VggDemos.DemoInfo demoInfo);

}

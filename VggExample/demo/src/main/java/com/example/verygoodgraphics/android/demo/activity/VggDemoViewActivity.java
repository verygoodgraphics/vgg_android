package com.example.verygoodgraphics.android.demo.activity;

import static com.example.verygoodgraphics.android.demo.Utils.unsafeThrow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.example.verygoodgraphics.android.demo.VggDemoDelegate;
import com.example.verygoodgraphics.android.demo.VggDemos;
import com.verygoodgraphics.android.container.view.VggView;

public class VggDemoViewActivity extends Activity {

    public static final String KEY_DEMO_INDEX = "VggDemoViewActivity.KEY_DEMO_INDEX";

    private int mDemoIndex = -1;
    private VggView mVggView;

    public static Intent createIntent(Context ctx, int demoIndex) {
        Intent intent = new Intent(ctx, VggDemoViewActivity.class);
        intent.putExtra(KEY_DEMO_INDEX, demoIndex);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // no title
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        if (intent != null) {
            mDemoIndex = intent.getIntExtra(KEY_DEMO_INDEX, -1);
        }
        if (mDemoIndex < 0 || mDemoIndex >= VggDemos.DEMO_INFOS.size()) {
            finish();
            return;
        }
        mVggView = new VggView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(mVggView, params);
        setContentView(frameLayout);
        setupVggDemo();
    }

    private void setupVggDemo() {
        VggDemoDelegate mDelegate;
        try {
            mDelegate = VggDemos.DEMO_INFOS.get(mDemoIndex).delegateClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw unsafeThrow(e);
        }

        mDelegate.setup(this, mVggView, VggDemos.DEMO_INFOS.get(mDemoIndex));
    }

}

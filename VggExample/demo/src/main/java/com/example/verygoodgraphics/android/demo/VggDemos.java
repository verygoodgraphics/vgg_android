package com.example.verygoodgraphics.android.demo;

import java.util.ArrayList;

public class VggDemos {

    public static final ArrayList<DemoInfo> DEMO_INFOS = new ArrayList<>();

    static {
        DEMO_INFOS.add(new DemoInfo() {{
            name = "Counter with code in Java";
            delegateClass = com.example.verygoodgraphics.android.demo.impl.VggCounter.class;
        }});
        DEMO_INFOS.add(new DemoInfo() {{
            name = "Counter with code in JavaScript";
            delegateClass = com.example.verygoodgraphics.android.demo.impl.VggJsCounter.class;
        }});
        DEMO_INFOS.add(new DemoInfo() {{
            name = "Prototype interactions";
            delegateClass = com.example.verygoodgraphics.android.demo.impl.VggJsExample.class;
        }});
    }

    private VggDemos() {
    }

    public static class DemoInfo {
        public String name;
        public Class<? extends VggDemoDelegate> delegateClass;
    }

}

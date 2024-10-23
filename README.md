# VGG container for Android

An android runtime library for [VGG](https://vgg.cool/).

:fire: This project is participating OSPP 2024 Summer of Code! Please checkout it [here](https://summer-ospp.ac.cn/org/prodetail/246540051?lang=en&list=pro).

## Getting Started

1. [Build](https://github.com/verygoodgraphics/vgg_runtime?tab=readme-ov-file#android-building-example) VggContainer.
2. Open the VggRuntime in Android Studio.

> [!NOTE]
> If you don't want to compile all architectures while developing, you can comment the unwanted arch in the line
> 
> ```kotlin
> ndk.abiFilters.addAll(listOf("x86_64", "arm64-v8a"))
> ```
> 
> in file `VggRuntime/container/build.gradle.kts`

## Usage

Firstly, You should initialize the VggRuntime before any VggViews are created. For example, in your costume Application class:

```java
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initVggRuntime();
        // any other initialization code
    }

}
```

### Using in XML

Put your VGG model file in the `res/raw` directory, and use the `app:model` attribute to specify the model file.

```xml
<com.verygoodgraphics.android.container.view.VggView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:model="@raw/counter_with_js" />
```

### Using programmatically

You can use [Android Uri](https://developer.android.com/reference/android/net/Uri) to specify the VGG model programmatically.

```java
public class VggDemoViewActivity extends Activity {
    
    private VggView mVggView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        mVggView = new VggView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(mVggView, params);
        vggView.vggModelUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(BuildConfig.APPLICATION_ID)
                .path(R.raw.counter_without_js.toString())
                .build();
        setContentView(frameLayout);
    }
}
```

### API Reference

- `String getVggModelUri()`: Get the Uri of the VGG model.
- `void setVggModelUri(Uri)`: Set the Uri of the VGG model.
- `ByteArray getVggModelBuffer()`: Get the buffered VGG model.
- `void setVggModelBuffer(ByteArray)`: Set the buffered VGG model.
- `String getElements().get(String)`: Get the elements of the VGG model by ID.
- `void getElements().set(String, String)`: Set the elements of the VGG model by ID.
- `void addVggEventListener(EventListener)`: Add a listener to VGG events.

## Example
You can run our Android example app in this repository, open the VggRuntime in Android Studio and select the demo module.

## Contribution

By contributing to this project, you shall by default agree with the
[DCO document](./DCO) automatically, otherwise please don't send PR.

## License

MIT License

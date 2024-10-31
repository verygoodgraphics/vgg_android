package com.verygoodgraphics.android.container.view

import android.content.ContentResolver
import android.content.Context
import android.graphics.PixelFormat
import android.net.Uri
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import com.verygoodgraphics.android.container.R
import com.verygoodgraphics.android.container.data.VggEvent
import com.verygoodgraphics.android.container.data.VggMouseButtonDownEvent
import com.verygoodgraphics.android.container.data.VggMouseButtonUpEvent
import com.verygoodgraphics.android.container.data.VggMouseMotionEvent
import com.verygoodgraphics.android.container.data.VggWindowSizeChangedEvent
import com.verygoodgraphics.android.container.listener.VggEventListener
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private const val TARGET_PIXEL_FORMAT = PixelFormat.RGBA_8888

class VggView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null
) :
    GLSurfaceView(mContext, attrs), SurfaceHolder.Callback, GLSurfaceView.Renderer {
    private var mNativePtr: Long = 0
    private var mHasGlContext = false
    private var mVggEventListeners: ArrayList<VggEventListener> = arrayListOf()
    var vggModelBuffer: ByteArray? = null
        set(value) {
            value?.let {
                if (mNativePtr != 0L) {
                    nativeSetModel(mNativePtr, it)
                }
            }
            field = value
        }
    var vggModelUri: Uri? = null
        set(value) {
            vggModelBuffer = value?.let { uri ->
                mContext.contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }
            }
            field = value
        }

    init {
        setEGLContextClientVersion(3)
        setEGLConfigChooser(8, 8, 8, 8, 0, 8)
        // keep it in sync with the native side
        holder.setFormat(PixelFormat.RGBA_8888)
        holder.addCallback(this)
        setRenderer(this)

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.VggView, 0, 0)
        val id = typedArray.getResourceId(R.styleable.VggView_model, -1)
        if (id != -1) {
            vggModelUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(mContext.packageName)
                .path(id.toString())
                .build()
        } else {
            typedArray.getString(R.styleable.VggView_model)?.let {
                vggModelUri = Uri.parse(it)
            }
        }
        typedArray.recycle()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        super.surfaceDestroyed(holder)
        if (mNativePtr != 0L) {
            nativeUpdateVggView(mNativePtr, null, TARGET_PIXEL_FORMAT, 0, 0)
        }
        //mHasGlContext = false
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
//        setRenderMode(RENDERMODE_WHEN_DIRTY);
        renderMode = RENDERMODE_CONTINUOUSLY
        mHasGlContext = true
        if (mNativePtr == 0L) {
            mNativePtr = nativeCreateVggView()
            nativeAddEventListener(mNativePtr, object : VggEventListener {
                override fun onEvent(type: String, id: String, path: String) {
                    for (vggEventListener in mVggEventListeners) {
                        vggEventListener.onEvent(type, id, path)
                    }
                }
            })
        }
        vggModelBuffer?.let { nativeSetModel(mNativePtr, it) }
    }

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        val holder = holder
        if (holder.surface.isValid) {
            if (mWidth != width || mHeight != height) {
                val vggEvent = VggWindowSizeChangedEvent(width, height)
                //TODO: Will crash in vgg_runtime
                //onVggEvent(vggEvent)
            }
            mWidth = width
            mHeight = height
            nativeUpdateVggView(mNativePtr, holder.surface, TARGET_PIXEL_FORMAT, width, height)
        }
    }

    override fun onDrawFrame(gl: GL10) {
        if (mNativePtr != 0L) {
            nativeDrawVggView(mNativePtr)
        }
    }

    override fun invalidate() {
        super.invalidate()
        if (mNativePtr != 0L) {
            requestRender()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mNativePtr != 0L) {
            nativeDestroyVggView(mNativePtr)
            mNativePtr = 0
        }
        mVggEventListeners.clear()
    }

    override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        if (isVisible) {
            this.onResume()
        } else {
            this.onPause()
        }
    }

    private var previousX: Int = 0
    private var previousY: Int = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                val vggEvent = VggMouseButtonUpEvent(x, y)
                return onVggEvent(vggEvent)
            }

            MotionEvent.ACTION_DOWN -> {
                val vggEvent = VggMouseButtonDownEvent(x, y)
                return onVggEvent(vggEvent)
            }

            MotionEvent.ACTION_MOVE -> {
                val vggEvent = VggMouseMotionEvent(x, y, x - previousX, y - previousY)
                previousX = x
                previousY = y
                return onVggEvent(vggEvent)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun onVggEvent(event: VggEvent): Boolean {
        if (mNativePtr == 0L) {
            return false
        }
        return when (event) {
            is VggMouseButtonDownEvent -> {
                nativeOnButtonDownEvent(mNativePtr, event.button, event.windowX, event.windowY)
            }

            is VggMouseButtonUpEvent -> {
                nativeOnButtonUpEvent(mNativePtr, event.button, event.windowX, event.windowY)
            }

            is VggMouseMotionEvent -> {
                nativeOnMotionEvent(
                    mNativePtr,
                    event.windowX,
                    event.windowY,
                    event.xRel,
                    event.yRel
                )
            }

            is VggWindowSizeChangedEvent -> {
                nativeOnWindowSizeChangedEvent(mNativePtr, event.width, event.height)
                return true
            }
        }
    }

    inner class Elements {
        operator fun get(id: String): String? {
            return nativeGetElementById(mNativePtr, id)
        }

        operator fun set(id: String, element: String) {
            nativeUpdateElement(mNativePtr, id, element)
        }
    }

    val elements: Elements = Elements()

    fun addVggEventListener(vggEventListener: VggEventListener) {
        mVggEventListeners.add(vggEventListener)
    }

    private external fun nativeCreateVggView(): Long

    private external fun nativeDestroyVggView(nativePtr: Long)

    private external fun nativeUpdateVggView(
        nativePtr: Long,
        surface: Surface?,
        format: Int,
        width: Int,
        height: Int
    )

    private external fun nativeDrawVggView(nativePtr: Long)

    private external fun nativeSetModel(nativePtr: Long, modelContent: ByteArray)

    private external fun nativeOnButtonUpEvent(
        nativePtr: Long,
        button: Int,
        windowX: Int,
        windowY: Int
    ): Boolean

    private external fun nativeOnButtonDownEvent(
        nativePtr: Long,
        button: Int,
        windowX: Int,
        windowY: Int
    ): Boolean

    private external fun nativeOnMotionEvent(
        nativePtr: Long,
        windowX: Int,
        windowY: Int,
        xRel: Int,
        yRel: Int
    ): Boolean

    private external fun nativeOnWindowSizeChangedEvent(nativePtr: Long, width: Int, height: Int)

    private external fun nativeGetElementById(nativePtr: Long, id: String): String?

    private external fun nativeUpdateElement(nativePtr: Long, id: String, element: String)

    private external fun nativeAddEventListener(nativePtr: Long, vggEventListener: VggEventListener)
}

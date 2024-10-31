package com.verygoodgraphics.android.container.data

enum class VggEventType {
    VGG_MOUSE_BUTTON_DOWN,
    VGG_MOUSE_MOTION,
    VGG_MOUSE_BUTTON_UP,
    VGG_WINDOW_SIZE_CHANGED,
}

sealed class VggEvent(
    open val type: VggEventType
)

sealed class VggMouseButtonEvent @JvmOverloads constructor(
    override val type: VggEventType,
    open val windowX: Int,
    open val windowY: Int,
    open val button: Int = 1,
) : VggEvent(type)

data class VggMouseButtonDownEvent @JvmOverloads constructor(
    override val windowX: Int,
    override val windowY: Int,
    override val button: Int = 1,
) : VggMouseButtonEvent(VggEventType.VGG_MOUSE_BUTTON_DOWN, windowX, windowY, button)

data class VggMouseButtonUpEvent @JvmOverloads constructor(
    override val windowX: Int,
    override val windowY: Int,
    override val button: Int = 1,
) : VggMouseButtonEvent(VggEventType.VGG_MOUSE_BUTTON_UP, windowX, windowY, button)

data class VggMouseMotionEvent(
    val windowX: Int,
    val windowY: Int,
    val xRel: Int,
    val yRel: Int
) : VggEvent(VggEventType.VGG_MOUSE_MOTION)

data class VggWindowSizeChangedEvent(
    val width: Int,
    val height: Int,
) : VggEvent(VggEventType.VGG_WINDOW_SIZE_CHANGED)

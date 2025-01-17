package org.example.project

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseListener
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener


object GlobalEventListener : NativeKeyListener, NativeMouseListener, NativeMouseMotionListener {
    private var keyCount = 0
    private var mouseCount = 0

    init {
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(this)
        GlobalScreen.addNativeMouseListener(this)
        GlobalScreen.addNativeMouseMotionListener(this)
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        keyCount++
        println("Key pressed: ${NativeKeyEvent.getKeyText(e.keyCode)} | Total: $keyCount")
    }

    override fun nativeMouseClicked(e: NativeMouseEvent) {
        mouseCount++
        println("Mouse clicked at (${e.x}, ${e.y}) | Total: $mouseCount")
    }

    override fun nativeMouseMoved(e: NativeMouseEvent) {
        println("Mouse moved to (${e.x}, ${e.y})")
    }

    // Implement other necessary listeners as needed
}

package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseListener
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Global Event Counter",
    ) {
        DesktopApp()
    }
}

@Composable
fun DesktopApp() {
    var keyCount by remember { mutableStateOf(0) }
    var mouseCount by remember { mutableStateOf(0) }
    var isListening by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    DisposableEffect(isListening) {
        if (isListening) {
            try {
                GlobalScreen.registerNativeHook()
                GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
                    override fun nativeKeyPressed(e: NativeKeyEvent?) {
                        keyCount++
                    }

                    override fun nativeKeyReleased(e: NativeKeyEvent?) {}
                    override fun nativeKeyTyped(e: NativeKeyEvent?) {}
                })
                GlobalScreen.addNativeMouseListener(object : NativeMouseListener {
                    override fun nativeMouseClicked(e: NativeMouseEvent?) {
                        mouseCount++
                    }

                    override fun nativeMousePressed(e: NativeMouseEvent?) {}
                    override fun nativeMouseReleased(e: NativeMouseEvent?) {}
                })
                GlobalScreen.addNativeMouseMotionListener(object : NativeMouseMotionListener {
                    override fun nativeMouseMoved(e: NativeMouseEvent?) {}
                    override fun nativeMouseDragged(e: NativeMouseEvent?) {}
                })
            } catch (ex: NativeHookException) {
                errorMessage = "Failed to enable global hooks: ${ex.message}"
                isListening = false
            }
        } else {
            try {
                GlobalScreen.unregisterNativeHook()
            } catch (ex: NativeHookException) {
                errorMessage = "Failed to unregister global hooks: ${ex.message}"
            }
        }
        onDispose {
            if (isListening) {
                try {
                    GlobalScreen.unregisterNativeHook()
                } catch (ex: NativeHookException) {
                    errorMessage = "Failed to unregister global hooks: ${ex.message}"
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Global Keyboard/Mouse Event Counter", modifier = Modifier.padding(16.dp))

        Row(modifier = Modifier.padding(8.dp)) {
            Button(
                onClick = { isListening = true },
                enabled = !isListening,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Start Listening")
            }
            Button(
                onClick = { isListening = false },
                enabled = isListening,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Stop Listening")
            }
        }

        Text("Keyboard Events: $keyCount", modifier = Modifier.padding(8.dp))
        Text("Mouse Events: $mouseCount", modifier = Modifier.padding(8.dp))

        if (errorMessage.isNotEmpty()) {
            BasicText(
                text = "Error: $errorMessage",
                modifier = Modifier.padding(16.dp),
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "If the app does not work, grant Accessibility permissions:",
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = ::openAccessibilitySettings) {
            Text("Open Accessibility Settings")
        }
    }
}

fun openAccessibilitySettings() {
    val runtime = Runtime.getRuntime()
    try {
        runtime.exec("osascript -e 'tell application \"System Preferences\" to reveal anchor \"Privacy_Accessibility\" of pane \"com.apple.preference.security\"'")
        runtime.exec("osascript -e 'tell application \"System Preferences\" to activate'")
    } catch (ex: Exception) {
        println("Failed to open Accessibility settings: ${ex.message}")
    }
}

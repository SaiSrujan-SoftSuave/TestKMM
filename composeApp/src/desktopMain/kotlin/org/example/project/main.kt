package org.example.project

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseListener
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener
import jdk.javadoc.internal.doclets.toolkit.util.DocPath.parent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Global Event Counter",
    ) {
        ScreenCaptureAPP()
    }
}

@Composable
fun ScreenCaptureAPP(modifier: Modifier = Modifier) {
    var message by remember { mutableStateOf("Click the button to capture screenshot!") }
    var screenshotBitmap by remember { mutableStateOf<BufferedImage?>(null) }
    var taken by remember { mutableStateOf(false) }
    val window = ComposeWindow()

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicText(text = message)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                screenshotBitmap = captureFullScreenScreenshot()
//                screenshotBitmap = screenshot?.asImageBitmap()
                message = "Screenshot captured!"
            }) {
                Text("Capture Screenshot $screenshotBitmap")
            }
            Spacer(modifier = Modifier.height(16.dp))
//            screenshotBitmap?.let {
//                Image(bitmap = it, contentDescription = "Screenshot", modifier = Modifier.size(300.dp))
//            }
        }
    }
}
fun captureFullScreenScreenshot(): java.awt.image.BufferedImage? {
    return try {
        // Get the entire screen dimensions
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val screenRect = Rectangle(screenSize)

        // Create Robot instance
        val robot = Robot()

        // Capture the entire screen
        val screenshot = robot.createScreenCapture(screenRect)

        // Save the image to a file
        val file = File("full_screenshot.png")
        ImageIO.write(screenshot, "png", file)


        screenshot
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun captureScreenshot(window: ComposeWindow):Boolean {
    try {
        // Get screen dimensions
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val screenRect = Rectangle(screenSize)

        // Create Robot instance
        val robot = Robot()

        // Capture the screen
        val screenshot = robot.createScreenCapture(screenRect)

        // Save the image to a file
        val file = File("screenshot2.png")
        return ImageIO.write(screenshot, "png", file)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

@OptIn(ExperimentalTime::class)
@Composable
@Preview
fun StopwatchApp() {
    var elapsedTime by remember { mutableStateOf(Duration.ZERO) }
    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Stopwatch",
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = elapsedTime.toString(),
                fontSize = 48.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    if (!isRunning) {
                        isRunning = true
                        scope.launch {
                            while (isRunning) {
                                delay(1000L) // 1 second interval
                                elapsedTime += 1.seconds
                            }
                        }
                    }
                }) {
                    Text(if (isRunning) "Pause" else "Start")
                }
                Button(
                    onClick = {
                        isRunning = false
                        elapsedTime = Duration.ZERO
                    }
                ) {
                    Text("Reset")
                }
            }
        }
    }
}


//fun formatDuration(duration: kotlin.time.Duration): String {
//    val totalSeconds = duration.inWholeSeconds
//    val minutes = totalSeconds / 60
//    val seconds = totalSeconds % 60
//    return String.format("%02d:%02d", minutes, seconds)
//}

@Composable
fun DesktopClicksApp() {
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

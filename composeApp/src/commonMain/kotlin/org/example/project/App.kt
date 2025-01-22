package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import testkmm.composeapp.generated.resources.Res
import testkmm.composeapp.generated.resources.bustle_white_logo


import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(InternalResourceApi::class, ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    var elapsedTime by remember { mutableStateOf(Duration.ZERO) }
    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        Image(
            painter = painterResource(resource = Res.drawable.bustle_white_logo),
            contentDescription = "asdf"
        )
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


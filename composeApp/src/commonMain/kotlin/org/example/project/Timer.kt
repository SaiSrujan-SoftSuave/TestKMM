package org.example.project

import kotlinx.coroutines.*
import kotlin.time.Duration

class Timer(private val duration: Duration, private val tickInterval: Duration) {
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun start(onTick: suspend (Duration) -> Unit, onComplete: suspend () -> Unit) {
        job?.cancel()
        job = scope.launch {
            var remainingTime = duration
            while (remainingTime > Duration.ZERO) {
                onTick(remainingTime)
                delay(tickInterval.inWholeMilliseconds)
                remainingTime -= tickInterval
            }
            onComplete()
        }
    }

    fun stop() {
        job?.cancel()
    }
}



class StopWatch(private val duration: Duration, private val tickInterval: Duration) {
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun start(onTick: suspend (Duration) -> Unit, onComplete: suspend () -> Unit) {
        job?.cancel()
        job = scope.launch {
            var remainingTime = duration
            while (remainingTime > Duration.ZERO) {
                onTick(remainingTime)
                delay(tickInterval.inWholeMilliseconds)
                remainingTime -= tickInterval
            }
            onComplete()
        }
    }

    fun stop() {
        job?.cancel()
    }
}

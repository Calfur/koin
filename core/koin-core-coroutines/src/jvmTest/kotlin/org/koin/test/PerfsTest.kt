package org.koin.test

import org.koin.core.lazyModules
import kotlin.test.Test
import org.koin.core.logger.*
import org.koin.core.time.Timer
import org.koin.core.waitAllStartJobs
import org.koin.dsl.koinApplication

class PerfsTest {

    @Test
    fun perfModule400_module_perfs() {
        (1..3).forEach { runPerfs() }
    }

    @Test
    fun perfModule400_lazy_module_perfs() {
        (1..3).forEach { runPerfs(isLazy = true) }
    }

    private fun runPerfs(log: Logger = EmptyLogger(), isLazy: Boolean = false) {
        val timerStart = Timer.start()
        val app = koinApplication {
            if (isLazy) {
                lazyModules(perfModule400())
            } else {
                modules(perfModule400().value)
            }
        }
        timerStart.stop()
        println("perf400 - start in ${timerStart.getTimeInMillis()}")

        val koin = app.koin

        if (isLazy) {
            koin.waitAllStartJobs()
        }

        val timerRun = Timer.start()
        koin.get<Perfs.A27>()
        koin.get<Perfs.B31>()
        koin.get<Perfs.C12>()
        koin.get<Perfs.D42>()
        timerRun.stop()
        println("perf400 - executed in ${timerRun.getTimeInMillis()}")

        app.close()
    }
}
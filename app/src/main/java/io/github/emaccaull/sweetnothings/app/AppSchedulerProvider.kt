/*
 * Copyright (C) 2019 Emmanuel MacCaull
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.emaccaull.sweetnothings.app

import io.github.emaccaull.sweetnothings.core.SchedulerProvider
import io.github.emaccaull.sweetnothings.core.concurrent.NamedThreadFactory
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

/** Scheduler provider used by the app in production.  */
internal class AppSchedulerProvider : SchedulerProvider {
    companion object {
        private val IO_SCHEDULER: Scheduler

        init {
            val cpuCount: Int = Runtime.getRuntime().availableProcessors()
            val corePoolSize: Int = max(2, min(cpuCount - 1, 4))
            val maxPoolSize: Int = cpuCount * 2 + 1
            val keepAliveSeconds = 30L

            val ioExecutor: Executor = ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                LinkedBlockingQueue(),
                NamedThreadFactory("IO")
            )

            IO_SCHEDULER = Schedulers.from(ioExecutor)
        }
    }

    override fun io(): Scheduler {
        return IO_SCHEDULER
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}

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
package io.github.emaccaull.sweetnothings.init

import android.app.Application
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

class StartupTasksExtensionPointImpl @Inject internal constructor(
    private val tasksSet: Set<@JvmSuppressWildcards StartupTask>
) : StartupTasksExtensionPoint {

    override val tasks: List<StartupTask>
        get() {
            val tasks: MutableList<StartupTask> = ArrayList(tasksSet.size + 2)
            tasks.add(LogTask("Started running initialization tasks"))
            tasks.addAll(tasksSet)
            tasks.add(LogTask("Finished running initialization tasks"))
            return Collections.unmodifiableList(tasks)
        }

    private class LogTask(private val message: String) : StartupTask {
        override fun run(app: Application) {
            logger.info(message)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(StartupTasksExtensionPointImpl::class.java)
    }
}

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
package io.github.emaccaull.sweetnothings.data.init

import android.app.Application
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource
import io.github.emaccaull.sweetnothings.glue.IOScheduler
import io.github.emaccaull.sweetnothings.init.StartupTask
import io.reactivex.Scheduler
import javax.inject.Inject

/**
 * Populates the data store with stock sweet nothings.
 */
class DataSourceStartupTask @Inject internal constructor(
    private val stockMessageProvider: StockMessageProvider,
    private val dataSource: MessageDataSource,
    @param:IOScheduler private val ioScheduler: Scheduler
) : StartupTask {

    override fun run(app: Application) {
        dataSource.createIfNotPresent(*stockMessageProvider.messages)
            .subscribeOn(ioScheduler)
            .subscribe()
    }
}

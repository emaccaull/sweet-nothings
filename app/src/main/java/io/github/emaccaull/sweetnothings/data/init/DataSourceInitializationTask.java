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

package io.github.emaccaull.sweetnothings.data.init;

import android.app.Application;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.glue.IOScheduler;
import io.github.emaccaull.sweetnothings.init.InitializationTask;
import io.reactivex.Scheduler;

import javax.inject.Inject;

/**
 * Populates the data store with stock sweet nothings.
 */
public class DataSourceInitializationTask implements InitializationTask {

    private final StockMessageProvider stockMessageProvider;
    private final MessageDataSource dataSource;
    private final Scheduler ioScheduler;

    @Inject
    DataSourceInitializationTask(
            StockMessageProvider stockMessageProvider,
            MessageDataSource dataSource,
            @IOScheduler Scheduler ioScheduler) {
        this.stockMessageProvider = stockMessageProvider;
        this.dataSource = dataSource;
        this.ioScheduler = ioScheduler;
    }

    @Override
    public void run(Application app) {
        dataSource.createIfNotPresent(stockMessageProvider.getMessages())
                .subscribeOn(ioScheduler)
                .subscribe();
    }
}

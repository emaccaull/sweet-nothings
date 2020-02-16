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

package io.github.emaccaull.sweetnothings.glue;

import androidx.annotation.VisibleForTesting;
import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.GetSweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.MarkUsed;
import io.github.emaccaull.sweetnothings.data.init.StockMessageProvider;
import io.github.emaccaull.sweetnothings.init.InitializationTasksPlugin;

import static androidx.annotation.VisibleForTesting.NONE;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Manages component dependencies to allow easy object creation.
 */
public final class Injection {

    private static final Injection INSTANCE = new Injection();

    private Configuration config;

    public static GetRandomSweetNothing provideGetRandomSweetNothing() {
        return new GetRandomSweetNothing(provideMessageDataSource());
    }

    public static GetSweetNothing provideGetSweetNothing() {
        return new GetSweetNothing(provideMessageDataSource());
    }

    public static MarkUsed provideMarkUsed() {
        return new MarkUsed(provideMessageDataSource());
    }

    public static SchedulerProvider provideSchedulerProvider() {
        return getConfiguration().schedulerProvider();
    }

    public static MessageDataSource provideMessageDataSource() {
        return getConfiguration().messageDataSource();
    }

    public static InitializationTasksPlugin provideInitializationTasksPlugin() {
        return getConfiguration().initializationTasksPlugin();
    }

    public static StockMessageProvider provideStockMessageProvider() {
        return getConfiguration().stockMessageProvider();
    }

    public static synchronized void setConfiguration(Configuration config) {
        INSTANCE.config = config;
    }

    public static synchronized Configuration getConfiguration() {
        return checkNotNull(INSTANCE.config, "config is null");
    }

    @VisibleForTesting(otherwise = NONE)
    public static void reset() {
        INSTANCE.config = null;
    }

    private Injection() {
    }
}

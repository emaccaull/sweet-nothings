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

import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.data.init.StockMessageProvider;
import io.github.emaccaull.sweetnothings.init.InitializationTaskPlugins;

import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Lazily gets instances from a delegate configuration and reuses them.
 */
class InstanceCachingConfiguration implements Configuration {

    private final Lazy<SchedulerProvider> lazySchedulerProvider;
    private final Lazy<MessageDataSource> lazyMessageDataSource;
    private final Lazy<InitializationTaskPlugins> lazyInitializationTaskPlugins;
    private final Lazy<StockMessageProvider> lazyStockMessageProvider;

    InstanceCachingConfiguration(Configuration delegate) {
        // TODO: selectively hold refs based on @Singleton annotation of instance's class.
        lazySchedulerProvider = new Lazy<>(notNull(delegate::schedulerProvider));
        lazyMessageDataSource = new Lazy<>(notNull(delegate::messageDataSource));
        lazyInitializationTaskPlugins = new Lazy<>(notNull(delegate::initializationTaskPlugins));
        lazyStockMessageProvider = new Lazy<>(notNull(delegate::stockMessageProvider));
    }

    @Override
    public SchedulerProvider schedulerProvider() {
        return lazySchedulerProvider.get();
    }

    @Override
    public MessageDataSource messageDataSource() {
        return lazyMessageDataSource.get();
    }

    @Override
    public InitializationTaskPlugins initializationTaskPlugins() {
        return lazyInitializationTaskPlugins.get();
    }

    @Override
    public StockMessageProvider stockMessageProvider() {
        return lazyStockMessageProvider.get();
    }

    private static <T> Provider<T> notNull(Provider<T> provider) {
        return () -> checkNotNull(provider.get(), "provider returned null value");
    }
}

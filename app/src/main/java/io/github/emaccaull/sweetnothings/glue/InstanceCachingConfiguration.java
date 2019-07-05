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

import java.util.HashMap;
import java.util.Map;
import javax.inject.Provider;

/**
 * Lazily gets instances from a delegate configuration and reuses them.
 */
class InstanceCachingConfiguration implements Configuration {

    private final Map<String, Provider<?>> providers = new HashMap<>();

    InstanceCachingConfiguration(Configuration delegate) {
        put(SchedulerProvider.class, new Lazy<>(delegate::schedulerProvider));
        put(MessageDataSource.class, new Lazy<>(delegate::messageDataSource));
        put(InitializationTaskPlugins.class, new Lazy<>(delegate::initializationTaskPlugins));
        put(StockMessageProvider.class, delegate::stockMessageProvider); // New instance each time
    }

    @Override
    public SchedulerProvider schedulerProvider() {
        return get(SchedulerProvider.class);
    }

    @Override
    public MessageDataSource messageDataSource() {
        return get(MessageDataSource.class);
    }

    @Override
    public InitializationTaskPlugins initializationTaskPlugins() {
        return get(InitializationTaskPlugins.class);
    }

    @Override
    public StockMessageProvider stockMessageProvider() {
        return get(StockMessageProvider.class);
    }

    private <T> void put(Class<T> clazz, Provider<? extends T> provider) {
        providers.put(getKey(clazz), provider);
    }

    @SuppressWarnings("unchecked")
    private <T> T get(Class<T> clazz) {
        String key = getKey(clazz);
        Provider<T> provider = (Provider<T>)providers.get(key);
        if (provider == null) {
            throw new IllegalArgumentException("No provider for " + clazz);
        }
        return provider.get();
    }

    private static String getKey(Class<?> clazz) {
        String key = clazz.getCanonicalName();
        if (key == null) {
            throw new IllegalArgumentException("clazz");
        }
        return key;
    }
}

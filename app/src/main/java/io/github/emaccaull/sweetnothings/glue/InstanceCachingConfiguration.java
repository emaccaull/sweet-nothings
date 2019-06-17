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

/**
 * Lazily gets instances from a delegate configuration and reuses them.
 */
class InstanceCachingConfiguration implements Configuration {

    private final Lazy<SchedulerProvider> lazySchedulerProvider;
    private final Lazy<MessageDataSource> lazyMessageDataSource;

    InstanceCachingConfiguration(Configuration delegate) {
        this.lazySchedulerProvider = new Lazy<>(delegate::schedulerProvider);
        this.lazyMessageDataSource = new Lazy<>(delegate::messageDataSource);
    }

    @Override
    public SchedulerProvider schedulerProvider() {
        return lazySchedulerProvider.get();
    }

    @Override
    public MessageDataSource messageDataSource() {
        return lazyMessageDataSource.get();
    }
}

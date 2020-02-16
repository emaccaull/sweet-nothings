/*
 * Copyright (C) 2020 Emmanuel MacCaull
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

package io.github.emaccaull.sweetnothings;

import io.github.emaccaull.sweetnothings.app.ProdConfiguration;
import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.github.emaccaull.sweetnothings.core.TrampolineSchedulerProvider;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.data.InMemoryMessageDataSource;
import io.github.emaccaull.sweetnothings.data.init.StockMessageProvider;
import io.github.emaccaull.sweetnothings.init.InitializationTasksPlugin;
import io.github.emaccaull.sweetnothings.init.InitializationTasksPluginImpl;
import io.github.emaccaull.sweetnothings.testsupport.EmptyStockMessageProvider;

/**
 * Dependency config for large "unit" tests (more like integration tests).
 */
public class UnitTestConfiguration implements ProdConfiguration {

    @Override
    public SchedulerProvider schedulerProvider() {
        return TrampolineSchedulerProvider.INSTANCE;
    }

    @Override
    public MessageDataSource messageDataSource() {
        return new InMemoryMessageDataSource();
    }

    @Override
    public InitializationTasksPlugin initializationTasksPlugin() {
        return new InitializationTasksPluginImpl();
    }

    @Override
    public StockMessageProvider stockMessageProvider() {
        return new EmptyStockMessageProvider();
    }
}

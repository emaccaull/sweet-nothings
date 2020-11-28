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
package io.github.emaccaull.sweetnothings.testsupport

import android.app.Application
import androidx.annotation.RestrictTo
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.github.emaccaull.sweetnothings.app.ExtModule
import io.github.emaccaull.sweetnothings.app.ProdConfiguration
import io.github.emaccaull.sweetnothings.core.SchedulerProvider
import io.github.emaccaull.sweetnothings.core.TrampolineSchedulerProvider
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource
import io.github.emaccaull.sweetnothings.data.InMemoryMessageDataSource
import io.github.emaccaull.sweetnothings.data.init.StockMessageProvider
import io.github.emaccaull.sweetnothings.init.StartupTasksModule
import javax.inject.Singleton

/** Dependencies for instrumentation tests.  */
@RestrictTo(RestrictTo.Scope.TESTS)
@Singleton
@Component(modules = [TestConfiguration.TestModule::class, ExtModule::class, StartupTasksModule::class])
interface TestConfiguration : ProdConfiguration {

    @Module
    object TestModule {
        @Provides
        @Singleton
        fun schedulerProvider(): SchedulerProvider {
            // We're using an in-memory DB, so we don't need to run on a background thread.
            return TrampolineSchedulerProvider
        }

        @Provides
        @Singleton
        fun messageDataSource(): MessageDataSource {
            return InMemoryMessageDataSource()
        }

        @Provides
        @Singleton
        fun stockMessageProvider(): StockMessageProvider {
            return EmptyStockMessageProvider()
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application?): Builder
        fun build(): TestConfiguration
    }
}

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

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.github.emaccaull.sweetnothings.core.SchedulerProvider
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource
import io.github.emaccaull.sweetnothings.data.init.StockMessageProvider
import io.github.emaccaull.sweetnothings.data.local.LocalMessageDataSource
import io.github.emaccaull.sweetnothings.glue.Configuration
import io.github.emaccaull.sweetnothings.init.StartupTasksModule
import io.github.emaccaull.sweetnothings.ui.ondemand.OnDemandBuilder.ParentComponent
import javax.inject.Singleton

/** Configuration used for production builds.  */
@Singleton
@Component(modules = [ProdConfiguration.ProdModule::class, ExtModule::class, StartupTasksModule::class])
interface ProdConfiguration : Configuration, ParentComponent {

    fun inject(app: SweetNothingsApp)

    @Module
    object ProdModule {
        @Singleton
        @Provides
        fun provideSchedulerProvider(): SchedulerProvider {
            return AppSchedulerProvider()
        }

        @Singleton
        @Provides
        fun provideMessageDataSource(application: Application): MessageDataSource {
            return LocalMessageDataSource(application)
        }

        @Singleton
        @Provides
        fun provideStockMessageProvider(application: Application): StockMessageProvider {
            return AppStockMessageProvider(application)
        }
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ProdConfiguration
    }
}

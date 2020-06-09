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

package io.github.emaccaull.sweetnothings.app;

import android.app.Application;
import dagger.Binds;
import dagger.BindsInstance;
import dagger.Provides;
import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.data.init.StockMessageProvider;
import io.github.emaccaull.sweetnothings.data.local.LocalMessageDataSource;
import io.github.emaccaull.sweetnothings.glue.Configuration;
import io.github.emaccaull.sweetnothings.init.InitializationTasksModule;
import io.github.emaccaull.sweetnothings.init.InitializationTasksPlugin;
import io.github.emaccaull.sweetnothings.init.InitializationTasksPluginImpl;
import io.github.emaccaull.sweetnothings.ui.ondemand.OnDemandBuilder;

import javax.inject.Singleton;

/** Configuration used for production builds. */
@Singleton
@dagger.Component(
        modules = {
            ProdConfiguration.ProdModule.class,
            ExtModule.class,
            InitializationTasksModule.class
        })
public interface ProdConfiguration extends Configuration, OnDemandBuilder.ParentComponent {

    void inject(SweetNothingsApp app);

    @dagger.Module
    abstract class ProdModule {

        @Singleton
        @Provides
        static SchedulerProvider provideSchedulerProvider() {
            return new AppSchedulerProvider();
        }

        @Singleton
        @Provides
        static MessageDataSource provideMessageDataSource(Application application) {
            return new LocalMessageDataSource(application);
        }

        @Singleton
        @Provides
        static StockMessageProvider provideStockMessageProvider(Application application) {
            return new AppStockMessageProvider(application);
        }
    }

    @dagger.Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        ProdConfiguration build();
    }
}

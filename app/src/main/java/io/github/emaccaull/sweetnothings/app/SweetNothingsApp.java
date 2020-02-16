/*
 * Copyright (C) 2018 Emmanuel MacCaull
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

import androidx.multidex.MultiDexApplication;
import io.github.emaccaull.sweetnothings.init.InitializationTask;
import io.github.emaccaull.sweetnothings.init.InitializationTasksPlugin;

import javax.inject.Inject;

/**
 * Sweet Nothings application class.
 *
 * Application initialization happens here.
 */
public class SweetNothingsApp extends MultiDexApplication {

    private ProdConfiguration configuration;

    @Inject
    InitializationTasksPlugin initializationTasksPlugin;

    @Override
    public void onCreate() {
        super.onCreate();
        configureDependencies();
        initialize();
    }

    private void configureDependencies() {
        configuration = createConfiguration();
        configuration.inject(this);
    }

    protected ProdConfiguration createConfiguration() {
        return DaggerProdConfiguration.builder().application(this).build();
    }

    public ProdConfiguration getConfiguration() {
        return configuration;
    }

    private void initialize()  {
        for (InitializationTask task : initializationTasksPlugin.getTasks()) {
            task.run(this);
        }
    }
}

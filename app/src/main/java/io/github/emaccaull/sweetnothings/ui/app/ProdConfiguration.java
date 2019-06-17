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

package io.github.emaccaull.sweetnothings.ui.app;

import android.app.Application;
import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.data.local.LocalMessageDataSource;
import io.github.emaccaull.sweetnothings.glue.Configuration;

/**
 * Configuration used for production builds.
 */
class ProdConfiguration implements Configuration {

    private final Application context;

    ProdConfiguration(Application context) {
        this.context = context;
    }

    @Override
    public SchedulerProvider schedulerProvider() {
        return null;
    }

    @Override
    public MessageDataSource messageDataSource() {
        return new LocalMessageDataSource(context);
    }
}

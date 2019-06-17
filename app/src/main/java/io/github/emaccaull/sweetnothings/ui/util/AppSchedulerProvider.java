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

package io.github.emaccaull.sweetnothings.ui.util;

import android.os.AsyncTask;
import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Scheduler provider used by the app in production.
 */
public class AppSchedulerProvider implements SchedulerProvider {

    // Use the AsyncTask thread pool since it has a reasonable config for a mobile device.
    private final Scheduler diskIOScheduler = Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);

    @Override
    public Scheduler diskIO() {
        return diskIOScheduler;
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}

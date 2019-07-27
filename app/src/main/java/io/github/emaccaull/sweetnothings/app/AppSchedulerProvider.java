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

import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.github.emaccaull.sweetnothings.core.concurrent.NamedThreadFactory;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Scheduler provider used by the app in production.
 */
class AppSchedulerProvider implements SchedulerProvider {

    private static final Scheduler DISK_IO_SCHEDULER;
    static {
        int cpuCount         = Runtime.getRuntime().availableProcessors();
        int corePoolSize     = Math.max(2, Math.min(cpuCount - 1, 4));
        int maxPoolSize      = cpuCount * 2 + 1;
        int keepAliveSeconds = 30;

        final Executor diskIOExecutor = new ThreadPoolExecutor(
                corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(128), new NamedThreadFactory("DiskIO"));

        DISK_IO_SCHEDULER = Schedulers.from(diskIOExecutor);
    }

    @Override
    public Scheduler diskIO() {
        return DISK_IO_SCHEDULER;
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}

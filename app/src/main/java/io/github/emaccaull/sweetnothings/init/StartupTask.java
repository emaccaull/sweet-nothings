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

package io.github.emaccaull.sweetnothings.init;

import android.app.Application;
import androidx.annotation.MainThread;

/**
 * Plugin point which allows arbitrary tasks to run after app launch, before any Activity is
 * created. Each startup task is run exactly one time each time the application process is created.
 *
 * <p> Tasks must be short lived since they are executed on the main thread. If a task needs to
 * perform a long running action, it should do so on a background thread. If the task is expected to
 * run for more than a second or is essential, the task should delegate to a JobService or use
 * WorkManager.
 */
public interface StartupTask {

    /**
     * Executes this task for the given app context on the UI thread.
     */
    @MainThread
    void run(Application app);
}

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

public class InitializationTasksPluginImpl implements InitializationTasksPlugin {
    private static final Logger logger =
            LoggerFactory.getLogger(InitializationTasksPluginImpl.class);

    private final Set<InitializationTask> tasksSet;

    @Inject
    InitializationTasksPluginImpl(Set<InitializationTask> tasks) {
        this.tasksSet = tasks;
    }

    @Override
    public List<InitializationTask> getTasks() {
        List<InitializationTask> tasks = new ArrayList<>(tasksSet.size() + 2);
        tasks.add(new LogTask("Started running initialization tasks"));
        tasks.addAll(tasksSet);
        tasks.add(new LogTask("Finished running initialization tasks"));
        return Collections.unmodifiableList(tasks);
    }

    private static final class LogTask implements InitializationTask {
        private final String message;

        LogTask(String message) {
            this.message = message;
        }

        @Override
        public void run(Application app) {
            logger.info(message);
        }
    }
}

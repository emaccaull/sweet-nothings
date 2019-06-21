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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InitializationTaskPluginsImpl implements InitializationTaskPlugins {
    private static final Logger logger =
            LoggerFactory.getLogger(InitializationTaskPluginsImpl.class);

    private final List<InitializationTask> tasks = new ArrayList<>();

    @Override
    public void load() {
        // Task implementations will not actually be here.
        tasks.add(app -> logger.debug("Started running initialization tasks"));
        tasks.add(app -> logger.debug("Finished running initialization tasks"));
    }

    @Override
    public List<InitializationTask> getTasks() {
        return tasks;
    }
}

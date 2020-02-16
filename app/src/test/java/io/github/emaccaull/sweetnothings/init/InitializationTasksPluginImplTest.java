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

import io.github.emaccaull.sweetnothings.UnitTestConfiguration;
import io.github.emaccaull.sweetnothings.data.init.DataSourceInitializationTask;
import io.github.emaccaull.sweetnothings.glue.Injection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;

public class InitializationTasksPluginImplTest {

    private final InitializationTasksPluginImpl plugins = new InitializationTasksPluginImpl();

    @Before
    public void setUp() {
        Injection.setConfiguration(new UnitTestConfiguration());
    }

    @After
    public void tearDown() {
        Injection.reset();
    }

    @Test
    public void load() {
        // When
        List<InitializationTask> tasks = plugins.getTasks();

        // Then
        assertThat(tasks, hasItem(instanceOf(DataSourceInitializationTask.class)));
    }
}

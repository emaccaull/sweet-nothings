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

import io.github.emaccaull.sweetnothings.data.init.DataSourceStartupTask;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;

public class StartupTasksExtensionPointImplTest {

    private final Set<StartupTask> tasks = new HashSet<>();

    @Test
    public void load() {
        // Given
        tasks.add(mock(DataSourceStartupTask.class));
        StartupTasksExtensionPointImpl plugins = new StartupTasksExtensionPointImpl(tasks);

        // When
        List<StartupTask> tasks = plugins.getTasks();

        // Then
        assertThat(tasks, hasItem(instanceOf(DataSourceStartupTask.class)));
    }
}

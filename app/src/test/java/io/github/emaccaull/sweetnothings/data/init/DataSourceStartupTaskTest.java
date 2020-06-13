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

package io.github.emaccaull.sweetnothings.data.init;

import android.app.Application;

import io.github.emaccaull.sweetnothings.BaseTestFixture;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceStartupTaskTest extends BaseTestFixture {

    private final Scheduler scheduler = Schedulers.trampoline();

    @Mock
    private StockMessageProvider messageProvider;

    @Mock
    private MessageDataSource dataSource;

    @Mock
    private Application application;

    private DataSourceStartupTask task;

    @Before
    public void setUp() {
        task = new DataSourceStartupTask(messageProvider, dataSource, scheduler);
    }

    @Test
    public void run() {
        String[] messages = { "Hello", "<3 u" };
        when(messageProvider.getMessages()).thenReturn(messages);

        when(dataSource.createIfNotPresent(any())).thenReturn(Single.just(Collections.emptyList()));

        task.run(application);

        verify(dataSource).createIfNotPresent(messages);
    }
}

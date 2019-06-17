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

package io.github.emaccaull.sweetnothings.glue;

import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.github.emaccaull.sweetnothings.core.TrampolineSchedulerProvider;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.GetSweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.MarkUsed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InjectionTest {

    @Mock
    private Configuration config;

    @Mock
    private MessageDataSource messageDataSource;

    private SchedulerProvider schedulerProvider = TrampolineSchedulerProvider.INSTANCE;

    @Test
    public void getInstance() {
        assertThat(Injection.getInstance(), is(notNullValue()));
    }

    @Test
    public void dependenciesAreSharedInstances() {
        // Given that each invocation of the config yields a new instance
        when(config.messageDataSource()).then(invocation -> mock(MessageDataSource.class));
        when(config.schedulerProvider()).then(invocation -> mock(SchedulerProvider.class));
        Injection.getInstance().setConfiguration(config);

        // When requesting data
        Configuration config = Injection.getInstance().getConfiguration();

        // Then the config should cache the first value
        MessageDataSource aMds = config.messageDataSource();
        MessageDataSource bMds = config.messageDataSource();
        assertThat(aMds, is(bMds));

        SchedulerProvider aSp = config.schedulerProvider();
        SchedulerProvider bSp = config.schedulerProvider();
        assertThat(aSp, is(bSp));
    }

    @Test
    public void provideGetRandomSweetNothing() {
        when(config.messageDataSource()).thenReturn(messageDataSource);

        // Given
        Injection.getInstance().setConfiguration(config);

        // When
        GetRandomSweetNothing grsw = Injection.provideGetRandomSweetNothing();

        // Then
        assertThat(grsw, is(notNullValue()));
    }

    @Test
    public void provideGetSweetNothing() {
        when(config.messageDataSource()).thenReturn(messageDataSource);

        // Given
        Injection.getInstance().setConfiguration(config);

        // When
        GetSweetNothing gsn = Injection.provideGetSweetNothing();

        // Then
        assertThat(gsn, is(notNullValue()));
    }

    @Test
    public void provideMarkUsed() {
        when(config.messageDataSource()).thenReturn(messageDataSource);

        // Given
        Injection.getInstance().setConfiguration(config);

        // When
        MarkUsed mu = Injection.provideMarkUsed();

        // Then
        assertThat(mu, is(notNullValue()));
    }

    @Test
    public void provideSchedulerProvider() {
        when(config.schedulerProvider()).thenReturn(schedulerProvider);

        // Given
        Injection.getInstance().setConfiguration(config);

        // When
        SchedulerProvider sp = Injection.provideSchedulerProvider();

        // Then
        assertThat(sp, is(schedulerProvider));
    }
}

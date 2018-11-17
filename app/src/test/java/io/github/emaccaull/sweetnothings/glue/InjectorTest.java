/*
 * Copyright (C) 2018 Emmanuel MacCaull
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

import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class InjectorTest {

    @Mock
    private MessageDataSource messageDataSource;

    @After
    public void tearDown() {
        Injector.reset();
    }

    @Test
    public void provideMessageDataSource() {
        Injector.setModule(new TestModule());

        MessageDataSource result = Injector.provideMessageDataSource();

        assertThat(result, is(messageDataSource));
    }

    @Test
    public void provideMessageDataSource_isSingleton() {
        Injector.setModule(new NewInstanceModule());
        MessageDataSource ds1 = Injector.provideMessageDataSource();
        assertThat(Injector.provideMessageDataSource(), is(ds1));

        // Change app component and message data source should reset.
        Injector.setModule(new NewInstanceModule());
        MessageDataSource ds2 = Injector.provideMessageDataSource();
        assertThat(Injector.provideMessageDataSource(), is(ds2));
        assertThat(ds2, is(not(ds1)));
    }

    @Test(expected = IllegalStateException.class)
    public void provideMessageDataSource_whenAppComponentNotSet() {
        Injector.provideMessageDataSource();
    }

    @Test
    public void provideGetRandomSweetNothing() {
        Injector.setModule(new NewInstanceModule());

        GetRandomSweetNothing useCase = Injector.provideGetRandomSweetNothing();

        assertThat(useCase, is(notNullValue()));
    }

    class TestModule implements Injector.Module {
        @Override
        public MessageDataSource messageDataSource() {
            return messageDataSource;
        }

        @Override
        public GetRandomSweetNothing getRandomSweetNothing(MessageDataSource dataSource) {
            return mock(GetRandomSweetNothing.class);
        }
    }

    class NewInstanceModule implements Injector.Module {
        @Override
        public MessageDataSource messageDataSource() {
            return mock(MessageDataSource.class);
        }

        @Override
        public GetRandomSweetNothing getRandomSweetNothing(MessageDataSource dataSource) {
            return mock(GetRandomSweetNothing.class);
        }
    }
}

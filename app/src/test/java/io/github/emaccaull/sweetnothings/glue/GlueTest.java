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
public class GlueTest {

    @Mock
    private MessageDataSource messageDataSource;

    @After
    public void tearDown() {
        Glue.reset();
    }

    @Test
    public void provideMessageDataSource() {
        Glue.setDataAccessComponent(new TestDataAccessComponent());

        MessageDataSource result = Glue.provideMessageDataSource();

        assertThat(result, is(messageDataSource));
    }

    @Test
    public void provideMessageDataSource_isSingleton() {
        Glue.setDataAccessComponent(new NewInstanceDataAccessComponent());
        MessageDataSource ds1 = Glue.provideMessageDataSource();
        assertThat(Glue.provideMessageDataSource(), is(ds1));

        // Change app component and message data source should reset.
        Glue.setDataAccessComponent(new NewInstanceDataAccessComponent());
        MessageDataSource ds2 = Glue.provideMessageDataSource();
        assertThat(Glue.provideMessageDataSource(), is(ds2));
        assertThat(ds2, is(not(ds1)));
    }

    @Test(expected = IllegalStateException.class)
    public void provideMessageDataSource_whenAppComponentNotSet() {
        Glue.provideMessageDataSource();
    }

    @Test
    public void provideGetRandomSweetNothing() {
        Glue.setDataAccessComponent(new NewInstanceDataAccessComponent());

        GetRandomSweetNothing useCase = Glue.provideGetRandomSweetNothing();

        assertThat(useCase, is(notNullValue()));
    }

    class TestDataAccessComponent implements DataAccessComponent {
        @Override
        public MessageDataSource messageDataSource() {
            return messageDataSource;
        }
    }

    class NewInstanceDataAccessComponent implements DataAccessComponent {
        @Override
        public MessageDataSource messageDataSource() {
            return mock(MessageDataSource.class);
        }
    }
}

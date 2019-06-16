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

package io.github.emaccaull.sweetnothings.core.data;

import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.data.internal.Ids;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(MockitoJUnitRunner.class)
public class AbstractMessageDataSourceTest {

    @Mock
    private Ids ids;

    private TestMessageDataSource dataSource = new TestMessageDataSource();

    @Test
    public void insertIfNotPresent() {
        //when(ids.nextUuid()).thenReturn("idXYZ");

        // Given that there is a sweet nothing in the database
        String existing = "A message";
        SweetNothing sweetNothing = dataSource.insert(existing).blockingGet();
        assertThat(sweetNothing, is(notNullValue()));

        // When adding new items and one is a duplicate
        List<SweetNothing> sweetNothings =
                dataSource.insertIfNotPresent("Something", existing, "hello!").blockingGet();

        // Then 2 of the three should have been add
        assertThat(sweetNothings, hasSize(2));
        assertThat(sweetNothings.get(0).getMessage(), is("Something"));
        assertThat(sweetNothings.get(1).getMessage(), is("hello!"));
    }

    static class TestMessageDataSource extends AbstractMessageDataSource {
        Set<String> existingMessages = new HashSet<>();

        @Override
        protected Set<String> getExistingMessages() {
            return existingMessages;
        }

        @Override
        public Maybe<SweetNothing> fetchRandomMessage(MessageFilter filter) {
            return null;
        }

        @Override
        public Maybe<SweetNothing> fetchMessage(String id) {
            return null;
        }

        @Override
        public Completable markUsed(String id) {
            return null;
        }

        @Override
        public Single<SweetNothing> insert(String message) {
            existingMessages.add(message);
            return Single.just(SweetNothing.builder("123").message(message).build());
        }

        @Override
        public Single<Integer> size() {
            return null;
        }
    }
}

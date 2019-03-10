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

package io.github.emaccaull.sweetnothings.core.usecase;

import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarkUsedTest {

    @Mock
    private MessageDataSource messageDataSource;

    private MarkUsed markUsed;

    @Before
    public void setUp() {
        markUsed = new MarkUsed(messageDataSource);
    }

    @Test
    public void apply() {
        // Given that the data source will complete successfully when an item is marked as used.
        when(messageDataSource.markUsed("theId")).thenReturn(Completable.complete());

        // When applying the use case
        TestObserver<Void> observer = markUsed.apply("theId").test();

        // Then the observer should complete
        observer.assertComplete();
    }
}

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

package io.github.emaccaull.sweetnothings.ui.framework;

import io.reactivex.disposables.Disposable;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class RxViewModelTest {

    @Test
    public void onCleared_compositeDisposable_isEmpty() {
        // Given
        RxViewModel vm = new RxViewModel();

        // When
        vm.add(mock(Disposable.class));
        vm.onCleared();

        // Then
        assertThat(vm.subscriberCount(), is(0));
    }

    @Test
    public void add_addsDisposable() {
        // Given
        RxViewModel vm = new RxViewModel();

        // When
        boolean r = vm.add(mock(Disposable.class));

        // Then
        assertThat(r, is(true));
        assertThat(vm.subscriberCount(), is(1));
    }

    @Test
    public void add_whenDisposed_doesNotAddDisposable() {
        // Given
        RxViewModel vm = new RxViewModel();
        vm.onCleared();

        // When
        boolean r = vm.add(mock(Disposable.class));

        // Then
        assertThat(r, is(false));
        assertThat(vm.subscriberCount(), is(0));
    }
}

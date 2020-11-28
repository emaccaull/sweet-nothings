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
package io.github.emaccaull.sweetnothings.ui.framework

import com.google.common.truth.Truth.assertThat
import io.github.emaccaull.sweetnothings.BaseTestFixture
import io.mockk.mockk
import io.reactivex.disposables.Disposable
import org.junit.Test

class RxViewModelTest : BaseTestFixture() {

    private val disposable = mockk<Disposable>(relaxed = true)

    private class TestRxViewModel : RxViewModel() {
        // Workaround since `manage` is protected and final.
        fun manage_(disposable: Disposable): Boolean {
            return super.manage(disposable)
        }

        public override fun onCleared() {
            super.onCleared()
        }
    }

    @Test
    fun onCleared_compositeDisposable_isEmpty() {
        // Given
        val vm = TestRxViewModel()

        // When
        vm.manage_(disposable)
        vm.onCleared()

        // Then
        assertThat(vm.subscriberCount()).isEqualTo(0)
    }

    @Test
    fun add_addsDisposable() {
        // Given
        val vm = TestRxViewModel()

        // When
        val r = vm.manage_(disposable)

        // Then
        assertThat(r).isTrue()
        assertThat(vm.subscriberCount()).isEqualTo(1)
    }

    @Test
    fun add_whenDisposed_doesNotAddDisposable() {
        // Given
        val vm = TestRxViewModel()
        vm.onCleared()

        // When
        val r = vm.manage_(disposable)

        // Then
        assertThat(r).isFalse()
        assertThat(vm.subscriberCount()).isEqualTo(0)
    }
}

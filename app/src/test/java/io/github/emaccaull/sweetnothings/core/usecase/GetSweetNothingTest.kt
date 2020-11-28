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
package io.github.emaccaull.sweetnothings.core.usecase

import io.github.emaccaull.sweetnothings.BaseTestFixture
import io.github.emaccaull.sweetnothings.core.SweetNothing.Companion.builder
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Maybe
import org.junit.Test

class GetSweetNothingTest : BaseTestFixture() {

    private val messageDataSource = mockk<MessageDataSource>()

    @Test
    fun apply() {
        // Given that the data source will return a sweet nothing for a given ID
        val sweetNothing = builder("Id01").message("<3").build()
        every { messageDataSource.fetchMessage("Id01") } returns Maybe.just(sweetNothing)

        // Given that we have a sweet nothing
        val getSweetNothing = GetSweetNothing(messageDataSource)

        // When the use case is applied
        val observer = getSweetNothing.apply("Id01").test()

        // Then the sweet nothing should be received
        observer.assertValue(sweetNothing)
    }
}

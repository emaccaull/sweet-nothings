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

import io.github.emaccaull.sweetnothings.core.SweetNothing.Companion.builder
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test

class MarkUsedTest {
    
    private val messageDataSource = mockk<MessageDataSource>()
    private val markUsed = MarkUsed(messageDataSource)

    @Test
    fun apply() {
        // Given that the data source will complete successfully when an item is marked as used.
        every { messageDataSource.markUsed("theId") } returns Completable.complete()

        // And that the data source has a SweetNothing for the given message
        val sweetNothing = builder("theId").message("my message").build()
        every { messageDataSource.search("my message") } returns Maybe.just(sweetNothing)

        // When applying the use case
        markUsed.apply("my message") // Then it should complete
            .test()
            .assertComplete()
    }
}

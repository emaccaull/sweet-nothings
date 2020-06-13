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

import io.github.emaccaull.sweetnothings.core.SweetNothing
import io.github.emaccaull.sweetnothings.core.UseCase
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource
import io.github.emaccaull.sweetnothings.core.data.MessageFilter
import io.github.emaccaull.sweetnothings.core.data.MessageFilter.Companion.builder
import io.reactivex.Maybe
import javax.inject.Inject

/**
 * Retrieves a random [SweetNothing] from a data source.
 */
@UseCase
class GetRandomSweetNothing @Inject constructor(private val dataSource: MessageDataSource) {

    private val filter: MessageFilter = builder().includeUsed(false).build()

    fun apply(): Maybe<SweetNothing> {
        return dataSource.fetchRandomMessage(filter)
    }
}

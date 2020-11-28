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
package io.github.emaccaull.sweetnothings.core.data

import io.github.emaccaull.sweetnothings.core.SweetNothing
import io.reactivex.Single
import java.util.*

/**
 * Common implementation of MessageDataSource functionality.
 */
abstract class AbstractMessageDataSource internal constructor(private val ids: Ids) :
    MessageDataSource {

    protected abstract val existingMessages: Set<String>

    protected constructor() : this(Ids.instance)

    override fun create(message: String): Single<SweetNothing> = Single.fromCallable {
        val sweetNothing = SweetNothing.builder(ids.nextUuid())
            .message(message)
            .build()
        add(sweetNothing)
        sweetNothing
    }

    /**
     * Immediately adds the sweet nothing into the data store. If a sweet nothing with the same id
     * already exists, this `sweetNothing` overwrites the existing one.
     */
    protected abstract fun add(sweetNothing: SweetNothing)

    override fun createIfNotPresent(vararg messages: String): Single<List<SweetNothing>> {
        return Single.defer {
            if (messages.isEmpty()) {
                return@defer Single.just<List<SweetNothing>>(emptyList())
            }
            val existing = existingMessages
            val creates = mutableListOf<Single<SweetNothing>>()
            for (message in messages) {
                if (!existing.contains(message)) {
                    creates.add(create(message))
                }
            }
            Single.concat(creates).toList()
        }
    }
}

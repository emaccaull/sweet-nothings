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
package io.github.emaccaull.sweetnothings.data

import io.github.emaccaull.sweetnothings.core.SweetNothing
import io.github.emaccaull.sweetnothings.core.SweetNothing.Companion.builder
import io.github.emaccaull.sweetnothings.core.data.AbstractMessageDataSource
import io.github.emaccaull.sweetnothings.core.data.MessageFilter
import io.reactivex.Completable
import io.reactivex.Maybe
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory store of SweetNothings.
 */
class InMemoryMessageDataSource : AbstractMessageDataSource() {
    private val store = ConcurrentHashMap<String, SweetNothing>()

    override fun fetchRandomMessage(filter: MessageFilter): Maybe<SweetNothing> = Maybe.defer {
        for (sweetNothing in store.values) {
            if (!filter.includeUsed && sweetNothing.isUsed) {
                continue
            }
            return@defer Maybe.just(sweetNothing)
        }
        Maybe.empty<SweetNothing>()
    }

    override fun fetchMessage(id: String): Maybe<SweetNothing> = Maybe.defer {
        val sweetNothing = store[id] ?: return@defer Maybe.empty<SweetNothing>()
        Maybe.just(sweetNothing)
    }

    override fun search(exactMessage: String): Maybe<SweetNothing> = Maybe.fromCallable {
        store.values.find { it.message == exactMessage }
    }

    override fun markUsed(id: String): Completable {
        return fetchMessage(id)
            .filter { sweetNothing -> !sweetNothing.isUsed }
            .map { sweetNothing -> builder(sweetNothing).used(true).build() }
            .doOnSuccess { message -> add(message) }
            .flatMapCompletable { Completable.complete() }
    }

    override val existingMessages: Set<String>
        get() = store.values.mapTo(mutableSetOf()) { it.message }

    public override fun add(sweetNothing: SweetNothing) {
        store[sweetNothing.id] = sweetNothing
    }

    fun clear() {
        store.clear()
    }
}

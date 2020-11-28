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
package io.github.emaccaull.sweetnothings.core.data

import io.github.emaccaull.sweetnothings.core.SweetNothing
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Retrieves a [SweetNothing] from some type of persistent storage. Can be local or network.
 */
interface MessageDataSource {
    /**
     * Fetches a random [SweetNothing] from storage according the `filter`.
     *
     * @param filter determines which SweetNothings to exclude from the random result.
     * @return a `SweetNothing` if one could be found for the given `filter`.
     */
    fun fetchRandomMessage(filter: MessageFilter): Maybe<SweetNothing>

    /**
     * Fetches the [SweetNothing] with the given `id`, if it exists.
     *
     * @param id the ID of the message to fetch.
     * @return a `SweetNothing` if one could be found for the given `id`.
     */
    fun fetchMessage(id: String): Maybe<SweetNothing>

    /**
     * Searches for a [SweetNothing] with the message `exactMessage`. Only an exact
     * match will return a result. If more than one `SweetNothing` matches, then one of them
     * will be returned.
     *
     * @param exactMessage the exact message to match in the search.
     * @return one of the matching `SweetNothing` if at least one could be found.
     */
    fun search(exactMessage: String): Maybe<SweetNothing>

    /**
     * Marks the SweetNothing with the given `id` as used. Once a SweetNothing is used, it can
     * be excluded from subsequent queries by using the appropriate [MessageFilter].
     *
     * @param id the ID of the SweetNothing to mark.
     * @return a Completable that completes successfully when marking as used is successful.
     */
    fun markUsed(id: String): Completable

    /**
     * Creates a new SweetNothing and adds it to storage.
     *
     * @param message the new sweet nothing text to add.
     * @return the newly created [SweetNothing].
     */
    fun create(message: String): Single<SweetNothing>

    /**
     * Creates new SweetNothings and adds them to the data store for all `messages` that do
     * not exist in the store.
     *
     * @param messages list of sweet sayings. If null or empty, this method is a no-op.
     * @return the newly created [SweetNothings][SweetNothing].
     */
    fun createIfNotPresent(vararg messages: String): Single<List<SweetNothing>>
}

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

package io.github.emaccaull.sweetnothings.core.data;

import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

/**
 * Retrieves a {@link SweetNothing} from some type of persistent storage. Can be local or network.
 */
public interface MessageDataSource {

    /**
     * Fetches a random {@link SweetNothing} from storage according the {@code filter}.
     *
     * @param filter determines which SweetNothings to exclude from the random result.
     * @return a {@code SweetNothing} if one could be found for the given {@code filter}.
     */
    Maybe<SweetNothing> fetchRandomMessage(MessageFilter filter);

    /**
     * Fetches the {@link SweetNothing} with the given {@code id}, if it exists.
     *
     * @param id the ID of the message to fetch.
     * @return a {@code SweetNothing} if one could be found for the given {@code id}.
     */
    Maybe<SweetNothing> fetchMessage(String id);

    /**
     * Marks the SweetNothing with the given {@code id} as used. Once a SweetNothing is used, it can
     * be excluded from subsequent queries by using the appropriate {@link MessageFilter}.
     *
     * @param id the ID of the SweetNothing to mark.
     * @return a Completable that completes successfully when marking as used is successful.
     */
    Completable markUsed(String id);

    /**
     * Creates a new SweetNothing and adds it to storage.
     *
     * @param message the new sweet nothing text to add.
     * @return the newly created {@link SweetNothing}.
     */
    Single<SweetNothing> insert(String message);

    /**
     * Creates new SweetNothings and inserts them into the database for all {@code messages} that do
     * not exist in the database.
     *
     * @param messages list of sweet sayings. If null or empty, this method is a no-op.
     * @return the newly created {@link SweetNothing SweetNothings}.
     */
    Single<List<SweetNothing>> insertIfNotPresent(String... messages);

    /**
     * Counts the total number of items currently available, regardless of whether they have been
     * used or not. Does not operate on any particular scheduler.
     *
     * @return the number of items currently available.
     */
    Single<Integer> size();
}

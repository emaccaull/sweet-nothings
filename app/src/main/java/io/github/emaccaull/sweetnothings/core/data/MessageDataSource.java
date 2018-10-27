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

import android.support.annotation.NonNull;
import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.reactivex.Completable;
import io.reactivex.Maybe;

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
    Maybe<SweetNothing> fetchRandomMessage(@NonNull MessageFilter filter);

    /**
     * Fetches the {@link SweetNothing} with the given {@code id}, if it exists.
     *
     * @param id the ID of the message to fetch.
     * @return a {@code SweetNothing} if one could be found for the given {@code id}.
     */
    Maybe<SweetNothing> fetchMessage(@NonNull String id);

    /**
     * Marks the SweetNothing with the given {@code id} as used. Once a SweetNothing is used, it can
     * be excluded from subsequent queries by using the appropriate {@link MessageFilter}.
     *
     * @param id the ID of the SweetNothing to mark.
     * @return a Completable that completes successfully when marking as used is successful.
     */
    Completable markUsed(@NonNull String id);
}

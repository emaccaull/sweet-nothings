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

package io.github.emaccaull.sweetnothings.core.data;

import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.reactivex.Single;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Common implementation of MessageDataSource functionality.
 */
public abstract class AbstractMessageDataSource implements MessageDataSource {

    private final Ids ids;

    public AbstractMessageDataSource(Ids ids) {
        this.ids = ids;
    }

    @Override
    public Single<SweetNothing> insert(String message) {
        return Single.fromCallable(() -> {
            SweetNothing sweetNothing = SweetNothing.builder(ids.nextUuid())
                    .message(message)
                    .build();
            insertImmediate(sweetNothing);
            return sweetNothing;
        });
    }

    /**
     * Inserts the sweet nothing into the data store immediately. If a sweet nothing with the same
     * id already exists, this {@code sweetNothing} overwrites the existing one.
     */
    protected abstract void insertImmediate(SweetNothing sweetNothing);

    @Override
    public Single<List<SweetNothing>> insertIfNotPresent(String... messages) {
        return Single.defer(() -> {
            if (messages == null || messages.length == 0) {
                return Single.just(Collections.emptyList());
            }

            Set<String> existing = getExistingMessages();
            List<Single<SweetNothing>> inserts = new ArrayList<>();
            for (String message : messages) {
                if (!existing.contains(message)) {
                    inserts.add(insert(message));
                }
            }

            return Single.concat(inserts).toList();
        });
    }

    protected abstract Set<String> getExistingMessages();
}

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

    AbstractMessageDataSource(Ids ids) {
        this.ids = ids;
    }

    protected AbstractMessageDataSource() {
        this(Ids.getInstance());
    }

    @Override
    public Single<SweetNothing> create(String message) {
        return Single.fromCallable(() -> {
            SweetNothing sweetNothing = SweetNothing.builder(ids.nextUuid())
                    .message(message)
                    .build();
            addBlocking(sweetNothing);
            return sweetNothing;
        });
    }

    /**
     * Blocks while adding the sweet nothing into the data store. If a sweet nothing with the same
     * id already exists, this {@code sweetNothing} overwrites the existing one.
     */
    protected abstract void addBlocking(SweetNothing sweetNothing);

    @Override
    public Single<List<SweetNothing>> createIfNotPresent(String... messages) {
        return Single.defer(() -> {
            if (messages == null || messages.length == 0) {
                return Single.just(Collections.emptyList());
            }

            Set<String> existing = getExistingMessages();
            List<Single<SweetNothing>> creates = new ArrayList<>();
            for (String message : messages) {
                if (!existing.contains(message)) {
                    creates.add(create(message));
                }
            }

            return Single.concat(creates).toList();
        });
    }

    protected abstract Set<String> getExistingMessages();
}

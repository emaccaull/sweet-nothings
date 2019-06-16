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

package io.github.emaccaull.sweetnothings.data;

import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.core.data.AbstractMessageDataSource;
import io.github.emaccaull.sweetnothings.core.data.MessageFilter;
import io.github.emaccaull.sweetnothings.data.internal.Ids;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory store of SweetNothings.
 */
public class InMemoryMessageDataSource extends AbstractMessageDataSource {

    private final ConcurrentMap<String, SweetNothing> store = new ConcurrentHashMap<>();
    private final Ids ids;

    InMemoryMessageDataSource(Ids ids) {
        this.ids = ids;
    }

    public InMemoryMessageDataSource() {
        this(Ids.getInstance());
    }

    @Override
    public Maybe<SweetNothing> fetchRandomMessage(MessageFilter filter) {
        return Maybe.defer(() -> {

            for (SweetNothing sweetNothing : store.values()) {
                if (!filter.includeUsed() && sweetNothing.isUsed()) {
                    continue;
                }
                return Maybe.just(sweetNothing);
            }

            return Maybe.empty();
        });
    }

    @Override
    public Maybe<SweetNothing> fetchMessage(String id) {
        return Maybe.defer(() -> {
            SweetNothing sweetNothing = store.get(id);

            if (sweetNothing == null) {
                return Maybe.empty();
            }

            return Maybe.just(sweetNothing);
        });
    }

    @Override
    public Completable markUsed(String id) {
        return fetchMessage(id)
                .filter(sweetNothing -> !sweetNothing.isUsed())
                .map(sweetNothing -> SweetNothing.builder(sweetNothing).used(true).build())
                .doOnSuccess(this::insertImmediate)
                .flatMapCompletable(__ -> Completable.complete());
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

    @Override
    protected Set<String> getExistingMessages() {
        Set<String> messages = new HashSet<>(store.size());
        for (SweetNothing sweetNothing : store.values()) {
            messages.add(sweetNothing.getMessage());
        }
        return messages;
    }

    public void insertImmediate(SweetNothing message) {
        store.put(message.getId(), message);
    }

    @Override
    public Single<Integer> size() {
        return Single.just(store.size());
    }

    public void clear() {
        store.clear();
    }
}

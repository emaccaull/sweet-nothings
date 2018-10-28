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
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.core.data.MessageFilter;
import io.reactivex.Completable;
import io.reactivex.Maybe;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory store of SweetNothings.
 */
public class FakeMessageDataSource implements MessageDataSource {

    private final Map<String, SweetNothing> store = new HashMap<>();

    @Override
    public Maybe<SweetNothing> fetchRandomMessage(MessageFilter filter) {

        for (SweetNothing sweetNothing : store.values()) {
            if (!filter.includeUsed() && sweetNothing.isUsed()) {
                continue;
            }
            return Maybe.just(sweetNothing);
        }

        return Maybe.empty();
    }

    @Override
    public Maybe<SweetNothing> fetchMessage(String id) {
        SweetNothing sweetNothing = store.get(id);

        if (sweetNothing == null) {
            return Maybe.empty();
        }

        return Maybe.just(sweetNothing);
    }

    @Override
    public Completable markUsed(String id) {
        return null;
    }

    public void insert(SweetNothing message) {
        store.put(message.getId(), message);
    }
}

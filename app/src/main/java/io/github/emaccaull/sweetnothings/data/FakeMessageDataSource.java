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

import android.support.annotation.NonNull;
import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.core.data.MessageFilter;
import io.reactivex.Completable;
import io.reactivex.Maybe;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory store of SweetNothings.
 */
public class FakeMessageDataSource implements MessageDataSource {

    private final Map<String, SweetNothing> store = new HashMap<>();

    @Override
    public Maybe<SweetNothing> fetchRandomMessage(@NonNull MessageFilter filter) {
        SweetNothing message = null;
        Collection<SweetNothing> items = store.values();

        if (filter.includeUsed() && items.size() > 0) {
            return Maybe.just(items.iterator().next());
        }

        for (SweetNothing sweetNothing : store.values()) {
            if (filter.includeUsed()) {
                message = sweetNothing;
                break;
            } else {
                if (!sweetNothing.isUsed()) {
                    message = sweetNothing;
                }
            }
        }

        if (message == null) {
            return Maybe.empty();
        }

        return Maybe.just(message);
    }

    @Override
    public Maybe<SweetNothing> fetchMessage(@NonNull String id) {
        return null;
    }

    @Override
    public Completable markUsed(@NonNull String id) {
        return null;
    }

    public void insert(SweetNothing message) {
        store.put(message.getId(), message);
    }
}

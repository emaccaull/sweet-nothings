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

package io.github.emaccaull.sweetnothings.data.local;

import android.content.Context;
import androidx.annotation.VisibleForTesting;
import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.core.data.AbstractMessageDataSource;
import io.github.emaccaull.sweetnothings.core.data.MessageFilter;
import io.github.emaccaull.sweetnothings.data.internal.Ids;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Retrieves from and persists to local storage.
 */
public class LocalMessageDataSource extends AbstractMessageDataSource {

    private final Context context;
    private final Ids ids;

    @VisibleForTesting
    LocalMessageDataSource(Context context, Ids ids) {
        this.context = context.getApplicationContext();
        this.ids = checkNotNull(ids, "ids is null");
    }

    public LocalMessageDataSource(Context context) {
        this(context, Ids.getInstance());
    }

    @Override
    public Maybe<SweetNothing> fetchRandomMessage(MessageFilter filter) {
        MessageDao dao = MessagesDatabase.getInstance(context).message();
        Maybe<Message> selection = filter.includeUsed()
                ? dao.selectRandom()
                : dao.selectRandomUnused();
        return selection.map(Message::toSweetNothing);
    }

    @Override
    public Maybe<SweetNothing> fetchMessage(String id) {
        MessageDao dao = MessagesDatabase.getInstance(context).message();
        return dao.selectById(id).map(Message::toSweetNothing);
    }

    @Override
    public Completable markUsed(String id) {
        MessageDao dao = MessagesDatabase.getInstance(context).message();
        return dao.selectById(id)
                .flatMapCompletable(message -> Completable.fromAction(() -> {
                    message.used = true;
                    dao.update(message);
                }));
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
        MessageDao dao = MessagesDatabase.getInstance(context).message();

        Set<String> texts = new HashSet<>();
        List<Message> messages = dao.selectAll();
        for (Message message : messages) {
            texts.add(message.content);
        }
        return texts;
    }

    void insertImmediate(SweetNothing sweetNothing) {
        Message message = Message.fromSweetNothing(sweetNothing);
        MessageDao dao = MessagesDatabase.getInstance(context).message();
        dao.insert(message);
    }

    @Override
    public Single<Integer> size() {
        MessageDao dao = MessagesDatabase.getInstance(context).message();
        return dao.size();
    }
}

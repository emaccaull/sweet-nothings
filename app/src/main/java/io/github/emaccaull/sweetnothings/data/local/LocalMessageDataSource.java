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
import io.github.emaccaull.sweetnothings.core.data.Ids;
import io.github.emaccaull.sweetnothings.core.data.MessageFilter;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Retrieves from and persists to local storage.
 */
public class LocalMessageDataSource extends AbstractMessageDataSource {
    private static final Logger logger = LoggerFactory.getLogger(LocalMessageDataSource.class);

    private final Context context;

    @VisibleForTesting
    LocalMessageDataSource(Context context, Ids ids) {
        super(ids);
        this.context = context.getApplicationContext();
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
                    if (dao.update(message) > 0) {
                        logger.debug("Marked '{}' as used", id);
                    }
                }));
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

    @Override
    protected void insertImmediate(SweetNothing sweetNothing) {
        logger.debug("Inserting {}", sweetNothing);
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

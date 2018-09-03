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

package io.github.emaccaull.sweetnothings.data.local;

import android.content.Context;
import io.github.emaccaull.sweetnothings.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.data.MessageEntry;
import io.reactivex.Maybe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Local database storage of messages.
 */
public class LocalMessageStore implements MessageDataSource {
    private static volatile LocalMessageStore INSTANCE;

    private static final int COUNT = 5;
    private static final List<MessageEntry> messages = new ArrayList<>();
    static {
        for (int i = 0; i < COUNT; ++i) {
            MessageEntry me = new MessageEntry("message " + i, false, false);
            messages.add(me);
        }
    }

    public static LocalMessageStore getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalMessageStore.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalMessageStore();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Maybe<MessageEntry> fetchRandomMessage() {
        if (messages.isEmpty()) {
            return Maybe.empty();
        } else {
            MessageEntry entry = getRandom(messages);
            return Maybe.just(entry);
        }
    }

    private static <T> T getRandom(List<T> items) {
        if (items.size() < 1)
            throw new IllegalArgumentException("Require at least one item in list");
        int index = new Random().nextInt(items.size());
        return items.get(index);
    }
}

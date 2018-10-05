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
import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Application access to stored messages.
 */
public class MessageRepository {

    private final MessageDataSource dataSource;

    public MessageRepository(@NonNull MessageDataSource dataSource) {
        this.dataSource = checkNotNull(dataSource, "dataSource is null");
    }

    public Single<MessageEntry> getRandomMessage() {
        // TODO: Switch on empty to network
        return dataSource.fetchRandomMessage()
                .toSingle(new MessageEntry("r", "", false, false));
    }
}

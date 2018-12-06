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

package io.github.emaccaull.sweetnothings.core.usecase;

import io.github.emaccaull.sweetnothings.core.UseCase;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.reactivex.Completable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Marks that a particular SweetNothing has been sent to someone.
 *
 * This helps the user ensure that they don't send the same message twice.
 * Future enhancement -- mark used for a particular contact.
 */
@UseCase
public class MarkUsed {
    private final MessageDataSource dataSource;

    public MarkUsed(MessageDataSource dataSource) {
        this.dataSource = checkNotNull(dataSource, "dataSource is null");
    }

    public Completable apply(String id) {
        return dataSource.markUsed(id);
    }
}

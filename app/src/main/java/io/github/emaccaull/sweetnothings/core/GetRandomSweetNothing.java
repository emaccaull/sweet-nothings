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

package io.github.emaccaull.sweetnothings.core;

import android.support.annotation.NonNull;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.core.data.MessageFilter;
import io.reactivex.Maybe;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Retrieves a random {@link SweetNothing} from a data source.
 */
@UseCase
public class GetRandomSweetNothing {

    private final MessageDataSource dataSource;
    private final MessageFilter filter;

    public GetRandomSweetNothing(@NonNull MessageDataSource dataSource) {
        this.dataSource = checkNotNull(dataSource, "dataSource is null");
        this.filter = MessageFilter.builder().includeUsed(false).build();
    }

    public Maybe<SweetNothing> apply() {
        return dataSource.fetchRandomMessage(filter);
    }
}

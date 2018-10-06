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

package io.github.emaccaull.sweetnothings.core.data;

import com.google.common.base.Objects;

/**
 * Allows retrieving a subset of SweetNothings based on some criteria.
 */
public final class MessageFilter {
    public static final MessageFilter SELECT_ALL = builder().build();

    private final boolean includeUsed;

    private MessageFilter(Builder builder) {
        this.includeUsed = builder.includeUsed;
    }

    public boolean includeUsed() {
        return includeUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof MessageFilter))
            return false;

        MessageFilter filter = (MessageFilter) o;
        return includeUsed == filter.includeUsed;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(includeUsed);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean includeUsed = true;

        protected Builder() {}

        public Builder includeUsed(boolean includeUsed) {
            this.includeUsed = includeUsed;
            return this;
        }

        public MessageFilter build() {
            return new MessageFilter(this);
        }
    }
}

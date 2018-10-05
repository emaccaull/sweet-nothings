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

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * Represents a sweet saying to send.
 */
public final class SweetNothing {
    private final String message;
    private final boolean blacklisted;
    private final boolean used;

    private SweetNothing(Builder builder) {
        this.message = builder.message;
        this.blacklisted = builder.blacklisted;
        this.used = builder.used;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public boolean isUsed() {
        return used;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof SweetNothing))
            return false;

        SweetNothing that = (SweetNothing) o;
        return blacklisted == that.blacklisted &&
                used == that.used &&
                Objects.equal(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message, blacklisted, used);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String message;
        private boolean blacklisted;
        private boolean used;

        protected Builder() {
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder blacklisted(boolean blacklisted) {
            this.blacklisted = blacklisted;
            return this;
        }

        public Builder used(boolean used) {
            this.used = used;
            return this;
        }

        public SweetNothing build() {
            if (Strings.isNullOrEmpty(message)) {
                throw new IllegalStateException("Must specify a message");
            }
            return new SweetNothing(this);
        }
    }
}

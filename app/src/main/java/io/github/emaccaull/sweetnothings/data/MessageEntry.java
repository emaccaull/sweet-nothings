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

/**
 * Database representation of a sweet nothing.
 */
public class MessageEntry {
    private final String id;
    private final String message;
    private final boolean blacklisted;
    private final boolean used;

    public MessageEntry(@NonNull String id, @NonNull String message, boolean blacklisted, boolean used) {
        this.id = id;
        this.message = message;
        this.blacklisted = blacklisted;
        this.used = used;
    }

    public String getId() {
        return id;
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
}

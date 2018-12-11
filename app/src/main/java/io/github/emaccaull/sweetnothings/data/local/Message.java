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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import io.github.emaccaull.sweetnothings.core.SweetNothing;

/**
 * Stored sweet nothing.
 */
@Entity(tableName = Message.TABLE_NAME)
class Message {

    static final String TABLE_NAME = "message";

    /** UUID of the Message. */
    @SuppressWarnings("NullableProblems") // Room requires NonNull annotation.
    @NonNull
    @PrimaryKey
    @ColumnInfo(index = true, name = "id")
    public String id;

    /** The sharable contents of the message. */
    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "is_blacklisted")
    public boolean blacklisted;

    @ColumnInfo(name = "is_used")
    public boolean used;

    SweetNothing toSweetNothing() {
        return SweetNothing.builder(id)
                .message(content)
                .used(used)
                .blacklisted(blacklisted)
                .build();
    }

    static Message fromSweetNothing(SweetNothing sweetNothing) {
        Message m = new Message();
        m.id = sweetNothing.getId();
        m.content = sweetNothing.getMessage();
        m.blacklisted = sweetNothing.isBlacklisted();
        m.used = sweetNothing.isUsed();
        return m;
    }
}

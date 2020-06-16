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
package io.github.emaccaull.sweetnothings.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.emaccaull.sweetnothings.core.SweetNothing

/**
 * Stored sweet nothing.
 */
@Entity(tableName = Message.TABLE_NAME)
internal data class Message(
    /**
     * Integer primary key (database ID).
     */
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    /**
     * UUID of the Message.
     */
    @ColumnInfo(index = true, name = "uuid") val uuid: String,

    /**
     * The sharable contents of the message.
     */
    @ColumnInfo(name = "content") val content: String,

    /**
     * True when this message should not be automatically suggested.
     */
    @ColumnInfo(name = "is_blacklisted") val isBlacklisted: Boolean,

    /**
     * True when this message has been sent.
     */
    @ColumnInfo(name = "is_used") val isUsed: Boolean
) {

    fun toSweetNothing(): SweetNothing {
        return SweetNothing.builder(uuid)
            .message(content)
            .used(isUsed)
            .blacklisted(isBlacklisted)
            .build()
    }

    companion object {
        const val TABLE_NAME: String = "message"
    }
}

internal fun messageOf(sweetNothing: SweetNothing): Message = Message(
    uuid = sweetNothing.id,
    content = sweetNothing.message,
    isBlacklisted = sweetNothing.isBlacklisted,
    isUsed = sweetNothing.isUsed
)

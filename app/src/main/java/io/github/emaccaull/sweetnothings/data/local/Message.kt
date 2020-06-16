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
internal class Message {
    /** UUID of the Message.  */
    @PrimaryKey
    @ColumnInfo(index = true, name = "id")
    lateinit var id: String

    /** The sharable contents of the message.  */
    @ColumnInfo(name = "content")
    lateinit var content: String

    @ColumnInfo(name = "is_blacklisted")
    var blacklisted = false

    @ColumnInfo(name = "is_used")
    var used = false

    /** For Room. */
    internal constructor()

    constructor(sweetNothing: SweetNothing) {
        id = sweetNothing.id
        content = sweetNothing.message
        blacklisted = sweetNothing.isBlacklisted
        used = sweetNothing.isUsed
    }

    companion object {
        const val TABLE_NAME = "message"
    }
}

internal fun Message.toSweetNothing(): SweetNothing {
    return SweetNothing.builder(id)
        .message(content)
        .used(used)
        .blacklisted(blacklisted)
        .build()
}

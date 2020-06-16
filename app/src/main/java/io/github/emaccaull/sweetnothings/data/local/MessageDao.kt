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

import androidx.room.*
import io.reactivex.Maybe

/**
 * Local message store.
 */
@Dao
internal interface MessageDao {
    /**
     * Inserts a message into the table.
     *
     * @param message A new message.
     * @return the row ID of the inserted message.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message): Long

    /**
     * Select a random sweet nothing from the db.
     */
    @Query(
        "SELECT * FROM " + Message.TABLE_NAME
                + " WHERE id = (SELECT id FROM " + Message.TABLE_NAME + " ORDER BY RANDOM() LIMIT 1)"
    )
    fun selectRandom(): Maybe<Message>

    /**
     * Select a random unused sweet nothing from the db.
     */
    @Query(
        "SELECT * FROM " + Message.TABLE_NAME
                + " WHERE id = "
                + "(SELECT id FROM " + Message.TABLE_NAME
                + " WHERE is_used=0 ORDER BY RANDOM() LIMIT 1)"
    )
    fun selectRandomUnused(): Maybe<Message>

    /**
     * Selects the Message for the given ID if it exists.
     *
     * @param id the UUID of the message to fetch.
     * @return a [Message] if it exists.
     */
    @Query("SELECT * FROM " + Message.TABLE_NAME + " WHERE id = :id")
    fun selectById(id: String): Maybe<Message>

    /**
     * Searches for a Message with the given text `content`.
     *
     * @param content the exact text of the message contents to fetch.
     * @return a [Message] if it can be found.
     */
    @Query("SELECT * FROM " + Message.TABLE_NAME + " WHERE content = :content")
    fun selectByMessage(content: String): Maybe<Message>

    /**
     * Retrieves all messages from the database.
     *
     * @return a list of all present messages.
     */
    @Query("SELECT * FROM " + Message.TABLE_NAME)
    fun selectAll(): List<Message>

    /**
     * Updates an existing message.
     *
     * @param message the data to replace.
     * @return the number of rows updated. Should always be one.
     */
    @Update
    fun update(message: Message): Int
}

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

package io.github.emaccaull.sweetnothings.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Local message store.
 */
@Dao
interface MessageDao {

    /**
     * Inserts a message into the table.
     *
     * @param message A new message.
     * @return the row ID of the inserted message.
     */
    @SuppressWarnings("UnusedReturnValue")
    @Insert
    long insert(Message message);

    /**
     * Select a random sweet nothing from the db.
     */
    @Query("SELECT * FROM " + Message.TABLE_NAME
            + " WHERE id = (SELECT id FROM " + Message.TABLE_NAME + " ORDER BY RANDOM() LIMIT 1)")
    Maybe<Message> selectRandom();

    /**
     * Selects the Message for the given ID if it exists.
     *
     * @param id the UUID of the message to fetch.
     * @return a {@link Message} if it exists.
     */
    @Query("SELECT * FROM " + Message.TABLE_NAME + " WHERE id = :id")
    Maybe<Message> selectById(String id);

    @Query("SELECT COUNT(*) FROM " + Message.TABLE_NAME)
    Single<Integer> size();

    /**
     * Updates an existing message.
     *
     * @param message the data to replace.
     * @return the number of rows updated. Should always be one.
     */
    @SuppressWarnings("UnusedReturnValue")
    @Update
    int update(Message message);
}

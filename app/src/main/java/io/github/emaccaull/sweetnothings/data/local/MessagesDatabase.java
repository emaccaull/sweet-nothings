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

import android.content.Context;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * SQLite database definition for messages.
 */
@Database(entities = {Message.class}, version = 1)
public abstract class MessagesDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "messages";

    private static volatile MessagesDatabase instance;

    /**
     * @return the DAO for the message table.
     */
    public abstract MessageDao message();

    /**
     * Gets the singleton instance of MessagesDatabase.
     *
     * @param context The context.
     * @return The singleton instance of MessagesDatabase.
     */
    public static MessagesDatabase getInstance(Context context) {
        if (instance != null) {
            return instance;
        }
        synchronized (MessagesDatabase.class) {
            if (instance == null) {
                instance = newDatabase(context);
            }
        }
        return instance;
    }

    private static MessagesDatabase newDatabase(Context context) {
        Context appContext = context.getApplicationContext();
        return Room.databaseBuilder(appContext, MessagesDatabase.class, DATABASE_NAME).build();
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        Context appContext = context.getApplicationContext();
        instance = Room.inMemoryDatabaseBuilder(appContext, MessagesDatabase.class).build();
    }
}

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

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * SQLite database definition for messages.
 */
@Database(entities = [Message::class], version = 1)
abstract class MessagesDatabase : RoomDatabase() {
    /**
     * @return the DAO for the message table.
     */
    internal abstract fun message(): MessageDao

    companion object {
        private const val DATABASE_NAME = "messages"

        @Volatile
        private var instance: MessagesDatabase? = null

        /**
         * Gets the singleton instance of MessagesDatabase.
         *
         * @param context The context.
         * @return The singleton instance of MessagesDatabase.
         */
        @JvmStatic
        fun getInstance(context: Context): MessagesDatabase {
            var i = instance
            if (i != null) {
                return i
            }
            synchronized(MessagesDatabase::class.java) {
                if (instance == null) {
                    i = newDatabase(context)
                    instance = i
                }
            }
            return i!!
        }

        private fun newDatabase(context: Context): MessagesDatabase {
            val appContext = context.applicationContext
            return Room.databaseBuilder(
                appContext,
                MessagesDatabase::class.java,
                DATABASE_NAME
            ).build()
        }

        /**
         * Switches the internal implementation with an empty in-memory database.
         *
         * @param context The context.
         */
        @VisibleForTesting
        @JvmStatic
        fun switchToInMemory(context: Context) {
            val appContext = context.applicationContext
            instance = Room.inMemoryDatabaseBuilder(
                appContext,
                MessagesDatabase::class.java
            ).build()
        }
    }
}

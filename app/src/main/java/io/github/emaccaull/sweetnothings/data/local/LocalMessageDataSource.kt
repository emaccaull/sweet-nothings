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
import io.github.emaccaull.sweetnothings.core.SweetNothing
import io.github.emaccaull.sweetnothings.core.data.AbstractMessageDataSource
import io.github.emaccaull.sweetnothings.core.data.MessageFilter
import io.reactivex.Completable
import io.reactivex.Maybe
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Retrieves from and persists to local storage.
 */
class LocalMessageDataSource internal constructor(private val messagesDatabase: MessagesDatabase) :
    AbstractMessageDataSource() {

    constructor(context: Context) : this(MessagesDatabase.getInstance(context))

    private val messageDao: MessageDao
        get() = messagesDatabase.message()

    override fun fetchRandomMessage(filter: MessageFilter): Maybe<SweetNothing> {
        val dao = messageDao
        val selection = if (filter.includeUsed()) dao.selectRandom() else dao.selectRandomUnused()
        return selection.map { obj: Message -> obj.toSweetNothing() }
    }

    override fun fetchMessage(id: String): Maybe<SweetNothing> {
        val dao = messageDao
        return dao.selectById(id)
            .map { obj: Message -> obj.toSweetNothing() }
    }

    override fun search(exactMessage: String): Maybe<SweetNothing> {
        val dao = messageDao
        return dao.selectByMessage(exactMessage)
            .map { obj: Message -> obj.toSweetNothing() }
    }

    override fun markUsed(id: String): Completable {
        val dao = messageDao
        return dao.selectById(id)
            .flatMapCompletable { message: Message ->
                Completable.fromAction {
                    message.used = true
                    if (dao.update(message) > 0) {
                        logger.debug("Marked '{}' as used", id)
                    }
                }
            }
    }

    override val existingMessages: Set<String>
        get() {
            val dao = messageDao
            val texts: MutableSet<String> = HashSet()
            for (message in dao.selectAll()) {
                texts.add(message.content)
            }
            return texts
        }

    public override fun add(sweetNothing: SweetNothing) {
        logger.debug("Inserting {}", sweetNothing)
        val message = Message.fromSweetNothing(sweetNothing)
        messageDao.insert(message)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LocalMessageDataSource::class.java)
    }
}

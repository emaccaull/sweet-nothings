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
package io.github.emaccaull.sweetnothings.core.data

import com.google.common.base.Objects

/**
 * Allows retrieving a subset of SweetNothings based on some criteria.
 */
class MessageFilter private constructor(builder: Builder) {

    internal object SelectAllHolder {
        val FILTER = builder().build()
    }

    private val includeUsed: Boolean

    init {
        includeUsed = builder.includeUsed
    }

    fun includeUsed(): Boolean {
        return includeUsed
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MessageFilter) return false
        return includeUsed == other.includeUsed
    }

    override fun hashCode(): Int {
        return Objects.hashCode(includeUsed)
    }

    class Builder internal constructor() {
        internal var includeUsed = true

        fun includeUsed(includeUsed: Boolean): Builder {
            this.includeUsed = includeUsed
            return this
        }

        fun build(): MessageFilter {
            return MessageFilter(this)
        }
    }

    companion object {

        @JvmStatic fun selectAll(): MessageFilter {
            return SelectAllHolder.FILTER
        }

        @JvmStatic fun builder(): Builder {
            return Builder()
        }
    }
}

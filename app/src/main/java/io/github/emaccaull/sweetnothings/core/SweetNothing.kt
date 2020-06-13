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
package io.github.emaccaull.sweetnothings.core

import com.google.common.base.Objects

/**
 * Represents a sweet saying to send.
 */
class SweetNothing private constructor(builder: Builder) {

    val id: String = builder.id
    val message: String = builder.message!!
    val isBlacklisted: Boolean = builder.blacklisted
    val isUsed: Boolean = builder.used

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SweetNothing) return false
        return isBlacklisted == other.isBlacklisted && isUsed == other.isUsed &&
                Objects.equal(id, other.id) &&
                Objects.equal(message, other.message)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, message, isBlacklisted, isUsed)
    }

    override fun toString(): String {
        return "SweetNothing{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", blacklisted=" + isBlacklisted +
                ", used=" + isUsed +
                '}'
    }

    class Builder {
        val id: String
        var message: String? = null
        var blacklisted = false
        var used = false

        internal constructor(id: String) {
            this.id = id
        }

        internal constructor(seed: SweetNothing) {
            id = seed.id
            message = seed.message
            blacklisted = seed.isBlacklisted
            used = seed.isUsed
        }

        fun message(message: String?): Builder {
            this.message = message
            return this
        }

        fun blacklisted(blacklisted: Boolean): Builder {
            this.blacklisted = blacklisted
            return this
        }

        fun used(used: Boolean): Builder {
            this.used = used
            return this
        }

        fun build(): SweetNothing {
            require(!message.isNullOrEmpty()) { "Must specify a message" }
            return SweetNothing(this)
        }
    }

    companion object {
        @JvmStatic
        fun builder(id: String): Builder {
            return Builder(id)
        }

        @JvmStatic
        fun builder(seed: SweetNothing): Builder {
            return Builder(seed)
        }
    }
}

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

import io.github.emaccaull.sweetnothings.BaseTestFixture;
import io.github.emaccaull.sweetnothings.core.SweetNothing;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MessageTest extends BaseTestFixture {

    @Test
    public void toSweetNothing() {
        Message message = new Message();
        message.id = "123";
        message.content = "testing";
        message.used = true;
        message.blacklisted = true;

        SweetNothing sweetNothing = message.toSweetNothing();

        assertThat(sweetNothing.getId(), is("123"));
        assertThat(sweetNothing.getMessage(), is("testing"));
        assertThat(sweetNothing.isUsed(), is(true));
        assertThat(sweetNothing.isBlacklisted(), is(true));
    }

    @Test
    public void fromSweetNothing() {
        SweetNothing sn = SweetNothing.builder("foo")
                .message("hello").blacklisted(true).used(true)
                .build();

        Message msg = Message.fromSweetNothing(sn);

        assertThat(msg.id, is("foo"));
        assertThat(msg.content, is("hello"));
        assertThat(msg.blacklisted, is(true));
        assertThat(msg.used, is(true));
    }
}

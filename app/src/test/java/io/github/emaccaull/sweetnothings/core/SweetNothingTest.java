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

package io.github.emaccaull.sweetnothings.core;

import io.github.emaccaull.sweetnothings.BaseTestFixture;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SweetNothingTest extends BaseTestFixture {

    @Test
    public void getMessage() {
        SweetNothing sn = SweetNothing.builder("9923").message("hi").build();
        assertThat(sn.getMessage(), is("hi"));
    }

    @Test(expected = IllegalStateException.class)
    public void builder_whenNullMessage_throwsException() {
        // When building a sweet nothing that doesn't have a message
        SweetNothing.builder("123").message(null).build();
        // Then an exception should be thrown
    }

    @Test(expected = IllegalStateException.class)
    public void builder_whenEmptyMessage_throwsException() {
        // When building a sweet nothing that doesn't have a message
        SweetNothing.builder("6473")
                .message("")
                .build();
        // Then an exception should be thrown
    }

    @Test(expected = NullPointerException.class)
    public void builder_whenIdEmpty_throwsException() {
        // When building a sweet nothing that doesn't have an ID
        SweetNothing.builder((String)null).message("foo").build();
        // Then an exception should be thrown
    }

    @Test
    public void builder_canCopyAllData() {
        // Given that there is an existing sweet nothing
        SweetNothing sn = SweetNothing.builder("abc")
                .message("hi")
                .used(true)
                .blacklisted(true)
                .build();

        // When making a copy of the sweet nothing
        SweetNothing snCopy = SweetNothing.builder(sn).build();

        // Then the two should be equal
        assertThat(snCopy, is(sn));
    }

    @Test
    public void id() {
        // When constructing SweetNothing with a valid ID
        SweetNothing sn = SweetNothing.builder("foobar").message("hello").build();

        // Then it should have that ID
        assertThat(sn.getId(), is("foobar"));
    }

    @Test
    public void isBlacklisted() {
        SweetNothing sn = SweetNothing.builder("aaa")
                .message("a")
                .blacklisted(true)
                .build();
        assertThat(sn.isBlacklisted(), is(true));
    }

    @Test
    public void isUsed() {
        SweetNothing sn = SweetNothing.builder("bbb")
                .message("b")
                .used(true)
                .build();
        assertThat(sn.isUsed(), is(true));
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(SweetNothing.class).verify();
    }
}

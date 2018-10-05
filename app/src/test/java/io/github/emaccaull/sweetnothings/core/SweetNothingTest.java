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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SweetNothingTest {

    @Test
    public void getMessage() {
        SweetNothing sn = SweetNothing.builder().message("hi").build();
        assertThat(sn.getMessage(), is("hi"));
    }

    @Test(expected = IllegalStateException.class)
    public void builder_whenNullMessage_throwsException() {
        // When building a sweet nothing that doesn't have a message
        SweetNothing.builder().build();
        // Then an exception should be thrown
    }

    @Test(expected = IllegalStateException.class)
    public void builder_whenEmptyMessage_throwsException() {
        // When building a sweet nothing that doesn't have a message
        SweetNothing.builder()
                .message("")
                .build();
        // Then an exception should be thrown
    }

    @Test
    public void isBlacklisted() {
        SweetNothing sn = SweetNothing.builder()
                .message("a")
                .blacklisted(true)
                .build();
        assertThat(sn.isBlacklisted(), is(true));
    }

    @Test
    public void isUsed() {
        SweetNothing sn = SweetNothing.builder()
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

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

package io.github.emaccaull.sweetnothings.core.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MessageFilterTest {

    @Test
    public void builder_defaultIncludesAll() {
        MessageFilter filter = MessageFilter.builder().build();

        assertThat(filter.includeUsed(), is(true));
    }

    @Test
    public void builder_whenIgnoreUsed_filterDoesNotIncludeUsed() {
        // Given that we can create a builder that excludes used messages
        MessageFilter.Builder builder = MessageFilter.builder().includeUsed(false);

        // When constructing a MessageFilter from that builder
        MessageFilter filter = builder.build();

        // Then the filter should also exclude used messages
        assertThat(filter.includeUsed(), is(false));
    }

    @Test
    public void selectAll_IncludesAll() {
        MessageFilter filter = MessageFilter.selectAll();

        assertThat(filter.includeUsed(), is(true));
    }

    @Test
    public void equals() {
        EqualsVerifier.forClass(MessageFilter.class).verify();
    }
}

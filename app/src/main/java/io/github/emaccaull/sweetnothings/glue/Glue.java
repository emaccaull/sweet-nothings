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

package io.github.emaccaull.sweetnothings.glue;

import android.support.annotation.VisibleForTesting;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.MarkUsed;

import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Controls creation of application scoped objects.
 */
public final class Glue {

    private static volatile Component component;

    public static GetRandomSweetNothing provideGetRandomSweetNothing() {
        return getComponent().getRandomSweetNothingProvider.get();
    }

    public static MessageDataSource provideMessageDataSource() {
        return getComponent().messageDataSourceProvider.get();
    }

    public static MarkUsed provideMarkUsed() {
        return getComponent().markUsedProvider.get();
    }

    private static Component getComponent() {
        checkState(component != null, "No module set");
        return component;
    }

    /**
     * Configures data source implementations for the application.
     */
    public static void setDataAccessComponent(DataAccessComponent component) {
        Glue.component = new Component(checkNotNull(component));
    }

    @VisibleForTesting
    static void reset() {
        component = null;
    }

    /** Creates instances of classes which depend on a data source. */
    static class Module {

        /** @return a new GetRandomSweetNothing use case. */
        static GetRandomSweetNothing getRandomSweetNothing(MessageDataSource dataSource) {
            return new GetRandomSweetNothing(dataSource);
        }

        static MarkUsed markUsed(MessageDataSource dataSource) {
            return new MarkUsed(dataSource);
        }
    }

    /** Manages instantiation of the objects specified by {@link DataAccessComponent}. */
    static final class Component {
        Provider<MessageDataSource> messageDataSourceProvider;
        Provider<GetRandomSweetNothing> getRandomSweetNothingProvider;
        Provider<MarkUsed> markUsedProvider;

        Component(DataAccessComponent component) {
            messageDataSourceProvider = new Lazy<>(component::messageDataSource);
            getRandomSweetNothingProvider =
                    new Lazy<>(() -> Module.getRandomSweetNothing(messageDataSourceProvider.get()));
            markUsedProvider =
                    new Lazy<>(() -> Module.markUsed(messageDataSourceProvider.get()));
        }
    }
}

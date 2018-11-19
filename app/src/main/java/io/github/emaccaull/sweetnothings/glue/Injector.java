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

import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Controls creation of application scoped objects.
 */
public final class Injector {

    private static volatile Component component;

    public static GetRandomSweetNothing provideGetRandomSweetNothing() {
        checkState(component != null, "No module set");
        return component.getRandomSweetNothing();
    }

    public static MessageDataSource provideMessageDataSource() {
        checkState(component != null, "No module set");
        return component.messageDataSource();
    }

    /**
     * Configures data source implementations for the application.
     */
    public static void setDataAccessComponent(DataAccessComponent component) {
        Injector.component = new Component(checkNotNull(component));
    }

    @VisibleForTesting
    static void reset() {
        component = null;
    }

    /** Defines implementations of data sources. */
    public interface DataAccessComponent {

        /** @return a MessageDataSource instance to share globally. */
        MessageDataSource messageDataSource();
    }

    /** Creates instances of classes which depend on a data source. */
    static class Module {

        /** @return a new GetRandomSweetNothing use case. */
        static GetRandomSweetNothing getRandomSweetNothing(MessageDataSource dataSource) {
            return new GetRandomSweetNothing(dataSource);
        }
    }

    /** Manages instantiation of the objects specified by {@link DataAccessComponent}. */
    static final class Component {
        Provider<MessageDataSource> messageDataSourceProvider;
        Provider<GetRandomSweetNothing> getRandomSweetNothingProvider;

        Component(DataAccessComponent component) {
            messageDataSourceProvider = new Lazy<>(component::messageDataSource);
            getRandomSweetNothingProvider =
                    new Lazy<>(() -> Module.getRandomSweetNothing(messageDataSourceProvider.get()));
        }

        MessageDataSource messageDataSource() {
            return messageDataSourceProvider.get();
        }

        GetRandomSweetNothing getRandomSweetNothing() {
            return getRandomSweetNothingProvider.get();
        }
    }
}

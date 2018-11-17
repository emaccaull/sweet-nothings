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
import javax.inject.Singleton;

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

    public static void setModule(Module module) {
        component = new Component(module);
    }

    @VisibleForTesting
    static void reset() {
        component = null;
    }

    /** Defines implementations of application scoped dependencies. */
    public interface Module {

        /** @return a MessageDataSource instance to share globally. */
        @Singleton
        MessageDataSource messageDataSource();

        /** @return a new GetRandomSweetNothing use case. */
        @Singleton
        GetRandomSweetNothing getRandomSweetNothing(MessageDataSource dataSource);
    }

    /** Manages instantiation of the objects specified in {@link Module}. */
    @Singleton
    static final class Component {
        Provider<MessageDataSource> messageDataSourceProvider;
        Provider<GetRandomSweetNothing> getRandomSweetNothingProvider;

        Component(Module module) {
            checkNotNull(module, "module is null");
            messageDataSourceProvider = new Lazy<>(module::messageDataSource);
            getRandomSweetNothingProvider = new Lazy<>(
                    () -> module.getRandomSweetNothing(messageDataSourceProvider.get()));
        }

        MessageDataSource messageDataSource() {
            return messageDataSourceProvider.get();
        }

        GetRandomSweetNothing getRandomSweetNothing() {
            return getRandomSweetNothingProvider.get();
        }
    }
}

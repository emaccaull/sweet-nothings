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

import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;

/**
 * Wires together application components.
 */
public final class Injector {

    private static volatile Lazy<MessageDataSource> messageDataSource;

    public static GetRandomSweetNothing provideGetRandomSweetNothing(MessageDataSource dataSource) {
        return new GetRandomSweetNothing(dataSource);
    }

    public static MessageDataSource provideMessageDataSource() {
        return messageDataSource.get();
    }

    private static AppComponent checkAppComponent(AppComponent component) {
        if (component == null) {
            throw new IllegalStateException("AppComponent not set");
        }
        return component;
    }

    public static void setAppComponent(AppComponent appComponent) {
        messageDataSource = new Lazy<>(
                () -> checkAppComponent(appComponent).messageDataSource());
    }
}

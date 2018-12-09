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

import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Lazily creates an instance of T.
 */
public class Lazy<T> implements Provider<T> {

    private Provider<T> provider;
    private volatile T instance;

    public Lazy(Provider<T> provider) {
        checkNotNull(provider);
        this.provider = provider;
    }

    @Override
    public T get() {
        if (instance != null) {
            return instance;
        }
        synchronized (this) {
            if (instance == null) {
                instance = provider.get();
                provider = null; // help gc.
            }
        }
        return instance;
    }
}

/*
 * Copyright (C) 2020 Emmanuel MacCaull
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

package io.github.emaccaull.sweetnothings.ui.framework;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;

/** Creates ViewModel instances from the dagger configuration. */
public class AppViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> providers;

    @Inject
    AppViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providers) {
        this.providers = providers;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<? extends ViewModel> provider = providers.get(modelClass);
        if (provider == null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry :
                    providers.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    provider = entry.getValue();
                    break;
                }
            }
        }
        if (provider == null) {
            throw new IllegalArgumentException("No provider for class " + modelClass);
        }
        //noinspection unchecked
        return (T) provider.get();
    }

    /**
     * Binds {@link AppViewModelFactory} to {@link ViewModelProvider.Factory} so that objects need
     * not reference {@code AppViewModelFactory} directly.
     */
    @dagger.Module
    public static abstract class Binding {
        @Binds
        public abstract ViewModelProvider.Factory bindViewModelFactory(AppViewModelFactory factory);
    }
}

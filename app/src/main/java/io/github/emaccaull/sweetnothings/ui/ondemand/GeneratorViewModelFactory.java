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

package io.github.emaccaull.sweetnothings.ui.ondemand;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.github.emaccaull.sweetnothings.glue.Injector;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Creates instances of ViewModels in this package.
 */
class GeneratorViewModelFactory implements ViewModelProvider.Factory {

    private GetRandomSweetNothing getRandomSweetNothing;

    GeneratorViewModelFactory() {
        this(Injector.provideGetRandomSweetNothing(Injector.provideMessageDataSource()));
    }

    GeneratorViewModelFactory(GetRandomSweetNothing getRandomSweetNothing) {
        this.getRandomSweetNothing = checkNotNull(getRandomSweetNothing);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (GeneratorViewModel.class.isAssignableFrom(modelClass)) {
            //noinspection unchecked
            return (T) new GeneratorViewModel(getRandomSweetNothing);
        }
        throw new IllegalArgumentException("Cannot instantiate " + modelClass);
    }
}

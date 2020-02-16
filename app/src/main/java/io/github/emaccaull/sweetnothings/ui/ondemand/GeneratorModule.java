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

package io.github.emaccaull.sweetnothings.ui.ondemand;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import io.github.emaccaull.sweetnothings.ui.framework.ViewModelKey;

/**
 * Describes how to add Generator* objects to the Dagger graph.
 */
@dagger.Module
abstract class GeneratorModule {

    @Binds
    @IntoMap
    @ViewModelKey(GeneratorViewModel.class)
    abstract ViewModel bindGeneratorViewModel(GeneratorViewModel viewModel);
}

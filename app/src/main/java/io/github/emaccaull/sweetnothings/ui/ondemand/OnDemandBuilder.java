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
import dagger.multibindings.IntoMap;
import io.github.emaccaull.sweetnothings.glue.Configuration;
import io.github.emaccaull.sweetnothings.ui.framework.AppViewModelFactory;
import io.github.emaccaull.sweetnothings.ui.framework.FragmentScope;
import io.github.emaccaull.sweetnothings.ui.framework.ViewModelKey;

/** Builds the "on demand" component, keeping Dagger specifics isolated from the Fragment code. */
public final class OnDemandBuilder {
    private final ParentComponent dependency;

    OnDemandBuilder(ParentComponent dependency) {
        this.dependency = dependency;
    }

    public void inject(GeneratorFragment fragment) {
        DaggerOnDemandBuilder_Component.builder()
                .parentComponent(dependency)
                .build()
                .inject(fragment);
    }

    static void inject(GeneratorFragment fragment, ParentComponent dependencies) {
        DaggerOnDemandBuilder_Component.builder()
                .parentComponent(dependencies)
                .build()
                .inject(fragment);
    }

    /** Describes how to add Generator* objects to the Dagger graph. */
    @dagger.Module
    abstract static class Module {
        @Binds
        @IntoMap
        @ViewModelKey(GeneratorViewModel.class)
        abstract ViewModel bindGeneratorViewModel(GeneratorViewModel viewModel);
    }

    public interface ParentComponent extends Configuration {}

    @FragmentScope
    @dagger.Component(
            dependencies = ParentComponent.class,
            modules = {AppViewModelFactory.Binding.class, Module.class})
    interface Component {

        void inject(GeneratorFragment fragment);

        @dagger.Component.Builder
        interface Builder {

            Builder parentComponent(ParentComponent component);

            Component build();
        }
    }
}

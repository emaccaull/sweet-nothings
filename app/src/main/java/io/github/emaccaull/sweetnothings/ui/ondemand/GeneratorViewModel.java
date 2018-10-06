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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;

/**
 * Generate/fetch a Random SweetNothing on demand.
 */
public class GeneratorViewModel extends ViewModel {

    private final GetRandomSweetNothing getRandomSweetNothing;
    private final MutableLiveData<ViewState> viewState = new MutableLiveData<>();

    public GeneratorViewModel(@NonNull GetRandomSweetNothing getRandomSweetNothing) {
        this.getRandomSweetNothing = getRandomSweetNothing;
    }

    public void requestNewMessage() {
        getRandomSweetNothing.apply()
                .doOnSubscribe(__ -> viewState.postValue(new ViewState(true, null)))
                .subscribe(
                        (SweetNothing sn) ->
                                viewState.postValue(new ViewState(false, sn.getMessage()))
                );
    }

    public LiveData<ViewState> getViewState() {
        return viewState;
    }
}

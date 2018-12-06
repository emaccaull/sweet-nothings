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
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.MarkUsed;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate/fetch a Random SweetNothing on demand.
 */
public final class GeneratorViewModel extends ViewModel {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorViewModel.class);

    private final MutableLiveData<ViewState> viewState = new MutableLiveData<>();

    final CompositeDisposable disposables = new CompositeDisposable();
    private final GetRandomSweetNothing getRandomSweetNothing;
    private final MarkUsed markUsed;

    @SuppressWarnings("WeakerAccess")
    public GeneratorViewModel(GetRandomSweetNothing getRandomSweetNothing, MarkUsed markUsed) {
        this.getRandomSweetNothing = getRandomSweetNothing;
        this.markUsed = markUsed;
        resetViewState();
    }

    /**
     * Finds an unused sweet nothing and updates the view state.
     */
    void requestNewMessage() {
        logger.info("Requesting a new sweet nothing");

        Disposable d = getRandomSweetNothing.apply()
                .doOnSubscribe(__ -> viewState.postValue(ViewState.loading()))
                .map(ViewState::loaded)
                .onErrorComplete()
                .subscribe(
                        viewState::postValue,
                        throwable -> logger.error("Couldn't load sweet nothing", throwable),
                        () -> viewState.postValue(ViewState.noMessageFound())
                );

        disposables.add(d);
    }

    /**
     * Computes the next view state once a sweet nothing has been successfully shared.
     */
    void onShareSuccessful(String id) {
        logger.info("Sharing a sweet nothing; id='{}'", id);

        Disposable d = markUsed.apply(id)
                .subscribe(this::resetViewState);

        disposables.add(d);
    }

    void onShareFailed(String id) {
        // Reset the view for now. TODO notify user.
        resetViewState();
    }

    /**
     * Sets the view back to its initial state (as if it was loaded for the first time).
     */
    void resetViewState() {
        viewState.postValue(ViewState.initial());
    }

    /**
     * A View or View Controller should observe this state to know what to display.
     */
    LiveData<ViewState> getViewState() {
        return viewState;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}

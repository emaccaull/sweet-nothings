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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.MarkUsed;
import io.github.emaccaull.sweetnothings.ui.framework.RxViewModel;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Generate/fetch a Random SweetNothing on demand.
 */
final class GeneratorViewModel extends RxViewModel {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorViewModel.class);

    private final MutableLiveData<ViewState> viewState = new MutableLiveData<>();

    private final SchedulerProvider schedulerProvider;
    private final GetRandomSweetNothing getRandomSweetNothing;
    private final MarkUsed markUsed;

    @Inject
    @SuppressWarnings("WeakerAccess")
    public GeneratorViewModel(SchedulerProvider schedulerProvider,
            GetRandomSweetNothing getRandomSweetNothing, MarkUsed markUsed) {
        this.schedulerProvider = schedulerProvider;
        this.getRandomSweetNothing = getRandomSweetNothing;
        this.markUsed = markUsed;
        resetViewState();
    }

    void requestInitialMessage() {
        loadRandomMessage(ViewState.initial());
    }

    /**
     * Finds an unused sweet nothing and updates the view state.
     */
    void requestNewMessage() {
        loadRandomMessage(ViewState.noMessageFound());
    }

    private void loadRandomMessage(ViewState defaultIfNotFound) {
        logger.info("Requesting a new sweet nothing");

        Disposable d = getRandomSweetNothing.apply().toObservable()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .map(ViewState::loaded)
                .defaultIfEmpty(defaultIfNotFound)
                .startWith(ViewState.loading())
                .subscribe(
                        viewState::setValue,
                        throwable -> {
                            logger.error("Couldn't load sweet nothing", throwable);
                            viewState.setValue(defaultIfNotFound);
                        }
                );

        manage(d);
    }

    /**
     * Computes the next view state once a sweet nothing has been successfully shared.
     */
    void onShareSuccessful(String message) {
        logger.info("Sharing a sweet nothing; message='{}'", message);

        Disposable d = markUsed.apply(message)
                .subscribeOn(schedulerProvider.io())
                .subscribe(this::resetViewState);

        manage(d);
    }

    void onShareFailed(String message) {
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
}

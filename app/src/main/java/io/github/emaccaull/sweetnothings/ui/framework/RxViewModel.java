/*
 * Copyright (C) 2019 Emmanuel MacCaull
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

import androidx.annotation.CallSuper;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * A ViewModel with built-in RxJava stream management.
 */
public class RxViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    /**
     * Adds a disposable to ViewModel scope. When this ViewModel is cleared, the given {@code
     * disposable} will be disposed.
     *
     * @return true if the disposable was added, false if this ViewModel was already cleared.
     */
    protected final boolean add(Disposable disposable) {
        return disposables.add(disposable);
    }

    /** @return the number of currently subscribed disposables. */
    public final int subscriberCount() {
        return disposables.size();
    }

    @CallSuper
    @Override
    protected void onCleared() {
        disposables.dispose();
    }
}

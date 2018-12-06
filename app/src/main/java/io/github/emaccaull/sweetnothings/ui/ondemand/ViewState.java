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

import android.support.annotation.Nullable;
import com.google.common.base.Objects;
import io.github.emaccaull.sweetnothings.core.SweetNothing;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Describes all possible states for views of {@link GeneratorViewModel}.
 */
final class ViewState {
    private final boolean loading;
    private final @Nullable SweetNothing sweetNothing;
    private final boolean notFound;

    private ViewState(boolean loading, @Nullable SweetNothing sweetNothing, boolean notFound) {
        this.loading = loading;
        this.sweetNothing = sweetNothing;
        this.notFound = notFound;
    }

    public static ViewState initial() {
        return new ViewState(false, null, false);
    }

    public static ViewState loading() {
        return new ViewState(true, null, false);
    }

    public static ViewState loaded(SweetNothing sweetNothing) {
        return new ViewState(false, checkNotNull(sweetNothing), false);
    }

    public static ViewState noMessageFound() {
        return new ViewState(false, null, true);
    }

    public boolean isLoading() {
        return loading;
    }

    @Nullable
    public SweetNothing getSweetNothing() {
        return sweetNothing;
    }

    public boolean isNotFound() {
        return notFound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof ViewState))
            return false;

        ViewState viewState = (ViewState) o;
        return loading == viewState.loading &&
                notFound == viewState.notFound &&
                Objects.equal(sweetNothing, viewState.sweetNothing);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(loading, sweetNothing, notFound);
    }

    @Override
    public String toString() {
        return "ViewState{" +
                "loading=" + loading +
                ", sweetNothing=" + sweetNothing +
                ", notFound=" + notFound +
                '}';
    }
}

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

/**
 * Describes all possible states for views of {@link GeneratorViewModel}.
 */
public final class ViewState {
    private final boolean loading;
    private final @Nullable String message;
    private final boolean notFound;

    public ViewState(boolean loading, @Nullable String message, boolean notFound) {
        this.loading = loading;
        this.message = message;
        this.notFound = notFound;
    }

    public boolean isLoading() {
        return loading;
    }

    @Nullable
    public String getMessage() {
        return message;
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
                Objects.equal(message, viewState.message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(loading, message, notFound);
    }

    @Override
    public String toString() {
        return "ViewState{" +
                "loading=" + loading +
                ", message='" + message + '\'' +
                ", notFound=" + notFound +
                '}';
    }
}

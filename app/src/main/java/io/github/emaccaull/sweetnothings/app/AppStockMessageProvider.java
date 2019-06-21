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

package io.github.emaccaull.sweetnothings.app;

import android.content.Context;
import io.github.emaccaull.sweetnothings.R;
import io.github.emaccaull.sweetnothings.data.init.StockMessageProvider;

/**
 * Production stock message provider.
 */
class AppStockMessageProvider implements StockMessageProvider {

    private final Context context;

    AppStockMessageProvider(Context context) {
        this.context = context;
    }

    @Override
    public String[] getMessages() {
        return context.getResources().getStringArray(R.array.canned_messages);
    }
}

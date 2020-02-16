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

package io.github.emaccaull.sweetnothings.app;

import io.github.emaccaull.sweetnothings.glue.Configuration;
import io.github.emaccaull.sweetnothings.testsupport.DaggerTestConfiguration;

/**
 * Perform common setup for Espresso tests.
 */
public class TestSweetNothingsApp extends SweetNothingsApp {

    @Override
    protected Configuration createConfiguration() {
        return DaggerTestConfiguration.builder().application(this).build();
    }
}

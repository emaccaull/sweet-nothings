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

package io.github.emaccaull.sweetnothings.ui;

import io.github.emaccaull.sweetnothings.data.FakeMessageDataSource;
import io.github.emaccaull.sweetnothings.glue.Injector;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Ensures that a fake data source is used for all tests. Declare before any ActivityTestRules.
 *
 * Clears the data source between test method execution.
 */
public class FakeDataSourceTestRule implements TestRule {

    private final FakeMessageDataSource dataSource =
            (FakeMessageDataSource) Injector.provideMessageDataSource();

    static {
        Injector.setAppComponent(new TestAppComponent());
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } finally {
                    dataSource.clear();
                }
            }
        };
    }

    public FakeMessageDataSource getDataSource() {
        return dataSource;
    }
}

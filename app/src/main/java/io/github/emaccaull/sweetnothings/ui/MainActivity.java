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

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import io.github.emaccaull.sweetnothings.R;
import io.github.emaccaull.sweetnothings.ui.ondemand.GeneratorFragment;
import io.github.emaccaull.sweetnothings.ui.util.FragmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.debug("app running");
        setContentView(R.layout.main_activity);

        showGeneratorFragment();
    }

    private void showGeneratorFragment() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (!(fragment instanceof GeneratorFragment)) {
            GeneratorFragment f = GeneratorFragment.newInstance();
            FragmentUtils.replace(getSupportFragmentManager(), f, R.id.fragment_container);
        }
    }
}

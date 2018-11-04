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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import io.github.emaccaull.sweetnothings.R;
import io.github.emaccaull.sweetnothings.databinding.MainActivityBinding;
import io.github.emaccaull.sweetnothings.ui.ondemand.GeneratorFragment;
import io.github.emaccaull.sweetnothings.ui.util.FragmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {
    private final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.debug("app running");

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        GeneratorFragment fragment = findOrInsertGeneratorFragment();

        FragmentUtils.replace(getSupportFragmentManager(), fragment, R.id.fragment_container);
    }

    private GeneratorFragment findOrInsertGeneratorFragment() {
        GeneratorFragment fragment = (GeneratorFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = GeneratorFragment.newInstance();
        }

        return fragment;
    }
}

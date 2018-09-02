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

package io.github.emaccaull.sweetnothings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import io.github.emaccaull.sweetnothings.databinding.ActivityMainBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {
    private final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private ActivityMainBinding binding;

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    binding.message.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    binding.message.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    binding.message.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.info("+onCreate");
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.navigation.setOnNavigationItemSelectedListener(bottomNavigationListener);
        binding.navigation.setSelectedItemId(R.id.navigation_home);

        logger.info("-onCreate");
    }

}

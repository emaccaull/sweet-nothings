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
package io.github.emaccaull.sweetnothings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import io.github.emaccaull.sweetnothings.R
import io.github.emaccaull.sweetnothings.app.ProdConfiguration
import io.github.emaccaull.sweetnothings.app.SweetNothingsApp
import io.github.emaccaull.sweetnothings.ui.ondemand.GeneratorFragment
import io.github.emaccaull.sweetnothings.ui.ondemand.GeneratorFragment.Companion.newInstance
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            logger.debug("app running")
        }
        showGeneratorFragment()
    }

    private fun showGeneratorFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment !is GeneratorFragment) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, newInstance())
            }
        }
    }

    val configuration: ProdConfiguration
        get() = (application as SweetNothingsApp).configuration

    companion object {
        private val logger = LoggerFactory.getLogger(MainActivity::class.java)
    }
}
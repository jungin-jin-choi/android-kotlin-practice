/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer

import android.app.Application
import android.os.Build
import androidx.work.*
import com.example.android.devbyteviewer.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 */

// Application() --> OS uses this to interact with the app
class DevByteApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)


    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    // runs every time the app launches!!!
    override fun onCreate() {
        super.onCreate()
        // Configure logging in the app
        Timber.plant(Timber.DebugTree())
        // Can check what happens.. --> should do the least work inside onCreate!!!
//        Thread.sleep(4_000_000)

        delayedInit()
    }
    // Run some long-running process in the background, using coroutine
    private fun delayedInit(){
        applicationScope.launch {
            // Inside here,  delay doesn't happen in initialization, just before launch step!
//            Thread.sleep(4_000_000)
            setupRecurringWork()

        }
    }

    private fun setupRecurringWork() {
        // define constraints
        val constraints = Constraints.Builder()
                // Use network in unmetered situation (Ex. wifi zone..)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                // require that the device is in charging state
                .setRequiresCharging(true)
                .apply{
                    // for higher versions
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        // do the background thing when the device is idle
                        setRequiresDeviceIdle(true)
                    }
                }.build()

        // build work request
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
                1,
                TimeUnit.DAYS
        )
                .setConstraints(constraints)
                .build()
        // schedule the work
        WorkManager.getInstance().enqueueUniquePeriodicWork(
                // work should be unique
                RefreshDataWorker.WORK_NAME,
                // what if not unique? --> policy
                // KEEP --> keep the previous work request & discard the new one
                // REPLACE --> replace the old request
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
        )
    }
}

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
 */

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SleepNight::class], version = 1,  exportSchema = false)
abstract class SleepDatabase : RoomDatabase(){
    abstract val sleepDatabaseDao: SleepDatabaseDao
    // Companion object allows clients to access the methods
    // for creating or getting the database without instantiating the class
    companion object{
        /**
         * @Volatile ensures the property to be always up-to-date & same to all exec threads
         * Value of a volatile var will never be cached : All R/W are done in main memory
         * Changes made by one thread are visible to all other threads immediately
         **/
        @Volatile
        // INSTANCE will keep a reference to the database once we have one
        private var INSTANCE: SleepDatabase? = null

        fun getInstance(context: Context): SleepDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    // Use database builder to get a database
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            SleepDatabase::class.java,
                            "sleep_history_database"
                    )
                            // Add the required migration strategy to the builder
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

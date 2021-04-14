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

package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosRepository(private val database: VideosDatabase){
    val videos: LiveData<List<Video>> = Transformations.map(database.videoDao.getVideos()){
        it.asDomainModel()
    }

    // suspend keyword ---> function will be called from a coroutine
    suspend fun refreshVideos(){
        // withContext(Dispatchers.IO) --> force the Kotlin coroutine to switch to the IO dispatcher
        withContext(Dispatchers.IO){
            // getPlaylist() --> function for making a network call
            // await() ---> coroutine will suspend until the data is available
            val playlist = Network.devbytes.getPlaylist().await()
            // insert through Dao interface
            // `*` asterisk allows to pass in array to a function that expects varargs
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        }

    }
}
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

package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://mars.udacity.com/"

/**
 * Create Moshi object
 * */
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

/**
 *  Create Retrofit object
 *  1) Add converter factory
 *  2) Add base URL
 * */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

/**
 * Service API, implement as an interface
 * Define method to request JSON response string
 * */
interface MarsApiService{
    /**
     * @GET specifies the endpoint for JSON Response (ex. realestate)
     * When getProperties() is called, Retrofit appends to the endpoint to the base URL
     * and creates a Deferred object, which creates and starts the HTTP network request
     * (By calling it in a coroutine scope, the network request will be started in a background thread)
     * */
    @GET("realestate")
    suspend fun getProperties():
            // Deferred - coroutine job that can directly return a result
            List<MarsProperty>
}

/**
 * Create a Retrofit Service to expose it to the rest of the app
 * (Above interface is "interface", creation is done here by passing in the Service API interface)
 *
 *  retrofit.create() calls are expensive!
 *  Should declare as `object` since our app needs only one retrofit service instance.
 * */
object MarsAPI{
    /**
     * Lazy initialization! (Created the first time it is actually used)
     * Calling MarsAPI.retrofitService will return a retrofit object that implements MarsApiService
     * */
    val retrofitService: MarsApiService by lazy{
        retrofit.create(MarsApiService::class.java)
    }
}
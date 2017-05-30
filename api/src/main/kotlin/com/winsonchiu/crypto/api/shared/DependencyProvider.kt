package com.winsonchiu.crypto.api.shared

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

object DependencyProvider {

    val okHttpClient: OkHttpClient

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()

        if (Configuration.DEBUG) {
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        okHttpClient = okHttpClientBuilder.build()
    }

}
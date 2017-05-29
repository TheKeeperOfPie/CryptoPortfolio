package com.winsonchiu.crypto.api.coinmarketcap

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.winsonchiu.crypto.api.coinmarketcap.data.Currency
import com.winsonchiu.crypto.api.coinmarketcap.data.GlobalData
import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker
import com.winsonchiu.crypto.api.coinmarketcap.data.TickerDeserializer
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

class CoinMarketCapApi {

    companion object {
        const val BASE_URL = "https://api.coinmarketcap.com/v1/"
    }

    val coinMarketCapApiService: CoinMarketCapApiService

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()

//        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//        }

        val module = SimpleModule().addDeserializer(Ticker::class.java, TickerDeserializer())

        val objectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(module)

        coinMarketCapApiService = Retrofit.Builder()
                .client(okHttpClientBuilder.build())
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(CoinMarketCapApiService::class.java)
    }

    fun ticker(limit: Int?, currency: Currency?): Single<List<Ticker>> {
        return coinMarketCapApiService.ticker(limit, currency?.serializedName)
    }

    fun ticker(id: String, convertCurrency: Currency?): Single<Ticker> {
        return coinMarketCapApiService.ticker(id, convertCurrency?.serializedName)
    }

    fun globalData(convertCurrency: Currency?): Single<GlobalData> {
        return coinMarketCapApiService.globalData(convertCurrency?.serializedName)
    }
}
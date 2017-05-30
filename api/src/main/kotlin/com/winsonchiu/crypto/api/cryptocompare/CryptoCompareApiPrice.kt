package com.winsonchiu.crypto.api.cryptocompare

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.winsonchiu.crypto.api.shared.DependencyProvider
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

class CryptoCompareApiPrice {

    val apiService: CryptoCompareApiServicePrice

    init {
        val objectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        apiService = Retrofit.Builder()
                .client(DependencyProvider.okHttpClient)
                .baseUrl(CryptoCompareApiServicePrice.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(CryptoCompareApiServicePrice::class.java)
    }

    fun prices(symbols: List<String>, currencies: List<String>): Single<Map<String, Map<String, BigDecimal>>> {
        return apiService.priceMulti(symbols.joinToString(separator = ","), currencies.joinToString(separator = ","))
    }
}
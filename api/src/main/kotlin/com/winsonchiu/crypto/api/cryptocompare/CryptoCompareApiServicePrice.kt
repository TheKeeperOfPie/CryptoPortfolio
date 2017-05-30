package com.winsonchiu.crypto.api.cryptocompare

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

interface CryptoCompareApiServicePrice {

    companion object {
        const val BASE_URL = "https://min-api.cryptocompare.com/"
    }

    @GET("data/price")
    fun price(
            @Query("fsym") fromCurrency: String,
            @Query("tsyms") toCurrency: String
    ): Single<Map<String, BigDecimal>>

    @GET("data/pricemulti")
    fun priceMulti(
            @Query("fsyms") fromCurrency: String,
            @Query("tsyms") toCurrency: String
    ): Single<Map<String, Map<String, BigDecimal>>>
}
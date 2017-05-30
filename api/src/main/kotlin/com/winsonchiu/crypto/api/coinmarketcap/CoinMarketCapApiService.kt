package com.winsonchiu.crypto.api.coinmarketcap

import com.winsonchiu.crypto.api.coinmarketcap.data.GlobalData
import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

interface CoinMarketCapApiService {

    companion object {
        const val BASE_URL = "https://api.coinmarketcap.com/v1/"
    }

    @GET("ticker")
    fun ticker(
            @Query("limit") limit: Int?,
            @Query("convert") convertCurrency: String?
    ): Single<List<Ticker>>

    @GET("ticker/{id}")
    fun ticker(
            @Path("id") id: String,
            @Query("convert") convertCurrency: String?
    ): Single<Ticker>

    @GET("global")
    fun globalData(
            @Query("convert") convertCurrency: String?
    ): Single<GlobalData>
}
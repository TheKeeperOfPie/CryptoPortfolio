package com.winsonchiu.crypto.api.cryptocompare

import com.winsonchiu.crypto.api.cryptocompare.data.CoinListResponse
import retrofit2.http.GET

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

interface CryptoCompareApiServiceCoin {

    @GET("https://www.cryptocompare.com/api/data/coinlist/")
    fun coinList(): CoinListResponse

}
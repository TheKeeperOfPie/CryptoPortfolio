package com.winsonchiu.crypto.api.coinmarketcap

import com.winsonchiu.crypto.api.coinmarketcap.data.GlobalData
import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */
interface CoinMarketCapDataStore {

    var globalData: GlobalData?

    fun updateTicker(ticker: Ticker)

    fun readTicker(id: String?): Ticker?

    fun storeTickers(tickers: List<Ticker>)

    fun readTickers(): List<Ticker>

    fun clearTickers()
}
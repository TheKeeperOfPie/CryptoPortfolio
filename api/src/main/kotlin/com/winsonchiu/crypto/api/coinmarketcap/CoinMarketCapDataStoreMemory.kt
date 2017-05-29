package com.winsonchiu.crypto.api.coinmarketcap

import com.winsonchiu.crypto.api.coinmarketcap.data.GlobalData
import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */
class CoinMarketCapDataStoreMemory(
        override var globalData: GlobalData?
) : CoinMarketCapDataStore {
    private val tickerMap: MutableMap<String?, Ticker> = HashMap()

    override fun updateTicker(ticker: Ticker) {
        tickerMap.put(ticker.id, ticker)
    }

    override fun readTicker(id: String?): Ticker? = tickerMap[id]

    override fun storeTickers(tickers: List<Ticker>) {
        tickers.forEach({ updateTicker(it) })
    }

    override fun readTickers(): List<Ticker> {
        return tickerMap.keys
                .map { readTicker(it) }
                .filterNotNull()
    }

    override fun clearTickers() = tickerMap.clear()
}
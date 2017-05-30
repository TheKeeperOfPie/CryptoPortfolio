package com.winsonchiu.crypto.api.cryptocompare

import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

class CryptoCompareDataStoreMemory : CryptoCompareDataStore {

    val prices = HashMap<String, Map<String, BigDecimal>>()

    override fun storePrices(prices: Map<String, Map<String, BigDecimal>>) {
        this.prices.clear()
        this.prices.putAll(prices)
    }

    override fun readPrices(): Map<String, Map<String, BigDecimal>> = prices
}
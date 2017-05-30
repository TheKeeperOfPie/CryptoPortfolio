package com.winsonchiu.crypto.api.cryptocompare

import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

interface CryptoCompareDataStore {

    fun storePrices(prices: Map<String, Map<String, BigDecimal>>)

    fun readPrices(): Map<String, Map<String, BigDecimal>>
}
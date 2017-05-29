package com.winsonchiu.cryptoportfolio.home

import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker
import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/29/2017.
 */

data class TickerViewModel(
        val displayName: String?,
        val displayValue: BigDecimal,
        val displayChangeAmount: BigDecimal,
        val displayChangePercent: BigDecimal,
        val ticker: Ticker
)
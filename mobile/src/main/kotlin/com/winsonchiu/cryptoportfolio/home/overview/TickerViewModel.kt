package com.winsonchiu.cryptoportfolio.home.overview

import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker
import java.math.BigDecimal
import java.math.MathContext

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

fun Ticker.toViewModel(tickersSort: TickersSort): TickerViewModel {
    val displayValue = when (tickersSort) {
        TickersSort.PRICE -> price
        TickersSort.MARKET_CAP -> price // TODO
        TickersSort.DAY_VOLUME -> dayVolume
        TickersSort.CHANGE_HOUR -> percentChangeHour
        TickersSort.CHANGE_DAY -> percentChangeDay
        TickersSort.CHANGE_WEEK -> percentChangeWeek
        else -> price
    }.round(MathContext(5))

    val percentChange = when (tickersSort) {
        TickersSort.CHANGE_HOUR -> percentChangeHour
        TickersSort.CHANGE_DAY -> percentChangeDay
        TickersSort.CHANGE_WEEK -> percentChangeWeek
        else -> percentChangeDay
    }

    val percentage = BigDecimal(100L) - percentChange
    val originalValue = price * BigDecimal(100L) / percentage
    val amountChange = (originalValue - price).round(MathContext(5))

    return TickerViewModel(symbol, displayValue, amountChange, percentChange, this)
}
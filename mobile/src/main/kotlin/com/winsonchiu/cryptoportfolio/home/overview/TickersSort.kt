package com.winsonchiu.cryptoportfolio.home.overview

import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker
import com.winsonchiu.cryptoportfolio.R

/**
 * Created by TheKeeperOfPie on 5/29/2017.
 */

enum class TickersSort(val menuItemId: Int, val defaultSortAscending: Boolean = false) {
    PRICE(R.id.menu_item_price),
    MARKET_CAP(R.id.menu_item_market_cap),
    DAY_VOLUME(R.id.menu_item_day_volume),
    CHANGE_HOUR(R.id.menu_item_change_hour),
    CHANGE_DAY(R.id.menu_item_change_day),
    CHANGE_WEEK(R.id.menu_item_change_week),
    SYMBOL(R.id.menu_item_symbol, true),
    NAME(R.id.menu_item_name, true);

    fun getSortComparable(ticker: Ticker): Comparable<*>? {
        return when (this) {
            TickersSort.PRICE -> ticker.price
            TickersSort.MARKET_CAP -> ticker.marketCap
            TickersSort.DAY_VOLUME -> ticker.dayVolume
            TickersSort.CHANGE_HOUR -> ticker.percentChangeHour
            TickersSort.CHANGE_DAY -> ticker.percentChangeDay
            TickersSort.CHANGE_WEEK -> ticker.percentChangeWeek
            TickersSort.SYMBOL -> ticker.symbol
            TickersSort.NAME -> ticker.name
        }
    }
}
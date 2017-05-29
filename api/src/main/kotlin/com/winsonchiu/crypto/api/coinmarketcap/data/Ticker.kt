package com.winsonchiu.crypto.api.coinmarketcap.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

class Ticker(
        @JsonProperty("id") val id: String?,
        @JsonProperty("name") val name: String?,
        @JsonProperty("symbol") val symbol: String?,
        @JsonProperty("rank") val rank: Long?,
        @JsonProperty("price_usd") var priceUsd: BigDecimal = BigDecimal.ZERO,
        @JsonProperty("price_btc") val priceBtc: BigDecimal = BigDecimal.ZERO,
        @JsonProperty("24h_volume_usd") var dayVolumeUsd: BigDecimal = BigDecimal.ZERO,
        @JsonProperty("market_cap_usd") var marketCapUsd: BigDecimal = BigDecimal.ZERO,
        @JsonProperty("available_supply") val availableSupply: BigDecimal = BigDecimal.ZERO,
        @JsonProperty("total_supply") val totalSupply: BigDecimal = BigDecimal.ZERO,
        @JsonProperty("percent_change_1h") val percentChangeHour: BigDecimal = BigDecimal.ZERO,
        @JsonProperty("percent_change_24h") val percentChangeDay: BigDecimal = BigDecimal.ZERO,
        @JsonProperty("percent_change_7d") val percentChangeWeek: BigDecimal = BigDecimal.ZERO,
        @JsonIgnore var currency: Currency = Currency.USD,
        @JsonIgnore var price: BigDecimal = BigDecimal.ZERO,
        @JsonIgnore var dayVolume: BigDecimal = BigDecimal.ZERO,
        @JsonIgnore var marketCap: BigDecimal = BigDecimal.ZERO
) {

    @JsonProperty("last_updated") var lastUpdatedSeconds: Long? = 0

    override fun toString(): String {
        return "Ticker(id=$id, name=$name, symbol=$symbol, rank=$rank, priceUsd=$priceUsd, priceBtc=$priceBtc, dayVolumeUsd=$dayVolumeUsd, marketCapUsd=$marketCapUsd, availableSupply=$availableSupply, totalSupply=$totalSupply, percentChangeHour=$percentChangeHour, percentChangeDay=$percentChangeDay, percentChangeWeek=$percentChangeWeek, currency=$currency, price=$price, dayVolume=$dayVolume, marketCap=$marketCap, lastUpdatedSeconds=$lastUpdatedSeconds)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Ticker

        if (id != other.id) return false
        if (name != other.name) return false
        if (symbol != other.symbol) return false
        if (rank != other.rank) return false
        if (priceUsd != other.priceUsd) return false
        if (priceBtc != other.priceBtc) return false
        if (dayVolumeUsd != other.dayVolumeUsd) return false
        if (marketCapUsd != other.marketCapUsd) return false
        if (availableSupply != other.availableSupply) return false
        if (totalSupply != other.totalSupply) return false
        if (percentChangeHour != other.percentChangeHour) return false
        if (percentChangeDay != other.percentChangeDay) return false
        if (percentChangeWeek != other.percentChangeWeek) return false
        if (currency != other.currency) return false
        if (price != other.price) return false
        if (dayVolume != other.dayVolume) return false
        if (marketCap != other.marketCap) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (symbol?.hashCode() ?: 0)
        result = 31 * result + (rank?.hashCode() ?: 0)
        result = 31 * result + priceUsd.hashCode()
        result = 31 * result + priceBtc.hashCode()
        result = 31 * result + dayVolumeUsd.hashCode()
        result = 31 * result + marketCapUsd.hashCode()
        result = 31 * result + availableSupply.hashCode()
        result = 31 * result + totalSupply.hashCode()
        result = 31 * result + percentChangeHour.hashCode()
        result = 31 * result + percentChangeDay.hashCode()
        result = 31 * result + percentChangeWeek.hashCode()
        result = 31 * result + currency.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + dayVolume.hashCode()
        result = 31 * result + marketCap.hashCode()
        return result
    }


}
package com.winsonchiu.crypto.api.coinmarketcap.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

data class GlobalData(
        @JsonProperty("total_market_cap_usd") val totalMarketCapUsd: BigDecimal,
        @JsonProperty("total_24h_volume_usd") val totalDayVolumeUsd: BigDecimal,
        @JsonProperty("bitcoin_percentage_of_market_cap") val bitcoinPercentageOfMarketCap: BigDecimal,
        @JsonProperty("active_currencies") val activeCurrencies: Long,
        @JsonProperty("active_assets") val activeAssets: Long,
        @JsonProperty("active_markets") val activeMarkets: Long,

        @JsonProperty("total_market_cap_aud") val totalMarketCapAud: BigDecimal?,
        @JsonProperty("total_24h_volume_aud") val totalDayVolumeAud: BigDecimal?,

        @JsonProperty("total_market_cap_brl") val totalMarketCapBrl: BigDecimal?,
        @JsonProperty("total_24h_volume_brl") val totalDayVolumeBrl: BigDecimal?,

        @JsonProperty("total_market_cap_cad") val totalMarketCapCad: BigDecimal?,
        @JsonProperty("total_24h_volume_cad") val totalDayVolumeCad: BigDecimal?,

        @JsonProperty("total_market_cap_chf") val totalMarketCapChf: BigDecimal?,
        @JsonProperty("total_24h_volume_chf") val totalDayVolumeChf: BigDecimal?,

        @JsonProperty("total_market_cap_cny") val totalMarketCapCny: BigDecimal?,
        @JsonProperty("total_24h_volume_cny") val totalDayVolumeCny: BigDecimal?,

        @JsonProperty("total_market_cap_eur") val totalMarketCapEur: BigDecimal?,
        @JsonProperty("total_24h_volume_eur") val totalDayVolumeEur: BigDecimal?,

        @JsonProperty("total_market_cap_gbp") val totalMarketCapGbp: BigDecimal?,
        @JsonProperty("total_24h_volume_gbp") val totalDayVolumeGbp: BigDecimal?,

        @JsonProperty("total_market_cap_hkd") val totalMarketCapHkd: BigDecimal?,
        @JsonProperty("total_24h_volume_hkd") val totalDayVolumeHkd: BigDecimal?,

        @JsonProperty("total_market_cap_idr") val totalMarketCapIdr: BigDecimal?,
        @JsonProperty("total_24h_volume_idr") val totalDayVolumeIdr: BigDecimal?,

        @JsonProperty("total_market_cap_inr") val totalMarketCapInr: BigDecimal?,
        @JsonProperty("total_24h_volume_inr") val totalDayVolumeInr: BigDecimal?,

        @JsonProperty("total_market_cap_jpy") val totalMarketCapJpy: BigDecimal?,
        @JsonProperty("total_24h_volume_jpy") val totalDayVolumeJpy: BigDecimal?,

        @JsonProperty("total_market_cap_krw") val totalMarketCapKrw: BigDecimal?,
        @JsonProperty("total_24h_volume_krw") val totalDayVolumeKrw: BigDecimal?,

        @JsonProperty("total_market_cap_mxn") val totalMarketCapMxn: BigDecimal?,
        @JsonProperty("total_24h_volume_mxn") val totalDayVolumeMxn: BigDecimal?,

        @JsonProperty("total_market_cap_rub") val totalMarketCapRub: BigDecimal?,
        @JsonProperty("total_24h_volume_rub") val totalDayVolumeRub: BigDecimal?
) {

    fun toTotalMarketCap(currency: Currency) = when (currency) {
        Currency.AUD -> totalMarketCapAud
        Currency.BRL -> totalMarketCapBrl
        Currency.CAD -> totalMarketCapCad
        Currency.CHF -> totalMarketCapChf
        Currency.CNY -> totalMarketCapCny
        Currency.EUR -> totalMarketCapEur
        Currency.GBP -> totalMarketCapGbp
        Currency.HKD -> totalMarketCapHkd
        Currency.IDR -> totalMarketCapIdr
        Currency.INR -> totalMarketCapInr
        Currency.JPY -> totalMarketCapJpy
        Currency.KRW -> totalMarketCapKrw
        Currency.MXN -> totalMarketCapMxn
        Currency.RUB -> totalMarketCapRub
        Currency.USD -> totalMarketCapUsd
    }

    fun toTotalDayVolume(currency: Currency) = when (currency) {
        Currency.AUD -> totalDayVolumeAud
        Currency.BRL -> totalDayVolumeBrl
        Currency.CAD -> totalDayVolumeCad
        Currency.CHF -> totalDayVolumeChf
        Currency.CNY -> totalDayVolumeCny
        Currency.EUR -> totalDayVolumeEur
        Currency.GBP -> totalDayVolumeGbp
        Currency.HKD -> totalDayVolumeHkd
        Currency.IDR -> totalDayVolumeIdr
        Currency.INR -> totalDayVolumeInr
        Currency.JPY -> totalDayVolumeJpy
        Currency.KRW -> totalDayVolumeKrw
        Currency.MXN -> totalDayVolumeMxn
        Currency.RUB -> totalDayVolumeRub
        Currency.USD -> totalDayVolumeUsd
    }
}
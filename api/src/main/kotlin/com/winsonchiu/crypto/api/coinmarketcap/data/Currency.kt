package com.winsonchiu.crypto.api.coinmarketcap.data

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

enum class Currency(
        val serializedName: String,
        val displayNameIso4217: String
) {
    AUD("AUD", "Australian Dollar"),
    BRL("BRL", "Brazilian Real"),
    CAD("CAD", "Canadian Dollar"),
    CHF("CHF", "Swiss Franc"),
    CNY("CNY", "Chinese Yuan"),
    EUR("EUR", "Euro"),
    GBP("GBP", "Pound Sterling"),
    HKD("HKD", "Hong Kong Dollar"),
    IDR("IDR", "Indonesian Rupiah"),
    INR("INR", "Indian Rupee"),
    JPY("JPY", "Japanese Yen"),
    KRW("KRW", "South Korean Won"),
    MXN("MXN", "Mexican Peso"),
    RUB("RUB", "Russian Ruble"),
    USD("USD", "United States Dollar")
}
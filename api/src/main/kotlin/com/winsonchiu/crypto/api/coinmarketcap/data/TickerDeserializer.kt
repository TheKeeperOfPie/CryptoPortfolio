package com.winsonchiu.crypto.api.coinmarketcap.data

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/29/2017.
 */

class TickerDeserializer : JsonDeserializer<Ticker?>() {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext?): Ticker? {
        val node: JsonNode = jsonParser.readValueAsTree()
        val json = objectMapper.writeValueAsString(node)

        val currency = Currency.values().firstOrNull { node.has("price_" + it.serializedName.toLowerCase()) } ?: return null

        val lowercaseName = currency.serializedName.toLowerCase()
        val price = node.get("price_" + lowercaseName).asText()
        val dayVolume = node.get("24h_volume_" + lowercaseName).asText()
        val marketCap = node.get("market_cap_" + lowercaseName).asText()

        val ticker = objectMapper.readValue(json, Ticker::class.java)
        ticker.currency = currency
        ticker.price = if (price == null) BigDecimal.ZERO else BigDecimal(price)
        ticker.dayVolume = if (dayVolume == null) BigDecimal.ZERO else BigDecimal(dayVolume)
        ticker.marketCap = if (marketCap == null) BigDecimal.ZERO else BigDecimal(marketCap)
        return ticker
    }

}
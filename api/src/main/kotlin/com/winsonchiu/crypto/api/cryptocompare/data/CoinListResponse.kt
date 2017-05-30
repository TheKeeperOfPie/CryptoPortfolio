package com.winsonchiu.crypto.api.cryptocompare.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

data class CoinListResponse(
        @JsonProperty("Response") val responseType: String,
        @JsonProperty("Message") val message: String,
        @JsonProperty("BaseImageUrl") val baseImageUrl: String,
        @JsonProperty("BaseLinkUrl") val baseLinkUrl: String,
        @JsonProperty("Data") val data: Map<String, Coin>,
        @JsonProperty("Type") val type: Long
)
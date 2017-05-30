package com.winsonchiu.crypto.api.cryptocompare.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

data class Coin(
    @JsonProperty("Id") val id: String,
    @JsonProperty("Url") val relativeUrl: String,
    @JsonProperty("ImageUrl") val relativeImageUrl: String,
    @JsonProperty("Name") val name: String,
    @JsonProperty("CoinName") val coinName: String,
    @JsonProperty("FullName") val fullName: String,
    @JsonProperty("Algorithm") val algorithm: String,
    @JsonProperty("ProofType") val proofType: String,
    @JsonProperty("SortOrder") val sortOrder: Long
)
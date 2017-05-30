package com.winsonchiu.cryptoportfolio.home.portfolio

import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

data class PriceViewModel(
        val displayName: String,
        val displayValue: BigDecimal,
        val changeFromPrevious: BigDecimal
)
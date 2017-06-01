package com.winsonchiu.cryptoportfolio.home.overview

import android.icu.text.NumberFormat
import com.airbnb.epoxy.Typed3EpoxyController
import com.winsonchiu.crypto.api.coinmarketcap.data.Currency
import com.winsonchiu.crypto.api.coinmarketcap.data.GlobalData
import java.util.*

/**
 * Created by TheKeeperOfPie on 6/1/2017.
 */

class OverviewController : Typed3EpoxyController<GlobalData?, List<TickerViewModel>, Currency>() {

//    @AutoModel lateinit var globalDataModel: GlobalDataModel

    val currencyInstance = NumberFormat.getCurrencyInstance()

    val globalDataId = Random().nextInt(Int.MAX_VALUE)

    override fun buildModels(globalData: GlobalData?, tickerViewModels: List<TickerViewModel>, currency: Currency) {
        currencyInstance.currency = android.icu.util.Currency.getInstance(currency.serializedName)
        currencyInstance.maximumFractionDigits = Int.MAX_VALUE

        GlobalDataModel(globalData, currencyInstance)
                .id(globalDataId)
//                .globalData(globalData)
                .addIf(globalData != null, this)

        tickerViewModels.forEach {
            TickerModel(it, currencyInstance)
                    .id(it.ticker.id)
//                    .tickerViewModel(it)
//                    .currencyInstance(currencyInstance)
                    .addTo(this)
        }
    }
}
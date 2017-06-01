package com.winsonchiu.cryptoportfolio.home.overview

import android.icu.text.NumberFormat
import android.widget.TextView
import butterknife.BindView
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.winsonchiu.crypto.api.coinmarketcap.data.GlobalData
import com.winsonchiu.cryptoportfolio.R
import com.winsonchiu.cryptoportfolio.framework.epoxy.BaseEpoxyHolder

/**
 * Created by TheKeeperOfPie on 6/1/2017.
 */

//@EpoxyModelClass(layout = R.layout.global_data_view)
class GlobalDataModel(val globalData: GlobalData?,
                      val currencyInstance: NumberFormat) : EpoxyModelWithHolder<GlobalDataModel.GlobalDataViewHolder>() {

//    @EpoxyAttribute var globalData: GlobalData? = null
//    @EpoxyAttribute lateinit var currencyInstance: NumberFormat

    override fun getDefaultLayout(): Int =  R.layout.global_data_view

    override fun createNewHolder(): GlobalDataModel.GlobalDataViewHolder = GlobalDataModel.GlobalDataViewHolder()

    override fun bind(holder: GlobalDataViewHolder) {
        val globalData = globalData
        if (globalData != null) {
            holder.bindData(globalData, currencyInstance)
        }
    }

    class GlobalDataViewHolder : BaseEpoxyHolder() {

        @BindView(R.id.text_market_cap) lateinit var textMarketCap: TextView

        fun bindData(globalData: GlobalData, currencyInstance: NumberFormat) {
            textMarketCap.text = itemView.resources.getString(R.string.global_market_cap_format, currencyInstance.format(globalData.totalMarketCapUsd))
        }
    }
}
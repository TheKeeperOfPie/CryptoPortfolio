package com.winsonchiu.cryptoportfolio.home.overview

import android.icu.text.NumberFormat
import android.widget.TextView
import butterknife.BindView
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.winsonchiu.cryptoportfolio.R
import com.winsonchiu.cryptoportfolio.framework.epoxy.BaseEpoxyHolder

/**
 * Created by TheKeeperOfPie on 6/1/2017.
 */

//@EpoxyModelClass(layout = R.layout.ticker_view)
class TickerModel(val tickerViewModel: TickerViewModel,
                  val currencyInstance: NumberFormat) : EpoxyModelWithHolder<TickerModel.TickerViewHolder>() {

//    @EpoxyAttribute lateinit var tickerViewModel: TickerViewModel
//    @EpoxyAttribute lateinit var currencyInstance: NumberFormat

    override fun getDefaultLayout(): Int =  R.layout.ticker_view

    override fun createNewHolder(): TickerViewHolder = TickerViewHolder()

    override fun bind(holder: TickerViewHolder) {
        holder.bindData(tickerViewModel, currencyInstance)
    }

    class TickerViewHolder : BaseEpoxyHolder() {

        @BindView(R.id.text_name) lateinit var textName: TextView
        @BindView(R.id.text_change) lateinit var textChange: TextView
        @BindView(R.id.text_value) lateinit var textValue: TextView

        fun bindData(tickerViewModel: TickerViewModel, currencyInstance: NumberFormat) {
            val displayValue = tickerViewModel.displayValue

            textName.text = tickerViewModel.displayName
            textValue.text = currencyInstance.format(displayValue).replace("(?<=[A-Za-z])(?=[^A-Za-z])".toRegex(), " ")

            val displayChange = tickerViewModel.displayChangePercent
            val signum = displayChange.signum()
            textChange.setTextColor(when {
                signum > 0 -> android.graphics.Color.GREEN
                signum < 0 -> android.graphics.Color.RED
                else -> android.graphics.Color.GRAY
            })

            val changeResource = when {
                signum > 0 -> com.winsonchiu.cryptoportfolio.R.string.change_positive
                signum < 0 -> com.winsonchiu.cryptoportfolio.R.string.change_negative
                else -> com.winsonchiu.cryptoportfolio.R.string.change_none
            }

            val displayChangeAmount = currencyInstance.format(tickerViewModel.displayChangeAmount)
            textChange.text = itemView.resources.getString(changeResource, displayChangeAmount, displayChange)
        }
    }
}
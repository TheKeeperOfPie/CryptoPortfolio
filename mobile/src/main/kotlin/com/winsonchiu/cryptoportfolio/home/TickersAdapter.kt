package com.winsonchiu.cryptoportfolio.home

import android.graphics.Color
import android.icu.text.NumberFormat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.winsonchiu.crypto.api.coinmarketcap.data.Currency
import com.winsonchiu.cryptoportfolio.R

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

class TickersAdapter : RecyclerView.Adapter<TickersAdapter.ViewHolderTicker>() {

    val currencyInstance = NumberFormat.getCurrencyInstance()

    var currency = Currency.USD

    val tickers = ArrayList<TickerViewModel>()

    fun setData(newTickers: List<TickerViewModel>, diffResult: DiffUtil.DiffResult, currency: Currency) {
        this.currency = currency
        currencyInstance.currency = android.icu.util.Currency.getInstance(currency.serializedName)
        currencyInstance.maximumFractionDigits = Int.MAX_VALUE
        tickers.clear()
        tickers.addAll(newTickers)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(viewHolderTicker: ViewHolderTicker, position: Int) {
        viewHolderTicker.bindData(tickers[position], currencyInstance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTicker {
        return ViewHolderTicker(parent)
    }

    override fun getItemCount(): Int = tickers.size

    class ViewHolderTicker(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ticker_view, parent, false)) {

        @BindView(R.id.text_name) lateinit var textName: TextView
        @BindView(R.id.text_change) lateinit var textChange: TextView
        @BindView(R.id.text_value) lateinit var textValue: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindData(tickerViewModel: TickerViewModel, currencyInstance: NumberFormat) {
            val displayValue = tickerViewModel.displayValue

            textName.text = tickerViewModel.displayName
            textValue.text = currencyInstance.format(displayValue).replace("(?<=[A-Za-z])(?=[^A-Za-z])".toRegex(), " ")

            val displayChange = tickerViewModel.displayChangePercent
            val signum = displayChange.signum()
            textChange.setTextColor(when {
                signum > 0 -> Color.GREEN
                signum < 0 -> Color.RED
                else -> Color.GRAY
            })

            val changeResource = when {
                signum > 0 -> R.string.change_positive
                signum < 0 -> R.string.change_negative
                else -> R.string.change_none
            }

            val displayChangeAmount = currencyInstance.format(tickerViewModel.displayChangeAmount)
            textChange.text = itemView.resources.getString(changeResource, displayChangeAmount, displayChange)
        }
    }
}

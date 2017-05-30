package com.winsonchiu.cryptoportfolio.home.portfolio

import android.graphics.Color
import android.icu.text.NumberFormat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.winsonchiu.crypto.api.coinmarketcap.data.Currency
import com.winsonchiu.cryptoportfolio.R
import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

class PricesAdapter : RecyclerView.Adapter<PricesAdapter.ViewHolderPrice>() {

    val currencyInstance = NumberFormat.getCurrencyInstance()

    var currency = Currency.USD

    val prices = ArrayList<PriceViewModel>()

    fun setData(newPrices: List<PriceViewModel>, diffResult: DiffUtil.DiffResult) {
        currencyInstance.currency = android.icu.util.Currency.getInstance(currency.serializedName)
        currencyInstance.maximumFractionDigits = Int.MAX_VALUE
        prices.clear()
        prices.addAll(newPrices)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(viewHolderPrice: ViewHolderPrice, position: Int) {
        viewHolderPrice.bindData(prices[position], currencyInstance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPrice {
        return ViewHolderPrice(parent)
    }

    override fun getItemCount(): Int = prices.size

    class ViewHolderPrice(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.price_view, parent, false)) {

        @butterknife.BindView(R.id.text_name) lateinit var textName: TextView
        @butterknife.BindView(R.id.text_value) lateinit var textValue: TextView

        init {
            butterknife.ButterKnife.bind(this, itemView)
        }

        fun bindData(priceViewModel: PriceViewModel, currencyInstance: android.icu.text.NumberFormat) {
            val displayValue = priceViewModel.displayValue

            textName.text = priceViewModel.displayName
            textValue.text = currencyInstance.format(displayValue).replace("(?<=[A-Za-z])(?=[^A-Za-z])".toRegex(), " ")

            val changeFromPrevious = priceViewModel.changeFromPrevious
            textValue.setTextColor(when {
                changeFromPrevious > BigDecimal(0) -> Color.GREEN
                changeFromPrevious < BigDecimal(0) -> Color.RED
                else -> Color.WHITE
            })
        }
    }
}
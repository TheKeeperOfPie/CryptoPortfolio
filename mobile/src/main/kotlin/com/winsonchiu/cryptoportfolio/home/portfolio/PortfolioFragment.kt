package com.winsonchiu.cryptoportfolio.home.portfolio

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.fasterxml.jackson.databind.ObjectMapper
import com.jakewharton.rxrelay2.PublishRelay
import com.winsonchiu.crypto.api.cryptocompare.CryptoCompare
import com.winsonchiu.crypto.api.cryptocompare.CryptoCompareDataStore
import com.winsonchiu.crypto.api.shared.RequestState
import com.winsonchiu.cryptoportfolio.R
import com.winsonchiu.cryptoportfolio.framework.android.BaseFragment
import com.winsonchiu.cryptoportfolio.framework.android.ViewUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

class PortfolioFragment : BaseFragment() {

    companion object {
        fun newInstance(): PortfolioFragment {
            return PortfolioFragment()
        }

        val SYMBOLS = listOf("BTC", "ETH", "GNT", "REP", "LTC", "XAUR")
        val CURRENCIES = listOf("USD")
    }

    @BindView(R.id.layout_swipe_refresh) lateinit var layoutSwipeRefresh: SwipeRefreshLayout
    @BindView(R.id.recycler_tickers) lateinit var recyclerTickers: RecyclerView

    private lateinit var preferences: SharedPreferences

    private val objectMapper = ObjectMapper()

    private val cryptoCompare = CryptoCompare(object : CryptoCompareDataStore {

        override fun storePrices(prices: Map<String, Map<String, BigDecimal>>) {
            val json = objectMapper.writeValueAsString(prices)
            preferences.edit()
                    .putString("prices", json)
                    .apply()
        }

        override fun readPrices(): Map<String, Map<String, BigDecimal>> {
            val json = preferences.getString("prices", "")
            val typeFactory = objectMapper.typeFactory
            val stringType = typeFactory.constructType(String::class.java)
            val valueType = typeFactory.constructMapType(HashMap::class.java, String::class.java, BigDecimal::class.java)
            val resultType = typeFactory.constructMapType(HashMap::class.java, stringType, valueType)
            return objectMapper.readValue(json, resultType)
        }
    })
    private val pricesAdapter = PricesAdapter()

    private val relayContent = PublishRelay.create<Map<String, Map<String, BigDecimal>>>()

    override fun getTitleResource(): Int = R.string.name_portfolio

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.portfolio_fragment, container, false)
        unbinder = ButterKnife.bind(this, view)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)

        val dividerItemDecoration = DividerItemDecoration(context, OrientationHelper.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_vertical))

        recyclerTickers.addItemDecoration(dividerItemDecoration)
        recyclerTickers.adapter = pricesAdapter
        recyclerTickers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        layoutSwipeRefresh.setDistanceToTriggerSync(ViewUtils.dpToPixels(context, 120f).toInt())
        layoutSwipeRefresh.setOnRefreshListener { cryptoCompare.refreshPrices(SYMBOLS, CURRENCIES) }

        cryptoCompare.initialize()

        return view
    }

    override fun onResume() {
        super.onResume()

        val dataHolder = cryptoCompare.dataHolder

        Observable.merge(dataHolder.content, relayContent)
                .observeOn(Schedulers.computation())
                .flatMapSingle {
                    val oldPrices = pricesAdapter.prices

                    Observable.fromIterable(it.entries)
                            .map {
                                val key = it.key
                                val value = it.value["USD"] ?: BigDecimal.ZERO
                                val oldPriceViewModel = oldPrices.find { it.displayName == key }
                                val oldPrice = oldPriceViewModel?.displayValue ?: value
                                val difference = value - oldPrice
                                val changeFromPrevious = when {
                                    difference.compareTo(BigDecimal.ZERO) == 0 -> oldPriceViewModel?.changeFromPrevious ?: difference
                                    else -> difference
                                }

                                PriceViewModel(key, value ?: BigDecimal.ZERO, changeFromPrevious) }
                            .toList()
                }
                .map { Pair<List<PriceViewModel>, DiffUtil.DiffResult>(it, diffPrices(pricesAdapter.prices, it)) }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribe({ pricesAdapter.setData(it.first, it.second) }, { it.printStackTrace() })

        dataHolder.state
                .map { it == RequestState.LOADING }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribe({ layoutSwipeRefresh.isRefreshing = it })

        cryptoCompare.refreshPrices(SYMBOLS, CURRENCIES)
    }

    private fun diffPrices(oldPrices:List<PriceViewModel>, newPrices: List<PriceViewModel>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize(): Int = oldPrices.size

            override fun getNewListSize(): Int = newPrices.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldPrices[oldItemPosition].displayName == newPrices[newItemPosition].displayName
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldPrices[oldItemPosition] == newPrices[newItemPosition]
            }
        })
    }
}
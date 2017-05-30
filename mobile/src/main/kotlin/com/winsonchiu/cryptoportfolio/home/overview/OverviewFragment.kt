package com.winsonchiu.cryptoportfolio.home.overview

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.jakewharton.rxrelay2.PublishRelay
import com.winsonchiu.crypto.api.coinmarketcap.CoinMarketCap
import com.winsonchiu.crypto.api.coinmarketcap.CoinMarketCapDataStoreMemory
import com.winsonchiu.crypto.api.coinmarketcap.data.Currency
import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker
import com.winsonchiu.crypto.api.shared.RequestState
import com.winsonchiu.cryptoportfolio.R
import com.winsonchiu.cryptoportfolio.framework.android.BaseFragment
import com.winsonchiu.cryptoportfolio.framework.android.ViewUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.MathContext

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

class OverviewFragment : BaseFragment() {

    @BindView(R.id.layout_swipe_refresh) lateinit var layoutSwipeRefresh: SwipeRefreshLayout
    @BindView(R.id.recycler_tickers) lateinit var recyclerTickers: RecyclerView

    private val currencyMenuMap = LinkedHashMap<Int, Currency>()

    private val coinMarketCap = CoinMarketCap(CoinMarketCapDataStoreMemory(null))
    private val tickersAdapter = TickersAdapter()

    private var tickersSort: TickersSort = TickersSort.MARKET_CAP
    private var sortAscending = false

    private val relayContent: PublishRelay<List<Ticker>> = PublishRelay.create()

    companion object {
        fun newInstance(): OverviewFragment {
            return OverviewFragment()
        }
    }

    init {
        Currency.values().forEach {
            currencyMenuMap.put(View.generateViewId(), it)
        }

        coinMarketCap.limit = 100
    }

    override fun getTitleResource(): Int = R.string.name_overview

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.overview_fragment, container, false)
        unbinder = ButterKnife.bind(this, view)

        val dividerItemDecoration = DividerItemDecoration(context, OrientationHelper.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_vertical))

        recyclerTickers.addItemDecoration(dividerItemDecoration)
        recyclerTickers.adapter = tickersAdapter
        recyclerTickers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        layoutSwipeRefresh.setDistanceToTriggerSync(ViewUtils.dpToPixels(context, 120f).toInt())
        layoutSwipeRefresh.setOnRefreshListener { coinMarketCap.refreshTickers() }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home, menu)
        val subMenuCurrency = menu.findItem(R.id.menu_item_currency).subMenu
        currencyMenuMap.forEach { subMenuCurrency.add(Menu.NONE, it.key, Menu.NONE, it.value.displayNameIso4217) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuItemId = item.itemId
        val currency = currencyMenuMap[menuItemId]

        if (currency != null) {
            coinMarketCap.currency = currency
            return true
        } else {
            val sort = TickersSort.values().firstOrNull { it.menuItemId == menuItemId }

            if (sort != null) {
                sortAscending = if (tickersSort == sort) !sortAscending else sort.defaultSortAscending
                this.tickersSort = sort

                coinMarketCap.dataHolder.content
                        .firstElement()
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(lifecycleProvider.bindToLifecycle())
                        .subscribe(relayContent)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClickTitle() {
        recyclerTickers.smoothScrollToPosition(0)
    }

    @Suppress("UNCHECKED_CAST")
    private fun sortTickers(tickers: List<Ticker>): List<Ticker> {
        val newTickers = ArrayList<Ticker>(tickers)
        newTickers.sortWith(Comparator<Ticker> {
            first, second ->
            val firstObject = tickersSort.getSortComparable(first) as Comparable<Any>?
            val secondObject = tickersSort.getSortComparable(second) as Comparable<Any>?

            if (firstObject == null && secondObject == null) {
                0
            } else if (firstObject == null) {
                1
            } else if (secondObject == null) {
                -1
            } else if (sortAscending) {
                firstObject.compareTo(secondObject)
            } else {
                secondObject.compareTo(firstObject)
            }
        })

        return newTickers
    }

    private fun diffTickers(oldTickers:List<TickerViewModel>, newTickers: List<TickerViewModel>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize(): Int = oldTickers.size

            override fun getNewListSize(): Int = newTickers.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldTickers[oldItemPosition].ticker.id == newTickers[newItemPosition].ticker.id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldTickers[oldItemPosition] == newTickers[newItemPosition]
            }
        })
    }

    fun Ticker.toViewModel(): TickerViewModel {
        val displayValue = when (tickersSort) {
            TickersSort.PRICE -> price
            TickersSort.MARKET_CAP -> price // TODO
            TickersSort.DAY_VOLUME -> dayVolume
            TickersSort.CHANGE_HOUR -> percentChangeHour
            TickersSort.CHANGE_DAY -> percentChangeDay
            TickersSort.CHANGE_WEEK -> percentChangeWeek
            else -> price
        }.round(MathContext(5))

        val percentChange = when (tickersSort) {
            TickersSort.CHANGE_HOUR -> percentChangeHour
            TickersSort.CHANGE_DAY -> percentChangeDay
            TickersSort.CHANGE_WEEK -> percentChangeWeek
            else -> percentChangeDay
        }

        val percentage = BigDecimal(100L) - percentChange
        val originalValue = price * BigDecimal(100L) / percentage
        val amountChange = (originalValue - price).round(MathContext(5))

        return TickerViewModel(symbol, displayValue, amountChange, percentChange, this)
    }

    override fun onResume() {
        super.onResume()

        val dataHolder = coinMarketCap.dataHolder

        Observable.merge(dataHolder.content, relayContent)
                .observeOn(Schedulers.computation())
                .map { sortTickers(it).map { it.toViewModel() } }
                .map { Pair<List<TickerViewModel>, DiffUtil.DiffResult>(it, diffTickers(tickersAdapter.tickers, it)) }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribe({ tickersAdapter.setData(it.first, it.second, coinMarketCap.currency) }, { it.printStackTrace() })

        dataHolder.state
                .map { it == RequestState.LOADING }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle())
                .subscribe({ layoutSwipeRefresh.isRefreshing = it })

        coinMarketCap.refreshTickers()
    }

}
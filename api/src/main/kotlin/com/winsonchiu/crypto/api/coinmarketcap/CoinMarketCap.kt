package com.winsonchiu.crypto.api.coinmarketcap

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.winsonchiu.crypto.api.coinmarketcap.data.Currency
import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker
import com.winsonchiu.crypto.api.shared.DataHolder
import com.winsonchiu.crypto.api.shared.RequestState
import com.winsonchiu.crypto.api.shared.rateLimit
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

class CoinMarketCap(val dataStore: CoinMarketCapDataStore) {

    var limit: Int? = null
        set (limit) {
            if (this.limit != limit) {
                field = limit
                refreshTickers()
            }
        }

    var currency: Currency = Currency.USD
        set (currency) {
            if (this.currency != currency) {
                field = currency
                refreshTickers()
            }
        }

    val dataHolder: DataHolder<List<Ticker>, RequestState, CoinMarketCapError>

    private val api = CoinMarketCapApi()

    private val relayContent = BehaviorRelay.create<List<Ticker>>()
    private val relayState = BehaviorRelay.createDefault<RequestState>(RequestState.NONE)
    private val relayErrors = PublishRelay.create<CoinMarketCapError>()
    private val requestTickers = BehaviorRelay.create<String>()

    init {
        val content: Observable<List<Ticker>> = relayContent
                .observeOn(Schedulers.io())
                .doOnSubscribe { reloadIfNone() }
                .observeOn(Schedulers.trampoline())

        dataHolder = DataHolder(content, relayState, relayErrors)

        requestTickers
                .observeOn(Schedulers.io())
                .doOnNext { _ -> relayState.accept(RequestState.LOADING) }
                .rateLimit(10, TimeUnit.SECONDS)
                .switchMap({
                    api.ticker(limit, currency)
                            .map {
                                dataStore.storeTickers(it)
                                dataStore.readTickers()
                            }
                            .doOnSuccess(relayContent)
                            .doOnError { _ -> relayErrors.accept(CoinMarketCapError.UNKNOWN) }
                            .doFinally { relayState.accept(RequestState.COMPLETE) }
                            .doOnError { it.printStackTrace() }
                            .toObservable()
                            .onErrorResumeNext(Observable.empty())
                })
                .subscribe()
    }

    private fun reloadIfNone() {
        if (relayContent.value?.isEmpty() != false && relayState.value != RequestState.LOADING) {
            val tickers = dataStore.readTickers()
            if (tickers.isEmpty()) {
                refreshTickers()
            } else {
                relayContent.accept(tickers)
            }
        }
    }

    fun refreshTickers() {
        requestTickers.accept("")
    }

    fun refreshTicker(id: String) {
        requestTickers.accept(id)
    }
}


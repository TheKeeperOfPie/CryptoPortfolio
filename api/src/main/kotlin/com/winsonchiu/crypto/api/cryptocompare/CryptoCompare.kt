package com.winsonchiu.crypto.api.cryptocompare

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.winsonchiu.crypto.api.coinmarketcap.CoinMarketCapError
import com.winsonchiu.crypto.api.shared.DataHolder
import com.winsonchiu.crypto.api.shared.RequestState
import com.winsonchiu.crypto.api.shared.rateLimit
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

class CryptoCompare(private val dataStore: CryptoCompareDataStore = CryptoCompareDataStoreMemory()) {

    val dataHolder: DataHolder<Map<String, Map<String, BigDecimal>>, RequestState, CoinMarketCapError>

    private val apiPrice = CryptoCompareApiPrice()

    private val relayContent = BehaviorRelay.create<Map<String, Map<String, BigDecimal>>>()
    private val relayState = BehaviorRelay.createDefault<RequestState>(RequestState.NONE)
    private val relayErrors = PublishRelay.create<CoinMarketCapError>()

    private val requestPrices = BehaviorRelay.create<Pair<List<String>, List<String>>>()

    init {
        dataHolder = DataHolder(relayContent, relayState, relayErrors)

        requestPrices
                .observeOn(Schedulers.io())
                .doOnNext { _ -> relayState.accept(RequestState.LOADING) }
                .rateLimit(3, TimeUnit.SECONDS)
                .switchMap({
                    apiPrice.prices(it.first, it.second)
                            .map {
                                dataStore.storePrices(it)
                                dataStore.readPrices()
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

    fun initialize() {
        if (!relayContent.hasValue()) {
            relayContent.accept(dataStore.readPrices())
        }
    }

    fun refreshPrices(symbols: List<String>, currencies: List<String>) {
        requestPrices.accept(Pair(symbols, currencies))
    }
}
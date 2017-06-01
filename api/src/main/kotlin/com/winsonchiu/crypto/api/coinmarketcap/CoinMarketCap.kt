package com.winsonchiu.crypto.api.coinmarketcap

import com.winsonchiu.crypto.api.coinmarketcap.data.Currency
import com.winsonchiu.crypto.api.coinmarketcap.data.GlobalData
import com.winsonchiu.crypto.api.coinmarketcap.data.Ticker
import com.winsonchiu.crypto.api.shared.Request
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

class CoinMarketCap(val dataStore: CoinMarketCapDataStore = CoinMarketCapDataStoreMemory()) {

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

    private val api = CoinMarketCapApi()

    val tickersRequest: Request<String, List<Ticker>, CoinMarketCapError>

    val globalDataRequest: Request<Unit, GlobalData, CoinMarketCapError>

    init {
        tickersRequest = Request(Callable {
            Maybe.fromCallable({ dataStore.readTickers() })
                    .subscribeOn(Schedulers.io())
                    .filter { !it.isEmpty() }
        }, Callable {
            ""
        }, Function<String, Single<List<Ticker>>> {
            api.ticker(limit, currency)
                    .map {
                        dataStore.storeTickers(it)
                        dataStore.readTickers()
                    }
        }, Function<Throwable, CoinMarketCapError> {
            CoinMarketCapError.UNKNOWN
        })

        globalDataRequest = Request(Callable {
            Maybe.fromCallable<GlobalData> {
                dataStore.readGlobalData() ?: throw IllegalStateException()
            }
                    .onErrorResumeNext(Maybe.empty<GlobalData>())
                    .subscribeOn(Schedulers.io())
        }, Callable<Unit> { }, Function<Unit, Single<GlobalData>> {
            api.globalData(currency)
                    .map {
                        dataStore.storeGlobalData(it)
                        dataStore.readGlobalData()
                    }
        }, Function<Throwable, CoinMarketCapError> {
            CoinMarketCapError.UNKNOWN
        })
    }

    fun refreshTickers() {
        tickersRequest.refresh("")
    }

    fun refreshGlobalData() {
        globalDataRequest.refresh(Unit)
    }
}


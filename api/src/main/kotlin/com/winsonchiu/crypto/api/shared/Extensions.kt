package com.winsonchiu.crypto.api.shared

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

fun <T> Observable<T>.rateLimit(window: Long, unit: TimeUnit): Observable<T> {
    val relay = PublishRelay.create<T>()
    val disposable = CompositeDisposable()
    var lastRequestTime = 0L

    return compose({
        observable ->
        observable.flatMap {
            val result: Observable<T>

            if (lastRequestTime < System.currentTimeMillis() - unit.toMillis(window)) {
                result = Observable.just(it)
            } else {
                result = relay
                disposable.clear()
                disposable.add(Single.just(it).delay(lastRequestTime + unit.toMillis(window) - System.currentTimeMillis(), TimeUnit.MILLISECONDS).subscribe(relay))
            }

            lastRequestTime = System.currentTimeMillis()
            result
        }
    })
}
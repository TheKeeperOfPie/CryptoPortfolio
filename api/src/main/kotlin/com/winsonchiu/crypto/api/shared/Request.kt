package com.winsonchiu.crypto.api.shared

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * Created by TheKeeperOfPie on 6/1/2017.
 */

class Request<Input, Content, Error>(
        val initialContent: Callable<Maybe<Content>>,
        val initialRequest: Callable<Input>,
        val requestFunction: Function<Input, Single<Content>>,
        val errorFunction: Function<Throwable, Error>,
        val rateLimitTime: Long = 12,
        val rateLimitTimeUnit: TimeUnit = TimeUnit.SECONDS
) {

    private val relayContent = BehaviorRelay.create<Content>()
    private val relayState = BehaviorRelay.createDefault<RequestState>(RequestState.NONE)
    private val relayErrors = PublishRelay.create<Error>()
    private val relayRequest = BehaviorRelay.create<Input>()

    val dataHolder: DataHolder<Content, RequestState, Error>

    init {
        val content: Observable<Content> = relayContent
                .observeOn(Schedulers.io())
                .doOnSubscribe { reloadIfNone() }
                .observeOn(Schedulers.trampoline())

        dataHolder = DataHolder(content, relayState, relayErrors)

        relayRequest
                .observeOn(Schedulers.io())
                .doOnNext { _ -> relayState.accept(RequestState.LOADING) }
                .rateLimit(rateLimitTime, rateLimitTimeUnit)
                .switchMap({
                    requestFunction.apply(it)
                            .doOnSuccess(relayContent)
                            .doOnError { relayErrors.accept(errorFunction.apply(it)) }
                            .doFinally { relayState.accept(RequestState.COMPLETE) }
                            .doOnError { it.printStackTrace() }
                            .toObservable()
                            .onErrorResumeNext(Observable.empty())
                })
                .subscribe()
    }

    private fun reloadIfNone() {
        if (!relayContent.hasValue() && relayState.value != RequestState.LOADING) {
            initialContent.call()
                    .takeUntil(relayRequest.firstElement())
                    .doOnSuccess(relayContent)
                    .isEmpty
                    .filter { it }
                    .map { initialRequest.call() }
                    .subscribe(relayRequest)
        }
    }

    fun refresh(input: Input) {
        relayRequest.accept(input)
    }
}
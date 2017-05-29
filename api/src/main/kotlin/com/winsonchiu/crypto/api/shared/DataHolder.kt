package com.winsonchiu.crypto.api.shared

import io.reactivex.Observable

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

class DataHolder<Content, State, Error>(
        val content: Observable<Content>,
        val state: Observable<State>,
        val errors: Observable<Error>
)

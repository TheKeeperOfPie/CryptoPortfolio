package com.winsonchiu.cryptoportfolio.framework.dagger

import com.winsonchiu.cryptoportfolio.MainActivity

import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

@Subcomponent
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}

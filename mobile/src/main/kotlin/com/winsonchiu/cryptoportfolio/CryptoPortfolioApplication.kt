package com.winsonchiu.cryptoportfolio

import com.winsonchiu.cryptoportfolio.framework.dagger.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

class CryptoPortfolioApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<CryptoPortfolioApplication>? {
        return DaggerApplicationComponent.create()
    }
}

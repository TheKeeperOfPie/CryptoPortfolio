package com.winsonchiu.cryptoportfolio.framework.dagger

import com.winsonchiu.cryptoportfolio.CryptoPortfolioApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

@Component(
        modules = arrayOf(AndroidSupportInjectionModule::class)
)
interface ApplicationComponent : AndroidInjector<CryptoPortfolioApplication> {

    override fun inject(application: CryptoPortfolioApplication)
}

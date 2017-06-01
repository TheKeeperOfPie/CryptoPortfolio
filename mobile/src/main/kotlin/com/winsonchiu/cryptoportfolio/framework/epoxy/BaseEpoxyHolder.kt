package com.winsonchiu.cryptoportfolio.framework.epoxy

import android.support.annotation.CallSuper
import android.view.View
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder

/**
 * Created by TheKeeperOfPie on 6/1/2017.
 */

open class BaseEpoxyHolder : EpoxyHolder() {

    internal lateinit var itemView: View

    @CallSuper
    override fun bindView(view: View) {
        this.itemView = view;
        ButterKnife.bind(this, view)
    }
}
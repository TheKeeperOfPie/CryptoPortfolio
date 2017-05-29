package com.winsonchiu.cryptoportfolio.framework.android

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.view.View
import butterknife.Unbinder

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

open class BaseFragment : LifecycleFragment() {

    internal var unbinder: Unbinder? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (unbinder == null) {
            throw IllegalStateException("ButterKnife Unbinder not assigned")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        unbinder!!.unbind()
    }
}
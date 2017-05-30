package com.winsonchiu.cryptoportfolio.framework.android

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.view.View
import butterknife.Unbinder
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle2.LifecycleProvider
import com.winsonchiu.cryptoportfolio.R

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

open class BaseFragment : LifecycleFragment() {

    internal val lifecycleProvider: LifecycleProvider<Lifecycle.Event> = AndroidLifecycle.createLifecycleProvider(this)
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

    open fun getTitleResource(): Int = R.string.app_name

    open fun onClickTitle() {}
}
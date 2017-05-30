package com.winsonchiu.cryptoportfolio.home

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.winsonchiu.cryptoportfolio.framework.android.BaseFragment
import com.winsonchiu.cryptoportfolio.home.overview.OverviewFragment
import com.winsonchiu.cryptoportfolio.home.portfolio.PortfolioFragment

/**
 * Created by TheKeeperOfPie on 5/30/2017.
 */

class HomePagerAdapter(val context: Context, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = ArrayList<BaseFragment>()

    init {
        fragments.add(PortfolioFragment.newInstance())
        fragments.add(OverviewFragment.newInstance())
    }

    override fun getItem(index: Int): BaseFragment = fragments[index]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(fragments[position].getTitleResource())
    }
}
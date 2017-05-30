package com.winsonchiu.cryptoportfolio.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.winsonchiu.cryptoportfolio.R
import com.winsonchiu.cryptoportfolio.framework.android.BaseFragment

/**
 * Created by TheKeeperOfPie on 5/28/2017.
 */

class HomeFragment : BaseFragment() {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.layout_tabs) lateinit var layoutTabs: TabLayout
    @BindView(R.id.pager) lateinit var pager: ViewPager

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        unbinder = ButterKnife.bind(this, view)

        toolbar.inflateMenu(R.menu.empty)

        val menuInflater = MenuInflater(context)
        val adapter = HomePagerAdapter(context, fragmentManager)
        val onPageChangeListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                val menu = toolbar.menu
                val fragment = adapter.getItem(position)

                menu.clear()
                fragment.onCreateOptionsMenu(menu, menuInflater)

                (0..menu.size() - 1)
                        .map { menu.getItem(it) }
                        .forEach { it.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS) }

                toolbar.requestLayout()
                toolbar.setOnMenuItemClickListener { fragment.onOptionsItemSelected(it) }
                toolbar.setOnClickListener { fragment.onClickTitle() }
            }
        }
        pager.adapter = adapter
        pager.addOnPageChangeListener(onPageChangeListener)

        layoutTabs.setupWithViewPager(pager)

        onPageChangeListener.onPageSelected(pager.currentItem)

        return view
    }
}

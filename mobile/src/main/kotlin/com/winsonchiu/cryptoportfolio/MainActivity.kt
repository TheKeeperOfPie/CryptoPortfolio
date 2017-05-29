package com.winsonchiu.cryptoportfolio

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.winsonchiu.cryptoportfolio.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private val TAG_HOME_FRAGMENT = HomeFragment::class.java.canonicalName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentByTag(TAG_HOME_FRAGMENT) == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment.newInstance())
                    .commitNow()
        }
    }
}

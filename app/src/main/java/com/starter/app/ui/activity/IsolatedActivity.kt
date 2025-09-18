package com.starter.app.ui.activity

import android.os.Bundle
import android.view.View
import com.starter.app.R
import com.starter.app.databinding.IsolatedAcitivtyFullBinding
import com.starter.app.ui.base.BaseActivity
import com.starter.app.ui.base.BaseFragment
import com.starter.app.ui.manager.ActivityStarter
import com.starter.app.utils.serializable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IsolatedActivity : BaseActivity() {

    private lateinit var isolatedFullActivityBinding: IsolatedAcitivtyFullBinding

    override fun findFragmentPlaceHolder(): Int {
        return R.id.placeHolder
    }

    override fun createViewBinding(): View {
        isolatedFullActivityBinding = IsolatedAcitivtyFullBinding.inflate(layoutInflater)
        return isolatedFullActivityBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val page : Class<BaseFragment<*>> = intent.serializable(ActivityStarter.ACTIVITY_FIRST_PAGE)
            load(page)
                .setBundle(intent.extras!!)
                .replace(false)
        }
    }
}
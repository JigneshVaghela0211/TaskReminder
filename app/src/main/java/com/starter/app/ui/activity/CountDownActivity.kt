package com.starter.app.ui.activity

import android.view.View
import com.starter.app.databinding.CountDownListAcctivityBinding
import com.starter.app.ui.base.BaseActivity

class CountDownActivity: BaseActivity() {
    private lateinit var binding: CountDownListAcctivityBinding
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = CountDownListAcctivityBinding.inflate(layoutInflater)
        return binding.root
    }
}
package com.starter.app.ui.activity

import android.os.Bundle
import android.view.View
import com.starter.app.databinding.CountDownListAcctivityBinding
import com.starter.app.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountDownListActivity: BaseActivity() {
    private lateinit var binding: CountDownListAcctivityBinding
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = CountDownListAcctivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.buttonAdd.setOnClickListener {
            loadActivity(CreateTaskActivity::class.java).start()
        }
    }
}
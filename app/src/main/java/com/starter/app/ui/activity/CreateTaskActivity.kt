package com.starter.app.ui.activity

import android.view.View
import com.starter.app.databinding.CreateCountActivityBinding
import com.starter.app.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTaskActivity : BaseActivity() {
    lateinit var binding: CreateCountActivityBinding
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = CreateCountActivityBinding.inflate(layoutInflater)
        return binding.root
    }
}
package com.starter.app.ui.activity

import android.view.View
import com.starter.app.databinding.ActivityCreateCountBinding
import com.starter.app.ui.base.BaseActivity

class CreateTaskActivity : BaseActivity() {

    lateinit var binding: ActivityCreateCountBinding
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = ActivityCreateCountBinding.inflate(layoutInflater)
        return binding.root
    }
}
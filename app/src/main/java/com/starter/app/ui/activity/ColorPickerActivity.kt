package com.starter.app.ui.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.starter.app.databinding.ColorPickerActivityBinding
import com.starter.app.ui.adapter.RainbowPickerAdapter
import com.starter.app.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColorPickerActivity: BaseActivity() {
lateinit var binding: ColorPickerActivityBinding
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = ColorPickerActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpRecyclerview()
    }

    private fun setUpRecyclerview() {
        binding.recyclerViewColor.layoutManager = GridLayoutManager(this, 6)
        binding.recyclerViewColor.adapter = RainbowPickerAdapter(90f,85F,255){

        }
    }
}
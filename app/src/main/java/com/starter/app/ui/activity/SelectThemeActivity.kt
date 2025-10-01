package com.starter.app.ui.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.starter.app.databinding.SelectThemeActivityBinding
import com.starter.app.ui.adapter.ThemeAdapter
import com.starter.app.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectThemeActivity : BaseActivity() {
    lateinit var binding: SelectThemeActivityBinding
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = SelectThemeActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewTheme.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewTheme.adapter = ThemeAdapter(arrayListOf())
    }
}
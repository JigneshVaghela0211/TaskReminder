package com.starter.app.ui.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.starter.app.databinding.SelectThemeActivityBinding
import com.starter.app.ui.adapter.ThemeAdapter
import com.starter.app.ui.base.BaseActivity
import com.starter.app.utils.Theme
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
        binding.recyclerViewTheme.adapter = ThemeAdapter(arrayListOf(
            Theme.BG_WHITE_SKY_BLUE,
            Theme.BG_WHITE_RED,
            Theme.BG_WHITE_YELLOW,
            Theme.BG_WHITE_GREEN,
            Theme.BG_WHITE_BLUE,
            Theme.BG_WHITE_BLACK,
            Theme.BG_BLACK_SKY_BLUE,
            Theme.BG_BLACK_RED,
            Theme.BG_BLACK_YELLOW,
            Theme.BG_BLACK_GREEN,
            Theme.BG_BLACK_BLUE,
            Theme.BG_BLACK_BLACK,
            Theme.BG_COLOR_SKY_BLUE,
            Theme.BG_COLOR_RED,
            Theme.BG_COLOR_YELLOW,
            Theme.BG_COLOR_GREEN,
            Theme.BG_COLOR_BLUE,
            Theme.BG_COLOR_BLACK,
            Theme.BG_COLOR_NO_PRO_SKY_BLUE,
            Theme.BG_COLOR_NO_PRO_RED,
            Theme.BG_COLOR_NO_PRO_YELLOW,
            Theme.BG_COLOR_NO_PRO_GREEN,
            Theme.BG_COLOR_NO_PRO_BLUE,
            Theme.BG_COLOR_NO_PRO_BLACK
        ))
    }
}
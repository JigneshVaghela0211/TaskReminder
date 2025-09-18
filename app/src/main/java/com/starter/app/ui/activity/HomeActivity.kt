package com.starter.app.ui.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.starter.app.R
import com.starter.app.databinding.HomeActivityBinding
import com.starter.app.ui.base.BaseActivity
import com.starter.app.ui.home.fragment.MainFragment
import com.starter.app.ui.home.fragment.OrderFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: HomeActivityBinding

    override fun createViewBinding(): View {
        binding = HomeActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun findFragmentPlaceHolder(): Int = R.id.placeHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        load(MainFragment::class.java).replace(false)
        binding.textViewOrder.setOnClickListener(this)
        binding.textViewHome.setOnClickListener(this)
        // binding.dl.openDrawer(Gravity.START)
        // binding.navigationView.setCheckedItem(R.id.nav_home)
        /*  binding.navigationView.setNavigationItemSelectedListener {
              it.isChecked = true
              binding.dl.closeDrawer(Gravity.START)
              true
          }*/
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.textViewHome -> load(MainFragment::class.java).replace(false)
            R.id.textViewOrder -> load(OrderFragment::class.java).replace(true)
        }
        binding.dl.closeDrawer(Gravity.START)
    }


}

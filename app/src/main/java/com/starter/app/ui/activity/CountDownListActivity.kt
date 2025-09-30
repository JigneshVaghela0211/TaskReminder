package com.starter.app.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.starter.app.R
import com.starter.app.databinding.CountDownListActivityBinding
import com.starter.app.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountDownListActivity: BaseActivity() {
    private lateinit var binding: CountDownListActivityBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var isGridLayout = false
    private var showExpired = false
    private var showWidget = false
    
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = CountDownListActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        setupNavigationDrawer()
        
        binding.buttonAdd.setOnClickListener {
            loadActivity(CreateTaskActivity::class.java).start()
        }
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        
        // Setup drawer toggle button
        findViewById<View>(R.id.buttonDrawerToggle).setOnClickListener {
            drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
        }
        
        // Setup layout toggle button
        findViewById<View>(R.id.buttonLayoutToggle).setOnClickListener {
            toggleLayout()
        }
        
        // Setup menu button
        findViewById<View>(R.id.buttonMenu).setOnClickListener { view ->
            showPopupMenu(view)
        }
    }
    
    private fun setupRecyclerView() {
        // Set initial layout manager
        val layoutManager = if (isGridLayout) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }
        binding.recyclerView.layoutManager = layoutManager
    }
    
    private fun toggleLayout() {
        isGridLayout = !isGridLayout
        
        // Update icon
        val layoutToggleButton = findViewById<View>(R.id.buttonLayoutToggle)
        layoutToggleButton.setBackgroundResource(
            if (isGridLayout) R.drawable.ic_grid_layout else R.drawable.ic_list_layout
        )
        
        // Update RecyclerView layout manager
        val layoutManager = if (isGridLayout) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }
        binding.recyclerView.layoutManager = layoutManager
        
        // Notify adapter about layout change if needed
        // binding.recyclerView.adapter?.notifyDataSetChanged()
    }
    
    private fun setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_auto_backups -> {
                    showMessage("Auto Backups clicked")
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_add_countdown -> {
                    loadActivity(CreateTaskActivity::class.java).start()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_invite_friends -> {
                    showMessage("Invite Friends clicked")
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_faq -> {
                    showMessage("FAQ clicked")
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_settings -> {
                    showMessage("Settings clicked")
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_terms_of_service -> {
                    showMessage("Terms of Service clicked")
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_privacy_policy -> {
                    showMessage("Privacy Policy clicked")
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }
    
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.countdown_list_menu, popupMenu.menu)
        
        // Set current checkbox states
        popupMenu.menu.findItem(R.id.menu_show_expired)?.isChecked = showExpired
        popupMenu.menu.findItem(R.id.menu_show_widget)?.isChecked = showWidget
        
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.menu_show_expired -> {
                    showExpired = !showExpired
                    menuItem.isChecked = showExpired
                    // Handle show expired logic here
                    showMessage("Show expired: $showExpired")
                    true
                }
                R.id.menu_show_widget -> {
                    showWidget = !showWidget
                    menuItem.isChecked = showWidget
                    // Handle show widget logic here
                    showMessage("Show widget: $showWidget")
                    true
                }
                R.id.menu_sort -> {
                    // Dismiss current popup and show sort submenu
                    popupMenu.dismiss()
                    showSortSubmenu(view)
                    true
                }
                else -> false
            }
        }
        
        popupMenu.show()
    }
    
    private fun showSortSubmenu(view: View) {
        val sortPopup = PopupMenu(this, view)
        sortPopup.menuInflater.inflate(R.menu.sort_submenu, sortPopup.menu)
        
        sortPopup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.sort_title_asc -> {
                    showMessage("Sorted by Title (A - Z)")
                    // TODO: Implement title ascending sort logic
                    true
                }
                R.id.sort_title_desc -> {
                    showMessage("Sorted by Title (Z - A)")
                    // TODO: Implement title descending sort logic
                    true
                }
                R.id.sort_date_asc -> {
                    showMessage("Sorted by Date (ascending)")
                    // TODO: Implement date ascending sort logic
                    true
                }
                R.id.sort_date_desc -> {
                    showMessage("Sorted by Date (descending)")
                    // TODO: Implement date descending sort logic
                    true
                }
                else -> false
            }
        }
        
        sortPopup.show()
    }
}
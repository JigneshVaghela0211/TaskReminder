package com.starter.app.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.starter.app.databinding.ActivityCountdownListBinding
import com.starter.app.ui.adapter.CountdownEventAdapter
import com.starter.app.ui.base.BaseActivity
import com.starter.app.ui.viewmodel.CountdownViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountdownListActivity : BaseActivity() {
    
    private lateinit var binding: ActivityCountdownListBinding
    private val viewModel: CountdownViewModel by viewModels()
    private lateinit var adapter: CountdownEventAdapter
    
    override fun findFragmentPlaceHolder(): Int {
        return 0
    }

    override fun createViewBinding(): View {
        binding = ActivityCountdownListBinding.inflate(layoutInflater)
        return binding.root
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }
    
    private fun setupRecyclerView() {
        adapter = CountdownEventAdapter(
            onItemClick = { event ->
                // Handle item click - could open detail/edit activity
            },
            onItemDelete = { event ->
                lifecycleScope.launch {
                    viewModel.deleteEvent(event)
                }
            }
        )
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CountdownListActivity)
            adapter = this@CountdownListActivity.adapter
        }
    }
    
    private fun setupObservers() {
        viewModel.allEvents.observe(this) { events ->
            adapter.submitList(events)
            
            if (events.isEmpty()) {
                binding.emptyView.visibility = android.view.View.VISIBLE
                binding.recyclerView.visibility = android.view.View.GONE
            } else {
                binding.emptyView.visibility = android.view.View.GONE
                binding.recyclerView.visibility = android.view.View.VISIBLE
            }
        }
    }
    
    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            // Open create countdown activity
//            startActivity(android.content.Intent(this, CreateCountActivity::class.java))
            loadActivity(CreateTaskActivity::class.java).start()
        }
        
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}
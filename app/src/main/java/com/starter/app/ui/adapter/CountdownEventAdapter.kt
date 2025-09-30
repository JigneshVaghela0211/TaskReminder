package com.starter.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.starter.app.data.pojo.CountdownEvent
import com.starter.app.databinding.ItemCountdownEventBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CountdownEventAdapter(
    private val onItemClick: (CountdownEvent) -> Unit,
    private val onItemDelete: (CountdownEvent) -> Unit
) : ListAdapter<CountdownEvent, CountdownEventAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCountdownEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(private val binding: ItemCountdownEventBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(event: CountdownEvent) {
            with(binding) {
                titleText.text = event.title
                descriptionText.text = event.description
                
                val timeRemaining = calculateTimeRemaining(event.targetDate)
                countdownText.text = timeRemaining
                
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                dateText.text = dateFormat.format(event.targetDate)
                
                // Set theme color
                val colorRes = when (event.themeColor) {
                    "red" -> android.R.color.holo_red_dark
                    "blue" -> android.R.color.holo_blue_dark
                    "green" -> android.R.color.holo_green_dark
                    "orange" -> android.R.color.holo_orange_dark
                    "purple" -> android.R.color.holo_purple
                    else -> android.R.color.darker_gray
                }
                themeIndicator.setBackgroundResource(colorRes)
                
                root.setOnClickListener { onItemClick(event) }
                deleteButton.setOnClickListener { onItemDelete(event) }
            }
        }
        
        private fun calculateTimeRemaining(targetDate: Date): String {
            val currentTime = System.currentTimeMillis()
            val targetTime = targetDate.time
            val diff = targetTime - currentTime
            
            if (diff <= 0) {
                return "Event Over"
            }
            
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
            
            return when {
                days > 0 -> String.format("%d days, %d hours", days, hours)
                hours > 0 -> String.format("%d hours, %d minutes", hours, minutes)
                else -> String.format("%d minutes", minutes)
            }
        }
    }
    
    class DiffCallback : DiffUtil.ItemCallback<CountdownEvent>() {
        override fun areItemsTheSame(oldItem: CountdownEvent, newItem: CountdownEvent): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: CountdownEvent, newItem: CountdownEvent): Boolean {
            return oldItem == newItem
        }
    }
}
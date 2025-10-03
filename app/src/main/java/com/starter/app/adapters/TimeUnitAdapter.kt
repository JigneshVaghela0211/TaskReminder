package com.starter.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starter.app.databinding.ItemTimeUnitOptionBinding
import com.starter.app.utils.TimeUnitType

class TimeUnitAdapter(
    private val timeUnitTypes: List<TimeUnitType>,
    private val onItemClick: (TimeUnitType) -> Unit
) : RecyclerView.Adapter<TimeUnitAdapter.TimeUnitViewHolder>() {

    inner class TimeUnitViewHolder(private val binding: ItemTimeUnitOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(timeUnitType: TimeUnitType) {
            binding.textViewTimeUnitOption.text = timeUnitType.getDisplayName()
            binding.root.setOnClickListener {
                onItemClick(timeUnitType)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeUnitViewHolder {
        val binding = ItemTimeUnitOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeUnitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeUnitViewHolder, position: Int) {
        holder.bind(timeUnitTypes[position])
    }

    override fun getItemCount(): Int = timeUnitTypes.size
}
package com.starter.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starter.app.databinding.ItemRecurrenceOptionBinding
import com.starter.app.utils.RecurrenceType

class RecurrenceAdapter(
    private val recurrenceTypes: List<RecurrenceType>,
    private val onItemClick: (RecurrenceType) -> Unit
) : RecyclerView.Adapter<RecurrenceAdapter.RecurrenceViewHolder>() {

    inner class RecurrenceViewHolder(private val binding: ItemRecurrenceOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recurrenceType: RecurrenceType) {
            binding.textViewRecurrenceOption.text = recurrenceType.getDisplayName()
            binding.root.setOnClickListener {
                onItemClick(recurrenceType)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecurrenceViewHolder {
        val binding = ItemRecurrenceOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecurrenceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecurrenceViewHolder, position: Int) {
        holder.bind(recurrenceTypes[position])
    }

    override fun getItemCount(): Int = recurrenceTypes.size
}
package com.starter.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starter.app.databinding.ItemFontOptionBinding

class FontAdapter(
    private val fonts: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<FontAdapter.FontViewHolder>() {

    inner class FontViewHolder(private val binding: ItemFontOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fontName: String) {
            binding.textViewFontName.text = fontName
            binding.root.setOnClickListener {
                onItemClick(fontName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        val binding = ItemFontOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FontViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        holder.bind(fonts[position])
    }

    override fun getItemCount(): Int = fonts.size
}
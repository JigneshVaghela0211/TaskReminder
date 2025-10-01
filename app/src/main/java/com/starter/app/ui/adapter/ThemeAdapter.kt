package com.starter.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.starter.app.R
import com.starter.app.databinding.RawThemeTimeBinding

class ThemeAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            RawThemeTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(private val binding: RawThemeTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.pressed.apply {

                setColorArc(ContextCompat.getColor(this.context,R.color.gray))
                setColorBackground(ContextCompat.getColor(this.context,R.color.black))
                setColorCircle(ContextCompat.getColor(this.context,R.color.red))
                percent = 50
                text = "1000 Day"
                setColorFont(ContextCompat.getColor(this.context,R.color.black))
                textSubtitle = "10:00"
                setColorFont(ContextCompat.getColor(this.context,R.color.white))
            }
        }

    }
}
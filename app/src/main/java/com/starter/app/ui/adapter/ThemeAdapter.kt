package com.starter.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.starter.app.databinding.RawThemeTimeBinding
import com.starter.app.utils.Theme

class ThemeAdapter(private val list: ArrayList<Theme>) :
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
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: RawThemeTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(theme: Theme) {
            binding.pressed.apply {

                setColorArc(theme.progressColor.toColorInt())
                setColorBackground(theme.background.toColorInt())
                setColorCircle(theme.borderColor.toColorInt())
                percent = 50
                text = "1000 Day"
                textSubtitle = "10:00"
                setColorFont(theme.textColor.toColorInt())
//                setColorFont(ContextCompat.getColor(this.context,R.color.white))
            }
        }

    }
}
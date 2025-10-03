package com.starter.app.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starter.app.R
import com.starter.app.databinding.RawColorBinding

/**
 * RecyclerView Adapter for displaying a rainbow spectrum of colors in a picker
 * Generates colors across the HSV spectrum plus grayscale values
 */
class RainbowPickerAdapter(
    private var brightness: Float,
    private var saturation: Float,
    private var alpha: Int,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<RainbowPickerAdapter.ColorViewHolder>() {

    private val colorList = mutableListOf<Int>()

    companion object {
        private const val COLOR_STEP = 16
        private const val GRAYSCALE_STEP = 11
    }

    init {
        generateColors()
    }

    /**
     * Generate the rainbow spectrum of colors
     * Creates colors by traversing through RGB space to form a rainbow,
     * then adds grayscale values and transparent option
     */
    private fun generateColors() {
        colorList.clear()
        val hsvValues = FloatArray(3)

        // Red to Yellow (R=255, G increasing, B=0)
        for (green in 0..255 step COLOR_STEP) {
            addColorFromRGB(255, green, 0, hsvValues)
        }

        // Yellow to Green (R decreasing, G=255, B=0)
        for (red in 255 downTo 0 step COLOR_STEP) {
            addColorFromRGB(red, 255, 0, hsvValues)
        }

        // Green to Cyan (R=0, G=255, B increasing)
        for (blue in 0..255 step COLOR_STEP) {
            addColorFromRGB(0, 255, blue, hsvValues)
        }

        // Cyan to Blue (R=0, G decreasing, B=255)
        for (green in 255 downTo 0 step COLOR_STEP) {
            addColorFromRGB(0, green, 255, hsvValues)
        }

        // Blue to Magenta (R increasing, G=0, B=255)
        for (red in 0..255 step COLOR_STEP) {
            addColorFromRGB(red, 0, 255, hsvValues)
        }

        // Magenta to Red (R=255, G=0, B decreasing)
        for (blue in 255 downTo 0 step COLOR_STEP) {
            addColorFromRGB(255, 0, blue, hsvValues)
        }

        // Add grayscale gradient (white to black)
        for (grayValue in 255 downTo 0 step GRAYSCALE_STEP) {
            colorList.add(Color.argb(alpha, grayValue, grayValue, grayValue))
        }

        // Add transparent/no color option at the end
        colorList.add(0)
    }

    /**
     * Convert RGB to HSV, apply saturation and brightness adjustments,
     * then add the resulting color to the list
     */
    private fun addColorFromRGB(red: Int, green: Int, blue: Int, hsvValues: FloatArray) {
        Color.colorToHSV(Color.rgb(red, green, blue), hsvValues)
        hsvValues[1] = saturation  // Apply custom saturation
        hsvValues[2] = brightness   // Apply custom brightness
        colorList.add(Color.HSVToColor(alpha, hsvValues))
    }

    /**
     * Update brightness and regenerate colors
     */
    fun setBrightness(brightness: Float) {
        this.brightness = brightness
        generateColors()
        notifyDataSetChanged()
    }

    /**
     * Update saturation and regenerate colors
     */
    fun setSaturation(saturation: Float) {
        this.saturation = saturation
        generateColors()
        notifyDataSetChanged()
    }

    /**
     * Update alpha and regenerate colors
     */
    fun setAlpha(alpha: Int) {
        this.alpha = alpha
        generateColors()
        notifyDataSetChanged()
    }

    /**
     * Get color at specific position
     */
    fun getColorAt(position: Int): Int {
        return if (position in colorList.indices) {
            colorList[position]
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            RawColorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colorList[position])
    }

    override fun getItemCount(): Int = colorList.size

    /**
     * ViewHolder for color items
     */
    inner class ColorViewHolder(private val binding: RawColorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Assuming your layout has a view with id 'color_view'
        // If the entire item is the color view, use itemView directly

        init {
            // Set transparent background for the list item
            binding.imageViewColor.setBackgroundResource(R.drawable.transparent_bg)

            // Set click listener
            binding.imageViewColor.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onColorSelected(colorList[position])
                }
            }
        }

        fun bind(color: Int) {
            // If color is 0 (transparent), show transparent background pattern
            // Otherwise show the solid color
            if (color == 0) {
                binding.imageViewColor.setBackgroundResource(R.drawable.transparent_bg)
            } else {
                binding.imageViewColor.setBackgroundColor(color)
            }

            // Store color value in the view
            binding.imageViewColor.tag = color
        }
    }
}
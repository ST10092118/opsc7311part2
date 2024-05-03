package com.opsc7311.opsc7311poepart2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R

class ColorAdapter(private val colorList: ArrayList<Int>,
                   private val onColorClick: (Int) -> Unit):
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>()
{
    // This class was adapted from Youtube
    // https://www.youtube.com/watch?v=E3x6pCZutLA&t=140s
    // Smartherd
    // https://www.youtube.com/@smartherd

    private var selectedItemPosition = RecyclerView.NO_POSITION
    private var selectedColor: Int? = null

    private val colorMap = mapOf(
        "Red" to R.color.red,
        "Blue" to R.color.blue,
        "Green" to R.color.green,
        "Yellow" to R.color.yellow,
        "Orange" to R.color.primary,
        "Black" to R.color.black,
        "Grey" to R.color.grey,
        "Purple" to R.color.purple
    )



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.color_item_layout, parent, false)
        return ColorViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val colorArr = ArrayList(colorMap.keys)[position]
        val colorId = colorMap[colorArr]
        val color = if (colorId != null) {
            ContextCompat.getColor(holder.itemView.context, colorId)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.primary) // Set a default color
        }
        holder.colorLayout.setBackgroundColor(color)
    }

    fun getSelectedItem(): Int? {
        return if (selectedItemPosition != RecyclerView.NO_POSITION) {
            colorList[selectedItemPosition]
        } else {
            null
        }
    }

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorLayout: LinearLayout = itemView.findViewById(R.id.color_layout)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectedItemPosition = position
                    onColorClick(colorList[position])
                }
            }
        }
    }
}
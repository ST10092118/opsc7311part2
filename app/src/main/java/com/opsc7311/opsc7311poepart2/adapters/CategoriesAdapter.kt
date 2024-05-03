package com.opsc7311.opsc7311poepart2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.database.model.Category

class CategoriesAdapter:
    RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    // This class was adapted from Youtube
    // https://www.youtube.com/watch?v=E3x6pCZutLA&t=140s
    // Smartherd
    // https://www.youtube.com/@smartherd


    private var categories = mutableListOf<Category>()

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
    fun updateData(data: List<Category>) {
        categories.clear()
        categories.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_item_layout, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categories[position]

        val color = colorMap[currentItem.color]

        if (color != null) {
            val backgroundColor = ContextCompat.getColor(holder.itemView.context, color)
            holder.cardView.setCardBackgroundColor(backgroundColor)
        }


        holder.categoryName.text = currentItem.name
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
        val cardView: CardView = itemView.findViewById(R.id.categories_card_view)


    }
}
package com.opsc7311.opsc7311poepart2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.database.model.Category

class CategoryInputAdapter( private val onItemClick: (Category) -> Unit ) :
    RecyclerView.Adapter<CategoryInputAdapter.CategoryViewHolder>() {
    /// This class was adapted from Youtube
    // https://www.youtube.com/watch?v=E3x6pCZutLA&t=140s
    // Smartherd
    // https://www.youtube.com/@smartherd

    private var categoryList = mutableListOf<Category>()
    private val colorMap = mapOf(
        "Red" to R.color.red,
        "Blue" to R.color.blue,
        "Green" to R.color.green,
        "Yellow" to R.color.yellow,
        "Orange" to R.color.theme_primary,
        "Black" to R.color.theme_on_primary_container,
        "Grey" to R.color.theme_on_primary_container,
        "Purple" to R.color.purple
    )

    private var selectedItemPosition = RecyclerView.NO_POSITION
    private var selectedCategory: Category? = null

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val background: LinearLayout = itemView.findViewById(R.id.cat_container)
        val name: TextView = itemView.findViewById(R.id.category_name)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectedItemPosition = position
                    onItemClick(categoryList[position])
                }
            }
        }
    }

    fun getSelectedItem(): Category? {
        return if (selectedItemPosition != RecyclerView.NO_POSITION) {
            categoryList[selectedItemPosition]
        } else {
            null
        }
    }

    fun setSelectedCategory(category: Category?) {
        val previousSelectedPosition = selectedItemPosition
        selectedCategory = category
        if (previousSelectedPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(previousSelectedPosition)
        }
        selectedCategory?.let { cat ->
            val newSelectedPosition = categoryList.indexOf(cat)
            if (newSelectedPosition != -1) {
                selectedItemPosition = newSelectedPosition
                notifyItemChanged(selectedItemPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_input_item_layout, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        val color = colorMap[category.color]
        color?.let {
            ContextCompat.getColor(holder.itemView.context,
                it
            )
        }?.let { holder.background.setBackgroundColor(it) }
        holder.name.text = category.name
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun updateData(data: List<Category>) {
        categoryList.clear()
        categoryList.addAll(data)
        notifyDataSetChanged()
    }

}
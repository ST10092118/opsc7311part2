package com.opsc7311.opsc7311poepart2.adapters

import android.graphics.drawable.GradientDrawable
import android.util.Log
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
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimesheetEntriesAdapter(private val onItemClick: (Timesheet, Category) -> Unit) : RecyclerView.Adapter<TimesheetEntriesAdapter.ViewHolder>() {
    // This class was adapted from Youtube
    // https://www.youtube.com/watch?v=E3x6pCZutLA&t=140s
    // Smartherd
    // https://www.youtube.com/@smartherd

    private val timesheetEntries = mutableListOf<Pair<Timesheet, Category>>()

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

    fun updateData(data: List<Pair<Timesheet, Category>>) {
        timesheetEntries.clear()
        timesheetEntries.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.timesheet_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return timesheetEntries.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (timesheet, category) = timesheetEntries[position]


        holder.itemView.setOnClickListener {
            onItemClick(timesheet, category)
        }

        val categoryBackgroundColor = colorMap[category.color]

        val originalColor = if (categoryBackgroundColor != null) {
            ContextCompat.getColor(holder.itemView.context, categoryBackgroundColor)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.primary) // Set a default color
        }

        holder.timesheetName.text = timesheet.name
        holder.category.text = category.name
        holder.category.setTextColor(originalColor)

        if(timesheet.image.isNotEmpty()){
            holder.isImage.setImageResource(R.drawable.baseline_add_a_photo_24)
            holder.imageText.text = "Image attachment"
        }else{
            holder.imageText.text = "No image attachment"
            // remove image
        }
        holder.time.text = "${formatTime(timesheet.startTime)} - ${formatTime(timesheet.endTime)}"

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timesheetName: TextView = itemView.findViewById(R.id.entry_name)
        val category: TextView = itemView.findViewById(R.id.category_name)
        val isImage: ImageView = itemView.findViewById(R.id.camera_img)
        val imageText: TextView = itemView.findViewById(R.id.image_text)
        val time: TextView = itemView.findViewById(R.id.time)
    }

    fun formatTime(timestamp: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return timeFormat.format(date)
    }
}
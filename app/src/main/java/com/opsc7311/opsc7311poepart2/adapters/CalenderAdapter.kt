package com.opsc7311.opsc7311poepart2.adapters

import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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

class CalenderAdapter() : RecyclerView.Adapter<CalenderAdapter.TaskViewHolder>() {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calender_item_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val (timesheet, category) = timesheetEntries[position]

        val categoryBackgroundColor = colorMap[category.color]

        val originalColor = if (categoryBackgroundColor != null) {
            ContextCompat.getColor(holder.itemView.context, categoryBackgroundColor)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.primary) // Set a default color
        }

        holder.timesheetName.text = timesheet.name
        holder.category.text = category.name
        holder.timesheetName.setTextColor(originalColor)

        holder.description.text = timesheet.description



        holder.time.text = "${formatTime(timesheet.startTime)} - ${formatTime(timesheet.endTime)}"
        holder.main_time.text = formatTime(timesheet.startTime)

        if (categoryBackgroundColor != null) {

            holder.colorFrame.setBackgroundColor(originalColor)
            val cornerRadius = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.corner_radius)
            val shapeDrawable = GradientDrawable()
            shapeDrawable.setColor(originalColor)
            shapeDrawable.cornerRadius = cornerRadius.toFloat()
            holder.colorFrame.background = shapeDrawable
        }


    }

    override fun getItemCount(): Int {
        return timesheetEntries.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timesheetName: TextView = itemView.findViewById(R.id.entry_name)
        val category: TextView = itemView.findViewById(R.id.category_name)
        val description: TextView = itemView.findViewById(R.id.description_text)
        val time: TextView = itemView.findViewById(R.id.time_text)
        val main_time:TextView = itemView.findViewById(R.id.time)

        val colorFrame: FrameLayout = itemView.findViewById(R.id.color_frame)
    }

    fun formatTime(timestamp: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(timestamp)
        return timeFormat.format(date)
    }
}
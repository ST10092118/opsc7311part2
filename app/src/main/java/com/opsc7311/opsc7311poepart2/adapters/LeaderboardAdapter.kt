package com.opsc7311.opsc7311poepart2.adapters

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.database.model.User
import com.opsc7311.opsc7311poepart2.fragments.GoalFragment
import com.opsc7311.opsc7311poepart2.fragments.LeaderboardFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    // This class was adapted from Youtube
    // https://www.youtube.com/watch?v=E3x6pCZutLA&t=140s
    // Smartherd
    // https://www.youtube.com/@smartherd

    private val users = mutableListOf<Pair<User, Long>>()

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

    fun updateData(data: List<Pair<User, Long>>) {
        users.clear()
        users.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (user, time) = users[position]

        holder.username.text = "${position + 1}. ${user.username}"

        val totalSeconds = time / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        holder.time.text = timeString

        when(position){
            0 -> holder.username.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.yellow))
            1 -> holder.username.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
            2 -> holder.username.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.purple))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val time: TextView = itemView.findViewById(R.id.time)
    }
}
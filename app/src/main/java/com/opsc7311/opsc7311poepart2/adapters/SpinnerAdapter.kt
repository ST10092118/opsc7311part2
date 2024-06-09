package com.opsc7311.opsc7311poepart2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapter (
    context: Context,
    resource: Int,
    private val timePeriods: Array<String>
) : ArrayAdapter<String>(context, resource, timePeriods) {

    // This class adapted from geeksforgeeks
    // https://www.geeksforgeeks.org/android-recyclerview/
    // BaibhavOjha
    // https://auth.geeksforgeeks.org/user/BaibhavOjha/articles?utm_source=geeksforgeeks&utm_medium=article_author&utm_campaign=auth_user


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // This method returns the view for the spinner when it's not expanded
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        // This method returns the view for each item in the dropdown list
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        // This method creates the view for each item in the spinner or dropdown list
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = timePeriods[position]
        return view
    }
}
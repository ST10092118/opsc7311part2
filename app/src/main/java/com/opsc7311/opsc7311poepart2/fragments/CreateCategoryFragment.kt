package com.opsc7311.opsc7311poepart2.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.ColorAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateCategoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var colorAdapter: ColorAdapter

    private lateinit var colorPickerDialog: AlertDialog

    private lateinit var colorList: ArrayList<Int>

    private lateinit var colorContainer: LinearLayout

    private lateinit var color: TextView

    private lateinit var colorFrame: FrameLayout

    private lateinit var categoryName: EditText

    private lateinit var saveButton: Button



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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_category, container, false)

        colorContainer = view.findViewById(R.id.color_container)

        categoryName = view.findViewById<EditText>(R.id.category_name)

        color = view.findViewById<TextView>(R.id.color_textview)

        colorFrame = view.findViewById(R.id.color_frame)

        saveButton = view.findViewById(R.id.save_category_button)

        colorList = arrayListOf<Int>()

        colorList = colorList()


        colorAdapter = ColorAdapter(colorList) { selectedColor ->
            colorPickerDialog.dismiss()
            color.text = colorName(selectedColor)
            colorFrame.setBackgroundColor(ContextCompat.getColor(requireContext(), selectedColor))
            // Dismiss the dialog after selecting an icon
        }

        colorContainer.setOnClickListener {
            setUpColorPicker()
        }

        saveButton.setOnClickListener {
            addCategory()
        }

        return view
    }

    private fun colorName(value: Int): String{
        return colorMap.entries.find { it.value == value }?.key ?: "Unknown color"

    }
    private fun colorList(): ArrayList<Int> {
        return ArrayList(colorMap.values)
    }

    fun setUpColorPicker(){
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.color_dialog_picker, null)

        recyclerView = dialogView.findViewById(R.id.color_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = colorAdapter

        colorPickerDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        colorPickerDialog.show()
    }
    fun addCategory(){
        Toast.makeText(requireContext(), "add category button pressed", Toast.LENGTH_SHORT).show()

    }

}
package com.opsc7311.opsc7311poepart2.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.ColorAdapter
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import com.opsc7311.opsc7311poepart2.viewmodel.CategoryViewModel
import com.opsc7311.opsc7311poepart2.viewmodel.UserViewModel


class CreateCategoryFragment : Fragment() {
    private val categoryViewModel: CategoryViewModel by viewModels()

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
        val name = categoryName.text.toString()
        val color = colorAdapter.getSelectedItem()?.let { colorName(it) }

        if(name.isEmpty()) {
            Toast.makeText(requireContext(), "Enter a category name", Toast.LENGTH_SHORT).show()
            return
        }
        if(color == null) {
            Toast.makeText(requireContext(), "Select a color", Toast.LENGTH_SHORT).show()
            return
        }
        categoryViewModel.status.observe(viewLifecycleOwner){
            // This method was adapted from stackoverflow
            // https://stackoverflow.com/questions/59521691/use-viewlifecycleowner-as-the-lifecycleowner
            // CommonsWare
            // https://stackoverflow.com/users/115145/commonsware

            status ->
            if (status.first == RegistrationStatus.SUCCESS) {
                Toast.makeText(requireContext(), status.second,
                    Toast.LENGTH_SHORT).show();
                redirectToCategories()
            } else {
                Toast.makeText(requireContext(), status.second,
                    Toast.LENGTH_SHORT).show();            }
        }

        //Log.d("Tag", "name: $name color: \n$color")

        categoryViewModel.createNewCategory(name, color)

    }

    private fun redirectToCategories() {
        val categoriesFragment = CategoriesFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, categoriesFragment)
            .addToBackStack(null)
            .commit()
    }

}
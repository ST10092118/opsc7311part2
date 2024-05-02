package com.opsc7311.opsc7311poepart2.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.CategoryInputAdapter
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.model.Timesheet
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import com.opsc7311.opsc7311poepart2.viewmodel.CategoryViewModel
import com.opsc7311.opsc7311poepart2.viewmodel.TimesheetViewModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class TimesheetEntryFragment : Fragment() {
    private val viewModel: TimesheetViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    private var selectedCategoryName:String = "Category"


    private lateinit var timesheetName: TextView
    private lateinit var date: TextView
    private lateinit var startTextView: TextView
    private lateinit var endTextView: TextView
    private lateinit var description: EditText
    private lateinit var categoryTextView: TextView

    private lateinit var datePicker: LinearLayout
    private lateinit var startTimePicker: LinearLayout
    private lateinit var endTimePicker: LinearLayout

    private lateinit var imageView: ImageView

    private lateinit var categoryContainer: LinearLayout
    private lateinit var imageContainer: LinearLayout

    private lateinit var colorFrame: FrameLayout

    private val REQUEST_IMAGE_CAPTURE = 102
    private var selectedImageUri: Uri? = null
    private var selectedImageBitmap: Bitmap? = null

    private lateinit var recyclerView: RecyclerView

    private lateinit var categoryInputAdapter: CategoryInputAdapter

    private lateinit var pickerDialog: AlertDialog

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
        val view = inflater.inflate(R.layout.fragment_timesheet_entry, container, false)

        timesheetName = view.findViewById(R.id.timesheet_name)
        date = view.findViewById(R.id.date)

        datePicker = view.findViewById(R.id.date_picker)
        startTimePicker = view.findViewById(R.id.start_time)
        endTimePicker = view.findViewById(R.id.end_time)

        startTextView = view.findViewById(R.id.start_time_text)
        endTextView = view.findViewById(R.id.end_time_text)

        description = view.findViewById(R.id.description)

        categoryTextView = view.findViewById(R.id.category_textview)

        imageView = view.findViewById(R.id.selected_image)

        categoryContainer = view.findViewById(R.id.category_container)

        imageContainer = view.findViewById(R.id.image_container)
        colorFrame = view.findViewById(R.id.color_frame)

        saveButton = view.findViewById(R.id.save_timesheet_button)

        date.text = setDate()

        startTextView.text = setStartTime()

        endTextView.text = setEndTime()



        categoryInputAdapter = CategoryInputAdapter(){
            selectedCategory ->
            selectedCategoryName = selectedCategory.name
            categoryTextView.text = selectedCategoryName

            colorMap[selectedCategory.color]?.let {
                ContextCompat.getColor(requireContext(),
                    it
                )
            }?.let {
                colorFrame.setBackgroundColor(
                    it
                )
            }

            pickerDialog.dismiss()
        }

        getCategories()

        datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        startTimePicker.setOnClickListener {
            showTimePicker(startTextView)
        }

        endTimePicker.setOnClickListener {
            showTimePicker(endTextView)
        }

        categoryContainer.setOnClickListener {
            pickerDialogPopup()
        }

        imageContainer.setOnClickListener {
            openCamera()
        }

        saveButton.setOnClickListener {
            createTimesheet()
        }

        return view
    }

    private fun showDatePickerDialog() {


        try {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                    // Update the TextView with the selected date
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    date.text = selectedDate
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showTimePicker(setTextView: TextView) {

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        // date picker dialog
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            val mSelectedTime = SimpleDateFormat("HH:mm").format(c.time)
            setTextView.text = mSelectedTime
        }
        val timePickerDialog = TimePickerDialog(requireContext(),
            AlertDialog.THEME_TRADITIONAL, timeSetListener, hour, minute,
            DateFormat.is24HourFormat(requireContext()))

        timePickerDialog.show()
    }


    fun setDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun setStartTime():String {
        // Get current time
        val currentTime = Calendar.getInstance()

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTimeString = timeFormat.format(currentTime.time)

        return currentTimeString
    }

    private fun setEndTime():String {
        // Get current time
        val currentTime = Calendar.getInstance()
        // Add one hour to the current time
        currentTime.add(Calendar.HOUR_OF_DAY, 1)

        // Format the end time into "hour:minute" format
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val endTimeString = timeFormat.format(currentTime.time)

        return endTimeString
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                REQUEST_IMAGE_CAPTURE -> {
                    // Handle image capture with camera
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    imageBitmap?.let {
                        selectedImageBitmap = imageBitmap
                        selectedImageUri = getImageUri(requireContext(), it)
                        updateSelectedImage(selectedImageUri)
                    }
                }
            }
        }
    }




    private fun updateSelectedImage(uri: Uri?) {
        // Update UI to show selected image, e.g., set ImageView's image
        imageView?.setImageURI(uri)
        // You can also update other UI elements like image_name and image_size
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "IMG_${System.currentTimeMillis()}",
            null
        )
        return Uri.parse(path)
    }

    fun pickerDialogPopup() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.category_picker, null)

        recyclerView = dialogView.findViewById(R.id.icon_recycler_view)

        recyclerView.adapter = categoryInputAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        pickerDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        pickerDialog.show()
    }

    private fun getCategories(){
        categoryViewModel._categories.observe(viewLifecycleOwner){
                categories ->
            categoryInputAdapter.updateData(categories)
        }

        categoryViewModel.getCategories()
    }

    private fun createTimesheet(){
        val name = timesheetName.text.toString()
        val selectedDate = date.text.toString()
        val selectedCategory = categoryInputAdapter.getSelectedItem()
        val time_start = startTextView.text.toString()
        val time_end = endTextView.text.toString()
        val timesheet_description = description.text.toString()

        val data = "name: $name, date: $selectedDate, category: $selectedCategory, start time: $time_start, end time: $time_end, description: $timesheet_description"
        Log.d("TaskDetails", data)

        if(name.isEmpty() || selectedCategory == null || timesheet_description.isEmpty()){
            return
        }

        val timesheet = Timesheet(
            id = "", name= name, date = returnDate(selectedDate), startTime = returnLong(time_start),
            endTime = returnLong(time_end), image ="", categoryId = selectedCategory.id, userId = ""
        )

        viewModel.createNewTimesheet(selectedImageUri, timesheet)

        viewModel.status.observe(viewLifecycleOwner){
                status ->
            if (status.first == RegistrationStatus.SUCCESS) {
                Toast.makeText(requireContext(), status.second,
                    Toast.LENGTH_SHORT).show();
                //redirectToCategories()
            } else {
                Toast.makeText(requireContext(), status.second,
                    Toast.LENGTH_SHORT).show();            }
        }
    }
    private fun returnDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.parse(dateString) ?: Date()
    }

    private fun returnLong(timeString: String): Long {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = timeFormat.parse(timeString) ?: Date()
        return time.time
    }

}
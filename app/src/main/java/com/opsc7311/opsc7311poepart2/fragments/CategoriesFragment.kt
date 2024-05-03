package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.CategoriesAdapter
import com.opsc7311.opsc7311poepart2.database.model.Category
import com.opsc7311.opsc7311poepart2.database.service.CategoryService
import com.opsc7311.opsc7311poepart2.database.status.RegistrationStatus
import com.opsc7311.opsc7311poepart2.viewmodel.CategoryViewModel


class CategoriesFragment : Fragment() {
    private val categoryViewModel: CategoryViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var navigateToAddCategory: FrameLayout


    private lateinit var categoryList: ArrayList<Category>

    private lateinit var categoriesAdapter: CategoriesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        navigateToAddCategory = view.findViewById(R.id.to_add_category)

        recyclerView = view.findViewById(R.id.category_recycle_view)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.setHasFixedSize(true)

        categoryList = arrayListOf<Category>()

        categoriesAdapter = CategoriesAdapter()

        navigateToAddCategory.setOnClickListener{
            navigateToCreate()
        }
        recyclerView.adapter = categoriesAdapter

        getAllCategories()

        return view
    }

    private fun navigateToCreate() {
        val fragment = CreateCategoryFragment()

        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }

    private fun getAllCategories(){
        // This method was adapted from stackoverflow
        // https://stackoverflow.com/questions/59521691/use-viewlifecycleowner-as-the-lifecycleowner
        // CommonsWare
        // https://stackoverflow.com/users/115145/commonsware

        categoryViewModel._categories.observe(viewLifecycleOwner){
                categories ->
            categoriesAdapter.updateData(categories)
        }

        //Log.d("Tag", "name: $name color: \n$color")

        categoryViewModel.getCategories()

        categoriesAdapter.updateData(categoryList)
    }
}
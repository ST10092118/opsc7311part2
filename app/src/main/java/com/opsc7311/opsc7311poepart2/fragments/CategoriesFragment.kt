package com.opsc7311.opsc7311poepart2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opsc7311.opsc7311poepart2.R
import com.opsc7311.opsc7311poepart2.adapters.CategoriesAdapter
import com.opsc7311.opsc7311poepart2.database.model.Category


class CategoriesFragment : Fragment() {
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


        categoriesAdapter.updateData(categoryList)
    }
}
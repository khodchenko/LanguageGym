package com.example.languagegym.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.languagegym.model.CategoryModel
import android.app.AlertDialog
import android.graphics.Color
import android.widget.Toast
import com.example.languagegym.databinding.DialogAddCategoryBinding
import com.example.languagegym.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val categories = mutableListOf<CategoryModel>()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddCategory.setOnClickListener {
            showAddCategoryDialog()
        }

        // Create an adapter for the GridView
        categoryAdapter = CategoryAdapter(requireContext(), categories)
        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(category: CategoryModel) {
                Toast.makeText(requireContext(), category.name, Toast.LENGTH_SHORT).show()
            }
        })
        binding.gridViewCategories.adapter = categoryAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddCategoryDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogViewBinding = DialogAddCategoryBinding.inflate(layoutInflater)
        val etCategoryName = dialogViewBinding.etCategoryName

        dialogBuilder.setView(dialogViewBinding.root)
            .setTitle("Create Category")
            .setPositiveButton("Create") { dialog, _ ->
                val categoryName = etCategoryName.text.toString()
                if (categoryName.isNotEmpty()) {
                    val categoryColor = getColorFromUser()
                    val category =
                        CategoryModel(id = id, categoryName, categoryColor, mutableListOf())
                    categories.add(category)
                    Toast.makeText(requireContext(), "Category created!", Toast.LENGTH_SHORT).show()

                    // Update the UI with the new category
                    updateCategoryList()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please enter a category name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun updateCategoryList() {
        categoryAdapter.notifyDataSetChanged()

    }
    private fun getColorFromUser(): Int {
        return Color.rgb((0..255).random(), (0..255).random(), (0..255).random())
    }
}
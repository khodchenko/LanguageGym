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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.languagegym.R
import com.example.languagegym.databinding.DialogAddCategoryBinding
import com.example.languagegym.databinding.FragmentCategoryBinding
import com.example.languagegym.model.CategoryDao
import com.example.languagegym.model.DictionaryDatabase
import com.example.languagegym.ui.list.ListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val categories = mutableListOf<CategoryModel>()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryDao: CategoryDao
    private lateinit var database: DictionaryDatabase

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
        //Initialize the database and DAO
        database = DictionaryDatabase.getInstance(requireContext())
        categoryDao = database.categoryDao()

        // Create an adapter for the GridView
        categoryAdapter = CategoryAdapter(requireContext(), categories)
        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(category: CategoryModel) {
                    openCategoryFragment(category.categoryId)
                Toast.makeText(requireContext(), category.name, Toast.LENGTH_SHORT).show()
            }
        })
        binding.gridViewCategories.adapter = categoryAdapter
        // Update the category list to display the current data from the database
        updateCategoryList()
    }

    private fun openCategoryFragment(categoryId: Int) {
        //todo add bundle
        val wordListFragment = ListFragment()
        val bundle = Bundle().apply {
            putInt("categoryId", categoryId)
        }
        wordListFragment.arguments = bundle
        findNavController().navigate(R.id.action_categoryFragment_to_nav_list)
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
                        CategoryModel(name = categoryName, categoryColor = categoryColor)
                    categories.add(category)

                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            categoryDao.insertCategory(category)
                    }}

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
    //todo fix refreshing ui
    private fun updateCategoryList() {
        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                categoryDao.getAllCategories()
            }

            this@CategoryFragment.categories.clear()
            this@CategoryFragment.categories.addAll(categories)
            categoryAdapter.notifyDataSetChanged()
        }
    }
    private fun getColorFromUser(): Int {
        return Color.rgb((0..255).random(), (0..255).random(), (0..255).random())
    }
}
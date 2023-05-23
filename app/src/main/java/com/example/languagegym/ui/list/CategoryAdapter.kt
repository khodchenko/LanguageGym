package com.example.languagegym.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.languagegym.databinding.ItemCategoryBinding
import com.example.languagegym.model.CategoryModel

class CategoryAdapter(private val context: Context, private val categories: List<CategoryModel>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Any {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemCategoryBinding

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = ItemCategoryBinding.inflate(inflater, parent, false)
            binding.root.tag = binding
        } else {
            binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        }

        val category = categories[position]
        binding.categoryNameTextView.text = category.name
        binding.categoryColorView.setBackgroundColor(category.categoryColor)

        return binding.root
    }
}
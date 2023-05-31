package com.example.languagegym.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.languagegym.databinding.ItemCategoryBinding
import com.example.languagegym.model.CategoryModel

class CategoryAdapter(private val context: Context, private val categories: List<CategoryModel>) :
    BaseAdapter() {

    private var itemClickListener: OnItemClickListener? = null
    private var itemLongClickListener: OnItemLongClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(category: CategoryModel)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(category: CategoryModel): Boolean
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.itemLongClickListener = listener
    }

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
        val rootView: View

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            binding = ItemCategoryBinding.inflate(inflater, parent, false)
            rootView = binding.root
            rootView.tag = binding
        } else {
            binding = (convertView.tag as ItemCategoryBinding)
            rootView = convertView
        }

        val category = categories[position]
        binding.categoryNameTextView.text = category.name
        binding.llCategoryItem.setBackgroundColor(category.categoryColor)

        rootView.setOnClickListener {
            itemClickListener?.onItemClick(category)
        }

        rootView.setOnLongClickListener {
            itemLongClickListener?.onItemLongClick(category) ?: false
        }

        return rootView
    }
}
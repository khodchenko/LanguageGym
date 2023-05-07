package com.example.languagegym.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.languagegym.databinding.RecyclerviewItemBinding


class RecyclerViewAdapter(private val mContext: Context, data: List<String>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private val mData: List<String>
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mClickListener: ItemClickListener? = null

    init {
        mData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerviewItemBinding =
            RecyclerviewItemBinding.inflate(mInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.binding.wordTextview
        holder.binding.translationTextview
        holder.binding.genderTextview
        holder.binding.partOfSpeechTextview
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder internal constructor(binding: RecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.getRoot()), View.OnClickListener {
        var binding: RecyclerviewItemBinding

        init {
            this.binding = binding
            binding.getRoot().setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    fun getItem(id: Int): String {
        return mData[id]
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}
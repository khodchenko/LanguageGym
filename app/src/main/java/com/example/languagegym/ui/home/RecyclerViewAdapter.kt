package com.example.languagegym.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.languagegym.data.Word
import com.example.languagegym.databinding.RecyclerviewItemBinding


class RecyclerViewAdapter(private val mContext: Context, private val mData: MutableList<Word>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mClickListener: ItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: RecyclerviewItemBinding =
            RecyclerviewItemBinding.inflate(mInflater, parent, false)
        Log.d("RecyclerViewAdapter", "onCreateViewHolder: called")
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.binding.wordTextview.text = item.word
        holder.binding.translationTextview.text = item.translation
        holder.binding.genderTextview.text = item.gender
        holder.binding.partOfSpeechTextview.text = item.partOfSpeech
        if (item.imageUrl != null){
            //todo make realization
            holder.binding.wordImage.setImageResource(android.R.drawable.presence_invisible)
        } else {
            holder.binding.wordImage.setImageResource(android.R.drawable.presence_invisible)
        }
        holder.binding.progressBar.progress = item.learningProgress
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder internal constructor(binding: RecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var binding: RecyclerviewItemBinding

        init {
            this.binding = binding
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    fun getItem(id: Int): Word? {
        return if (id in 0 until mData.size) mData[id] else null
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}
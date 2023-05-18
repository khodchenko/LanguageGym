package com.example.languagegym.ui.home

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.languagegym.R
import com.example.languagegym.data.WordModel
import com.example.languagegym.databinding.RecyclerviewItemBinding

class RecyclerViewAdapter(
    private val context: Context,
    private var words: List<WordModel>,
    private val listener: OnWordItemClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.WordViewHolder>() {

    interface OnWordItemClickListener {
        fun onWordItemClick(word: WordModel)
        fun onWordItemDeleteClick(word: WordModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return WordViewHolder(binding, listener)
    }
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.bind(word)
    }

    override fun getItemCount(): Int {
        return words.size
    }
    fun updateData(words: List<WordModel>) {
        this.words = words
        notifyDataSetChanged()
    }

    inner class WordViewHolder(
        private val binding: RecyclerviewItemBinding,
        private val listener: OnWordItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.onWordItemClick(words[adapterPosition])
            }

        }

        fun bind(word: WordModel) {
            binding.wordTextview.text = word.word
            binding.translationTextview.text = word.translation
            binding.partOfSpeechTextview.text = word.partOfSpeech
            binding.genderTextview.text = word.gender
            binding.progressBarLearning.progress = word.learningProgress
            if (word.imageUrl == "") {
                binding.wordImage.visibility = View.INVISIBLE
                //binding.wordImage.setImageResource(R.drawable.ic_error)
                //binding.wordImage.setColorFilter(Color.GRAY)
            } else {
                Glide.with(context)
                    .load(word.imageUrl)
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_error)
                    .into(binding.wordImage)
            }
            //todo implementation click-to-speech
//            binding.imageViewVoice.setOnClickListener {
//                // Implement text-to-speech functionality here
//            }
        }
    }

}

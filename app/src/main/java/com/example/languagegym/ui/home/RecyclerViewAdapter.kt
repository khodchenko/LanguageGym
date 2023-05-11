package com.example.languagegym.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

//            binding.deleteButton.setOnClickListener {
//                listener.onWordItemDeleteClick(words[adapterPosition])
//            }
        }

        fun bind(word: WordModel) {
            binding.wordTextview.text = word.word
            binding.translationTextview.text = word.translation
            binding.partOfSpeechTextview.text = word.partOfSpeech
            binding.genderTextview.text = word.gender
            binding.progressBarLearning.progress = word.learningProgress
            //todo implementation
//            binding.imageViewVoice.setOnClickListener {
//                // Implement text-to-speech functionality here
//            }
        }
    }
}

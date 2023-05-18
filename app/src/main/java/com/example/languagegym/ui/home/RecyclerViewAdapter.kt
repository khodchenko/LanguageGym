package com.example.languagegym.ui.home

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.languagegym.R
import com.example.languagegym.data.WordModel
import com.example.languagegym.databinding.RecyclerviewItemBinding
import java.util.Locale

class RecyclerViewAdapter(
    private val context: Context,
    private var words: List<WordModel>,
    private val listener: OnWordItemClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.WordViewHolder>() {

    private var textToSpeech: TextToSpeech? = null

    interface OnWordItemClickListener {
        fun onWordItemClick(word: WordModel)
        fun onWordItemDeleteClick(word: WordModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(context), parent, false)

        textToSpeech = TextToSpeech(context, object : TextToSpeech.OnInitListener {
            override fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {
                    // Установка языка (может быть настройкой пользователя)
                    val result = textToSpeech?.setLanguage(Locale.getDefault())

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Язык не поддерживается, обработайте эту ситуацию по вашему усмотрению
                    }
                } else {
                    // Ошибка инициализации TTS, обработайте эту ситуацию по вашему усмотрению
                }
            }
        })

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

            binding.ivTextToSpeech.setOnClickListener {
                val wordToSpeak = word.word // Получите слово для озвучивания

                if (textToSpeech != null && !textToSpeech!!.isSpeaking) {
                    val params = HashMap<String, String>()
                    params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "wordUtteranceId"
                    textToSpeech!!.speak(wordToSpeak, TextToSpeech.QUEUE_FLUSH, params)
                }
            }
        }
    }
}

package com.example.languagegym.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.languagegym.R
import com.example.languagegym.data.DictionaryDatabaseHelper
import com.example.languagegym.data.WordModel
import com.example.languagegym.databinding.DialogAddWordBinding
import com.example.languagegym.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var databaseHelper: DictionaryDatabaseHelper
    private lateinit var fab: FloatingActionButton
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        fab = binding.fab
        fab.hide()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // создаем экземпляр DictionaryDatabaseHelper
        databaseHelper = DictionaryDatabaseHelper(requireContext())

        // получаем список слов из базы данных
        val words = databaseHelper.getAllWords()

        // создаем адаптер для RecyclerView
        adapter = RecyclerViewAdapter(words, onWordItemClickListener)

        // устанавливаем адаптер в RecyclerView
        recyclerView.adapter = adapter


        // делаем FloatingActionButton активной
        fab.show()
        fab.setOnClickListener {
            showAddWordDialog()
        }
    }

    private fun showAddWordDialog() {
        val dialogBinding = DialogAddWordBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding.root)
        builder.setTitle("Add Word")

        builder.setPositiveButton("Add") { dialog, _ ->
            val word = dialogBinding.editTextWord.text.toString()
            val translation = dialogBinding.editTextTranslation.text.toString()
            val partOfSpeech = dialogBinding.editTextPartOfSpeech.text.toString()
            val gender = dialogBinding.editTextGender.text.toString()
            val declension = dialogBinding.editTextDeclension.text.toString().split(",")
            val synonyms = dialogBinding.editTextSynonyms.text.toString().split(",")
            val imageUrl = dialogBinding.editTextImageUrl.text.toString()

            val newWord = WordModel(
                word = word,
                translation = translation,
                partOfSpeech = partOfSpeech,
                gender = gender,
                declension = declension,
                synonyms = synonyms,
                imageUrl = imageUrl
            )

            val db = DictionaryDatabaseHelper(requireContext())
            db.insertWord(newWord)
            updateRecyclerView(db.getAllWords())

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun updateRecyclerView(allWords: List<WordModel>) {
        adapter.updateData(allWords)
    }

    private val onWordItemClickListener = object : RecyclerViewAdapter.OnWordItemClickListener {
        override fun onWordItemClick(word: WordModel) {
            // Обработка события клика на элементе списка
        }

        override fun onWordItemDeleteClick(word: WordModel) {
            // Обработка события удаления элемента списка
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        databaseHelper.close()
        _binding = null
    }

}
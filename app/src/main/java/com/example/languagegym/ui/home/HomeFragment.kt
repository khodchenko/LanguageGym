package com.example.languagegym.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.languagegym.R
import com.example.languagegym.data.DictionaryDatabaseHelper
import com.example.languagegym.data.WordModel
import com.example.languagegym.databinding.DialogAddWordBinding
import com.example.languagegym.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var databaseHelper: DictionaryDatabaseHelper
    private lateinit var fab: FloatingActionButton
    private lateinit var loadingIndicator: ProgressBar
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadingIndicator = binding.progressBar
        fab = binding.fab
        fab.hide()

        databaseHelper = DictionaryDatabaseHelper(requireContext())


        loadAllWordsToList()

        fab.setOnClickListener {
            showAddWordDialog()
        }

        setupSpinnerAndRecyclerView()

        return binding.root
    }

    private fun loadAllWordsToList() {
        GlobalScope.launch(Dispatchers.IO) {
            val words = databaseHelper.getAllWords()
            withContext(Dispatchers.Main) {
                setupRecyclerView(words)
                updateRecyclerView(words)
                fab.show()
            }
        }
    }

    fun filterByPartOfSpeech(partOfSpeech: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val words = databaseHelper.getWordsByPartOfSpeech(partOfSpeech)
            withContext(Dispatchers.Main) {
                setupRecyclerView(words)
                updateRecyclerView(words)
                fab.show()
            }
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
                id = 0,
                word = word,
                translation = translation,
                partOfSpeech = partOfSpeech,
                gender = gender,
                declension = declension,
                synonyms = synonyms,
                imageUrl = (imageUrl ?: R.drawable.ic_error) as String,
                learningProgress = 0
            )

            val db = DictionaryDatabaseHelper(requireContext())
            if (word.isEmpty()) {
                Toast.makeText(requireContext(), "Word field is empty", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            db.insertWord(newWord)

            val allWords = db.getAllWords()
            Log.d("Add Word", "New Word: $newWord")
            Log.d("Add Word", "All Words: $allWords")

            updateRecyclerView(db.getAllWords())

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun setupRecyclerView(words: List<WordModel>) {
        adapter = RecyclerViewAdapter(
            requireContext(),
            words,
            object : RecyclerViewAdapter.OnWordItemClickListener {

                override fun onWordItemClick(word: WordModel) {
                    val bundle = Bundle().apply {
                        putParcelable("word", word)
                    }
                    findNavController().navigate(R.id.action_nav_home_to_detailsFragment, bundle)
                }

                override fun onWordItemDeleteClick(word: WordModel) {
                    //todo implementation
                }
            })
        recyclerView.adapter = adapter
    }

    private fun updateRecyclerView(allWords: List<WordModel>) {
        adapter.updateData(allWords)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        databaseHelper.close()
        _binding = null
    }
    private fun setupSpinnerAndRecyclerView() {
        val spinner = binding.spinnerFilter


        val partsOfSpeech = listOf("All", "Noun", "Verb", "Adjective", "Adverb", "Preposition", "Conjunction", "Interjection")


        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, partsOfSpeech)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        spinner.adapter = adapter

        // Устанавливаем обработчик выбора элемента в спиннере
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPartOfSpeech = partsOfSpeech[position]
                if(selectedPartOfSpeech == "All"){
                    loadAllWordsToList() // показываем все слова, не фильтруя по части речи
                } else {
                    filterByPartOfSpeech(selectedPartOfSpeech) // фильтруем по выбранной части речи
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }


}
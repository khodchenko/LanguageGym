package com.example.languagegym.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        showLoadingIndicator()

        GlobalScope.launch(Dispatchers.IO) {
            val words = databaseHelper.getAllWords()
            withContext(Dispatchers.Main) {
                setupRecyclerView(words)
                hideLoadingIndicator()
                fab.show()
            }
        }

        fab.setOnClickListener {
            showAddWordDialog()
        }

        return binding.root
    }


    private fun showLoadingIndicator() {
        loadingIndicator.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        loadingIndicator.visibility = View.GONE
    }


    override fun onResume() {
        super.onResume()


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
                imageUrl = imageUrl,
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
        adapter = RecyclerViewAdapter(requireContext(), words, object : RecyclerViewAdapter.OnWordItemClickListener {

            override fun onWordItemClick(word: WordModel) {
                val bundle = Bundle().apply {
                    putParcelable("word", word)
                }
                findNavController().navigate(R.id.action_nav_home_to_detailsFragment, bundle)
            }

            override fun onWordItemDeleteClick(word: WordModel) {
                // todo implementation
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

}
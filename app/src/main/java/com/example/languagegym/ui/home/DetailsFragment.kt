package com.example.languagegym.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.languagegym.R
import com.example.languagegym.data.DictionaryDatabaseHelper
import com.example.languagegym.data.WordModel
import com.example.languagegym.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var isEditMode = false
    private lateinit var dbHelper: DictionaryDatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        dbHelper = DictionaryDatabaseHelper(requireContext())

        binding.editFab.setOnClickListener {
            toggleEditMode()
        }

        binding.buttonWordDelete.setOnClickListener {
            val word = arguments?.getParcelable<WordModel>("word")
            if (word != null) {
                dbHelper.deleteWord(word)
                findNavController().navigateUp()
            }
        }
        binding.buttonWordEdit.setOnClickListener {

            with(binding) {
                hideEditButtons()
                editFab.setImageResource(R.drawable.ic_ok)
                val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("Female", "Male"))
                val posAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("NOUN", "PRONOUN", "VERB", "ADJECTIVE", "ADVERB", "PREPOSITION", "CONJUNCTION", "INTERJECTION"))

                val wordText = word.text.toString()
                val translationText = translation.text.toString()
                val synonymsText = synonyms.text.toString()

                listOf(word, translation, partOfSpeech, gender, synonyms).forEach { it.visibility = View.GONE }
                listOf(editWord, editTranslation, editPartOfSpeech, editGender, editSynonyms).forEach { it.visibility = View.VISIBLE }

                editWord.setText(wordText)
                editTranslation.setText(translationText)
                editSynonyms.setText(synonymsText)
                editPartOfSpeech.adapter = posAdapter
                editGender.adapter = genderAdapter
                buttonWordSave.visibility = View.VISIBLE
                buttonWordSave.setOnClickListener {
                    val newWord = editWord.text.toString()
                    val newTranslation = editTranslation.text.toString()
                    val newPos = editPartOfSpeech.selectedItem.toString()
                    val newGender = editGender.selectedItem.toString()
                    val newSynonyms = editSynonyms.text.toString()

                    val wordModel = word.id.let { WordModel(it.toLong(), newWord, newTranslation, newPos, newGender, listOf(newSynonyms)) }
                    val rowsAffected = dbHelper.updateWord(wordModel)

                    if (rowsAffected > 0) {
                        binding.apply {
                            word.text = newWord
                            translation.text = newTranslation
                            partOfSpeech.text = newPos
                            gender.text = newGender
                            synonyms.text = newSynonyms

                            listOf(word, translation, partOfSpeech, gender, synonyms).forEach { it.visibility = View.VISIBLE }
                            listOf(editWord, editTranslation, editPartOfSpeech, editGender, editSynonyms).forEach { it.visibility = View.GONE }

                            editFab.setImageResource(R.drawable.ic_edit)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to update word", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val word = arguments?.getParcelable<WordModel>("word")

        // Bind word data to view
        word?.let {
            binding.word.text = it.word
            binding.translation.text = it.translation
            binding.partOfSpeech.text = it.partOfSpeech
            binding.gender.text = it.gender
            binding.declension.text = it.declension.joinToString(", ")
            binding.synonyms.text = it.synonyms.joinToString(", ")
            binding.progressBarLearning.progress = it.learningProgress

            // Load image
            Glide.with(binding.imageUrl)
                .load(it.imageUrl)
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_error)
                .into(binding.imageUrl)
        }

    }

    private fun toggleEditMode() {
        isEditMode = !isEditMode

        if (isEditMode) {
            binding.editFab.setImageResource(R.drawable.ic_fab_cancel)
        } else {
            binding.editFab.setImageResource(R.drawable.ic_edit)
        }

        if (isEditMode) {
            binding.buttonWordEdit.visibility = View.VISIBLE
            binding.buttonWordDelete.visibility = View.VISIBLE
        } else {
            hideEditButtons()
        }
    }

    private fun hideEditButtons() {
        binding.buttonWordEdit.visibility = View.GONE
        binding.buttonWordDelete.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.languagegym.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.buttonWordEdit.setOnClickListener{
            val wordTextView = binding.word
            val translationTextView = binding.translation
            val partOfSpeechTextView = binding.partOfSpeech
            val genderTextView = binding.gender
            val synonymsTextView = binding.synonyms
            val editWordEditText = binding.editWord
            val editTranslationEditText = binding.editTranslation
            val editPartOfSpeechEditText = binding.editPartOfSpeech
            val editGenderEditText = binding.editGender
            val editSynonymsEditText = binding.editSynonyms
            // Hide elements
            wordTextView.visibility = View.GONE
            translationTextView.visibility = View.GONE
            partOfSpeechTextView.visibility = View.GONE
            genderTextView.visibility = View.GONE
            synonymsTextView.visibility = View.GONE

            // Show elements
            editWordEditText.visibility = View.VISIBLE
            editTranslationEditText.visibility = View.VISIBLE
            editPartOfSpeechEditText.visibility = View.VISIBLE
            editGenderEditText.visibility = View.VISIBLE
            editSynonymsEditText.visibility = View.VISIBLE
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
            binding.buttonWordEdit.visibility = View.GONE
            binding.buttonWordDelete.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
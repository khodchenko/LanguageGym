package com.example.languagegym.ui.add

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.languagegym.helpers.DictionaryDatabaseHelper
import com.example.languagegym.model.WordModel
import com.example.languagegym.databinding.FragmentAddWordBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddWordFragment : Fragment() {

    interface OnWordAddedListener {
        fun onWordAdded()
    }


    private lateinit var ivImagePicker: ImageView
    private var imageUri: Uri? = null
    private val REQUEST_IMAGE_GALLERY: Int = 100
    var onWordAddedListener: OnWordAddedListener? = null
    private var _binding: FragmentAddWordBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivImagePicker = binding.ivImagePicker
        binding.ivImagePicker.setOnClickListener {
            openGallery()
            Toast.makeText(
                requireContext(),
                " openGallery()",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.floatingActionButton.setOnClickListener {
            val word = binding.etWord.text.toString()
            val translation = binding.etWord2.text.toString()
            //todo change it to default category
            val partOfSpeech = binding.spinnerPos.selectedItem?.toString() ?: ""
            val category = binding.spinnerCategory.selectedItem?.toString() ?: ""

            val wordModelArg = arguments?.getParcelable<WordModel>("word")

            if (wordModelArg != null) {

                val updatedWord = WordModel(
                    word = word,
                    translation = translation,
                    partOfSpeech = partOfSpeech,

                )

                val dbHelper = DictionaryDatabaseHelper(requireContext())
                GlobalScope.launch(Dispatchers.IO) {
                    //todo fix update word
                    dbHelper.updateWord(updatedWord)
                }

                Toast.makeText(requireContext(), "Word updated successfully", Toast.LENGTH_SHORT).show()

                // Уведомить слушателя об обновлении слова
                onWordAddedListener?.onWordAdded()

                findNavController().navigateUp()
            } else {
                // Если аргументы отсутствуют, значит это добавление нового слова


                if (word.isNotEmpty() && translation.isNotEmpty()) {
                    val newWord = WordModel(
                        word = word,
                        translation = translation,
                        partOfSpeech = partOfSpeech,
                        imageUrl = imageUri.toString()
                    )

                    val dbHelper = DictionaryDatabaseHelper(requireContext())
                    GlobalScope.launch(Dispatchers.IO) {
                        dbHelper.insertWord(newWord)
                    }

                    Toast.makeText(requireContext(), "Word added successfully", Toast.LENGTH_SHORT).show()

                    // Уведомить слушателя о успешном добавлении слова
                    onWordAddedListener?.onWordAdded()

                    // Очистить поля ввода после добавления
                    binding.etWord.text.clear()
                    binding.etWord2.text.clear()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please enter word and translation",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            imageUri = data?.data
            imageUri?.let {
                Glide.with(this)
                    .load(imageUri)
                    .into(ivImagePicker)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}
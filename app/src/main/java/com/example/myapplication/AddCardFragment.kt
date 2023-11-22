package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAddCardBinding
import com.google.android.material.snackbar.Snackbar

class AddCardFragment : Fragment() {
    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentAddCardBinding.inflate(layoutInflater, container, false)

        binding.fab.setOnClickListener {
            if (fieldsValid()) {
                val question = binding.questionAddText.text.toString()
                val example = binding.hintAddText.text.toString()
                val answer = binding.answerAddText.text.toString()
                val translation = binding.translationAddText.text.toString()
                val newCard = Model.NewCard(question, example, answer, translation, imageUri)
                Model.addCard(newCard)
                val action = AddCardFragmentDirections.actionAddCardFragmentToMainFragment()
                findNavController().navigate(action)
            } else {
                fieldsIncompleteError()
            }
        }

        binding.cardImage.setOnClickListener {
            getSystemContent.launch("image/*")
        }
        return binding.root
    }
    private fun fieldsValid(): Boolean {
        return binding.questionAddText.text.isNotEmpty() &&
                binding.hintAddText.text.isNotEmpty() &&
                binding.answerAddText.text.isNotEmpty() &&
                binding.translationAddText.text.isNotEmpty()
    }

    private fun fieldsIncompleteError() {
        val errorMessage = getString(R.string.error_message)
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    private val getSystemContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        val name = requireActivity().packageName
        requireActivity().grantUriPermission(name, it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        binding.cardImage.setImageURI(it)
    }
}
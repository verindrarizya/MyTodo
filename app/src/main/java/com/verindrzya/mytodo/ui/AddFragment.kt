package com.verindrzya.mytodo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.verindrzya.mytodo.R
import com.verindrzya.mytodo.TodoViewModel
import com.verindrzya.mytodo.constant.PriorityLevel
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.databinding.FragmentAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment() {

    private val args: AddFragmentArgs by navArgs()

    val viewModel: TodoViewModel by hiltNavGraphViewModels(R.id.main_nav)

    private lateinit var item: Todo
    private lateinit var radioCheckedValue: String

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.id
        setRadio()

        if (id > 0) {
            // Update
            viewModel.getItem(id)
            viewModel.todoItem.observe(viewLifecycleOwner, this::bind)
        } else {
            // Add New
            binding.btnSubmit.setOnClickListener { submitTodo() }
        }
    }

    private fun bind(item: Todo) {
        this.item = item
        with(binding) {
            textInputTitle.setText(item.title)
            textInputDescription.setText(item.description)
            when (item.priorityLevel) {
                PriorityLevel.High.name -> radioHigh.isChecked = true
                PriorityLevel.Medium.name -> radioMedium.isChecked = true
                PriorityLevel.Low.name -> radioLow.isChecked = true
            }

            btnSubmit.setOnClickListener { updateItem() }
        }
    }

    private fun setRadio() {
        binding.rgPriority.check(R.id.radio_high)
        radioCheckedValue = PriorityLevel.High.name

        binding.rgPriority.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_high -> radioCheckedValue = PriorityLevel.High.name
                R.id.radio_medium -> radioCheckedValue = PriorityLevel.Medium.name
                R.id.radio_low -> radioCheckedValue = PriorityLevel.Low.name
            }
        }
    }

    private fun isEntryValid(): Boolean {
        with(binding) {

            tilTitle.error = ""
            tilDescription.error = ""

            return if (textInputTitle.text.isNullOrBlank() || textInputDescription.text.isNullOrBlank()) {
                when {
                    textInputTitle.text.isNullOrBlank() -> tilTitle.error = "Please fill in title"
                    textInputDescription.text.isNullOrBlank() -> tilDescription.error =
                        "Please fill in description"
                }

                false
            } else {
                true
            }
        }
    }

    private fun submitTodo() {
        if (isEntryValid()) {
            viewModel.insertItem(
                binding.textInputTitle.text.toString(),
                binding.textInputDescription.text.toString(),
                radioCheckedValue
            )

            navigateToList()
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                args.id,
                binding.textInputTitle.text.toString(),
                binding.textInputDescription.text.toString(),
                radioCheckedValue
            )

            navigateToList()
        }
    }

    private fun navigateToList() {
        val action = AddFragmentDirections.actionAddFragmentToListFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
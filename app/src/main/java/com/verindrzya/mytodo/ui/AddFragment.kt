package com.verindrzya.mytodo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.verindrzya.mytodo.R
import com.verindrzya.mytodo.TodoApplication
import com.verindrzya.mytodo.TodoViewModel
import com.verindrzya.mytodo.TodoViewModelFactory
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private val args: AddFragmentArgs by navArgs()

    private val viewModel: TodoViewModel by activityViewModels {
        TodoViewModelFactory(
            (activity?.application as TodoApplication).todoRepository
        )
    }

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
            when(item.priorityLevel) {
                getString(R.string.priority_high) -> radioHigh.isChecked = true
                getString(R.string.priority_medium) -> radioMedium.isChecked = true
                getString(R.string.priority_low) -> radioLow.isChecked = true
            }

            btnSubmit.setOnClickListener { updateItem() }
        }
    }

    private fun setRadio() {
        binding.rgPriority.check(R.id.radio_high)
        radioCheckedValue = getString(R.string.priority_high)

        Log.d("TestChecked2", "onViewCreated: $radioCheckedValue")

        binding.rgPriority.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.radio_high -> radioCheckedValue = getString(R.string.priority_high)
                R.id.radio_medium -> radioCheckedValue = getString(R.string.priority_medium)
                R.id.radio_low -> radioCheckedValue = getString(R.string.priority_low)
            }

            Log.d("TestChecked", "onViewCreated: $radioCheckedValue")
        }
    }

    private fun isEntryValid(): Boolean {
        with(binding) {

            tilTitle.error = ""
            tilDescription.error = ""

            return if(textInputTitle.text.isNullOrBlank() || textInputDescription.text.isNullOrBlank()) {
                when {
                    textInputTitle.text.isNullOrBlank() -> tilTitle.error = "Please fill in title"
                    textInputDescription.text.isNullOrBlank() -> tilDescription.error = "Please fill in description"
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
package com.verindrzya.mytodo.feature.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.verindrzya.mytodo.R
import com.verindrzya.mytodo.core.domain.model.Todo
import com.verindrzya.mytodo.databinding.FragmentAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment() {
    private val viewModel: TodoAddUpdateViewModel by viewModels()
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

        setRadio()
        setObserver()
    }

    private fun setObserver() {
        viewModel.todoDetail.observe(viewLifecycleOwner) { todo ->
            activity?.title = if (todo.id > 0) "Update" else "Add New"
            if (todo.id > 0) {
                bind(todo)
            } else {
                binding.btnSubmit.setOnClickListener { submitTodo() }
            }
        }
    }

    private fun bind(item: Todo) {
        with(binding) {
            textInputTitle.setText(item.title)
            textInputDescription.setText(item.description)
            when (item.priorityLevel) {
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

        binding.rgPriority.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_high -> radioCheckedValue = getString(R.string.priority_high)
                R.id.radio_medium -> radioCheckedValue = getString(R.string.priority_medium)
                R.id.radio_low -> radioCheckedValue = getString(R.string.priority_low)
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
            viewModel.addTodo(
                binding.textInputTitle.text.toString(),
                binding.textInputDescription.text.toString(),
                radioCheckedValue
            )

            navigateToList()
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateTodo(
                binding.textInputTitle.text.toString(),
                binding.textInputDescription.text.toString(),
                radioCheckedValue
            )

            navigateToList()
        }
    }

    private fun navigateToList() {
        val request = NavDeepLinkRequest.Builder
            .fromUri("app://com.verindrzya.todo/list".toUri())
            .build()

        findNavController().navigate(
            request = request,
            navOptions = NavOptions.Builder()
                .setPopUpTo(findNavController().graph.startDestinationId, true)
                .build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
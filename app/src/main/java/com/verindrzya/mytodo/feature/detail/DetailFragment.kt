package com.verindrzya.mytodo.feature.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.verindrzya.mytodo.R
import com.verindrzya.todo.core.domain.model.Todo
import com.verindrzya.mytodo.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: TodoDetailViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.todoDetail.observe(viewLifecycleOwner, this::bind)
        binding.btnUpdate.setOnClickListener { navigateToAdd() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                viewModel.deleteTodo()
                findNavController().navigateUp()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bind(todo: com.verindrzya.todo.core.domain.model.Todo) {
        with(binding) {
            textInputTitle.setText(todo.title)
            textInputDescription.setText(todo.description)
            textInputPriorityLevel.setText(todo.priorityLevel)
        }
    }

    private fun navigateToAdd() {
        val currentTodoId = viewModel.todoDetail.value?.id ?: 0
        val request = NavDeepLinkRequest.Builder
            .fromUri("app://com.verindrzya.todo/add/$currentTodoId".toUri())
            .build()

        findNavController().navigate(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.verindrzya.mytodo.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.verindrzya.mytodo.R
import com.verindrzya.mytodo.TodoApplication
import com.verindrzya.mytodo.TodoViewModel
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    val viewModel: TodoViewModel by hiltNavGraphViewModels(R.id.main_nav)

    private lateinit var item: Todo

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
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.id
        viewModel.getItem(id)
        viewModel.todoItem.observe(viewLifecycleOwner, this::bind)
        binding.btnUpdate.setOnClickListener { navigateToAdd() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_delete -> {
                viewModel.deleteItem(this.item)
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bind(todo: Todo) {
        item = todo
        with(binding) {
            textInputTitle.setText(todo.title)
            textInputDescription.setText(todo.description)
            textInputPriorityLevel.setText(todo.priorityLevel)
        }
    }

    private fun navigateToAdd() {
        val action  = DetailFragmentDirections.actionDetailFragmentToAddFragment(item.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
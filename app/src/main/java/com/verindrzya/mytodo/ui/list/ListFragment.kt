package com.verindrzya.mytodo.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.verindrzya.mytodo.R
import com.verindrzya.mytodo.TodoApplication
import com.verindrzya.mytodo.TodoViewModel
import com.verindrzya.mytodo.TodoViewModelFactory
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private val viewModel: TodoViewModel by activityViewModels {
        TodoViewModelFactory(
            (activity?.application as TodoApplication).todoRepository
        )
    }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentItem: Todo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TodoListAdapter { todoItem ->
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(todoItem.id)
            findNavController().navigate(action)
        }

        binding.rvList.layoutManager = LinearLayoutManager(context)
        binding.rvList.adapter = adapter

        val simpleCallback = object:ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currentItem = (viewHolder as TodoListAdapter.TodoViewHolder).currentItem
                viewModel.deleteItem(currentItem)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvList)

        viewModel.todoList.observe(viewLifecycleOwner) { todoList ->
            if (todoList.isEmpty()) {
                setEmptyStatement()
            } else {
                adapter.submitList(todoList)
            }
        }

        binding.fabAdd.setOnClickListener { navigateToAdd() }
    }

    private fun setEmptyStatement() {
        binding.rvList.visibility = View.GONE
        binding.tvStatement.visibility = View.VISIBLE
        binding.tvStatement.text = getString(R.string.empty_statement)
    }

    private fun navigateToAdd() {
        val action = ListFragmentDirections.actionListFragmentToAddFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.verindrzya.mytodo.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.verindrzya.mytodo.R
import com.verindrzya.mytodo.TodoViewModel
import com.verindrzya.mytodo.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {

    val viewModel: TodoViewModel by hiltNavGraphViewModels(R.id.main_nav)

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

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

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

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
        })
        itemTouchHelper.attachToRecyclerView(binding.rvList)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todoList.collectLatest {
                adapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { loadState: CombinedLoadStates ->
                    val isListEmpty =
                        loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                    if (isListEmpty) {
                        setEmptyStatement()
                    } else {
                        displayList()
                    }
                }
        }

        binding.fabAdd.setOnClickListener { navigateToAdd() }
    }

    private fun setEmptyStatement() {
        binding.rvList.visibility = View.GONE
        binding.tvStatement.visibility = View.VISIBLE
        binding.tvStatement.text = getString(R.string.empty_statement)
    }

    private fun displayList() {
        binding.tvStatement.visibility = View.GONE
        binding.rvList.visibility = View.VISIBLE
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
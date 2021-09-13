package com.verindrzya.mytodo.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.verindrzya.mytodo.R
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.databinding.ItemListBinding

class TodoListAdapter(private val onClicked: (Todo) -> Unit = {})
    : PagedListAdapter<Todo, TodoListAdapter.TodoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = getItem(position)

        item?.let {
            holder.bind(item)
        }
    }

    inner class TodoViewHolder(private val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {

        lateinit var currentItem: Todo

        fun bind(item: Todo) {
            with(binding) {
                currentItem = item
                tvTitle.text = item.title
                tvDescription.text = itemView.context.getString(R.string.description, item.description)
                tvPriorityLevel.text = item.priorityLevel
            }

            itemView.setOnClickListener{ onClicked(item) }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Todo>(){
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem == newItem
            }
        }
    }

}
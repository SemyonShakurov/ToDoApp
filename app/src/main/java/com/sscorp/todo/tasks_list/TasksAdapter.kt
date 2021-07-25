package com.sscorp.todo.tasks_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sscorp.todo.R
import com.sscorp.todo.fragments.TasksFragment
import com.sscorp.todo.models.Task

class TasksAdapter(
    val context: Context,
    private val view: TasksFragment,
    private val onCheckBoxClick: (task: Task, pos: Int) -> Unit
) : ListAdapter<Task, RecyclerView.ViewHolder>(TaskCallback()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NOTE -> TasksViewHolder(
                context,
                onCheckBoxClick,
                inflater.inflate(R.layout.list_item_task, parent, false)
            )
            TYPE_NEW -> LastItemViewHolder(
                context, inflater.inflate(R.layout.list_item_new, parent, false)
            )
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TasksViewHolder)
            holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> TYPE_NEW
            else -> TYPE_NOTE
        }
    }

    override fun getItemCount(): Int = currentList.size + 1

    fun deleteItem(position: Int) = view.deleteNote(getItem(position))

    fun checkItem(position: Int) = view.checkNote(getItem(position), position)

    companion object {
        const val TYPE_NOTE = 0
        const val TYPE_NEW = 1
    }
}
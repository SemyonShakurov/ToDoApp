package com.sscorp.todo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sscorp.todo.R
import com.sscorp.todo.activities.MainActivity
import com.sscorp.todo.models.Note
import java.lang.IllegalArgumentException

class NotesAdapter(
    val context: Context
) : ListAdapter<Note, RecyclerView.ViewHolder>(NoteCallback()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NOTE -> NotesViewHolder(
                context,
                inflater.inflate(R.layout.list_item_note, parent, false)
            )
            TYPE_NEW -> LastItemViewHolder(inflater.inflate(R.layout.list_item_new, parent, false))
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotesViewHolder)
            holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> TYPE_NEW
            else -> TYPE_NOTE
        }
    }

    override fun getItemCount(): Int = currentList.size + 1

    fun deleteItem(position: Int) = (context as MainActivity).deleteNote(position)

    fun checkItem(position: Int) {

    }

    companion object {
        const val TYPE_NOTE = 0
        const val TYPE_NEW = 1
    }
}
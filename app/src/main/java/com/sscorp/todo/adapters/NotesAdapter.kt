package com.sscorp.todo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sscorp.todo.R
import com.sscorp.todo.fragments.NotesFragment
import com.sscorp.todo.models.Note
import java.lang.IllegalArgumentException

class NotesAdapter(
    val context: Context,
    private val view: NotesFragment,
    private val onCheckBoxClick: (note: Note, pos: Int) -> Unit
) : ListAdapter<Note, RecyclerView.ViewHolder>(NoteCallback()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NOTE -> NotesViewHolder(
                context,
                onCheckBoxClick,
                inflater.inflate(R.layout.list_item_note, parent, false)
            )
            TYPE_NEW -> LastItemViewHolder(
                context, inflater.inflate(R.layout.list_item_new, parent, false)
            )
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

    fun deleteItem(position: Int) = view.deleteNote(getItem(position))

    fun checkItem(position: Int) = view.checkNote(getItem(position), position)

    companion object {
        const val TYPE_NOTE = 0
        const val TYPE_NEW = 1
    }
}
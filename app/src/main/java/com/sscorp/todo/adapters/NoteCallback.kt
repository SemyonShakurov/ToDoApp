package com.sscorp.todo.adapters

import androidx.recyclerview.widget.DiffUtil
import com.sscorp.todo.models.Note

class NoteCallback : DiffUtil.ItemCallback<Note>() {

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}
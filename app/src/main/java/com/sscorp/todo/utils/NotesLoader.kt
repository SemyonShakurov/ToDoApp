package com.sscorp.todo.utils

import com.sscorp.todo.models.Importance
import com.sscorp.todo.models.Note
import java.util.*

object NotesLoader {

    fun getNotes(): MutableList<Note> {
        return mutableListOf(
            Note("Сходить в магазин"),
            Note("Написать ToDo приложение"),
            Note("Встретиться с друзьями", expirationDate = Date()),
            Note("Сдать сессию"),
            Note("Сходить в тренажерку", Importance.HIGH),
            Note("Покормить пса"),
            Note("Купить что-то, где-то, зачем-то, " +
                    "но зачем не очень понятно, " +
                    "но точно чтобы показать как обрезается…", isDone = true),
            Note("Купить что-то, где-то, зачем-то, " +
                    "но зачем не очень понятно, " +
                    "но точно чтобы показать как обрезается…", Importance.HIGH),
            Note("Купить что-то, где-то, зачем-то, " +
                    "но зачем не очень понятно, " +
                    "но точно чтобы показать как обрезается…", Importance.LOW),
            Note("Купить что-то, где-то, зачем-то, " +
                    "но зачем не очень понятно, " +
                    "но точно чтобы показать как обрезается…")
        )
    }
}
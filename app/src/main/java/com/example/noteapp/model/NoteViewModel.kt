package com.example.noteapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.database.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModal (application: Application) : AndroidViewModel(application) {


    private val repository : NoteRepository
    val allNotes : LiveData<List<Note>>



    init {
        val dao = NoteDatabase.getDatabase(application).getNotesDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }


    fun deleteNote (note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }


    fun upsertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.upsert(note)
    }
}
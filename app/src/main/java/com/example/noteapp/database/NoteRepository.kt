package com.example.noteapp.database

import androidx.lifecycle.LiveData
import com.example.noteapp.model.Note

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes : LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun upsert(note: Note){
        noteDao.upsert(note)
    }

    suspend fun delete(note: Note){
        noteDao.delete(note)
    }
}
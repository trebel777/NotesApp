package ru.netology.notesapp.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.notesapp.data.NoteData
import ru.netology.notesapp.data.NotesRepository

class NoteViewModel(application: Application): AndroidViewModel(application) {

    private val repository: NotesRepository
    val allNotes: LiveData<List<Note>>

    init {
        val dao = NoteData.getData(application).getNoteDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes
    }
    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }
    fun insertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }
    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }
}
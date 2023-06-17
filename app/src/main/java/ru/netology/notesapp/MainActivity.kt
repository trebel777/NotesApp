package ru.netology.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.netology.notesapp.adapter.NotesAdapter
import ru.netology.notesapp.data.NoteData
import ru.netology.notesapp.databinding.ActivityMainBinding
import ru.netology.notesapp.models.Note
import ru.netology.notesapp.models.NoteViewModel

class MainActivity : AppCompatActivity(), NotesAdapter.notesItemClickListenner,
    PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var data: NoteData
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote: Note
    private val updateNote =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null) {
                    viewModel.updateNote(note)
                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get((NoteViewModel::class.java))

        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }

        }
        data = NoteData.getData(this)
    }

    private fun initUi() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val note = result.data?.getSerializableExtra("note") as? Note
                    if (note != null) {
                        viewModel.insertNote(note)
                    }
                }
            }
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)

        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onLongClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {

        val popUp = PopupMenu(this, cardView)
        popUp.setOnMenuItemClickListener(this@MainActivity)
        popUp.inflate(R.menu.pop_up_menu)
        popUp.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.deleteNote) {
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}
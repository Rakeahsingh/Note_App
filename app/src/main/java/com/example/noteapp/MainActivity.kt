package com.example.noteapp

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.model.Note
import com.example.noteapp.model.NoteViewModal

class MainActivity : AppCompatActivity(), NoteAdapter.NotesClickListner, PopupMenu.OnMenuItemClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel : NoteViewModal
    lateinit var adapter: NoteAdapter
    lateinit var seletedNote: Note

    val upsertNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val note = result.data?.getSerializableExtra("note") as? Note
            if (note != null){
                viewModel.upsertNote(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModal::class.java)

        viewModel.allNotes.observe(this){list ->
            list?.let {
                adapter.updateList(list)
            }
        }



        database = NoteDatabase.getDatabase(this)
    }


    private fun initUI() {

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NoteAdapter(this,this)
        binding.rv.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null){
                    viewModel.upsertNote(note)
                }
            }
        }


        binding.flioatbtn.setOnClickListener {

            val intent = Intent(this@MainActivity, AddNotes::class.java)
            upsertNote.launch(intent)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    adapter.filterList(newText)
                }
                return true
            }
        })

    }

    override fun onItemClicked(note: Note) {

        val intent = Intent(this@MainActivity, AddNotes::class.java)
        intent.putExtra("current_note",note)
        upsertNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        seletedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {

        val popUp = PopupMenu(this, cardView)
        popUp.setOnMenuItemClickListener(this@MainActivity)
        popUp.inflate(R.menu.pop_up_menu)
        popUp.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean{

        if (item?.itemId == R.id.delete_note){
            viewModel.deleteNote(seletedNote)
            return true
        }
        return false
    }
}
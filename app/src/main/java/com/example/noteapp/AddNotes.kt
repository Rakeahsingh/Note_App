package com.example.noteapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.example.noteapp.databinding.ActivityAddNotesBinding
import java.text.SimpleDateFormat
import com.example.noteapp.model.Note
import java.util.*

class AddNotes : AppCompatActivity() {

    private lateinit var binding: ActivityAddNotesBinding
    private lateinit var note: Note
    private lateinit var old_note: Note
    var isUpdate = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.edt1.setText(old_note.title)
            binding.edt2.setText(old_note.note)

            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgchk.setOnClickListener {

            val title = binding.edt1.text.toString()
            val note_desc = binding.edt2.text.toString()

            if (title.isNotEmpty() && note_desc.isNotEmpty()) {

                val forMatter = SimpleDateFormat("EEE d MMM yyy HH:mm a")

                if (isUpdate) {
                    note = Note(
                        old_note.id, title, note_desc, forMatter.format(Date())
                    )
                } else {
                    note = Note(
                        null, title, note_desc, forMatter.format(Date())
                    )
                }


                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()

            } else {

                Toast.makeText(this@AddNotes, "please fill some Date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
        }

        binding.imgbk.setOnClickListener {
            onBackPressed()
        }

    }
}
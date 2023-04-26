package com.example.noteapp.adapter

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.noteapp.model.Note
import androidx.recyclerview.widget.RecyclerView

import com.example.noteapp.R
import com.example.noteapp.databinding.ItemListBinding
import kotlin.random.Random

class NoteAdapter(private val context: Context, val listner: NotesClickListner) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private lateinit var binding: ItemListBinding

    private val NoteList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemListBinding.inflate(inflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return NoteList.size
    }

    // below method is use to update our list of notes.
    fun updateList(newList: List<Note>) {

        fullList.clear()
        fullList.addAll(newList)

        NoteList.clear()
        NoteList.addAll(newList)

        notifyDataSetChanged()
    }

    fun filterList(search: String){

        NoteList.clear()

        for (item in fullList){

            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true){
                NoteList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun randomColor(): Int{
        val list = ArrayList<Int>()
        list.add(R.color.note1)
        list.add(R.color.note2)
        list.add(R.color.note3)
        list.add(R.color.note4)
        list.add(R.color.note5)
        list.add(R.color.note6)
        list.add(R.color.note7)
        list.add(R.color.note8)
        list.add(R.color.note9)
        list.add(R.color.note10)
        list.add(R.color.note11)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentList = NoteList[position]
        holder.title.text = currentList.title
        holder.title.isSelected = true
        holder.note.text = currentList.note
        holder.date.text = currentList.date
        holder.date.isSelected = true

        holder.note_layout.setCardBackgroundColor(
            holder.itemView.resources.getColor(randomColor(),null)
        )

        holder.note_layout.setOnClickListener{
            listner.onItemClicked(NoteList[holder.adapterPosition])
        }

        holder.note_layout.setOnLongClickListener {
            listner.onLongItemClicked(NoteList[holder.adapterPosition],
                holder.note_layout)
            true
        }
    }

    inner class NoteViewHolder(binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root){

        val note_layout = binding.cardv
        val title = binding.title
        val note = binding.note
        val date = binding.date
    }

    interface NotesClickListner {

        fun onItemClicked(note: Note)

        fun onLongItemClicked(note: Note, cardView: CardView)
    }
}
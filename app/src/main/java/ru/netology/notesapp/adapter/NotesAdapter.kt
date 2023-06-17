package ru.netology.notesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.netology.notesapp.R
import ru.netology.notesapp.models.Note
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener: notesItemClickListenner):
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val note = itemView.findViewById<TextView>(R.id.tvNote)
        val date = itemView.findViewById<TextView>(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true
        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.notes_layout.setOnClickListener{
            listener.onItemClicked(NotesList[holder.adapterPosition])
        }
        holder.notes_layout.setOnClickListener {
            listener.onLongClicked(NotesList[holder.adapterPosition],holder.notes_layout)
            true
        }

    }
    fun updateList(newList : List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)
        notifyDataSetChanged()
    }
    fun filterList(search : String){
        NotesList.clear()
        for (item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase()) == true ||
                    item.note?.lowercase()?.contains(search.lowercase()) == true){
                NotesList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun randomColor(): Int {
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)
        list.add(R.color.NoteColor7)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    interface notesItemClickListenner{
        fun onItemClicked(note: Note)
        fun onLongClicked(note: Note, cardView: CardView)
    }
}
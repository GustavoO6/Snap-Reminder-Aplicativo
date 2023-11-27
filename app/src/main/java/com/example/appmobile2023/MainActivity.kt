package com.example.appmobile2023

// Importe pacotes necessários
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Define o layout para a atividade principal
    }
}

data class Note(
    val id: Int,    // Identificador único para cada nota
    var title: String?,  // Título da nota
    var content: String?  // Conteúdo da nota
) {

}

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(),
    Parcelable {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
    }

    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Note)) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.titleTextView.text = currentNote.title
        holder.contentTextView.text = currentNote.content
    }

    override fun getItemCount() = notes.size
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(notes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteAdapter> {
        override fun createFromParcel(parcel: Parcel): NoteAdapter {
            return NoteAdapter(parcel)
        }

        override fun newArray(size: Int): Array<NoteAdapter?> {
            return arrayOfNulls(size)
        }
    }
}

private fun Parcel.writeTypedList(notes: List<Note>) {

}


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase() : RoomDatabase(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    abstract fun noteDao(): NoteDao
    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteDatabase> {
        override fun createFromParcel(parcel: Parcel): NoteDatabase {
            return NoteDatabase(parcel = parcel)
        }

        override fun newArray(size: Int): Array<NoteDatabase?> {
            return arrayOfNulls(size)
        }
    }
}

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    // Atualizar uma nota existente
    @Query("UPDATE notes SET title = :title, content = :content WHERE id = :id")
    suspend fun update(id: Int, title: String, content: String)

    // Excluir uma nota
    @Delete
    suspend fun delete(note: Note)

    // Obter todas as notas do banco de dados
    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<Note>
}

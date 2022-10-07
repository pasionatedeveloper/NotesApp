package com.example.notesapp.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.NotesViewModel
import com.example.notesapp.adapter.RecyclerAdapter.ViewHolder
import com.example.notesapp.database.NoteEntity
import com.example.notesapp.databinding.SampleNoteItemBinding
import com.example.notesapp.ui.NoteDetailsActivity
import kotlin.properties.Delegates


class RecyclerAdapter(
    var context: Context,
    var allNotesList: List<NoteEntity>,
    val noteClickDeleteInterface: NoteClickDeleteInterface
) : RecyclerView.Adapter<ViewHolder>() {

    var selectedPos = RecyclerView.NO_POSITION;

    lateinit var selectedNotesList: ArrayList<String>

    lateinit var currentTime: String

    var flag by Delegates.notNull<Boolean>()

    lateinit var notesViewModel: NotesViewModel
    var mediaPlayer: MediaPlayer? = MediaPlayer()

   var check =true
    var isEnable by Delegates.notNull<Boolean>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SampleNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false);
        return ViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        flag = false
        val note = allNotesList[position]
        holder.itemView.setSelected(selectedPos == position);
        Log.d("TAG", "onBindViewHolder: ${allNotesList.size}")
        var path = allNotesList.get(position).noteImage
        if (path.contains("/root")) {
            path = path.replace("/root", "")
        }
        Glide.with(context).load(path).into(holder.noteImageView)
        Log.d("TAG", "onBindViewHolder: $note")
        Log.d("TAG", "onBindViewHolder: ${note.noteImage}")
        holder.noteLocation.text = allNotesList.get(position).noteLocation
        holder.noteTitle.text = allNotesList.get(position).noteTitle

        holder.noteAudio.setOnClickListener {
            try {


                var audioPath = allNotesList[position].noteAudio
                if (audioPath.contains("/root")) {
                    audioPath = audioPath.replace("/root", "")
                }

                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()

                }

                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(audioPath)
                mediaPlayer?.prepare()
                mediaPlayer?.start()

//                 flag=true;
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }




        holder.itemView.setOnClickListener {

            val intent = Intent(context, NoteDetailsActivity::class.java)
            intent.putExtra("title", allNotesList.get(position).noteTitle)
            intent.putExtra("description", allNotesList.get(position).noteDescription)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent)
        }
        var onItemLongClick: ((NoteEntity) -> Unit)? = null

        holder.deleteIcon.setOnClickListener {
            noteClickDeleteInterface.onDeleteIconClick(allNotesList.get(position))
        }

        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener true
        }
    }





    override fun getItemCount(): Int {
        return allNotesList.size
    }

    fun filterList(filterlist: ArrayList<NoteEntity>) {

        allNotesList = filterlist
        notifyDataSetChanged()
    }


    inner class ViewHolder(binding: SampleNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val noteImageView = binding.noteImageViewItem
        val noteTitle = binding.noteTitleTextView
        val noteLocation = binding.noteLocationTextView
        val noteAudio = binding.noteAudioTextView
        val card_view = binding.cardViewId
        val deleteIcon = binding.deleteIcon

    }


    interface NoteClickDeleteInterface {

        fun onDeleteIconClick(note: NoteEntity)
    }

}
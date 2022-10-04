package com.example.notesapp.adapter


import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.adapter.RecyclerAdapter.*
import com.example.notesapp.database.NoteEntity
import com.example.notesapp.databinding.SampleNoteItemBinding



class RecyclerAdapter(var context: Context, var allNotesList :List<NoteEntity>) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SampleNoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false);
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = allNotesList[position]
        Log.d("TAG", "onBindViewHolder: ${allNotesList.size}")
        var path = allNotesList.get(position).noteImage
        if(path.contains("/root"))
        {
            path=path.replace("/root","")
        }
        Glide.with(context).load(path).into(holder.noteImageView)
        Log.d("TAG", "onBindViewHolder: $note")
        Log.d("TAG", "onBindViewHolder: ${note.noteImage}")
        holder.noteLocation.text=allNotesList.get(position).noteLocation
        holder.noteTitle.text=allNotesList.get(position).noteTitle

        holder.noteAudio.setOnClickListener{
            try{
                var audioPath = allNotesList.get(position).noteAudio
                if (audioPath.contains("/root")){
                    audioPath=audioPath.replace("/root","")
                }
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(audioPath)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
            catch (e : Exception){
                Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return allNotesList.size
    }

    fun updateList(newList : ArrayList<NoteEntity>){
        newList.addAll(newList)
        notifyDataSetChanged()
    }

//    interface NoteClickDeleteInterface {
//        // creating a method for click
//        // action on delete image view.
//        fun onDeleteIconClick(note: Note)
//    }
//
//    interface NoteClickInterface {
//        // creating a method for click action
//        // on recycler view item for updating it.
//        fun onNoteClick(note: Note)
//    }

    inner class ViewHolder(val binding : SampleNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val noteImageView = binding.noteImageViewItem
        val noteTitle = binding.noteTitleTextView
        val noteLocation = binding.noteLocationTextView
        val noteAudio = binding.noteAudioTextView
    }

}
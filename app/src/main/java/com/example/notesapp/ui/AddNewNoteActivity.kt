package com.example.notesapp.ui

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.NotesViewModel
import com.example.notesapp.database.NoteEntity
import com.example.notesapp.databinding.ActivityAddNewNoteBinding
import com.example.notesapp.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.net.URI

class AddNewNoteActivity : AppCompatActivity() {

    lateinit var title : String
    lateinit var location : String
    lateinit var description : String
    lateinit var audioPath : String

    lateinit var imagePath : String
    lateinit var binding : ActivityAddNewNoteBinding

    lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding=ActivityAddNewNoteBinding.inflate(layoutInflater)
         setContentView(binding.root)

         notesViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NotesViewModel::class.java)


         binding.btnSubmitNote.setOnClickListener{
            title= binding.noteTitleEditText.text.toString()
            description=binding.notesLongDescriptionEditText.text.toString()
            location=binding.noteLocationTextView.text.toString()
            audioPath=binding.noteAudioTextView.text.toString()


            if (title.equals("")||title.isEmpty()){
                binding.noteTitleEditText.setError("This field is mandatory")
            }
            if (description.equals("")||description.isEmpty()){
                binding.notesLongDescriptionEditText.setError("This field is mandatory")
            }
            if (location.equals("")||location.isEmpty()){
                binding.noteLocationTextView.setError("This field is mandatory")
            }
            if (audioPath.equals("")||audioPath.isEmpty()){
                binding.noteAudioTextView.setError("This field is mandatory")
            }
            else{
                notesViewModel.addNote(NoteEntity(title,location,audioPath,imagePath,description))
                Toast.makeText(applicationContext,"Note added successfully",Toast.LENGTH_LONG).show()
            }
         }
    }

    fun openGallery(view: View) {

        val intent = Intent()
        intent.setAction(Intent.ACTION_PICK)
        intent.setType("image/*");
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!=null && resultCode== RESULT_OK && requestCode==100){
            binding.noteImage.setImageURI(data.data)
            imagePath=data.data?.path.toString()
        }

        if (data!=null && resultCode== RESULT_OK && requestCode==200){
            binding.noteAudioTextView.text=data.data?.path.toString()
        }
    }

    fun selectAudio(view: View) {
        val intent = Intent()
        intent.setAction(Intent.ACTION_PICK)
        intent.setType("audio/*");
        startActivityForResult(intent,200)
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            var text = mLastLocation.latitude
            text=text+mLastLocation.longitude
            binding.noteLocationTextView.text=text.toString()
        }
    }
}
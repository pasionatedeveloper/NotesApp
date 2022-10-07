package com.example.notesapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.NotesViewModel
import com.example.notesapp.database.NoteEntity
import com.example.notesapp.databinding.ActivityAddNewNoteBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAd
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class AddNewNoteActivity : AppCompatActivity() {

    lateinit var title : String
    lateinit var location : String
    lateinit var description : String
    lateinit var audioPath : String

    lateinit var imagePath : String
    lateinit var binding : ActivityAddNewNoteBinding

    lateinit var notesViewModel: NotesViewModel

    lateinit var recordedAudioPath : String

    lateinit var mediaRecorder : MediaRecorder

    lateinit var add : String

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var capturedImagePath : String

    lateinit var currentTime : String
    lateinit var internalDirectory :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding=ActivityAddNewNoteBinding.inflate(layoutInflater)
         setContentView(binding.root)


         mediaRecorder = MediaRecorder()
         recordedAudioPath= "/storage/emulated/0/notes-app-recordings"+"/my-rec.3gp"

         capturedImagePath="/storage/emulated/0/notes-app-camera-images"+"/my-img.jpg"

         fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(applicationContext)

         notesViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NotesViewModel::class.java)


        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forUnifiedNativeAd { ad ->
                // Show the ad.
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder()
                // Methods in the NativeAdOptions.Builder class can be
                // used here to specify individual options settings.
                .build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

         binding.noteLocationTextView.setOnClickListener{

                val task = fusedLocationProviderClient.lastLocation
                if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
                    return@setOnClickListener
                }



                task.addOnSuccessListener {
                    if(it!=null){

                        var geocoder = Geocoder(this, Locale.getDefault())
                        try{
                            val addresses: List<Address> = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                            val obj: Address = addresses[0]
                            add = obj.getAddressLine(0);
                            add = add + "\n" + obj.getCountryName();
                        }
                        catch (e : Exception){
                            Toast.makeText(applicationContext,e.message,Toast.LENGTH_LONG).show()
                        }
                        Toast.makeText(applicationContext,add,Toast.LENGTH_LONG).show()
                        binding.noteLocationTextView.text= add
                    }
                }
        }

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
                Toast.makeText(applicationContext,imagePath,Toast.LENGTH_LONG).show()
            }
         }
    }

    fun openGallery(view: View) {

        var builder= AlertDialog.Builder(this)
        builder.setMessage("Choose image from")
        builder.setPositiveButton("Camera"){dialogInterface,which->
            currentTime=Calendar.getInstance().time.toString()
            currentTime=currentTime+capturedImagePath
            imagePath=currentTime
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 400)
        }
        builder.setNegativeButton("Pick Image from gallery"){dialogInterface,which->
            val intent = Intent()
            intent.setAction(Intent.ACTION_PICK)
            intent.setType("image/*");
            startActivityForResult(intent,100)
        }
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!=null && resultCode == RESULT_OK && requestCode==100){
            binding.noteImage.setImageURI(data.data)
            imagePath=data.data?.path.toString()
        }

        if (data!=null && resultCode == RESULT_OK && requestCode==200){
            binding.noteAudioTextView.text=data.data?.path.toString()
        }

        if (data!=null && resultCode == RESULT_OK && requestCode==400){
            binding.noteImage.setImageBitmap(data.extras?.get("data") as Bitmap)
            binding.noteImage.scaleType=ImageView.ScaleType.CENTER_CROP

        }
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    fun selectAudio(view: View) {

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setMessage("Select audio from")
        builder.setPositiveButton("Audio From Mobile Device"){dialogInterface, which ->
            val intent = Intent()
            intent.setAction(Intent.ACTION_PICK)
            intent.setType("audio/*");
            startActivityForResult(intent,200)
        }
        builder.setNegativeButton("Record Audio"){dialogInterface, which ->
            if(recordAudio())
            {
                currentTime=Calendar.getInstance().time.toString()
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                mediaRecorder.setOutputFile(recordedAudioPath)
                mediaRecorder.prepare()
                mediaRecorder.start()
                binding.bottomSheet.visibility=View.VISIBLE
                binding.btnDone.isEnabled=true
            }
            else{
                Toast.makeText(applicationContext,"Cannot record audio as your device doesn't support MIC",Toast.LENGTH_LONG).show()
            }
        }
        builder.create().show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun recordAudio() : Boolean {
        if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true
        }
        else{
            return false
        }
    }





    fun stopRecording(view: View) {
        mediaRecorder.stop()
        mediaRecorder.release()
        binding.bottomSheet.visibility=View.GONE
        binding.noteAudioTextView.text=recordedAudioPath
    }
}
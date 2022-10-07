package com.example.notesapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notesapp.databinding.ActivityNoteDetailsBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.formats.NativeAd
import com.google.android.gms.ads.formats.NativeAdOptions

class NoteDetailsActivity : AppCompatActivity() {


    lateinit var adRequest : AdRequest


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var noteTitle = intent.getStringExtra("title")
        var noteDescription = intent.getStringExtra("description")

        binding.notesTitleOnDetailActivity.text = noteTitle
        binding.notesDescriptionOnDetailActivity.text = noteDescription


        val adView = binding.adView
        adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


    }

}
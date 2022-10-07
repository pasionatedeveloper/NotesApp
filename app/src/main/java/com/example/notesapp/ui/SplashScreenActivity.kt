package com.example.notesapp.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.notesapp.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds


class SplashScreenActivity : AppCompatActivity() {
    lateinit var interstitialAd: InterstitialAd
    lateinit var adRequest: AdRequest




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        supportActionBar?.hide()
        MobileAds.initialize(this)

        // on below line we are
        // initializing our ad request.
        adRequest = AdRequest.Builder().build()

        // on below line we are
        // initializing our interstitial ad.
        interstitialAd = InterstitialAd(this)

        // on below line we are setting ad
        // unit id for our interstitial ad.
        interstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        interstitialAd.loadAd(adRequest)
        interstitialAd.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                displayInterstitialAd(interstitialAd)
            }
        })


        Handler().postDelayed(object :Runnable{
            override fun run() {
                startActivity(Intent(this@SplashScreenActivity , MainActivity::class.java))
             }
        },4000)

    }

    private fun displayInterstitialAd(interstitialAd: InterstitialAd) {
        // on below line we are
        // checking if the ad is loaded
        if (interstitialAd.isLoaded) {
            // if the ad is loaded we are displaying
            // interstitial ad by calling show method.
            interstitialAd.show()
        }
    }



}




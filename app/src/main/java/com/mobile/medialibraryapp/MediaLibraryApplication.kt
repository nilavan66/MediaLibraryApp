package com.mobile.medialibraryapp

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class  MediaLibraryApplication: Application(){
    private lateinit var auth: FirebaseAuth
    override fun onCreate() {
        super.onCreate()
       auth = Firebase.auth
        FirebaseApp.initializeApp(this)
        val providerFactory = PlayIntegrityAppCheckProviderFactory.getInstance()
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(providerFactory)
    }
}
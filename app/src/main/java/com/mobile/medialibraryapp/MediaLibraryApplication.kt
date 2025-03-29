package com.mobile.medialibraryapp

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class  MediaLibraryApplication: Application(){
    private lateinit var auth: FirebaseAuth
    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
    }
}
package com.mobile.medialibraryapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MediaLibraryApplication: Application(){

    override fun onCreate() {
        super.onCreate()
    }
}
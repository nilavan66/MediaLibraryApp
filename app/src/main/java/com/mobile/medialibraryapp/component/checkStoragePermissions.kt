package com.mobile.medialibraryapp.component

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mobile.medialibraryapp.util.Constants.Permission.STORAGE_PERMISSION_REQUEST_CODE

// Check and request storage permissions
fun checkStoragePermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
        permissionGranted
    } else {
        true
    }
}

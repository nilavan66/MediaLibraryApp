package com.mobile.medialibraryapp.viewmodel

import android.app.Application
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.medialibraryapp.state.MediaDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaDetailViewModel @Inject constructor(private val appContext: Application) :
    BaseViewModel<MediaDetailState>() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadFile(fileUrl: String, fileName: String) {
        try {
            val request = DownloadManager.Request(Uri.parse(fileUrl))
                .setTitle(fileName)
                .setDescription("Downloading...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    fileName
                )

            val downloadManager =
                appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)

            showToast("Download Started")

            observeDownloadStatus(appContext, downloadId)
        } catch (e: Exception) {
            showToast("Download Failed: ${e.message}")
        }
    }

    fun deleteMediaDocument(documentId: String) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("media_data").document(documentId)
                .delete()
                .addOnSuccessListener {
                    showToast("Deleted from Firestore")
                }
                .addOnFailureListener { e ->
                    showToast("Delete failed: ${e.message}")
                }
        } catch (e: Exception) {
            showToast("Error: ${e.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeDownloadStatus(context: Context, downloadId: Long) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    showToast("Download Completed")
                    context?.unregisterReceiver(this)
                }
            }
        }
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }
}

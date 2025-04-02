package com.mobile.medialibraryapp.util.sharedpreference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.mobile.medialibraryapp.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SharedPref @Inject constructor(@ApplicationContext private var context: Context) {


    private var preference: SharedPreferences? = null

    private fun getPreferenceInstance(): SharedPreferences? {
        return if (preference != null) {
            preference
        } else {
            preference = context.getSharedPreferences(context.packageName, MODE_PRIVATE)
            preference
        }
    }

    fun setSharedValue(key: String, value: String?) {
        getPreferenceInstance()
        val editor = preference?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }


    fun clearSharedValue() {
        getPreferenceInstance()
        val editor = preference?.edit()
        editor?.remove(Constants.SharedKey.USER_DATA)?.remove(Constants.SharedKey.USER_ID)
        editor?.apply()
    }



    fun getStringValue(key: String): String {
        getPreferenceInstance()
        return preference?.getString(key, "") ?: ""
    }



}
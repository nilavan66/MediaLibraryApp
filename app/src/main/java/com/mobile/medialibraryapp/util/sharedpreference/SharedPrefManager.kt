package com.mobile.medialibraryapp.util.sharedpreference

import com.mobile.medialibraryapp.util.Constants.SharedKey.TOKEN
import com.mobile.medialibraryapp.util.Constants.SharedKey.USER_ID
import javax.inject.Inject


class SharedPrefManager @Inject constructor(private val sharedPref: SharedPref) {

    fun clearData() {
        sharedPref.clearSharedValue()
    }

    var token: String
        get() = sharedPref.getStringValue(TOKEN)
        set(token) = sharedPref.setSharedValue(TOKEN, token)

    var userid: String
        get() = sharedPref.getStringValue(USER_ID)
        set(token) = sharedPref.setSharedValue(USER_ID, token)

}
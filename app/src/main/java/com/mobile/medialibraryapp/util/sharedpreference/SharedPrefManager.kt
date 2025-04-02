package com.mobile.medialibraryapp.util.sharedpreference

import com.mobile.medialibraryapp.util.Constants.SharedKey.TOKEN
import com.mobile.medialibraryapp.util.Constants.SharedKey.USER_ID
import javax.inject.Inject


class SharedPrefManager @Inject constructor(private val sharedPref: SharedPref) {

    fun clearData() {
        sharedPref.clearSharedValue()
    }

    var userid: String
        get() = sharedPref.getStringValue(USER_ID)
        set(userid) = sharedPref.setSharedValue(USER_ID, userid)
}
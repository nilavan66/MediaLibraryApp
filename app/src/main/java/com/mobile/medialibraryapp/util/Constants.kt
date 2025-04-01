package com.mobile.medialibraryapp.util

interface Constants {

    object SharedKey {
        const val USER_ID = "userid"
        const val USER_DATA = "userData"
        const val TOKEN = "token"
    }

    object InternalHttpCode {
        const val SUCCESS = 200
        const val CREATED = 201
        const val FAILURE_CODE = 404
        const val BAD_REQUEST_CODE = 400
        const val UNAUTHORIZED_CODE = 401
        const val INTERNAL_SERVER_ERROR_CODE = 500
        const val TIMEOUT_CODE = 504
        const val INTERNAL_SERVER_ERROR =
            "Our server is under maintenance. We will resolve shortly!"
    }
    object ScreenType{
        var SCREEN_TYPE = "screenType"
        var DATA= ""
        const val IMAGE ="image"
        const val AUDIO = "audio"
        const val VIDEO= "video"
    }

    object Permission{
        const val STORAGE_PERMISSION_REQUEST_CODE = 1001
    }
}


package com.mobile.medialibraryapp.webservice

import android.content.Context
import android.os.Looper
import com.mobile.medialibraryapp.model.ErrorResponse
import com.mobile.medialibraryapp.model.State
import com.mobile.medialibraryapp.state.BaseState
import com.mobile.medialibraryapp.util.Constants
import com.mobile.medialibraryapp.util.Constants.InternalHttpCode.INTERNAL_SERVER_ERROR
import com.mobile.medialibraryapp.util.hasNetworkConnection
import com.mobile.medialibraryapp.util.sharedpreference.SharedPrefManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext

import timber.log.Timber

abstract class NetworkBoundRepository<RESULT>(
    private val baseFlow: MutableSharedFlow<BaseState>?,
    private val isShowProgress: Boolean = true,
    private val isShowErrorToast: Boolean = true,
    private val sharedPrefManager: SharedPrefManager,
    private val context: Context
) {
    fun asFlow() = flow {
        if (context.hasNetworkConnection()) {

            if (isShowProgress) {
                baseFlow?.emit(BaseState.ShowLoader)
            }

            try {
                val remoteData: RESULT

                withContext(Dispatchers.IO) {
                    remoteData = fetchData()
                }

                if (isShowProgress)
                    baseFlow?.emit(BaseState.DismissLoader)

                emit(State.success(remoteData))

            } catch (e: Exception) {
                if (isShowProgress)
                    baseFlow?.emit(BaseState.DismissLoader)

                val errorMessage = e.message ?: INTERNAL_SERVER_ERROR
                emit(State.error(errorMessage))

                if (isShowErrorToast)
                    baseFlow?.emit(BaseState.ShowToast(errorMessage))
            }

        } else {
            baseFlow?.emit(BaseState.ShowNetworkAlert)
        }
    }.catch { e ->
        withContext(Dispatchers.Main) {
            if (isShowProgress)
                baseFlow?.emit(BaseState.DismissLoader)

            val errorMessage = e.message ?: INTERNAL_SERVER_ERROR
            emit(State.error(errorMessage))
            baseFlow?.emit(BaseState.ShowToast(errorMessage))

            e.printStackTrace()
            Timber.d("Firebase Error: ${e.message}")
        }
    }

    protected abstract suspend fun fetchData(): RESULT
}

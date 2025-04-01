package com.mobile.medialibraryapp.webservice

import android.content.Context
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mobile.medialibraryapp.model.State
import com.mobile.medialibraryapp.model.request.LoginRequest
import com.mobile.medialibraryapp.state.BaseState
import com.mobile.medialibraryapp.util.sharedpreference.SharedPrefManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPrefManager: SharedPrefManager
) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(
        request: LoginRequest, baseFlow: MutableSharedFlow<BaseState>?
    ): Flow<State<AuthResult>> {
        return object : NetworkBoundRepository<AuthResult>(
            baseFlow = baseFlow, sharedPrefManager = sharedPrefManager, context = context
        ) {
            override suspend fun fetchData(): AuthResult {
                return firebaseAuth.signInWithEmailAndPassword(
                    request.email, request.password
                ).addOnCompleteListener{task->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        Timber.d("LoginSuccess: Logged in as ${user?.uid}")
                    } else {
                        Timber.e("LoginError: ${task.exception?.message}")
                    }
                }.await()
            }
        }.asFlow()
    }

    fun logoutUser(baseFlow: MutableSharedFlow<BaseState>?): Flow<State<Boolean>> {
        return object : NetworkBoundRepository<Boolean>(
            baseFlow = baseFlow, sharedPrefManager = sharedPrefManager, context = context
        ) {
            override suspend fun fetchData(): Boolean {
                firebaseAuth.signOut()
                //sharedPrefManager.clearData()

                return firebaseAuth.currentUser == null
            }
        }.asFlow()
    }

}
//fun logout(baseFlow: MutableSharedFlow<BaseState>?) {}


package com.maha.inspireverse.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface AuthRepository {
    suspend fun signInAnonymously() : Result<FirebaseUser>
}

class FireBaseAuthRepository :AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    override suspend fun signInAnonymously(): Result<FirebaseUser> = suspendCancellableCoroutine{ cont->
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                if (user!= null ){
                    cont.resume(Result.success(user))
                }else
                {
                    cont.resume(Result.failure(Exception("user not registered")))
                }
                Log.d("FirebaseAuth User----", user?.uid.toString())
            } else {
                cont.resume(Result.failure(task.exception?: Exception("Authentication failed")))
                Log.e("FirebaseAuth User----", task.exception.toString())
            }


        }
    }
}

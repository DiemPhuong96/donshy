package com.example.donshy.data.repository

import com.example.donshy.data.model.AuthResponse
import com.example.donshy.data.model.User
import com.example.donshy.utils.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginRepository(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun login(user: User): Result<AuthResponse> = withContext(dispatcher) {
        try {
            val result = auth.signInWithEmailAndPassword(user.email, user.password).await()
            val currentUser = result.user
            if (currentUser == null) {
                Result.Error("Login Failed")
            } else if (currentUser.isEmailVerified) {
                Result.Success(
                    AuthResponse(
                        userId = currentUser.uid,
                        email = currentUser.email.toString(),
                        isEmailVerified = currentUser.isEmailVerified
                    )
                )
            } else {
                auth.signOut()
                Result.Error("Your email hasn't been verified")
            }
        } catch (e: Exception) {
            Result.Error(e.message?: "Login Failed")
        }
    }

}
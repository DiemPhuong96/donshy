package com.example.donshy.data.repository

import com.example.donshy.data.model.AuthResponse
import com.example.donshy.data.model.User
import com.example.donshy.utils.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpRepository(private val auth: FirebaseAuth, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    suspend fun registerUser(user: User): Result<AuthResponse> = withContext(dispatcher) {
        return@withContext try {
            val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                firebaseUser.sendEmailVerification().await()

                val response = AuthResponse(
                    userId = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    isEmailVerified = firebaseUser.isEmailVerified
                )
                Result.Success(response)
            } else {
                Result.Error("")
            }
        } catch (e: Exception) {
            Result.Error(e.message?: "")
        }
    }
    suspend fun isEmailVerified(): Boolean {
        val currentUser = auth.currentUser ?: return false
        try {
            currentUser.reload().await()
            return currentUser.isEmailVerified
        } catch (_: Exception) {
            return false
        }
    }
}
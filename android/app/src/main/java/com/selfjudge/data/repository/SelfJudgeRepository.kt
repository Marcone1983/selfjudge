package com.selfjudge.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.selfjudge.data.api.ApiClient
import com.selfjudge.data.model.*
import kotlinx.coroutines.tasks.await
import retrofit2.Response

class SelfJudgeRepository {
    private val auth = FirebaseAuth.getInstance()
    private val apiService = ApiClient.apiService

    // Auth methods
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // API methods
    private suspend fun getAuthToken(): String? {
        return try {
            auth.currentUser?.getIdToken(false)?.await()?.token?.let { "Bearer $it" }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun bootstrap(): Result<BootstrapResponse> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("No auth token"))
            val response = apiService.bootstrap(token)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Bootstrap failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun judge(artist: String, text: String): Result<JudgeResponse> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("No auth token"))
            val request = JudgeRequest(artist, text)
            val response = apiService.judge(token, request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Judge failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun battle(artist: String, textA: String, textB: String): Result<BattleResponse> {
        return try {
            val token = getAuthToken() ?: return Result.failure(Exception("No auth token"))
            val request = BattleRequest(artist, textA, textB)
            val response = apiService.battle(token, request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Battle failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package com.selfjudge.data.api

import com.selfjudge.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("bootstrap")
    suspend fun bootstrap(@Header("Authorization") token: String): Response<BootstrapResponse>

    @POST("judge")
    suspend fun judge(
        @Header("Authorization") token: String,
        @Body request: JudgeRequest
    ): Response<JudgeResponse>

    @POST("battle")
    suspend fun battle(
        @Header("Authorization") token: String,
        @Body request: BattleRequest
    ): Response<BattleResponse>

    @POST("billing/ack")
    suspend fun acknowledgePurchase(
        @Header("Authorization") token: String,
        @Body payload: Map<String, Any>
    ): Response<Map<String, Any>>
}